/*
 * Copyright (c) 2007-2009, 2011-2013, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Victor Roldan Betancort - maintenance
 */
package org.eclipse.emf.cdo.internal.ui.actions;

import org.eclipse.emf.cdo.internal.ui.dialogs.RollbackTransactionDialog;
import org.eclipse.emf.cdo.internal.ui.messages.Messages;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.view.CDOView;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IWorkbenchPage;

/**
 * @author Eike Stepper
 */
public final class RollbackTransactionAction extends AbstractViewAction
{
  private static final String TITLE = Messages.getString("RollbackTransactionAction.0"); //$NON-NLS-1$

  private static final String TOOL_TIP = Messages.getString("RollbackTransactionAction.1"); //$NON-NLS-1$

  public RollbackTransactionAction(IWorkbenchPage page, CDOView view)
  {
    super(page, TITLE + INTERACTIVE, TOOL_TIP, null, view);
    setEnabled(getTransaction().isDirty());
  }

  @Override
  protected void preRun() throws Exception
  {
    CDOTransaction transaction = (CDOTransaction)getView();
    RollbackTransactionDialog dialog = new RollbackTransactionDialog(getPage(), TITLE, TOOL_TIP, transaction);
    if (dialog.open() != RollbackTransactionDialog.OK)
    {
      cancel();
    }
  }

  @Override
  protected void doRun(IProgressMonitor progressMonitor) throws Exception
  {
    getTransaction().rollback();
  }
}
