package ca.uqac.lif.cep.cornipickle.lab;

import ca.uqac.lif.labpal.Experiment;
import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.labpal.table.ExperimentTable;
import ca.uqac.lif.mtnp.plot.TwoDimensionalPlot.Axis;
import ca.uqac.lif.mtnp.plot.gnuplot.Scatterplot;

public class MyLaboratory extends Laboratory
{
	@Override
	public void setup()
	{
		Region num_li = new Region();
		num_li.addRange(LiExperiment.SIZE, 1, 30000, 1000);
		ExperimentTable t = new ExperimentTable(LiExperiment.SIZE, JsonExperiment.TIME);
		for (Region r : num_li.all(LiExperiment.SIZE))
		{
		  Experiment e = new LiExperiment("For each $x in $(li) ($x's left equals 30).", r.getInt(LiExperiment.SIZE));
		  add(e);
		  t.add(e);
		}
		Scatterplot sp = new Scatterplot(t);
		sp.setTitle("Evaluation time vs. size");
		sp.setCaption(Axis.X, LiExperiment.SIZE).setCaption(Axis.Y, JsonExperiment.TIME);
		add(sp);
		
	}
	
	public static void main(String[] args)
	{
		// Nothing else to do here
		MyLaboratory.initialize(args, MyLaboratory.class);
	}
}
