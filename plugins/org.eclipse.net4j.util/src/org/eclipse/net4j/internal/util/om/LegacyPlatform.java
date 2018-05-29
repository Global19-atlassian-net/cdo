/*
 * Copyright (c) 2007, 2008, 2011, 2012, 2015, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.internal.util.om;

import org.eclipse.net4j.internal.util.bundle.AbstractPlatform;
import org.eclipse.net4j.util.om.LegacyUtil;
import org.eclipse.net4j.util.om.OMBundle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Eike Stepper
 */
public class LegacyPlatform extends AbstractPlatform
{
  private Map<String, String> debugOptions = new ConcurrentHashMap<String, String>(0);

  public LegacyPlatform()
  {
  }

  public boolean isOSGiRunning()
  {
    return false;
  }

  @Override
  protected OMBundle createBundle(String bundleID, Class<?> accessor)
  {
    return new LegacyBundle(this, bundleID, accessor);
  }

  @Override
  protected String getDebugOption(String bundleID, String option)
  {
    return debugOptions.get(bundleID + "/" + option); //$NON-NLS-1$
  }

  @Override
  protected void setDebugOption(String bundleID, String option, String value)
  {
    debugOptions.put(bundleID + "/" + option, value); //$NON-NLS-1$
  }

  public String[] getCommandLineArgs()
  {
    return LegacyUtil.getCommandLineArgs();
  }

  public static boolean clearDebugOptions()
  {
    if (INSTANCE instanceof LegacyPlatform)
    {
      LegacyPlatform platform = (LegacyPlatform)INSTANCE;
      platform.debugOptions.clear();
      return true;
    }

    return false;
  }
}
