/*
 * Copyright (c) 2007, 2009, 2011, 2012, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.internal.ui.actions;

import org.eclipse.emf.cdo.session.CDOSession;

import org.eclipse.net4j.util.ui.actions.LongRunningAction;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;

/**
 * @author Eike Stepper
 */
public abstract class SessionAction extends LongRunningAction
{
  private CDOSession session;

  public SessionAction(IWorkbenchPage page, String text, String toolTipText, ImageDescriptor image, CDOSession session)
  {
    super(page, text, toolTipText, image);
    this.session = session;
  }

  public CDOSession getSession()
  {
    return session;
  }
}
