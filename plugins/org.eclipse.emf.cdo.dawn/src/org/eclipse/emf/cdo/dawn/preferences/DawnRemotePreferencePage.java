/*
 * Copyright (c) 2010-2012, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Martin Fluegge - initial API and implementation
 */
package org.eclipse.emf.cdo.dawn.preferences;

import org.eclipse.emf.cdo.dawn.DawnRuntimePlugin;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author Martin Fluegge
 */
public class DawnRemotePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{

  public DawnRemotePreferencePage()
  {
    super(GRID);
    AbstractUIPlugin pluginInstance = DawnRuntimePlugin.getDefault();
    setPreferenceStore(pluginInstance.getPreferenceStore()); // TODO generation
    setDescription("Connect to a CDO repository.");
  }

  /**
   * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various
   * types of preferences. Each field editor knows how to save and restore itself.
   */
  @Override
  public void createFieldEditors()
  {
    addField(new StringFieldEditor(PreferenceConstants.P_SERVER_NAME, "Server Name:", getFieldEditorParent()));
    addField(new IntegerFieldEditor(PreferenceConstants.P_SERVER_PORT, "Server Port:", getFieldEditorParent()));
    addField(new StringFieldEditor(PreferenceConstants.P_REPOSITORY_NAME, "Repository:", getFieldEditorParent()));
    // TODO change this to a list of repositories to allow auto connection for the clients
    addField(new StringFieldEditor(PreferenceConstants.P_PROTOCOL, "Protocol:", getFieldEditorParent()));
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
   */
  public void init(IWorkbench workbench)
  {
  }

}
