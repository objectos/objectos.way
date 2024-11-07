/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Objects;
import javax.sql.DataSource;

final class SqlDatabaseConfig implements Sql.Database.Config {

  DataSource dataSource;

  Note.Sink noteSink = Note.NoOpSink.INSTANCE;

  @Override
  public final void dataSource(DataSource value) {
    dataSource = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void noteSink(Note.Sink value) {
    noteSink = Objects.requireNonNull(value, "value == null");
  }

  final SqlDatabase build() {
    try (Connection connection = dataSource.getConnection()) {
      DatabaseMetaData data;
      data = connection.getMetaData();

      Note.Ref3<DatabaseMetaData, String, String> metadata;
      metadata = Note.Ref3.create(Sql.class, "Database metadata", Note.DEBUG);

      noteSink.send(metadata, data, data.getDatabaseProductName(), data.getDatabaseProductVersion());

      SqlDialect dialect;
      dialect = SqlDialect.of(data);

      return new SqlDatabase(dataSource, dialect, noteSink);
    } catch (SQLException e) {
      throw new Sql.DatabaseException(e);
    }
  }

}