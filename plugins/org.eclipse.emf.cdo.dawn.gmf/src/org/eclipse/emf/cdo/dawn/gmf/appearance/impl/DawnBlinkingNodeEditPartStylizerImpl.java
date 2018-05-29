/*
 * Copyright (c) 2011, 2012 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Martin Fluegge - initial API and implementation
 */
package org.eclipse.emf.cdo.dawn.gmf.appearance.impl;

import org.eclipse.emf.cdo.dawn.gmf.appearance.DawnAppearancer;
import org.eclipse.emf.cdo.dawn.ui.DawnColorConstants;

import org.eclipse.gef.EditPart;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin Fluegge
 * @since 2.0
 */
public class DawnBlinkingNodeEditPartStylizerImpl extends DawnBasicGraphicalEditPartStylizerImpl
{
  BlinkingThread blinkingThread;

  public DawnBlinkingNodeEditPartStylizerImpl()
  {
    blinkingThread = new BlinkingThread();
    blinkingThread.start();
  }

  @Override
  public void setDefault(EditPart editPart)
  {
    blinkingThread.stop(editPart);
    setBorder(editPart, DawnColorConstants.COLOR_NO_CONFLICT, 0);
  }

  @Override
  public void setConflicted(EditPart editPart, int type)
  {
    blinkingThread.start(editPart);
  }

  @Override
  public void setLocked(EditPart editPart, int type)
  {
    setBorder(editPart, DawnColorConstants.COLOR_LOCKED_REMOTELY, DawnAppearancer.DEFAULT_BORDER_THICKNESS);
  }

  /**
   * @author Martin Fluegge
   * @since 2.0
   */
  protected class BlinkingThread extends Thread
  {

    Map<EditPart, Boolean> editParts = new HashMap<EditPart, Boolean>();

    @Override
    public void run()
    {
      while (true)
      {
        for (EditPart e : editParts.keySet())
        {
          if (editParts.get(e))
          {
            setBorder(e, DawnColorConstants.COLOR_NO_CONFLICT, DawnAppearancer.DEFAULT_BORDER_THICKNESS);
          }
          else
          {
            setBorder(e, DawnColorConstants.COLOR_DELETE_CONFLICT, DawnAppearancer.DEFAULT_BORDER_THICKNESS);
          }

          editParts.put(e, !editParts.get(e));
        }
        try
        {
          Thread.sleep(500);
        }
        catch (InterruptedException e1)
        {
          e1.printStackTrace();
        }
      }
    }

    /**
     * @since 2.0
     */
    public void start(EditPart editPart)
    {
      editParts.put(editPart, true);
    }

    public void stop(EditPart editPart)
    {
      editParts.remove(editPart);
    }
  }
}
