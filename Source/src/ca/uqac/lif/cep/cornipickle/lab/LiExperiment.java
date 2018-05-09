package ca.uqac.lif.cep.cornipickle.lab;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;

public class LiExperiment extends JsonExperiment
{
  public static final transient String SIZE = "Size";
  
  public LiExperiment(String property, int num_li)
  {
    super(getInterpreter(property));
    setDescription("Evaluates the time required to evaluate an expression, in function of " 
        + "the size of the input document.");
    describe(SIZE, "The size of the page (in number of elements)");
    setInput(SIZE, num_li);
  }
  
  protected static Interpreter getInterpreter(String property)
  {
    Interpreter my_int = new Interpreter();
    try
    {
      my_int.parseProperties(property);
      return my_int;
    }
    catch (ParseException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public JsonElement getJson()
  {
    int id = 0;
    int size = readInt(SIZE);
    JsonMap map = new JsonMap();
    map.put("tagname", "window");
    map.put("cornipickleid", id++);
    JsonList children = new JsonList();
    for (int i = 0; i < size; i++)
    {
      JsonMap child = new JsonMap();
      map.put("tagname", "li");
      map.put("cornipickleid", id++);
      map.put("left", 30);
      map.put("top", 30 * i);
      children.add(child);
    }
    map.put("children", children);
    return map;
  }

}
