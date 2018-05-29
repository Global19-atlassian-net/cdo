/*
 * Copyright (c) 2013, 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.expressions;

/**
 * A context for variable {@link #get(Object) lookups} during expression evaluations.
 *
 * @author Eike Stepper
 */
public interface EvaluationContext
{
  public Class<?> getClass(String name);

  public Object get(Object name);
}
