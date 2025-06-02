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

public class WebFormSpecTest {

  @Test(description = "textInput: first render")
  public void textInput01() {
    Web.FormSpec spec;
    spec = Web.FormSpec.create(config -> {
      config.action("/test");

      config.textInput(input -> {
        input.label("First name");
        input.id("some_id");
        input.name("first_name");
      });
    });

    Web.Form form;
    form = Web.Form.of(spec);

    testWebForm(form, """
    # Form

    /test      | true

    # Fields

    TextInput  | First name |                 | some_id    | first_name | text
    """);
  }

  @Test(description = "textInput: successful parse")
  public void textInput02() {
    Web.FormSpec spec;
    spec = Web.FormSpec.create(config -> {
      config.action("/test");

      config.textInput(input -> {
        input.label("First name");
        input.id("some_id");
        input.name("first_name");
        input.required();
        input.maxLength(30);
      });
    });

    Http.Exchange http;
    http = Http.Exchange.create(config -> {
      config.formParam("first_name", "First");
    });

    Web.Form form;
    form = spec.parse(http);

    testWebForm(form, """
    # Form

    /test      | true

    # Fields

    TextInput  | First name | First           | some_id    | first_name | text
    """);

    testSql(form, """
    setObject(1, First)
    executeUpdate()
    close()
    """);
  }

  @Test(description = "textInput: fail required")
  public void textInput03() {
    Web.FormSpec spec;
    spec = Web.FormSpec.create(config -> {
      config.action("/test");

      config.textInput(input -> {
        input.label("First name");
        input.id("some_id");
        input.name("first_name");
        input.required();
      });
    });

    Http.Exchange http;
    http = Http.Exchange.create(config -> {
      config.formParam("last_name", "Last");
    });

    Web.Form form;
    form = spec.parse(http);

    testWebForm(form, """
    # Form

    /test      | false

    # Fields

    TextInput  | First name |                 | some_id    | first_name | text
    Error      | Please enter a value
    """);
  }

  @Test(description = "textInput: fail maxLength")
  public void textInput04() {
    Web.FormSpec spec;
    spec = Web.FormSpec.create(config -> {
      config.action("/test");

      config.textInput(input -> {
        input.label("First name");
        input.id("some_id");
        input.name("first_name");
        input.maxLength(3);
      });
    });

    Http.Exchange http;
    http = Http.Exchange.create(config -> {
      config.formParam("first_name", "First");
    });

    Web.Form form;
    form = spec.parse(http);

    testWebForm(form, """
    # Form

    /test      | false

    # Fields

    TextInput  | First name | First           | some_id    | first_name | text
    Error      | Maximum of 3 characters allowed; you entered 5
    """);
  }

  private void testWebForm(Web.Form form, String expected) {
    Testable.Formatter w;
    w = Testable.Formatter.create();

    w.heading1("Form");

    w.row(
        form.action(), 10,
        Boolean.toString(form.isValid()), 5
    );

    w.heading1("Fields");

    for (Web.Form.Field field : form.fields()) {
      switch (field) {

        case Web.Form.TextInput input -> w.row(
            "TextInput", 10,
            input.label(), 10,

            input.value(), 15,
            input.id(), 10,
            input.name(), 10,
            input.type(), 10
        );

      }

      for (Web.Form.Error error : field.errors()) {
        w.row(
            "Error", 10,
            error.message(), 50
        );
      }

    }

    assertEquals(w.toString(), expected);
  }

  private void testSql(Web.Form form, String expected) {
    TestingConnection conn;
    conn = new TestingConnection();

    TestingPreparedStatement stmt;
    stmt = new TestingPreparedStatement();

    stmt.updates(1);

    conn.preparedStatements(stmt);

    Sql.Dialect dialect;
    dialect = Sql.Dialect.TESTING;

    SqlTransaction trx;
    trx = new SqlTransaction(dialect, conn);

    trx.sql("insert into dummy values (?)");

    for (Web.Form.Field field : form.fields()) {
      field.setValue(trx);
    }

    trx.update();

    assertEquals(stmt.toString(), expected);
  }

}