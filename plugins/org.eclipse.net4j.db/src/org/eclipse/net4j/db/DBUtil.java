/***************************************************************************
 * Copyright (c) 2004 - 2008 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.net4j.db;

import org.eclipse.net4j.db.ddl.IDBField;
import org.eclipse.net4j.db.ddl.IDBSchema;
import org.eclipse.net4j.db.ddl.IDBTable;
import org.eclipse.net4j.internal.db.DataSourceConnectionProvider;
import org.eclipse.net4j.internal.db.bundle.OM;
import org.eclipse.net4j.internal.db.ddl.DBSchema;
import org.eclipse.net4j.internal.util.om.trace.ContextTracer;
import org.eclipse.net4j.util.ReflectUtil;

import javax.sql.DataSource;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class DBUtil
{
  private static final ContextTracer TRACER = new ContextTracer(OM.DEBUG_SQL, DBUtil.class);

  private DBUtil()
  {
  }

  public static IDBSchema createSchema(String name)
  {
    return new DBSchema(name);
  }

  public static DataSource createDataSource(Map<Object, Object> properties)
  {
    return createDataSource(properties, null);
  }

  public static DataSource createDataSource(Map<Object, Object> properties, String namespace)
  {
    return createDataSource(properties, namespace, "class");
  }

  public static DataSource createDataSource(Map<Object, Object> properties, String namespace, String driverClassKey)
  {
    try
    {
      return (DataSource)ReflectUtil.instantiate(properties, namespace, driverClassKey, OM.class.getClassLoader());
    }
    catch (Exception ex)
    {
      throw new DBException(ex);
    }
  }

  public static IDBConnectionProvider createConnectionProvider(DataSource dataSource)
  {
    return new DataSourceConnectionProvider(dataSource);
  }

  public static IDBAdapter getDBAdapter(String adapterName)
  {
    return IDBAdapter.REGISTRY.get(adapterName);
  }

  public static Exception close(Connection connection)
  {
    if (connection != null)
    {
      try
      {
        connection.close();
      }
      catch (Exception ex)
      {
        OM.LOG.error(ex);
        return ex;
      }
    }

    return null;
  }

  public static Exception close(Statement statement)
  {
    if (statement != null)
    {
      try
      {
        statement.close();
      }
      catch (Exception ex)
      {
        OM.LOG.error(ex);
        return ex;
      }
    }

    return null;
  }

  public static Exception close(ResultSet resultSet)
  {
    if (resultSet != null)
    {
      try
      {
        Statement statement = resultSet.getStatement();
        if (statement != null)
        {
          statement.setMaxRows(0);
        }
      }
      catch (Exception ex)
      {
        OM.LOG.error(ex);
      }

      try
      {
        resultSet.close();
      }
      catch (Exception ex)
      {
        OM.LOG.error(ex);
        return ex;
      }
    }

    return null;
  }

  public static List<String> getAllTableNames(Connection connection, String dbName)
  {
    ResultSet tables = null;

    try
    {
      List<String> names = new ArrayList<String>();
      DatabaseMetaData metaData = connection.getMetaData();
      tables = metaData.getTables(dbName, null, null, new String[] { "TABLE" });
      while (tables.next())
      {
        String name = tables.getString(3);
        names.add(name);
      }

      return names;
    }
    catch (SQLException ex)
    {
      throw new DBException(ex);
    }
    finally
    {
      close(tables);
    }
  }

  public static void dropAllTables(Connection connection, String dbName)
  {
    Statement statement = null;

    try
    {
      statement = connection.createStatement();
      for (String tableName : DBUtil.getAllTableNames(connection, dbName))
      {
        String sql = "DROP TABLE " + tableName;
        DBUtil.trace(sql);
        statement.execute(sql);
      }
    }
    catch (SQLException ex)
    {
      throw new DBException(ex);
    }
    finally
    {
      DBUtil.close(statement);
    }
  }

  public static int selectMaximumInt(Connection connection, IDBField field) throws DBException
  {
    Number number = getMaximumNumber(connection, field);
    if (number instanceof Integer)
    {
      return (Integer)number;
    }
    else if (number == null)
    {
      return 0;
    }

    throw new DBException("Not an integer number: " + number);
  }

  public static long selectMaximumLong(Connection connection, IDBField field) throws DBException
  {
    Number number = getMaximumNumber(connection, field);
    if (number instanceof Long)
    {
      return (Long)number;
    }
    else if (number == null)
    {
      return 0L;
    }

    throw new DBException("Not a long number: " + number);
  }

  private static Number getMaximumNumber(Connection connection, IDBField field) throws DBException
  {
    StringBuilder builder = new StringBuilder();
    builder.append("SELECT MAX(");
    builder.append(field);
    builder.append(") FROM ");
    builder.append(field.getTable());

    String sql = builder.toString();
    trace(sql);
    Statement statement = null;
    ResultSet resultSet = null;

    try
    {
      statement = connection.createStatement();

      try
      {
        resultSet = statement.executeQuery(sql);
        if (!resultSet.next())
        {
          return null;
        }

        return (Number)resultSet.getObject(1);
      }
      catch (SQLException ex)
      {
        throw new DBException(ex);
      }
      finally
      {
        close(resultSet);
      }
    }
    catch (SQLException ex)
    {
      throw new DBException(ex);
    }
    finally
    {
      close(statement);
    }
  }

  public static int update(Connection connection, String sql)
  {
    trace(sql);
    Statement statement = null;

    try
    {
      statement = connection.createStatement();
      return statement.executeUpdate(sql);
    }
    catch (SQLException ex)
    {
      throw new DBException(ex);
    }
    finally
    {
      close(statement);
    }
  }

  public static void insertRow(Connection connection, IDBAdapter dbAdapter, IDBTable table, Object... args)
      throws DBException
  {
    IDBField[] fields = table.getFields();
    if (fields.length != args.length)
    {
      throw new IllegalArgumentException("Wrong number of args for " + table + ": " + Arrays.asList(args) + " --> "
          + Arrays.asList(table.getFields()));
    }

    StringBuilder builder = new StringBuilder();
    builder.append("INSERT INTO ");
    builder.append(table);
    builder.append(" VALUES (");

    for (int i = 0; i < fields.length; i++)
    {
      if (i > 0)
      {
        builder.append(", ");
      }

      dbAdapter.appendValue(builder, fields[i], args[i]);
    }

    builder.append(")");
    String sql = builder.toString();

    int count = update(connection, sql);
    if (count == 0)
    {
      throw new DBException("No row inserted into table " + table);
    }
  }

  public static int select(Connection connection, IDBRowHandler rowHandler, String where, IDBField... fields)
      throws DBException
  {
    IDBTable table = fields[0].getTable();
    for (int i = 1; i < fields.length; i++)
    {
      if (fields[i].getTable() != table)
      {
        throw new IllegalArgumentException("Multiple tables not allowed: " + Arrays.asList(fields));
      }
    }

    StringBuilder builder = new StringBuilder();
    builder.append("SELECT ");
    for (int i = 0; i < fields.length; i++)
    {
      if (i > 0)
      {
        builder.append(", ");
      }

      builder.append(fields[i]);
    }

    builder.append(" FROM ");
    builder.append(table);
    if (where != null)
    {
      builder.append(" WHERE ");
      builder.append(where);
    }

    String sql = builder.toString();
    trace(sql);
    Statement statement = null;
    ResultSet resultSet = null;

    try
    {
      statement = connection.createStatement();

      try
      {
        int rows = 0;
        boolean proceed = true;
        Object[] values = new Object[fields.length];
        resultSet = statement.executeQuery(sql);
        while (proceed && resultSet.next())
        {
          for (int i = 0; i < fields.length; i++)
          {
            values[i] = resultSet.getObject(i + 1);
            if (values[i] instanceof Clob)
            {
              Clob clob = (Clob)values[i];
              long length = clob.length();
              if (length > Integer.MAX_VALUE)
              {
                throw new IllegalStateException("String too long: " + length);
              }

              values[i] = clob.getSubString(1, (int)length);
            }
          }

          proceed = rowHandler.handle(rows++, values);
        }

        return rows;
      }
      catch (SQLException ex)
      {
        throw new DBException(ex);
      }
      finally
      {
        close(resultSet);
      }
    }
    catch (SQLException ex)
    {
      throw new DBException(ex);
    }
    finally
    {
      close(statement);
    }
  }

  public static int select(Connection connection, IDBRowHandler rowHandler, IDBField... fields) throws DBException
  {
    return select(connection, rowHandler, null, fields);
  }

  public static Object[] select(Connection connection, String where, IDBField... fields) throws DBException
  {
    final Object[][] result = new Object[1][];
    IDBRowHandler rowHandler = new IDBRowHandler()
    {
      public boolean handle(int row, Object... values)
      {
        result[0] = values;
        return false;
      }
    };

    select(connection, rowHandler, where, fields);
    return result[0];
  }

  public static void trace(String sql)
  {
    if (TRACER.isEnabled())
    {
      TRACER.trace(sql);
    }
  }
}
