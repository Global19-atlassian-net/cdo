/*
 * Copyright (c) 2007, 2011-2013, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Christian W. Damus (CEA) - bug 399641: container-aware factories
 */
package org.eclipse.net4j.util.factory;

import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.container.IManagedContainerFactory;

/**
 * {@link #create(String) Creates} objects from a string {@link #getDescriptionFor(Object) description}.
 * <p>
 * If a factory is registered in an {@link IManagedContainer}, the {@link IManagedContainerFactory} extension interface
 * injects that container into the factory for it to reach back into to obtain dependencies.
 *
 * @author Eike Stepper
 * @see IManagedContainerFactory
 */
public interface IFactory
{
  public IFactoryKey getKey();

  public Object create(String description) throws ProductCreationException;

  public String getDescriptionFor(Object product);
}
