/*
 * Copyright (c) 2015, 2016 Eike Stepper (Loehne, Germany) and others.
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
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.tests.AbstractCDOTest;
import org.eclipse.emf.cdo.tests.config.impl.ConfigTest.Requires;
import org.eclipse.emf.cdo.tests.config.impl.RepositoryConfig;
import org.eclipse.emf.cdo.util.CDOURIData;
import org.eclipse.emf.cdo.view.CDOView;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import java.util.Collections;

/**
 * Bug 458149 about {@link CDOResource#getURI()} call when the {@link CDOResource} is removed from its {@link ResourceSet}.
 *
 * @author Esteban Dugueperoux
 */
@Requires("TCP")
public class Bugzilla_458149_Test extends AbstractCDOTest
{
  /**
   * Test {@link CDOResource#getURI()} when the resource is in a {@link ResourceSet} and also once removed from it.
   */
  public void testCDOResource_getURI() throws Exception
  {
    getRepository();
    ResourceSet resourceSet = new ResourceSetImpl();

    URI sharedResourceURI = URI.createURI(CDONet4jUtil.PROTOCOL_TCP + "://localhost/" + RepositoryConfig.REPOSITORY_NAME + getResourcePath("/sharedResource"))
        .appendQuery(CDOURIData.TRANSACTIONAL_PARAMETER + "=true");
    Resource sharedResource = resourceSet.createResource(sharedResourceURI);
    sharedResource.save(Collections.emptyMap());
    assertEquals(sharedResourceURI, sharedResource.getURI());

    CDOView view = ((CDOResource)sharedResource).cdoView();
    view.close();
    view.getSession().close();

    resourceSet.getResources().remove(sharedResource);
    assertEquals(sharedResourceURI, sharedResource.getURI());
  }

  /**
   * Test {@link XMIResource#getURI()} when  the resource is in a {@link ResourceSet} and also once removed from it.
   */
  public void testXMIResource_getURI() throws Exception
  {
    Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
    registry.getExtensionToFactoryMap().put("model1", new XMIResourceFactoryImpl());
    ResourceSet resourceSet = new ResourceSetImpl();

    URI localResourceURI = URI.createFileURI(createTempFile(getName(), ".model1").getCanonicalPath());
    Resource localResource = resourceSet.createResource(localResourceURI);
    localResource.save(Collections.emptyMap());
    assertEquals(localResourceURI, localResource.getURI());

    resourceSet.getResources().remove(localResource);
    assertEquals(localResourceURI, localResource.getURI());
  }
}
