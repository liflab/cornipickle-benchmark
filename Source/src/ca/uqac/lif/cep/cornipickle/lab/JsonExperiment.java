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

import java.util.Collection;
import java.util.Map;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.Verdict;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.labpal.ExperimentException;

public abstract class JsonExperiment extends CornipickleExperiment
{
  public JsonExperiment(String property_name, int num_loops)
  {
    super();
    setInput(PROPERTY_NAME, property_name);
    setInput(NUM_LOOPS, num_loops);
  }
  
  @Override
  public void execute() throws ExperimentException, InterruptedException
  {
    Interpreter my_int = new Interpreter();
    try
    {
    	String prop = readString(PROPERTY);
    	prop = prop.replaceAll("\\.", ".\n");
      my_int.parseProperties(prop);
    }
    catch (ParseException e)
    {
      throw new ExperimentException(e);
    }
    
    long time_start = System.currentTimeMillis();
    for (int i = 0; i < readInt(NUM_LOOPS); i++)
    {
    	JsonElement je = getJson();
        my_int.evaluateAll(je);
        Map<Interpreter.StatementMetadata,Verdict> verdicts = my_int.getVerdicts();
        Collection<Verdict> values = verdicts.values();
        if (values.isEmpty())
        {
          throw new ExperimentException("The interpreter returned an empty verdict.");
        }
    }
    long time_end = System.currentTimeMillis();
    write(TIME, time_end - time_start);
  }

  public abstract JsonElement getJson();
  
  @Override
  public abstract JsonExperiment newExperiment();
}
