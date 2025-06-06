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

import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Consumer;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SqlTransactionTestTemplate extends SqlTransactionTestSupport {

  @Test
  @Override
  public void addBatch01() {
    invalidOperation(
        trx -> {
          trx.sql(Sql.TEMPLATE, """
          select A, B, C from FOO
          --
          where X = ?
          """);

          trx.param("ABC");

          trx.addBatch();
        },

        """
        The 'addBatch' operation cannot be executed on a SQL template.
        """
    );
  }

  @Test(description = """
  addIf
  - happy path
  - value present
  """)
  @Override
  public void addIf01() {
    assertEquals(
        preparedStatement(
            List.of(),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A, B, C from FOO
              --
              where X = ?
              """);

              trx.paramIf("SOME", true);

              return trx.query(Foo::new);
            }
        ),

        List.of()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, SOME)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test(description = """
  addIf
  - happy path
  - value absent
  """)
  public void addIf02() {
    assertEquals(
        preparedStatement(
            List.of(),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A, B, C from FOO
              --
              where X = ?
              """);

              trx.paramIf("SOME", false);

              return trx.query(Foo::new);
            }
        ),

        List.of()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test(description = """
  addIf
  - query
  - prelude with placeholders
  - value absent
  """)
  public void addIf03() {
    assertEquals(
        preparedStatement(
            List.of(),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A, B, C from FOO
              where X = ?
              --
              and Y = ?
              """);

              trx.param("XPTO");

              trx.paramIf("SOME", false);

              return trx.query(Foo::new);
            }
        ),

        List.of()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, XPTO)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test(description = """
  addIf
  - query
  - fragment 1 absent
  - fragment 2 present
  """)
  public void addIf04() {
    assertEquals(
        preparedStatement(
            List.of(),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A, B, C from FOO
              where 1 = 1
              --
              and X = ?
              --
              and Y = ?
              """);

              trx.paramIf("1", false);

              trx.paramIf("2", true);

              return trx.query(Foo::new);
            }
        ),

        List.of()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where 1 = 1 and Y = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 2)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test(description = """
  addIf
  - query
  - fragment 1 present
  - fragment 2 absent
  """)
  public void addIf05() {
    assertEquals(
        preparedStatement(
            List.of(),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A, B, C from FOO
              where 1 = 1
              --
              and X = ?
              --
              and Y = ?
              """);

              trx.param("1");

              trx.paramIf("2", false);

              return trx.query(Foo::new);
            }
        ),

        List.of()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where 1 = 1 and X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 1)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test(description = """
  addIf
  - reject use in a fragment with more than one placeholder
  """)
  public void addIf06() {
    iae(
        trx -> {
          trx.sql(Sql.TEMPLATE, """
          select A, B, C from FOO
          where 1 = 1
          --
          and X = ? and Y = ?
          """);

          trx.param("1");

          trx.paramIf("2", false);
        },

        """
        The 'addIf' operation cannot not be used with a fragment containing more than one placeholder:

        and X = ? and Y = ?
        """
    );
  }

  @Test(description = """
  addIf
  - query
  - fragment 1 present
  - fragment 2 no placeholders
  """)
  public void addIf07() {
    assertEquals(
        preparedStatement(
            List.of(),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A, B, C from FOO
              --
              where X = ?
              --
              order by C
              """);

              trx.paramIf("2", true);

              return trx.query(Foo::new);
            }
        ),

        List.of()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where X = ? order by C, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, 2)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test(description = """
  addIf
  - query
  - fragment 1 absent
  - fragment 2 present (with > 1 placeholders)
  """)
  public void addIf08() {
    assertEquals(
        preparedStatement(
            List.of(),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A, B, C from FOO
              where 1 = 1
              --
              and X = ?
              --
              and Y = ?
              and Z = ?
              """);

              trx.paramIf("X", false);
              trx.param("Y");
              trx.param("Z");

              return trx.query(Foo::new);
            }
        ),

        List.of()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where 1 = 1 and Y = ? and Z = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, Y)
        setObject(2, Z)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test(enabled = false, description = """
  addIf
  - query
  - fail if fragment has less than required args
  """, expectedExceptions = IllegalArgumentException.class)
  public void addIf09() {
    ise(
        trx -> {
          trx.sql("""
          select A, B, C from FOO
          where 1 = 1
          --
          and X = ?
          --
          and Y = ?
          and Z = ?
          """);

          trx.paramIf("X", false);
          trx.param("Y");
          // missing Z

          trx.query(Foo::new);
        },

        """
        The 'addIf' operation cannot not be used with a fragment containing more than one placeholder:

        and X = ? and Y = ?
        """
    );
  }

  @Test(enabled = false, description = """
  addIf
  - query
  - fail if fragment has less than required args
  """, expectedExceptions = IllegalArgumentException.class)
  public void addIf10() {
    addIfTransaction(trx -> {
      trx.sql(Sql.TEMPLATE, """
      select A, B, C from FOO
      where 1 = 1
      --
      and X = ?
      --
      and Y = ?
      """);

      trx.paramIf("X", true);
      // missing Y
    });
  }

  @Test
  @Override
  public void addNullable01() {
    assertEquals(
        (long) preparedStatement(
            List.of(
                Map.of("1", 256L)
            ),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A from FOO
              --
              where X = ?
              """);

              trx.param(null, Types.DATE);

              return trx.querySingleLong();
            }
        ),

        256L
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setNull(1, 91)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getLong(1)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void addNullable02() {
    assertEquals(
        (long) preparedStatement(
            List.of(
                Map.of("1", 256L)
            ),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A from FOO
              --
              where X = ?
              """);

              trx.param("ABC", Types.VARCHAR);

              return trx.querySingleLong();
            }
        ),

        256L
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, ABC)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getLong(1)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test(description = """
  batchUpdate
  - currently unsupported
  """)
  @Override
  public void batchUpdate01() {
    invalidOperation(
        trx -> {
          trx.sql(Sql.TEMPLATE, """
          select A, B, C from FOO
          --
          where X = ?
          """);

          trx.param("ABC");

          trx.batchUpdate();
        },

        """
        The 'batchUpdate' operation cannot be executed on a SQL template.
        """
    );
  }

  @Test
  @Override
  public final void batchUpdateWithResult01() {
    invalidOperation(
        trx -> {
          trx.sql(Sql.TEMPLATE, """
          select A, B, C from FOO
          --
          where X = ?
          """);

          trx.param("ABC");

          trx.batchUpdateWithResult();
        },

        """
        The 'batchUpdate' operation cannot be executed on a SQL template.
        """
    );
  }

  @Test
  @Override
  public void close01() {
    preparedStatement(
        List.of(),

        trx -> {
          trx.sql(Sql.TEMPLATE, """
              select A, B, C from FOO
              --
              order by C
              """);

          return 0;
        }
    );

    assertEquals(
        connection.toString(),

        """
        close()
        """
    );

    assertEmpty(preparedStatement);

    assertEmpty(resultSet);

    assertEmpty(statement);
  }

  @Test
  @Override
  public void query01() {
    assertEquals(
        preparedStatement(
            List.of(
                Map.of("1", 1, "2", "FOO", "3", LocalDate.of(2020, 12, 1))
            ),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A, B, C from FOO
              --
              order by C
              """);

              return trx.query(Foo::new);
            }
        ),

        List.of(
            new Foo(1, "FOO", LocalDate.of(2020, 12, 1))
        )
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO order by C, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getInt(1)
        getString(2)
        getObject(3, class java.time.LocalDate)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  @Override
  public void queryOptional01() {
    assertEquals(
        preparedStatement(
            List.of(
                Map.of("1", 1, "2", "FOO", "3", LocalDate.of(2020, 12, 1))
            ),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A, B, C from FOO
              --
              where X = ?
              """);

              trx.param("BAR");

              return trx.queryOptional(Foo::new);
            }
        ),

        Optional.of(
            new Foo(1, "FOO", LocalDate.of(2020, 12, 1))
        )
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, BAR)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getInt(1)
        getString(2)
        getObject(3, class java.time.LocalDate)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void queryOptional02() {
    assertEquals(
        preparedStatement(
            List.of(),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A, B, C from FOO
              --
              where X = ?
              """);

              trx.param("BAR");

              return trx.queryOptional(Foo::new);
            }
        ),

        Optional.empty()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, BAR)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void queryOptional03() {
    try {
      preparedStatement(
          List.of(
              Map.of("1", 1, "2", "FOO1", "3", LocalDate.of(2020, 12, 1)),
              Map.of("1", 2, "2", "FOO2", "3", LocalDate.of(2020, 12, 2))
          ),

          trx -> {
            trx.sql(Sql.TEMPLATE, """
              select A, B, C from FOO
              --
              where X = ?
              """);

            trx.param("BAR");

            return trx.queryOptional(Foo::new);
          }
      );

      Assert.fail("It should have thrown Sql.TooManyRowsException");
    } catch (Sql.TooManyRowsException expected) {

    }

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, BAR)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getInt(1)
        getString(2)
        getObject(3, class java.time.LocalDate)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  @Override
  public void queryOptionalInt01() {
    assertEquals(
        preparedStatement(
            List.of(
                Map.of("1", 1)
            ),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A from FOO
              --
              where X = ?
              """);

              trx.param("BAR");

              return trx.queryOptionalInt();
            }
        ),

        OptionalInt.of(1)
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, BAR)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getInt(1)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void queryOptionalInt02() {
    assertEquals(
        preparedStatement(
            List.of(),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A from FOO
              --
              where X = ?
              """);

              trx.param("BAR");

              return trx.queryOptionalInt();
            }
        ),

        OptionalInt.empty()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, BAR)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void queryOptionalInt03() {
    try {
      preparedStatement(
          List.of(
              Map.of("1", 1),
              Map.of("1", 12)
          ),

          trx -> {
            trx.sql(Sql.TEMPLATE, """
              select A from FOO
              --
              where X = ?
              """);

            trx.param("BAR");

            return trx.queryOptionalInt();
          }
      );

      Assert.fail("It should have thrown Sql.TooManyRowsException");
    } catch (Sql.TooManyRowsException expected) {

    }

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, BAR)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getInt(1)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  @Override
  public void queryOptionalLong01() {
    assertEquals(
        preparedStatement(
            List.of(
                Map.of("1", 1L)
            ),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A from FOO
              --
              where X = ?
              """);

              trx.param("BAR");

              return trx.queryOptionalLong();
            }
        ),

        OptionalLong.of(1L)
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, BAR)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getLong(1)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void queryOptionalLong02() {
    assertEquals(
        preparedStatement(
            List.of(),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A from FOO
              --
              where X = ?
              """);

              trx.param("BAR");

              return trx.queryOptionalLong();
            }
        ),

        OptionalLong.empty()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, BAR)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void queryOptionalLong03() {
    try {
      preparedStatement(
          List.of(
              Map.of("1", 1L),
              Map.of("1", 12L)
          ),

          trx -> {
            trx.sql(Sql.TEMPLATE, """
              select A from FOO
              --
              where X = ?
              """);

            trx.param("BAR");

            return trx.queryOptionalLong();
          }
      );

      Assert.fail("It should have thrown Sql.TooManyRowsException");
    } catch (Sql.TooManyRowsException expected) {

    }

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, BAR)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getLong(1)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  @Override
  public void querySingle01() {
    assertEquals(
        preparedStatement(
            List.of(
                Map.of("1", 1, "2", "FOO", "3", LocalDate.of(2020, 12, 1))
            ),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A, B, C from FOO
              --
              where X = ?
              """);

              trx.param("BAR");

              return trx.querySingle(Foo::new);
            }
        ),

        new Foo(1, "FOO", LocalDate.of(2020, 12, 1))
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, BAR)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getInt(1)
        getString(2)
        getObject(3, class java.time.LocalDate)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void querySingle02() {
    try {
      preparedStatement(
          List.of(),

          trx -> {
            trx.sql(Sql.TEMPLATE, """
            select A, B, C from FOO
            --
            where X = ?
            """);

            trx.param("BAR");

            return trx.querySingle(Foo::new);
          }
      );

      Assert.fail("It should have thrown Sql.NoSuchRowException");
    } catch (Sql.NoSuchRowException expected) {

    }

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, BAR)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void querySingle03() {
    try {
      preparedStatement(
          List.of(
              Map.of("1", 1, "2", "FOO1", "3", LocalDate.of(2020, 12, 1)),
              Map.of("1", 2, "2", "FOO2", "3", LocalDate.of(2020, 12, 2))
          ),

          trx -> {
            trx.sql(Sql.TEMPLATE, """
            select A, B, C from FOO
            --
            where X = ?
            """);

            trx.param("BAR");

            return trx.querySingle(Foo::new);
          }
      );

      Assert.fail("It should have thrown Sql.TooManyRowsException");
    } catch (Sql.TooManyRowsException expected) {

    }

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, BAR)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getInt(1)
        getString(2)
        getObject(3, class java.time.LocalDate)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  @Override
  public void querySingleInt01() {
    assertEquals(
        (int) preparedStatement(
            List.of(
                Map.of("1", 256)
            ),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A from FOO
              --
              where X = ?
              """);

              trx.paramIf("SOME", true);

              return trx.querySingleInt();
            }
        ),

        256
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, SOME)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getInt(1)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void querySingleInt02() {
    try {
      preparedStatement(
          List.of(),

          trx -> {
            trx.sql(Sql.TEMPLATE, """
            select A from FOO
            --
            where X = ?
            """);

            trx.paramIf("SOME", true);

            return trx.querySingleInt();
          }
      );

      Assert.fail("It should have thrown Sql.NoSuchRowException");
    } catch (Sql.NoSuchRowException expected) {

    }

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, SOME)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void querySingleInt03() {
    try {
      preparedStatement(
          List.of(
              Map.of("1", 256),
              Map.of("1", 512)
          ),

          trx -> {
            trx.sql(Sql.TEMPLATE, """
            select A from FOO
            --
            where X = ?
            """);

            trx.paramIf("SOME", true);

            return trx.querySingleInt();
          }
      );

      Assert.fail("It should have thrown Sql.TooManyRowsException");
    } catch (Sql.TooManyRowsException expected) {

    }

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, SOME)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getInt(1)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  @Override
  public void querySingleLong01() {
    assertEquals(
        (long) preparedStatement(
            List.of(
                Map.of("1", 256L)
            ),

            trx -> {
              trx.sql(Sql.TEMPLATE, """
              select A from FOO
              --
              where X = ?
              """);

              trx.paramIf("SOME", true);

              return trx.querySingleLong();
            }
        ),

        256L
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, SOME)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getLong(1)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void querySingleLong02() {
    try {
      preparedStatement(
          List.of(),

          trx -> {
            trx.sql(Sql.TEMPLATE, """
            select A from FOO
            --
            where X = ?
            """);

            trx.paramIf("SOME", true);

            return trx.querySingleLong();
          }
      );

      Assert.fail("It should have thrown Sql.NoSuchRowException");
    } catch (Sql.NoSuchRowException expected) {

    }

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, SOME)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test
  public void querySingleLong03() {
    try {
      preparedStatement(
          List.of(
              Map.of("1", 256L),
              Map.of("1", 512L)
          ),

          trx -> {
            trx.sql(Sql.TEMPLATE, """
            select A from FOO
            --
            where X = ?
            """);

            trx.paramIf("SOME", true);

            return trx.querySingleLong();
          }
      );

      Assert.fail("It should have thrown Sql.TooManyRowsException");
    } catch (Sql.TooManyRowsException expected) {

    }

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A from FOO where X = ?, 2)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setObject(1, SOME)
        executeQuery()
        close()
        """
    );

    assertEquals(
        resultSet.toString(),

        """
        next()
        getLong(1)
        next()
        close()
        """
    );

    assertEmpty(statement);
  }

  @Test(description = """
  update
  - currently unsupported
  """)
  @Override
  public void update01() {
    invalidOperation(
        trx -> {
          trx.sql(Sql.TEMPLATE, """
          select A, B, C from FOO
          --
          where X = ?
          """);

          trx.param("ABC");

          trx.update();
        },

        """
        The 'update' operation cannot be executed on a SQL template.
        """
    );
  }

  private void addIfTransaction(Consumer<SqlTransaction> config) {
    connection = new TestingConnection();

    preparedStatement = new TestingPreparedStatement();

    resultSet = new TestingResultSet();

    preparedStatement.queries(resultSet);

    connection.preparedStatements(preparedStatement);

    SqlTransaction trx;
    trx = trx(connection);

    try {
      config.accept(trx);

      List<Foo> result;
      result = trx.query(Foo::new);

      assertEquals(result.size(), 0);
    } finally {
      trx.close();
    }
  }

}