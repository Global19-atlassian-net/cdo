/*
 * Copyright (c) 2010-2012 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Martin Fluegge - initial API and implementation
 */
package org.eclipse.emf.cdo.dawn;

import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.osgi.framework.BundleContext;

/**
 * @author Martin Fluegge
 */
public class DawnRuntimePlugin extends AbstractUIPlugin
{
  public static final String PLUGIN_ID = "org.eclipse.emf.cdo.dawn";

  private static DawnRuntimePlugin plugin;

  @Override
  public void start(BundleContext context) throws Exception
  {
    super.start(context);
    plugin = this;
  }

  @Override
  public void stop(BundleContext context) throws Exception
  {
    plugin = null;
    super.stop(context);
  }

  public static DawnRuntimePlugin getDefault()
  {
    return plugin;
  }
}
