package ca.uqac.lif.cep.cornipickle.lab;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;

import ca.uqac.lif.cornipickle.server.CornipickleServer;
import ca.uqac.lif.labpal.Experiment;
import ca.uqac.lif.labpal.ExperimentException;

public abstract class BrowserExperiment extends Experiment
{
  protected transient WebDriver m_driver;
  
  protected transient CornipickleServer m_server;
  
  public static final transient String TIME = "Time";
  
  public BrowserExperiment(WebDriver driver, CornipickleServer server)
  {
    super();
    describe(TIME, "The duration of the experiment, in milliseconds");
    m_driver = driver;
    m_server = server;
  }
  
  @Override
  public void execute() throws ExperimentException, InterruptedException
  {
    long time_start = 0;
    try
    {
      m_server.startServer();
      Navigation nav = m_driver.navigate();
      nav.to("TODO");
      time_start = System.currentTimeMillis();
      m_server.stopServer();
    }
    catch (IOException e)
    {
      throw new ExperimentException(e);
    }
    long time_stop = System.currentTimeMillis();
    write(TIME, time_stop - time_start);
  }

}
