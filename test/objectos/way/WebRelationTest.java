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
import static org.testng.Assert.assertTrue;

import objectos.way.Web.Form;
import org.testng.annotations.Test;

public class WebRelationTest {

  private final Web.Relation owners = Web.Relation.create(config -> {
    config.name("owners");

    config.stringAttribute(attr -> {
      attr.name("first_name");
      attr.description("First name");
      attr.required();
      attr.maxLength(30);
    });

    config.stringAttribute(attr -> {
      attr.name("last_name");
      attr.description("Last name");
      attr.required();
      attr.maxLength(30);
    });

    config.stringAttribute(attr -> {
      attr.name("address");
      attr.description("Address");
      attr.required();
      attr.maxLength(255);
    });

    config.stringAttribute(attr -> {
      attr.name("city");
      attr.description("City");
      attr.required();
      attr.maxLength(80);
    });

    config.stringAttribute(attr -> {
      attr.name("telephone");
      attr.description("Telephone");
      attr.required();
      attr.pattern("\\d{10}", "Telephone must be a 10-digit number");
    });
  });

  private class OwnersFormView extends Html.Template {

    private final Web.Form form;

    public OwnersFormView(Form form) {
      this.form = form;
    }

    @Override
    protected final void render() {
      form(
          action(form.action()),
          method("post"),

          input(type("hidden"), name("_csfr"), value("FIXME")),

          fieldset(
              renderFragment(this::fields)
          )
      );
    }

    private void fields() {
      for (Web.Form.Field field : form.fields()) {
        renderField(field);
      }
    }

    private Html.Instruction.OfElement renderField(Web.Form.Field field) {
      return switch (field) {

        case Web.Form.TextInput input -> div(
            label(
                forAttr(input.id()),

                text(input.label())
            ),

            input(
                id(input.id()),

                name(input.name()),

                type(input.type())
            )
        );

      };
    }

  }

  @Test(description = "owners: rendering")
  public void owners01() {
    Web.Form form = Web.Form.create(config -> {
      config.spec(owners);
      config.action("/owners");
      //config.custom("header", "foo");
    });

    assertTrue(form.isValid());

    OwnersFormView view;
    view = new OwnersFormView(form);

    assertEquals(
        view.toString(),

        """
        <form action="/owners" method="post"><input type="hidden" name="_csfr" value="FIXME">
        <fieldset>
        <div><label for="first_name">First name</label><input id="first_name" name="first_name" type="text"></div>
        <div><label for="last_name">Last name</label><input id="last_name" name="last_name" type="text"></div>
        <div><label for="address">Address</label><input id="address" name="address" type="text"></div>
        <div><label for="city">City</label><input id="city" name="city" type="text"></div>
        <div><label for="telephone">Telephone</label><input id="telephone" name="telephone" type="text"></div>
        </fieldset>
        </form>
        """
    );
  }

  @Test(enabled = false, description = "owners: happy path")
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
    form = parseForm(http);

    assertTrue(form.isValid());
  }

  private Web.Form parseForm(Http.Exchange http) {
    throw new UnsupportedOperationException("Implement me");
  }

}