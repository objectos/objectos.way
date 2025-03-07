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

import org.testng.annotations.Test;

public class SqlTransactionTestScriptParsing {

  @Test
  public void testCase01() {
    test(
        "",

        """
        """
    );
  }

  @Test
  public void testCase02() {
    test(
        """
        create table X (A int)
        """,

        """
        addBatch(create table X (A int))
        """
    );
  }

  @Test
  public void testCase03() {
    test(
        """
        create table X (A int);
        create table Y (B int)
        """,

        """
        addBatch(create table X (A int))
        addBatch(create table Y (B int))
        """
    );
  }

  @Test
  public void testCase04() {
    test(
        """
        create table X (A int);
        create table Y (B int);
        """,

        """
        addBatch(create table X (A int))
        addBatch(create table Y (B int))
        """
    );
  }

  @Test
  public void testCase05() {
    test(
        """
        create table X (A int);

        create table Y (B int);
        """,

        """
        addBatch(create table X (A int))
        addBatch(create table Y (B int))
        """
    );
  }

  @Test
  public void testCase06() {
    test(
        """
        -- foo
        create table X (A int);

        -- bar
        create table Y (B int);
        """,

        """
        addBatch(create table X (A int))
        addBatch(create table Y (B int))
        """
    );
  }

  @Test
  public void testCase07() {
    test(
        """
        delete from X where C = ';'
        """,

        """
        addBatch(delete from X where C = ';')
        """
    );
  }

  @Test
  public void testCase08() {
    test(
        """
        delete from X where C = ';';

        delete from Y where D = ';';
        """,

        """
        addBatch(delete from X where C = ';')
        addBatch(delete from Y where D = ';')
        """
    );
  }

  @Test
  public void testCase09() {
    test(
        """
        /* ignore me */ create table X /*;*/ (A int);
        """,

        """
        addBatch(create table X /*;*/ (A int))
        """
    );
  }

  @Test
  public void testCase10() {
    test(
        """
        create schema TEST;

        set schema TEST;

        create table T1 (
          ID int not null,

          primary key (ID)
        );
        """,

        """
        addBatch(create schema TEST)
        addBatch(set schema TEST)
        addBatch(create table T1 (   ID int not null,   primary key (ID) ))
        """
    );
  }

  private void test(String script, String expected) {
    SqlDialectTesting dialect;
    dialect = new SqlDialectTesting();

    TestingConnection connection;
    connection = new TestingConnection();

    TestingStatement statement;
    statement = new TestingStatement();

    connection.statements(statement);

    SqlTransaction trx;
    trx = new SqlTransaction(dialect, connection);

    trx.sql(Sql.SCRIPT, script);

    assertEquals(statement.toString(), expected);
  }

}