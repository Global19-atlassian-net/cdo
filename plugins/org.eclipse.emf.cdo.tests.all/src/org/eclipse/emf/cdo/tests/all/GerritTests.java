/*
 * Copyright (c) 2013, 2014, 2016, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Stefan Winkler - introduced variable mapping strategies
 */
package org.eclipse.emf.cdo.tests.all;

import org.eclipse.emf.cdo.tests.db.DBConfigs;
import org.eclipse.emf.cdo.tests.db.H2Config;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Eike Stepper
 */
public class GerritTests extends DBConfigs
{
  public static Test suite()
  {
    return new GerritTests().getTestSuite();
  }

  @Override
  protected void initConfigSuites(TestSuite parent)
  {
    addScenario(parent, new H2Config(), JVM, NATIVE);
    addScenario(parent, new H2Config().supportingBranches(true), JVM, NATIVE);
  }
}
