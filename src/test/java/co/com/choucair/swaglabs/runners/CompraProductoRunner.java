package co.com.choucair.swaglabs.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.FILTER_TAGS_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/compra_producto.feature")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "co.com.choucair.swaglabs.stepdefinitions")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
        value = "io.cucumber.core.plugin.SerenityReporterParallel,pretty")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@happyPath")
public class CompraProductoRunner {
}
