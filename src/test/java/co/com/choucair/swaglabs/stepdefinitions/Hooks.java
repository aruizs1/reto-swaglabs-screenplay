package co.com.choucair.swaglabs.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

public class Hooks {

    @Before
    public void prepararEscenario() {
        OnStage.setTheStage(new OnlineCast());
    }

    @After
    public void finalizarEscenario() {
        OnStage.drawTheCurtain();
    }
}
