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

import static org.testng.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.testng.annotations.Test;

public class SqlDialectTest03TrxClose extends SqlDialectTest00Support {

  private record TableT(int x) {
    TableT(ResultSet rs, int idx) throws SQLException {
      this(rs.getInt(idx++));
    }
  }

  @SuppressWarnings("exports")
  @Test(description = "commit", dataProvider = "dbDialectProvider")
  public void onClose01(Sql.Database db, SqlDialect dialect) {
    try (final Sql.Transaction trx = connect(db, dialect)) {
      trx.sql("insert into T values (?)");

      trx.param(123);

      trx.update();

      List<TableT> result;
      result = queryT(trx);

      assertEquals(result.size(), 1);
      assertEquals(result.get(0).x, 123);

      trx.commit();
    }

    try (final Sql.Transaction trx = db.connect()) {
      List<TableT> result;
      result = queryT(trx);

      assertEquals(result.size(), 1);
      assertEquals(result.get(0).x, 123);

      trx.commit();
    }
  }

  @SuppressWarnings("exports")
  @Test(description = "rollback", dataProvider = "dbDialectProvider")
  public void onClose02(Sql.Database db, SqlDialect dialect) {
    try (final Sql.Transaction trx = connect(db, dialect)) {
      trx.sql("insert into T values (?)");

      trx.param(123);

      trx.update();

      List<TableT> result;
      result = queryT(trx);

      assertEquals(result.size(), 1);
      assertEquals(result.get(0).x, 123);

      trx.rollback();
    }

    try (final Sql.Transaction trx = db.connect()) {
      List<TableT> result;
      result = queryT(trx);

      assertEquals(result.size(), 0);

      trx.commit();
    }
  }

  private Sql.Transaction connect(Sql.Database db, SqlDialect dialect) {
    db.migrate(migrations -> {
      migrations.apply("T", """
        create table T (
        X int not null,

        primary key (X)
      );
      """);
    });

    return db.connect();
  }

  private List<TableT> queryT(Sql.Transaction trx) {
    trx.sql("select X from T order by X");

    return trx.query(TableT::new);
  }

}
