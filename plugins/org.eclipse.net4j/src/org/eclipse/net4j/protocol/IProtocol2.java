/*
 * Copyright (c) 2012 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.protocol;

/**
 * A {@link #getVersion() versioned} {@link IProtocol protocol}.
 *
 * @author Eike Stepper
 * @since 4.2
 */
public interface IProtocol2<INFRA_STRUCTURE> extends IProtocol<INFRA_STRUCTURE>
{
  public static final int UNSPECIFIED_VERSION = 0;

  public int getVersion();
}
