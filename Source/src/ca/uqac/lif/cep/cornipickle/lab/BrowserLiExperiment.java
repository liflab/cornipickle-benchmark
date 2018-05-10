package ca.uqac.lif.cep.cornipickle.lab;

import ca.uqac.lif.jerrydog.RestCallback;
import ca.uqac.lif.jerrydog.RequestCallback.Method;

public class BrowserLiExperiment extends BrowserExperiment
{
  BrowserLiExperiment()
  {
    super("", "");
  }
  
  public BrowserLiExperiment(String property_name, String property)
  {
    super(property_name, property);
  }

  @Override
  public BrowserLiExperiment newExperiment()
  {
    return new BrowserLiExperiment();
  }

  @Override
  protected RestCallback getCallback()
  {
    return new LiCallback(Method.GET, "/page", readInt(SIZE));
  }

  public class LiCallback extends DynamicCallback
  {
    protected int m_size;
    
    public LiCallback(Method m, String path, int size)
    {
      super(m, path);
      m_size = size;
    }

    @Override
    public String getContents()
    {
      StringBuilder out = new StringBuilder();
      out.append(getHtmlHeader());
      out.append("<h1>My title</h1>\n");
      out.append("<ul>\n");
      for (int i = 0; i < m_size; i++)
      {
        out.append("<li>Some dummy text</li>\n");
      }
      out.append("</ul>\n");
      out.append(getHtmlFooter());
      return out.toString();
    }
  }
}
