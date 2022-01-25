package com.restfulbooker.cucumber;

import com.restfulbooker.testbase.TestBase;
import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;


/**
 * @RunWith annotation tells JUnit that tests should run using
 * CucumberWithSerenity class
 *
 * The @CucumberOptions can be used to provide additional
 * configuration to the runner.
 */
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(features = "src/test/java/resources/feature",
       tags = "@Regression")
public class RegressionCucumberRunner extends TestBase {


}
