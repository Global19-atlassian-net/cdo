/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.explorer.ui.handlers;

import org.eclipse.emf.cdo.explorer.repositories.CDORepositoryElement;
import org.eclipse.emf.cdo.explorer.ui.checkouts.wizards.CheckoutWizard;

import org.eclipse.net4j.util.ui.handlers.AbstractBaseHandler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Eike Stepper
 */
public class RepositoryCheckoutHandler extends AbstractBaseHandler<CDORepositoryElement>
{
  public RepositoryCheckoutHandler()
  {
    super(CDORepositoryElement.class, false);
  }

  @Override
  protected void doExecute(ExecutionEvent event, IProgressMonitor monitor) throws Exception
  {
    Shell shell = HandlerUtil.getActiveShell(event);
    checkout(shell, elements.get(0));
  }

  public static void checkout(final Shell shell, final CDORepositoryElement repositoryElement)
  {
    shell.getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        CheckoutWizard wizard = new CheckoutWizard();
        wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(repositoryElement));

        WizardDialog dialog = new WizardDialog(shell, wizard);
        dialog.open();
      }
    });
  }
}
