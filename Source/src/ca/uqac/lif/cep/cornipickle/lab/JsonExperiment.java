package ca.uqac.lif.cep.cornipickle.lab;

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.labpal.Experiment;
import ca.uqac.lif.labpal.ExperimentException;

public abstract class JsonExperiment extends Experiment
{
  protected transient Interpreter m_interpreter;
  
  public static final transient String TIME = "Time";
  
  public JsonExperiment(Interpreter interpreter)
  {
    super();
    describe(TIME, "The duration of the experiment, in milliseconds");
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
