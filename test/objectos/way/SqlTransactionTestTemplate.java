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
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import org.testng.annotations.Test;

public class SqlTransactionTestTemplate extends SqlTransactionTestSupport {

  private record Foo(Integer a, String b, LocalDate c) {
    Foo(ResultSet rs, int idx) throws SQLException {
      this(rs.getInt(idx++), rs.getString(idx++), rs.getObject(idx++, LocalDate.class));
    }
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

              trx.addIf("SOME", true);

              return trx.query(Foo::new);
            }
        ),

        List.of()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where X = ?, 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setString(1, SOME)
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

              trx.addIf("SOME", false);

              return trx.query(Foo::new);
            }
        ),

        List.of()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO, 2)
        setAutoCommit(true)
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

              trx.add("XPTO");

              trx.addIf("SOME", false);

              return trx.query(Foo::new);
            }
        ),

        List.of()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where X = ?, 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setString(1, XPTO)
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

              trx.addIf("1", false);

              trx.addIf("2", true);

              return trx.query(Foo::new);
            }
        ),

        List.of()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where 1 = 1 and Y = ?, 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setString(1, 2)
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

              trx.add("1");

              trx.addIf("2", false);

              return trx.query(Foo::new);
            }
        ),

        List.of()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where 1 = 1 and X = ?, 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setString(1, 1)
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

          trx.add("1");

          trx.addIf("2", false);
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

              trx.addIf("2", true);

              return trx.query(Foo::new);
            }
        ),

        List.of()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where X = ? order by C, 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setString(1, 2)
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

              trx.addIf("X", false);
              trx.add("Y");
              trx.add("Z");

              return trx.query(Foo::new);
            }
        ),

        List.of()
    );

    assertEquals(
        connection.toString(),

        """
        prepareStatement(select A, B, C from FOO where 1 = 1 and Y = ? and Z = ?, 2)
        setAutoCommit(true)
        close()
        """
    );

    assertEquals(
        preparedStatement.toString(),

        """
        setString(1, Y)
        setString(2, Z)
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

          trx.addIf("X", false);
          trx.add("Y");
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
      trx.sql("""
      select A, B, C from FOO
      where 1 = 1
      --
      and X = ?
      --
      and Y = ?
      """);

      trx.addIf("X", true);
      // missing Y
    });
  }

  @Override
  public void batchUpdate01() {
    // TODO
  }

  @Override
  public void update01() {
    // TODO
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