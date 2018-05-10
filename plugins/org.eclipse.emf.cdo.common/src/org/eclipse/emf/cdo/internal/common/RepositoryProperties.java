/*
 * Copyright (c) 2011-2013, 2015, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Christian W. Damus (CEA LIST) - bug 418452
 */
package org.eclipse.emf.cdo.internal.common;

import org.eclipse.emf.cdo.common.CDOCommonRepository;

import org.eclipse.net4j.util.properties.DefaultPropertyTester;
import org.eclipse.net4j.util.properties.IProperties;

/**
 * @author Eike Stepper
 */
public class RepositoryProperties extends AbstractRepositoryProperties<CDOCommonRepository>
{
  public static final IProperties<CDOCommonRepository> INSTANCE = new RepositoryProperties();

  private RepositoryProperties()
  {
    super(CDOCommonRepository.class);
  }

  @Override
  protected CDOCommonRepository getRepository(CDOCommonRepository receiver)
  {
    return receiver;
  }

  public static void main(String[] args)
  {
    new Tester().dumpContributionMarkup();
  }

  /**
   * @author Eike Stepper
   */
  public static final class Tester extends DefaultPropertyTester<CDOCommonRepository>
  {
    public static final String NAMESPACE = "org.eclipse.emf.cdo.repository";

    public Tester()
    {
      super(NAMESPACE, INSTANCE);
    }
  }
}
