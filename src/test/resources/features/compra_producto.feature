# language: es
@compra
Característica: Compra de productos en Swag Labs
  Como cliente de la tienda Swag Labs
  Quiero comprar un producto desde la aplicación móvil
  Para recibirlo en mi dirección

  Antecedentes:
    Dado que "Andres" inicia sesión en Swag Labs con usuario "standard_user" y clave "secret_sauce"

  @happyPath
  Escenario: Compra exitosa de un producto
    Cuando agrega el producto "Sauce Labs Backpack" al carrito
    Y finaliza la compra con los datos de envío
      | nombre | apellido | codigoPostal |
      | Andres | Sabogal  | 110111       |
    Entonces debería ver el mensaje de confirmación "THANK YOU FOR YOU ORDER"
