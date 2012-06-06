/*
 * Copyright (c) 2004 - 2012 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.internal.ui.views;

import org.eclipse.net4j.acceptor.IAcceptor;
import org.eclipse.net4j.ui.Net4jItemProvider;
import org.eclipse.net4j.ui.shared.SharedIcons;
import org.eclipse.net4j.util.container.IContainer;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.eclipse.net4j.util.ui.container.ElementWizardAction;
import org.eclipse.net4j.util.ui.views.ContainerItemProvider;
import org.eclipse.net4j.util.ui.views.ContainerView;
import org.eclipse.net4j.util.ui.views.IElementFilter;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.spi.net4j.AcceptorFactory;

/**
 * @author Eike Stepper
 */
public class AcceptorsView extends ContainerView
{
  public final static String ID = "org.eclipse.net4j.AcceptorsView"; //$NON-NLS-1$

  private IAction newAcceptorAction = new ElementWizardAction(getShell(), "New Acceptor", "Open a new Net4j acceptor",
      SharedIcons.getDescriptor(SharedIcons.ETOOL_ADD_ACCEPTOR), AcceptorFactory.PRODUCT_GROUP, getContainer(), "tcp")
  {
    @Override
    public String getDefaultDescription(String factoryType)
    {
      if ("tcp".equals(factoryType))
      {
        return "0.0.0.0:2036";
      }

      return null;
    }
  };

  //  private Action addAcceptorAction2036 = new SafeAction(Messages.getString("AcceptorsView_0"), //$NON-NLS-1$
  //      Messages.getString("AcceptorsView_1"), //$NON-NLS-1$
  // getAddImageDescriptor())
  // {
  // @Override
  // protected void safeRun() throws Exception
  // {
  //      Net4jUtil.getAcceptor(IPluginContainer.INSTANCE, "tcp", "0.0.0.0:2036"); //$NON-NLS-1$ //$NON-NLS-2$
  // }
  // };
  //
  // private Action addAcceptorAction2037 = new SafeAction(Messages.getString("AcceptorsView_4"),
  //      Messages.getString("AcceptorsView_5"), //$NON-NLS-1$
  // getAddImageDescriptor())
  // {
  // @Override
  // protected void safeRun() throws Exception
  // {
  //      Net4jUtil.getAcceptor(IPluginContainer.INSTANCE, "tcp", "0.0.0.0:2037"); //$NON-NLS-1$ //$NON-NLS-2$
  // }
  // };

  public AcceptorsView()
  {
  }

  @Override
  protected IManagedContainer getContainer()
  {
    return IPluginContainer.INSTANCE;
  }

  @Override
  protected ContainerItemProvider<IContainer<Object>> createContainerItemProvider()
  {
    return new Net4jItemProvider(new IElementFilter()
    {
      public boolean filter(Object element)
      {
        return element instanceof IAcceptor;
      }
    });
  }

  @Override
  protected void fillLocalToolBar(IToolBarManager manager)
  {
    manager.add(newAcceptorAction);
    super.fillLocalToolBar(manager);
  }
}
