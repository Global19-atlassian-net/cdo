/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Esteban Dugueperoux - initial API and implementation
 */
package org.eclipse.emf.cdo.transaction;

import org.eclipse.net4j.util.collection.Pair;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;

import java.util.Collection;

/**
 * Interface to externalize the cleaning of stale references created on invalidation.
 *
 * @author Esteban Dugueperoux
 * @since 4.4
 */
public interface CDOStaleReferenceCleaner
{
  public static final CDOStaleReferenceCleaner DEFAULT = new Default();

  /**
   * Clean the stale references created on invalidation.
   *
   * @param objectsToBeRemoved {@link Collection} of {@link Pair} from {@link Setting} to detached objects
   */
  public void cleanStaleReferences(Collection<Pair<Setting, EObject>> objectsToBeRemoved);

  /**
   * A default {@link CDOStaleReferenceCleaner} that can be used on invalidation.
   *
   * @author Esteban Dugueperoux
   */
  public static class Default implements CDOStaleReferenceCleaner
  {
    public void cleanStaleReferences(Collection<Pair<Setting, EObject>> objectsToBeRemoved)
    {
      for (Pair<Setting, EObject> pair : objectsToBeRemoved)
      {
        EcoreUtil.remove(pair.getElement1(), pair.getElement2());
      }
    }
  }
}
