package aspireApi;


import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
  
  @RunWith(Cucumber.class)
  
  
  @CucumberOptions( features = "src/test/resources", glue={"aspireApi"}) 
  public class  SerenityRunnerTest{
  
  } 

 
