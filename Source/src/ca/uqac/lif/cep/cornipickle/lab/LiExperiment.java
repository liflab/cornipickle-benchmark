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

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;

public class LiExperiment extends JsonExperiment
{
  public static final transient String SIZE = "Size";
  
  @SuppressWarnings("unused")
  private LiExperiment()
  {
    this("", "", 0);
  }
  
  public LiExperiment(String property_name, String property, int num_li)
  {
    super(property_name, getInterpreter(property));
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
