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

import java.sql.Connection;
import java.sql.SQLException;
import objectos.way.Note.Sink;

final class SqlMigrator implements Sql.Migrator, AutoCloseable {

  @SuppressWarnings("unused")
  private final Note.Sink noteSink;

  private final SqlDialect dialect;

  private final SqlTransaction trx;

  SqlMigrator(Sink noteSink, SqlDialect dialect, Connection connection) {
    this.noteSink = noteSink;

    this.dialect = dialect;

    trx = new SqlTransaction(dialect, connection);
  }

  public final void initialize() {
    dialect.migratorInitialize(trx);
  }

  @Override
  public final void add(String name, String script) {

  }

  @Override
  public final void close() throws SQLException {}

}