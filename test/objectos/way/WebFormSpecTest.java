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

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class WebFormSpecTest {

  private final Web.FormSpec owners = Web.FormSpec.create(config -> {
    config.action("/owners");

    config.textInput(input -> {
      input.label("First name");
      input.id("first_name");
      input.name("first_name");
      input.requiredWithMessage("Please enter a first name");
      // input.maxLength(30);
    });

    config.textInput(input -> {
      input.label("Last name");
      input.id("last_name");
      input.name("last_name");
      input.requiredWithMessage("Please enter a last name");
      // input.maxLength(30);
    });

    config.textInput(input -> {
      input.label("Address");
      input.id("address");
      input.name("address");
      input.requiredWithMessage("Please enter an address");
      // input.maxLength(255);
    });

    config.textInput(input -> {
      input.label("City");
      input.id("city");
      input.name("city");
      input.requiredWithMessage("Please enter a city");
      // input.maxLength(80);
    });

    config.textInput(input -> {
      input.label("Telephone");
      input.id("telephone");
      input.name("telephone");
      input.requiredWithMessage("Please enter a telephone");
      // input.pattern("\\d{10}", "Telephone must be a 10-digit number");
    });
  });

  @Test(description = "owners: rendering")
  public void owners01() {
    Web.Form form;
    form = Web.Form.of(owners);

    testWebForm(form, """
    # Form

    /owners    | true

    # Fields

    TextInput  | First name |                 | first_name | first_name | text
    TextInput  | Last name  |                 | last_name  | last_name  | text
    TextInput  | Address    |                 | address    | address    | text
    TextInput  | City       |                 | city       | city       | text
    TextInput  | Telephone  |                 | telephone  | telephone  | text
    """);
  }

  @Test(description = "owners: happy path")
  public void owners02() {
    Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {
      config.formParam("first_name", "First");
      config.formParam("last_name", "Last");
      config.formParam("address", "Some Address");
      config.formParam("city", "My City");
      config.formParam("telephone", "1122334455");
    });

    Web.Form form;
    form = owners.parse(http);

    testWebForm(form, """
    # Form

    /owners    | true

    # Fields

    TextInput  | First name | First           | first_name | first_name | text
    TextInput  | Last name  | Last            | last_name  | last_name  | text
    TextInput  | Address    | Some Address    | address    | address    | text
    TextInput  | City       | My City         | city       | city       | text
    TextInput  | Telephone  | 1122334455      | telephone  | telephone  | text
    """);
  }

  @Test(description = "owners: required fail")
  public void owners03() {
    Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {
      config.formParam("first_name", "First");
      config.formParam("last_name", ""); // empty
      config.formParam("address", "Some Address");
      // city missing
      config.formParam("telephone", "1122334455");
    });

    Web.Form form;
    form = owners.parse(http);

    testWebForm(form, """
    # Form

    /owners    | false

    # Fields

    TextInput  | First name | First           | first_name | first_name | text
    TextInput  | Last name  |                 | last_name  | last_name  | text
    Error      | Please enter a last name
    TextInput  | Address    | Some Address    | address    | address    | text
    TextInput  | City       |                 | city       | city       | text
    Error      | Please enter a city
    TextInput  | Telephone  | 1122334455      | telephone  | telephone  | text
    """);
  }

  private void testWebForm(Web.Form form, String expected) {
    Lang.Testable.Writer w;
    w = Lang.Testable.Writer.create();

    w.heading("Form");

    w.row(
        form.action(), 10,
        Boolean.toString(form.isValid()), 5
    );

    w.heading("Fields");

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
            error.message(), 40
        );
      }

    }

    assertEquals(w.toString(), expected);
  }

}