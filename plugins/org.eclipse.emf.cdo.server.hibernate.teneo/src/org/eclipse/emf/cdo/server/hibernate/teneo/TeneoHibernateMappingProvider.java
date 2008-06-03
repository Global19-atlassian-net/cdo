/***************************************************************************
 * Copyright (c) 2004 - 2008 Springsite B.V. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Martin Taal - initial API and implementation
 **************************************************************************/
package org.eclipse.emf.cdo.server.hibernate.teneo;

import org.eclipse.emf.cdo.common.model.CDOPackage;
import org.eclipse.emf.cdo.server.hibernate.IHibernateMappingProvider;
import org.eclipse.emf.cdo.server.hibernate.IHibernateStore;
import org.eclipse.emf.cdo.server.internal.hibernate.HibernateUtil;
import org.eclipse.emf.cdo.server.internal.hibernate.bundle.OM;

import org.eclipse.net4j.util.WrappedException;
import org.eclipse.net4j.util.om.trace.ContextTracer;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.emf.teneo.hibernate.cdo.CDOHelper;

import org.hibernate.cfg.Configuration;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Uses the ecore string in the cdoPackages of the store to generate a mapping.
 * 
 * @author Martin Taal
 */
public class TeneoHibernateMappingProvider implements IHibernateMappingProvider
{

  private static final ContextTracer TRACER = new ContextTracer(OM.DEBUG, TeneoHibernateMappingProvider.class);

  private IHibernateStore hibernateStore;

  public void addMapping(Configuration configuration)
  {
    final String mapping = generateMapping();
    TRACER.trace("Generated hibernate mapping:");
    TRACER.trace(mapping);
    System.err.println(mapping);
    configuration.addXML(mapping);
    TRACER.trace("Added mapping to configuration");
  }

  // the passed modelObjects collection is defined as a collection of Objects
  // to prevent binary dependency on emf.
  public String generateMapping()
  {
    TRACER.trace("Generating Hibernate Mapping");
    final Properties properties = HibernateUtil.getInstance().getPropertiesFromStore(getHibernateStore());

    // TODO: handle nested package structures
    final List<EPackage> epacks = new ArrayList<EPackage>();
    final ResourceSet rs = new ResourceSetImpl();
    rs.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
    rs.getPackageRegistry().put(XMLTypePackage.eNS_URI, XMLTypePackage.eINSTANCE);
    rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new EcoreResourceFactoryImpl());
    for (CDOPackage cdoPackage : getHibernateStore().getPackageHandler().getCDOPackages())
    {
      TRACER.trace("Using cdoPackage : " + cdoPackage.getName() + " - " + cdoPackage.getPackageURI());
      final String ecoreStr = cdoPackage.getEcore();
      if (ecoreStr == null)
      {
        // happens at initialization time
        continue;
      }
      // this assumes that the (default) encoding is the same on both the client and
      // server
      final ByteArrayInputStream bis = new ByteArrayInputStream(ecoreStr.getBytes());
      // fool the resourceset by passing a fake uri
      final URI epackageURI = URI.createURI(cdoPackage.getPackageURI());
      final Resource resource = rs.createResource(epackageURI);
      try
      {
        resource.load(bis, Collections.EMPTY_MAP);

        // now the toplevel content should be EPackage
        for (Object contentObject : resource.getContents())
        {
          epacks.addAll(resolveSubPackages((EPackage)contentObject));
        }
      }
      catch (Exception e)
      {
        throw WrappedException.wrap(e);
      }
    }

    // translate the list of EPackages to an array
    final EPackage[] ePackageArray = epacks.toArray(new EPackage[epacks.size()]);
    properties.put("teneo.mapping.also_map_as_class", "false");
    return CDOHelper.getInstance().generateMapping(ePackageArray, properties);
  }

  // this will check the global package registry and read the epackages from
  // there if the epackage is already present there
  protected List<EPackage> resolveSubPackages(EPackage epack)
  {
    final List<EPackage> epacks = new ArrayList<EPackage>();
    if (EPackage.Registry.INSTANCE.get(epack.getNsURI()) != null)
    {
      epacks.add((EPackage)EPackage.Registry.INSTANCE.get(epack.getNsURI()));
    }
    else
    {
      epacks.add(epack);
    }

    for (EPackage subEPackage : epack.getESubpackages())
    {
      epacks.addAll(resolveSubPackages(subEPackage));
    }
    return epacks;
  }

  public IHibernateStore getHibernateStore()
  {
    return hibernateStore;
  }

  public void setHibernateStore(IHibernateStore hibernateStore)
  {
    this.hibernateStore = hibernateStore;
  }
}
