/*
  Cornipickle benchmark
  Copyright (C) 2018 Sylvain Hallé and friends

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cep.cornipickle.lab;

import java.io.IOException;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.server.CornipickleServer;
import ca.uqac.lif.cornipickle.server.InterpreterNotification;
import ca.uqac.lif.jerrydog.RestCallback;
import ca.uqac.lif.labpal.ExperimentException;

public abstract class BrowserExperiment extends CornipickleExperiment
{
  protected transient long m_interpreterStartTime = 0;
  
  protected volatile boolean m_done = false;
  
  public BrowserExperiment(String property_name, String property)
  {
    super();
    setInput(PROPERTY_NAME, property_name);    
  }
  
  @Override
  public void execute() throws ExperimentException, InterruptedException
  {
    CornipickleServer server = getServer(readString(PROPERTY));
    WebDriver driver = getDriver();
    try
    {
      server.startServer();
      m_done = false;
      // Wait a little so that the server has the time to start
      Thread.sleep(250);
      String url = "http://localhost:" + MyLaboratory.SERVER_PORT + "/page";
      driver.get(url);
      m_interpreterStartTime = System.currentTimeMillis();
      WebElement elem = driver.findElement(By.cssSelector("body"));
      // Wait again so that the page has time to be loaded
      Thread.sleep(500);
      elem.click();
      // Wait until the interpreter is done processing the request
      while (!m_done)
      {
        Thread.sleep(10);
      }
      Date d = server.getLastProbeContact();
      if (d == null)
      {
        throw new ExperimentException("Cornipickle server reports no probe contact");
      }
      server.stopServer();
    }
    catch (IOException e)
    {
      throw new ExperimentException(e);
    }
  }

  /**
   * Gets an instance of a Cornipickle server evaluating a specific property.
   * @param property The property to evaluate
   * @return The server, or {@code null} if the parsing of the property failed
   */
  protected CornipickleServer getServer(String property)
  {
    CornipickleServer server = new CornipickleServer("localhost", MyLaboratory.SERVER_PORT);
    Interpreter interp = new Interpreter();
    try
    {
      interp.parseProperties(property);
    }
    catch (ParseException e)
    {
      return null;
    }
    server.setInterpreter(interp);
    server.persistState(false);
    server.registerCallback(0, getCallback());
    server.registerNotification(new CallbackNotification());
    return server;
  }
  
  /**
   * Gets an instance of a web driver
   * @return The driver
   */
  protected WebDriver getDriver()
  {
    return new HtmlUnitDriver(true);
  }
  
  protected abstract RestCallback getCallback();
  
  protected class CallbackNotification implements InterpreterNotification
  {
    @Override
    public void notify(Interpreter i)
    {
      long end_time = System.currentTimeMillis();
      write(TIME, end_time - m_interpreterStartTime);
      m_done = true;
    }
  }
}
