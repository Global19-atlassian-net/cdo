/*
 * Copyright (c) 2013, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Stefan Winkler - introduced variable mapping strategies
 */
package org.eclipse.emf.cdo.tests.db;

import org.eclipse.emf.cdo.common.CDOCommonRepository.IDGenerationLocation;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Eike Stepper
 */
public class AllTestsDBOracle extends DBConfigs
{
  public static Test suite()
  {
    return new AllTestsDBOracle().getTestSuite();
  }

  @Override
  protected void initConfigSuites(TestSuite parent)
  {
    addScenarios(parent, IDGenerationLocation.STORE);
    // addScenarios(parent, IDGenerationLocation.CLIENT);
  }

  private void addScenarios(TestSuite parent, IDGenerationLocation idGenerationLocation)
  {
    addScenario(parent, new OracleConfig.SingleUser().idGenerationLocation(idGenerationLocation), JVM, NATIVE);
  }
}
