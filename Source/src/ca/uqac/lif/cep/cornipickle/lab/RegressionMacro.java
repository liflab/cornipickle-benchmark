package ca.uqac.lif.cep.cornipickle.lab;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.macro.NumberMacro;
import ca.uqac.lif.mtnp.table.Table;
import ca.uqac.lif.mtnp.table.TableEntry;
import ca.uqac.lif.mtnp.table.TempTable;

public class RegressionMacro extends NumberMacro
{
  protected Table m_table;
  
  public RegressionMacro(Laboratory lab, String name, Table t)
  {
    super(lab, name, "The running time per element in the document size, approximated by linear regression");
    m_table = t;
  }
  
  @Override
  public Number getNumber()
  {
    // From table, create an array of pairs of doubles
    TempTable dt = m_table.getDataTable();
    int rc = dt.getRowCount();
    double[][] vals = new double[rc][2];
    int i = 0;
    for (TableEntry te : dt.getEntries())
    {
      double time = 0;
      if (te.get(CornipickleExperiment.TIME) != null)
      {
        time = te.get(CornipickleExperiment.TIME).numberValue().floatValue();
      }
      vals[i] = new double[] {te.get(CornipickleExperiment.SIZE).numberValue().doubleValue(), time};
      i++;
    }
    // Perform regression over this array and return the slope
    SimpleRegression simple_regression = new SimpleRegression();
    simple_regression.addData(vals);
    return simple_regression.getSlope();
  }

  @Override
  public String toLatex(boolean with_comments)
  {
    // TODO Auto-generated method stub
    return "";
  }
}
