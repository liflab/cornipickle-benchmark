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

import com.sun.net.httpserver.HttpExchange;

import ca.uqac.lif.jerrydog.CallbackResponse;
import ca.uqac.lif.jerrydog.RestCallback;

/**
 * Callback that dynamically generates a page when being called 
 */
public abstract class DynamicCallback extends RestCallback
{
  public DynamicCallback(Method m, String path)
  {
    super(m, path);
  }

  @Override
  public CallbackResponse process(HttpExchange he)
  {
    CallbackResponse cbr = new CallbackResponse(he);
    cbr.setCode(CallbackResponse.HTTP_OK);
    cbr.setContentType(CallbackResponse.ContentType.HTML);
    cbr.setContents(getContents());
    return cbr;
  }
  
  /**
   * Generates the contents of the page to serve
   * @return The contents of the page
   */
  public abstract String getContents();
  
  /**
   * Generates a boilerplate HTML header for a page
   * @return The header
   */
  protected static String getHtmlHeader()
  {
    StringBuilder out = new StringBuilder();
    out.append("<!DOCTYPE html>\n<head>\n")
    // Script line that loads the probe
    .append("<script type=\"text/javascript\" src=\"http://localhost:")
    .append(MyLaboratory.SERVER_PORT)
    .append("/probe\"></script>\n")
    .append("<title>Dummy page</title>\n</head>\n<body>\n");
    return out.toString();
  }
  
  /**
   * Generates a boilerplate HTML footer for a page
   * @return The footer
   */
  protected static String getHtmlFooter()
  {
    return "\n</body>\n</html>";
  }
}
