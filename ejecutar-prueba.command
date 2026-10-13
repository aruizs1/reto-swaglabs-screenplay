#!/bin/zsh -l
# Prepara el entorno y ejecuta la prueba del Reto SwagLabs.
cd "$(dirname "$0")"
LOG="$PWD/ejecucion.log"
exec > >(tee "$LOG") 2>&1

SDK=${ANDROID_HOME:-$HOME/Library/Android/sdk}
ADB="$SDK/platform-tools/adb"
EMU="$SDK/emulator/emulator"
APK="apps/Android.SauceLabs.Mobile.Sample.app.2.7.1.apk"
APK_URL="https://github.com/saucelabs/sample-app-mobile/releases/download/2.7.1/Android.SauceLabs.Mobile.Sample.app.2.7.1.apk"

echo "== 1. Java"
if [ -x /usr/libexec/java_home ] && /usr/libexec/java_home -v 17 >/dev/null 2>&1; then
  export JAVA_HOME=$(/usr/libexec/java_home -v 17)
fi
java -version 2>&1 | head -1

echo "== 2. Emulador / dispositivo"
if ! "$ADB" devices | grep -q -w device; then
  AVD=$("$EMU" -list-avds | head -1)
  echo "Iniciando emulador: $AVD"
  nohup "$EMU" -avd "$AVD" -no-snapshot-save > emulador.log 2>&1 &
  "$ADB" wait-for-device
  until [ "$("$ADB" shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = "1" ]; do sleep 3; done
fi
"$ADB" devices
DEVICE=$("$ADB" devices | awk 'NR>1 && $2=="device"{print $1; exit}')
echo "Dispositivo: $DEVICE"

echo "== 3. APK Swag Labs"
if [ ! -s "$APK" ]; then
  echo "Descargando APK..."
  curl -fL -o "$APK" "$APK_URL" || { echo "ERROR: no se pudo descargar el APK"; exit 1; }
fi
"$ADB" -s "$DEVICE" uninstall com.swaglabsmobileapp >/dev/null 2>&1
"$ADB" -s "$DEVICE" install -g "$APK" || "$ADB" -s "$DEVICE" install -r -g --bypass-low-target-sdk-block "$APK"

echo "== 4. Appium"
if ! curl -s http://127.0.0.1:4723/status | grep -q ready; then
  nohup appium --base-path / > appium.log 2>&1 &
  for i in {1..30}; do curl -s http://127.0.0.1:4723/status | grep -q ready && break; sleep 1; done
fi
curl -s http://127.0.0.1:4723/status | head -c 200; echo

echo "== 5. Prueba (capturas del emulador cada 2 s en evidencias/)"
rm -rf evidencias; mkdir -p evidencias
( while true; do "$ADB" -s "$DEVICE" exec-out screencap -p > "evidencias/$(date +%H%M%S).png" 2>/dev/null; sleep 2; done ) &
CAPTURAS=$!
./gradlew clean test -Dappium.deviceName="$DEVICE"
RC=$?
kill $CAPTURAS 2>/dev/null
echo "== FIN (codigo $RC)"
echo "Reporte: $PWD/target/site/serenity/index.html"
[ $RC -eq 0 ] && open target/site/serenity/index.html
