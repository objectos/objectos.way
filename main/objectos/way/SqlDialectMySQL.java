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

import java.util.List;
import objectos.way.Sql.MetaTable;

final class SqlDialectMySQL extends SqlDialect {

  @Override
  final SqlMigrator.Initialized migratorInitialize(SqlMigrator migrator) {
    final SqlTransaction trx;
    trx = migrator.trx();

    final Sql.Meta meta;
    meta = trx.meta();

    final List<MetaTable> tables;
    tables = meta.queryTables(filter -> {
      filter.tableName("SCHEMA_HISTORY");
    });

    if (tables.isEmpty()) {

      trx.sql(Sql.SCRIPT, """
      create table if not exists SCHEMA_HISTORY (
        INSTALLED_RANK int not null,
        DESCRIPTION varchar(200) not null,
        INSTALLED_BY varchar(100) not null,
        INSTALLED_ON timestamp default current_timestamp,
        EXECUTION_TIME int not null,
        SUCCESS boolean not null,

        constraint SCHEMA_HISTORY_PK primary key (INSTALLED_RANK)
      );

      create index SCHEMA_HISTORY_S_IDX on SCHEMA_HISTORY (SUCCESS);
      """);

      trx.batchUpdate();

      migratorHistory(migrator, 0, "SCHEMA_HISTORY table created", true);

    }

    trx.sql("""
    select
      not exists(select 1 from SCHEMA_HISTORY where SUCCESS = false),
      max(INSTALLED_RANK),
      user()
    from
      SCHEMA_HISTORY
    """);

    final SqlMigrator.Initialized result;
    result = trx.querySingle(SqlMigrator.Initialized::new);

    trx.commit();

    return result;
  }

  @Override
  final void migratorHistory(SqlMigrator migrator, int rank, String name, boolean success) {
    final SqlTransaction trx;
    trx = migrator.trx();

    trx.sql("""
    insert into SCHEMA_HISTORY
    values (?, ?, user(), ?, 0, ?)
    """);

    trx.add(rank);

    trx.add(name);

    trx.add(migrator.now());

    trx.add(success);

    trx.update();
  }

  @Override
  final void paginate(StringBuilder builder, int offset, int size) {
    builder.append("limit ");

    builder.append(size);

    builder.append(System.lineSeparator());

    if (offset > 0) {
      builder.append("offset ");

      builder.append(offset);

      builder.append(System.lineSeparator());
    }
  }

}