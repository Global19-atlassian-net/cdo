/*
 * Copyright (c) 2007, 2008, 2011, 2012, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.jms.internal.server.store;

import org.eclipse.net4j.jms.server.IStore;
import org.eclipse.net4j.util.lifecycle.Lifecycle;

/**
 * @author Eike Stepper
 */
public abstract class AbstractStore extends Lifecycle implements IStore
{
  private String storeType;

  private String instanceID;

  public AbstractStore(String storeType)
  {
    this.storeType = storeType;
  }

  public String getStoreType()
  {
    return storeType;
  }

  public String getInstanceID()
  {
    return instanceID;
  }

  public void setInstanceID(String instanceID)
  {
    this.instanceID = instanceID;
  }
}
