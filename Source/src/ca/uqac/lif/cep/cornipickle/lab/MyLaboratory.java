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

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.labpal.Experiment;
import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.labpal.table.ExperimentTable;
import ca.uqac.lif.mtnp.plot.TwoDimensionalPlot.Axis;
import ca.uqac.lif.mtnp.plot.gnuplot.Scatterplot;

public class MyLaboratory extends Laboratory
{
  protected static final transient String PROPERTY = "Property";
  
  @Override
  public void setup()
  {
    populateExperiments(LiExperiment.class, "data/properties/li.csv");
  }

  public static void main(String[] args)
  {
    // Nothing else to do here
    MyLaboratory.initialize(args, MyLaboratory.class);
  }
  
  protected void populateExperiments(Class<? extends Experiment> clazz, String filename)
  {
    Map<String,String> properties = new HashMap<String,String>();
    Scanner scanner = new Scanner(MyLaboratory.class.getResourceAsStream(filename));
    while (scanner.hasNextLine())
    {
      String line = scanner.nextLine();
      String[] parts = line.split("\t");
      properties.put(parts[0], parts[1]);
    }
    scanner.close();
    Region num_li = new Region();
    String[] prop_names = new String[properties.keySet().size()];
    int i = 0;
    for (String key : properties.keySet())
    {
      prop_names[i++] = key;
    }
    num_li.add(PROPERTY, prop_names);
    num_li.addRange(LiExperiment.SIZE, 1, 30000, 1000);
    for (Region r_prop : num_li.all(PROPERTY))
    {
      String property_key = ((JsonString) r_prop.get(PROPERTY)).stringValue();
      ExperimentTable t = new ExperimentTable(LiExperiment.SIZE, JsonExperiment.TIME);
      t.setTitle("Evaluation time vs. size; property " + property_key);
      for (Region r : num_li.all(LiExperiment.SIZE))
      {
        Experiment e = new LiExperiment(property_key, properties.get(property_key), r.getInt(LiExperiment.SIZE));
        add(e);
        t.add(e);
      }
      add(t);
      Scatterplot sp = new Scatterplot(t);
      sp.setTitle("Evaluation time vs. size; property " + property_key);
      sp.setCaption(Axis.X, LiExperiment.SIZE).setCaption(Axis.Y, JsonExperiment.TIME + " (ms)");
      add(sp);
    }
  }
}
