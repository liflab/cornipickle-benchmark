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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.labpal.CliParser;
import ca.uqac.lif.labpal.CliParser.Argument;
import ca.uqac.lif.labpal.CliParser.ArgumentMap;
import ca.uqac.lif.labpal.Experiment;
import ca.uqac.lif.labpal.Group;
import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.labpal.table.ExperimentTable;
import ca.uqac.lif.mtnp.plot.TwoDimensionalPlot.Axis;
import ca.uqac.lif.mtnp.plot.gnuplot.Scatterplot;

public class MyLaboratory extends Laboratory
{
  /**
   * The port used by the Cornipickle servers
   */
  protected static final transient int SERVER_PORT = 10101;
  
  /**
   * The maximum size of the pages in the JSON experiments
   */
  protected int m_maxSizeJson = 30000; 
  
  /**
   * The incement step for the page size in the JSON experiments 
   */
  protected int m_incrementJson = 5000;
  
  /**
   * The maximum size of the pages in the browser experiments
   */
  protected int m_maxSizeBrowser = 1000; 
  
  /**
   * The incement step for the page size in the browser experiments 
   */
  protected int m_incrementBrowser = 200;

  @Override
  public void setup()
  {
    // Override default parameters
    ArgumentMap args = getCliArguments();
    if (args.hasOption("max-size-json"))
    {
      m_maxSizeJson = Integer.parseInt(args.getOptionValue("max-size-json"));
    }
    if (args.hasOption("max-size-browser"))
    {
      m_maxSizeBrowser = Integer.parseInt(args.getOptionValue("max-size-browser"));
    }
    
    // JSON experiments
    Group eval_g = new Group("Evaluation experiments");
    eval_g.setDescription("Experiments that measure the time taken to evaluate various expressions on JSON snapshots.");
    add(eval_g);
    eval_g.add(populateExperiments(new JsonLiExperiment(), "data/properties/li.csv", new Region().addRange(CornipickleExperiment.SIZE, 0, m_maxSizeJson, m_incrementJson)));
    
    // Browser experiments
    Group eval_b = new Group("Browser experiments");
    eval_b.setDescription("Experiments that measure the round-trip time taken to evaluate various expressions in a browser.");
    add(eval_b);
    eval_b.add(populateExperiments(new BrowserLiExperiment(), "data/properties/li.csv", new Region().addRange(CornipickleExperiment.SIZE, 0, m_maxSizeBrowser, m_incrementBrowser)));
  }

  public static void main(String[] args)
  {
    // Nothing else to do here
    MyLaboratory.initialize(args, MyLaboratory.class);
  }

  @Override
  public String isEnvironmentOk()
  {
    return null;
  }
  
  @Override
  public void setupCli(CliParser parser)
  {
    parser.addArgument(new Argument().withLongName("max-size-json").withArgument("n").withDescription("Maximum size of the pages in JSON experiments"));
    parser.addArgument(new Argument().withLongName("max-size-browser").withArgument("n").withDescription("Maximum size of the pages in browser experiments"));
  }

  protected Experiment[] populateExperiments(CornipickleExperiment ref_experiment, String filename, Region num_li)
  {
    List<Experiment> experiments = new LinkedList<Experiment>();
    Map<String,String> properties = new HashMap<String,String>();
    Scanner scanner = new Scanner(MyLaboratory.class.getResourceAsStream(filename));
    while (scanner.hasNextLine())
    {
      String line = scanner.nextLine().trim();
      if (line.isEmpty() || line.startsWith("#"))
        continue;
      String[] parts = line.split("\t");
      properties.put(parts[0], parts[1]);
    }
    scanner.close();
    String[] prop_names = new String[properties.keySet().size()];
    int i = 0;
    for (String key : properties.keySet())
    {
      prop_names[i++] = key;
    }
    num_li.add(CornipickleExperiment.PROPERTY, prop_names);
    for (Region r_prop : num_li.all(CornipickleExperiment.PROPERTY))
    {
      String property_key = ((JsonString) r_prop.get(CornipickleExperiment.PROPERTY)).stringValue();
      ExperimentTable t = new ExperimentTable(CornipickleExperiment.SIZE, CornipickleExperiment.TIME);
      t.setTitle("Evaluation time vs. size; property " + property_key);
      for (Region r : num_li.all(CornipickleExperiment.SIZE))
      {
        CornipickleExperiment e = ref_experiment.newExperiment();
        e.setInput(CornipickleExperiment.SIZE, r.getInt(CornipickleExperiment.SIZE));
        e.setInput(CornipickleExperiment.PROPERTY_NAME, property_key);
        e.setInput(CornipickleExperiment.PROPERTY, properties.get(property_key));
        add(e);
        t.add(e);
        experiments.add(e);
      }
      add(t);
      Scatterplot sp = new Scatterplot(t);
      sp.setTitle("Evaluation time vs. size; property " + property_key);
      sp.setCaption(Axis.X, JsonLiExperiment.SIZE).setCaption(Axis.Y, JsonExperiment.TIME + " (ms)");
      add(sp);
    }
    Experiment[] out = new Experiment[experiments.size()];
    int n = 0;
    for (Experiment e : experiments)
    {
      out[n++] = e;
    }
    return out;
  }
}
