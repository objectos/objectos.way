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

import java.util.List;
import org.testng.annotations.Test;

public class SqlTransactionTestScript extends SqlTransactionTestSupport {

  @Test
  @Override
  public void addIf01() {
    uoe(
        trx -> {
          trx.sql(Sql.Kind.SCRIPT, """
          insert into FOO (A, B) values (1, 5)

          insert into BAR (X, Y) values ('A', 'B')
          """);

          trx.addIf("abc", true);
        },

        """
        The is a SQL script and the 'addIf' operation is not supported.

        To prevent this exception from being thrown:

        1) Do not use the addIf method. Or
        2) Use a SQL template instead.
        """
    );
  }

  @Test(description = """
  batchUpdate
  - happy path
  - one statement
  """)
  @Override
  public void batchUpdate01() {
    assertEquals(
        batchStatement(
            List.of(),

            batches(
                batch(1)
            ),

            trx -> {
              trx.sql(Sql.Kind.SCRIPT, """
              insert into FOO (A, B) values (1, 5)
              """);

              return trx.batchUpdate();
            }
        ),

        batch(1)
    );

    assertEquals(
        connection.toString(),

        """
        createStatement()
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        statement.toString(),

        """
        addBatch(insert into FOO (A, B) values (1, 5))
        executeBatch()
        close()
        """
    );

    assertEmpty(preparedStatement);

    assertEmpty(resultSet);
  }

  @Test(description = """
  batchUpdate
  - happy path
  - two statements
  """)
  public void batchUpdate02() {
    assertEquals(
        batchStatement(
            List.of(),

            batches(
                batch(1, 1)
            ),

            trx -> {
              trx.sql(Sql.Kind.SCRIPT, """
              insert into FOO (A, B) values (1, 5)

              insert into BAR (X, Y) values ('A', 'B')
              """);

              return trx.batchUpdate();
            }
        ),

        batch(1, 1)
    );

    assertEquals(
        connection.toString(),

        """
        createStatement()
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        statement.toString(),

        """
        addBatch(insert into FOO (A, B) values (1, 5))
        addBatch(insert into BAR (X, Y) values ('A', 'B'))
        executeBatch()
        close()
        """
    );

    assertEmpty(preparedStatement);

    assertEmpty(resultSet);
  }

  @Test
  @Override
  public final void update01() {
    uoe(
        trx -> {
          trx.sql(Sql.Kind.SCRIPT, """
          insert into FOO (A, B) values (1, 5)

          insert into BAR (X, Y) values ('A', 'B')
          """);

          trx.update();
        },

        """
        The is a SQL script and the 'update' operation is not supported.

        To prevent this exception from being thrown:

        1) Use the 'batchUpdate' method instead. Or
        2) Do not use a SQL script.
        """
    );
  }

}