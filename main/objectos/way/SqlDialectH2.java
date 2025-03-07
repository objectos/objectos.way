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

final class SqlDialectH2 extends SqlDialect {

  @Override
  final void migratorInitialize(SqlTransaction trx) {
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
  }

  @Override
  final void paginate(StringBuilder builder, int offset, int size) {
    if (offset > 0) {
      builder.append("offset ");

      builder.append(offset);

      builder.append(" rows");

      builder.append(System.lineSeparator());
    }

    builder.append("fetch first ");

    builder.append(size);

    builder.append(" rows only");

    builder.append(System.lineSeparator());
  }

}