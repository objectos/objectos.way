/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.way;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.stream.Stream;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;
import org.mariadb.jdbc.MariaDbDataSource;
import org.testng.annotations.DataProvider;

public abstract class SqlDialectTest0Support {

  @DataProvider
  public Iterator<Sql.Database> dbProvider(Method method) {
    final String methodName;
    methodName = method.getName();

    final String dbName;
    dbName = toUpperSnakeCase(methodName);

    return dbs(dbName).map(ds -> {
      return Sql.Database.create(config -> {
        config.clock(Y.clockIncMinutes(2025, 3, 10));

        config.dataSource(ds);

        config.noteSink(Y.noteSink());
      });
    }).iterator();
  }

  final SqlDialect dialect(Sql.Database db) {
    final SqlDatabase impl;
    impl = (SqlDatabase) db;

    return impl.dialect();
  }

  private Stream<DataSource> dbs(String dbName) {
    return Stream.of(
        dbH2(dbName),

        dbMySQL7(dbName)
    );
  }

  private DataSource dbH2(String dbName) {
    final String url;
    url = "jdbc:h2:mem:" + dbName + ";DB_CLOSE_DELAY=-1";

    final JdbcDataSource ds;
    ds = new JdbcDataSource();

    ds.setUrl(url);

    ds.setUser("sa");

    ds.setPassword("");

    return ds;
  }

  private DataSource dbMySQL7(String dbName) {
    try (
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:17003/", "root", "");
        Statement stmt = conn.createStatement();
    ) {
      stmt.execute("drop database if exists " + dbName);
      stmt.execute("drop user if exists " + dbName);

      stmt.execute("create database " + dbName);
      stmt.execute("create user " + dbName);

      stmt.execute("grant all on " + dbName + ".* to '" + dbName + "'@'localhost'");
    } catch (SQLException e) {
      throw new AssertionError("Failed to create DataSource", e);
    }

    try {
      final String url;
      url = "jdbc:mariadb://localhost:17003/" + dbName + "?user=" + dbName;

      return new MariaDbDataSource(url);
    } catch (SQLException e) {
      throw new AssertionError("Failed to create DataSource", e);
    }
  }

  private String toUpperSnakeCase(String input) {
    final StringBuilder result;
    result = new StringBuilder();

    for (int i = 0, len = input.length(); i < len; i++) {
      final char c;
      c = input.charAt(i);

      if (i > 0) {
        char prev;
        prev = input.charAt(i - 1);

        if (Character.isUpperCase(c) || (Character.isDigit(c) && !Character.isDigit(prev))) {
          result.append('_');
        }
      }

      final char upper;
      upper = Character.toUpperCase(c);

      result.append(upper);
    }

    return result.toString();
  }

}