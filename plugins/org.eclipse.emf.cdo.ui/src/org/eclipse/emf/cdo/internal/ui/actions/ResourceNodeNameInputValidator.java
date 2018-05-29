/*
 * Copyright (c) 2010-2012, 2015, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Victor Roldan Betancort - initial API and implementation
 */
package org.eclipse.emf.cdo.internal.ui.actions;

import org.eclipse.emf.cdo.eresource.CDOResourceFolder;
import org.eclipse.emf.cdo.eresource.CDOResourceNode;
import org.eclipse.emf.cdo.internal.ui.messages.Messages;

import org.eclipse.net4j.util.StringUtil;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.jface.dialogs.IInputValidator;

/**
 * @author Victor Roldan Betancort
 */
public class ResourceNodeNameInputValidator implements IInputValidator
{
  private CDOResourceNode node;

  private boolean isFolder;

  public ResourceNodeNameInputValidator(CDOResourceNode node)
  {
    this.node = node;
    isFolder = node instanceof CDOResourceFolder;
  }

  public String isValid(String newText)
  {
    // Do not allow empty names
    if (StringUtil.isEmpty(newText))
    {
      return isFolder ? Messages.getString("NewResourceNodeAction.3") : Messages.getString("NewResourceNodeAction.4"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    // Do not allow "/"
    if (newText.contains("/") || newText.contains("\\")) //$NON-NLS-1$ //$NON-NLS-2$
    {
      return Messages.getString("NewResourceNodeAction.2"); //$NON-NLS-1$
    }

    for (EObject childObject : node.eContents())
    {
      CDOResourceNode child = (CDOResourceNode)childObject;

      if (node.isRoot() && child.getFolder() != null)
      {
        continue;
      }

      if (child.getName().equals(newText))
      {
        return isFolder ? Messages.getString("NewResourceNodeAction.5") + " " + newText //$NON-NLS-1$ //$NON-NLS-2$
            : Messages.getString("NewResourceNodeAction.6") + " " + newText;
      }
    }

    return null;
  }
}
