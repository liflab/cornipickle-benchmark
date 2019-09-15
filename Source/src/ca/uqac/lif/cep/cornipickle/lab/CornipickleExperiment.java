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

import ca.uqac.lif.labpal.CloneableExperiment;
import ca.uqac.lif.labpal.Experiment;

public abstract class CornipickleExperiment extends Experiment implements CloneableExperiment<CornipickleExperiment>
{
  /**
   * The string representing the parameter "size"
   */
  public static final transient String SIZE = "Size";
  
  /**
   * The string representing the parameter "time"
   */
  public static final transient String TIME = "Time";
  
  /**
   * The string representing the parameter "property name"
   */
  public static final transient String PROPERTY_NAME = "Property name";
  
  /**
   * The string representing the parameter "property"
   */
  public static final transient String PROPERTY = "Property";
  
  /**
   * The string representing the parameter "number of events"
   */
  public static final transient String NUM_LOOPS = "Number of events";
  
  public CornipickleExperiment()
  {
    super();
    describe(TIME, "The time taken to evaluate the property in milliseconds");
    describe(PROPERTY_NAME, "The name of the property that is being evaluated");
    describe(PROPERTY, "The Cornipickle expression that is being evaluated");
    describe(NUM_LOOPS, "The number of successive events evaluated");
    addKeyToHide(PROPERTY);
    describe(SIZE, "The size of the page (in number of elements)");
  }
}
