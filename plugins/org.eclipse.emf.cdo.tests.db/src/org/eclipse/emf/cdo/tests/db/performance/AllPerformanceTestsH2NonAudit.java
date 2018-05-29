/*
 * Copyright (c) 2011-2013, 2016, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Stefan Winkler - initial API and implementation
 */
package org.eclipse.emf.cdo.tests.db.performance;

import org.eclipse.emf.cdo.tests.db.H2Config;
import org.eclipse.emf.cdo.tests.performance.AllPerformanceTests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Stefan Winkler
 */
public class AllPerformanceTestsH2NonAudit extends AllPerformanceTests
{
  public static Test suite()
  {
    return new AllPerformanceTestsH2NonAudit().getTestSuite();
  }

  @Override
  protected void initConfigSuites(TestSuite parent)
  {
    addScenario(parent, new H2Config(), JVM, NATIVE);
  }
}
