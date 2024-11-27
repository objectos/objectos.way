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

import org.testng.Assert;
import org.testng.annotations.Test;

public class WebRelationTest {

  @Test(enabled = false, description = "owners: rendering")
  public void owners01() {
  }

  @Test(enabled = false, description = "owners: happy path")
  public void owners02() {
    Web.Relation owners = Web.Relation.create(config -> {
      config.name("owners");

      config.stringAttribute(attr -> {
        attr.name("first_name");
        attr.required();
        attr.maxLength(30);
      });

      config.stringAttribute(attr -> {
        attr.name("last_name");
        attr.required();
        attr.maxLength(30);
      });

      config.stringAttribute(attr -> {
        attr.name("address");
        attr.required();
        attr.maxLength(255);
      });

      config.stringAttribute(attr -> {
        attr.name("city");
        attr.required();
        attr.maxLength(80);
      });

      config.stringAttribute(attr -> {
        attr.name("telephone");
        attr.required();
        attr.pattern("\\d{10}", "Telephone must be a 10-digit number");
      });
    });

    Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {
      config.formParam("first_name", "First");
      config.formParam("last_name", "Last");
      config.formParam("address", "Some Address");
      config.formParam("city", "My City");
      config.formParam("telephone", "1122334455");
    });

    Web.Form form;
    form = owners.parseForm(http);

    switch (form) {
      case Web.Form.Valid valid -> {}

      case Web.Form.Invalid invalid -> Assert.fail("Expected Web.Form.Valid");
    }
  }

}