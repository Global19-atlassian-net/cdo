/*
 * Copyright (c) 2011, 2012, 2016, 2017 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.tests.mongodb;

import org.eclipse.emf.cdo.server.IStore;
import org.eclipse.emf.cdo.server.mongodb.CDOMongoDBUtil;
import org.eclipse.emf.cdo.tests.config.impl.RepositoryConfig;

import org.eclipse.net4j.util.WrappedException;
import org.eclipse.net4j.util.container.IPluginContainer;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("deprecation")
public class MongoDBConfig extends RepositoryConfig
{
  public static final String STORE_NAME = "MongoDB";

  private static final long serialVersionUID = 1L;

  public MongoDBConfig()
  {
    super(STORE_NAME);
  }

  @Override
  protected String getStoreName()
  {
    return STORE_NAME;
  }

  @Override
  public void setUp() throws Exception
  {
    CDOMongoDBUtil.prepareContainer(IPluginContainer.INSTANCE);
    super.setUp();
  }

  public IStore createStore(String repoName)
  {
    MongoURI mongoURI = new MongoURI("mongodb://localhost");
    if (!isRestarting())
    {
      dropDatabase(mongoURI, repoName);
    }

    return CDOMongoDBUtil.createStore(mongoURI.toString(), repoName);
  }

  protected void dropDatabase(MongoURI mongoURI, String repoName)
  {
    Mongo mongo = null;

    try
    {
      mongo = new Mongo(mongoURI);
      DB db = mongo.getDB(repoName);
      if (!db.getCollectionNames().isEmpty())
      {
        db.dropDatabase();
      }
    }
    catch (Exception ex)
    {
      throw WrappedException.wrap(ex);
    }
    finally
    {
      if (mongo != null)
      {
        mongo.close();
      }
    }
  }

  @Override
  public boolean supportingExtRefs()
  {
    return false;
  }
}
