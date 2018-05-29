/*
 * Copyright (c) 2011, 2012, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Martin Fluegge - initial API and implementation
 */
package org.eclipse.emf.cdo.dawn.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Martin Fluegge
 * @since 2.0
 */
public abstract class SelectionHandler extends AbstractHandler
{
  protected List<Object> getSelectedObjects(ExecutionEvent event)
  {
    List<Object> objects = new ArrayList<Object>();
    ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
    if (selection != null & selection instanceof IStructuredSelection)
    {
      IStructuredSelection structuredSelection = (IStructuredSelection)selection;
      for (@SuppressWarnings("unchecked")
      Iterator<Object> iterator = structuredSelection.iterator(); iterator.hasNext();)
      {
        Object element = iterator.next();
        objects.add(element);
      }
    }
    return objects;
  }
}
