/*
 * Copyright (c) 2012 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Esteban Dugueperoux - initial API and implementation
 */
package org.eclipse.emf.cdo.tests.bugzilla;

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.tests.AbstractCDOTest;
import org.eclipse.emf.cdo.tests.model1.Product1;
import org.eclipse.emf.cdo.tests.model1.VAT;
import org.eclipse.emf.cdo.transaction.CDOTransaction;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Esteban Dugueperoux
 */
public class Bugzilla_376610_Test extends AbstractCDOTest
{
  public void testRemoveOneEnumLiteral() throws Throwable
  {
    Product1 product = getModel1Factory().createProduct1();
    product.setName("test1");
    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource mainResource = transaction.createResource(getResourcePath("/mainResource.model1"));
    mainResource.getContents().add(product);
    transaction.commit();

    // Add several enum literals
    Collection<VAT> vats = new ArrayList<VAT>();
    vats.add(VAT.VAT0);
    vats.add(VAT.VAT7);
    vats.add(VAT.VAT15);
    product.getOtherVATs().addAll(vats);

    // Remove them
    product.getOtherVATs().remove(VAT.VAT15);
  }

  public void testRemoveAllEnumLiterals() throws Throwable
  {
    Product1 product = getModel1Factory().createProduct1();
    product.setName("test1");
    CDOSession session = openSession();
    CDOTransaction transaction = session.openTransaction();
    CDOResource mainResource = transaction.createResource(getResourcePath("/mainResource.model1"));
    mainResource.getContents().add(product);
    transaction.commit();

    // Add several enum literals
    Collection<VAT> vats = new ArrayList<VAT>();
    vats.add(VAT.VAT0);
    vats.add(VAT.VAT7);
    vats.add(VAT.VAT15);
    product.getOtherVATs().addAll(vats);

    // Remove them
    product.getOtherVATs().removeAll(vats);
  }
}
