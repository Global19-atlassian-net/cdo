/*
 * Copyright (c) 2011, 2012 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.server.mongodb;

import org.eclipse.emf.cdo.server.IStoreAccessor;

/**
 * A {@link IStoreAccessor store accessor} for CDO's <a href="http://www.mongodb.org">MongoDB</a> back-end integration.
 *
 * @author Eike Stepper
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IMongoDBStoreAccessor extends IStoreAccessor
{
  public IMongoDBStore getStore();
}
