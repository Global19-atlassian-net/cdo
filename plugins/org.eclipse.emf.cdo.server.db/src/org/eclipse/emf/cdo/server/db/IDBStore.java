/***************************************************************************
 * Copyright (c) 2004 - 2007 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.emf.cdo.server.db;

import org.eclipse.emf.cdo.server.IStore;

import org.eclipse.net4j.db.ConnectionProvider;
import org.eclipse.net4j.db.IDBAdapter;


/**
 * @author Eike Stepper
 */
public interface IDBStore extends IStore
{
  public IMappingStrategy getMappingStrategy();

  public IDBAdapter getDBAdapter();

  public ConnectionProvider getConnectionProvider();
}
