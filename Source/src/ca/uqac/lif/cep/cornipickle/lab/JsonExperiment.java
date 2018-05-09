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

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.labpal.Experiment;
import ca.uqac.lif.labpal.ExperimentException;

public abstract class JsonExperiment extends Experiment
{
  protected transient Interpreter m_interpreter;
  
  public static final transient String TIME = "Time";
  
  public static final transient String PROPERTY_NAME = "Property name";
  
  public JsonExperiment(String property_name, Interpreter interpreter)
  {
    super();
    describe(TIME, "The time taken to evaluate the property in milliseconds");
    describe(PROPERTY_NAME, "The name of the property that is being evaluated");
    setInput(PROPERTY_NAME, property_name);
    m_interpreter = interpreter;
  }
  
  @Override
  public void execute() throws ExperimentException, InterruptedException
  {
    if (m_interpreter == null)
    {
      throw new ExperimentException("Error parsing the property: the interpreter is null");
    }
    JsonElement je = getJson();
    long time_start = System.currentTimeMillis();
    m_interpreter.evaluateAll(je);
    long time_end = System.currentTimeMillis();
    write(TIME, time_end - time_start);
  }

  public abstract JsonElement getJson();
}
