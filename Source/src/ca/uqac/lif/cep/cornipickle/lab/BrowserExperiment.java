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
