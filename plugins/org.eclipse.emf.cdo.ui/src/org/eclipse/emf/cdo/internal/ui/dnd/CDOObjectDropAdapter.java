/*
 * Copyright (c) 2008-2012 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Victor Roldan Betancort - initial API and implementation
 *    Eike Stepper - maintenance
 */
package org.eclipse.emf.cdo.internal.ui.dnd;

import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.emf.cdo.CDOState;
import org.eclipse.emf.cdo.util.CDOUtil;

import org.eclipse.net4j.util.container.IContainer;
import org.eclipse.net4j.util.ui.dnd.DNDDropAdapter;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;

import java.util.ArrayList;

/**
 * @author Victor Roldan Betancort
 */
public class CDOObjectDropAdapter extends DNDDropAdapter<TreeSelection>
{
  public static final Transfer[] TRANSFERS = new Transfer[] { org.eclipse.emf.edit.ui.dnd.LocalTransfer.getInstance() };

  protected CDOObjectDropAdapter(StructuredViewer viewer)
  {
    super(TRANSFERS, viewer);
    setExpandEnabled(false);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected boolean performDrop(TreeSelection data, Object target)
  {
    if (target instanceof IContainer.Modifiable<?>)
    {
      IContainer.Modifiable<CDOObject> objectContainer = (IContainer.Modifiable<CDOObject>)target;
      ArrayList<CDOObject> elementsToAdd = new ArrayList<CDOObject>();
      for (Object obj : data.toArray())
      {
        if (isWatchable(obj))
        {
          elementsToAdd.add(CDOUtil.getCDOObject((EObject)obj));
        }
      }

      objectContainer.addAllElements(elementsToAdd);
      return true;
    }

    return false;
  }

  @Override
  protected boolean validateTarget(Object target, int operation)
  {
    return true;
  }

  public static void support(StructuredViewer viewer)
  {
    int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
    viewer.addDropSupport(dndOperations, TRANSFERS, new CDOObjectDropAdapter(viewer));
  }

  public static boolean isWatchable(Object obj)
  {
    // Only CLEAN and DIRTY CDOObjects are watchable
    if (obj instanceof EObject)
    {
      CDOObject cdoObject = CDOUtil.getCDOObject((EObject)obj);
      if (cdoObject != null)
      {
        return cdoObject.cdoState() == CDOState.CLEAN || cdoObject.cdoState() == CDOState.DIRTY;
      }
    }

    return false;
  }
}
