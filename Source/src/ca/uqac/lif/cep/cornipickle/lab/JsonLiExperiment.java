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

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;

public class JsonLiExperiment extends JsonExperiment
{
  JsonLiExperiment()
  {
    this("", "", 0);
  }
  
  public JsonLiExperiment(String property_name, String property, int num_li)
  {
    super(property_name);
    setDescription("Evaluates the time required to evaluate an expression, in function of " 
        + "the size of the input document.");
    setInput(SIZE, num_li);
    setInput(PROPERTY, property);
  }
  
  @Override
  public JsonElement getJson()
  {
    int id = 0;
    int size = readInt(SIZE);
    JsonMap window = new JsonMap();
    window.put("tagname", "window");
    window.put("cornipickleid", id++);
    window.put("aspect-ratio", 1);
    window.put("device-aspect-ratio", 1);
    window.put("orientation", "landscape");
    window.put("scroll-width", 500);
    window.put("scroll-height", 500);
    window.put("device-width", 500);
    window.put("device-height", 500);
    window.put("width", 500);
    window.put("height", 500);
    window.put("mediaqueries", new JsonList());
    JsonList window_children = new JsonList();
    {
      JsonMap body = new JsonMap();
      body.put("tagname", "body");
      body.put("cornipickleid", id++);
      JsonList body_children = new JsonList();
      {
        JsonMap ul = new JsonMap();
        JsonList ul_children = new JsonList();
        ul.put("tagname", "ul");
        ul.put("cornipickleid", id++);
        for (int i = 0; i < size; i++)
        {
          JsonMap child = new JsonMap();
          child.put("tagname", "li");
          child.put("cornipickleid", id++);
          child.put("left", 30);
          child.put("top", 30 * i);
          ul_children.add(child);
        }
        ul.put("children", ul_children);
        body_children.add(ul);
      }
      body.put("children", body_children);
      window_children.add(body);
    }
    window.put("children", window_children);
    return window;
  }

  @Override
  public JsonLiExperiment newExperiment()
  {
    return new JsonLiExperiment();
  }
}
