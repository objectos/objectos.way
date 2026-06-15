/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.http.media;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MediaTypeValidatorTest {

  @DataProvider
  public Object[][] validProvider() {
    return new Object[][] {
        {"t/s", "min valid"},
        {"application/octet-stream", "regular"},
        {"text/html; charset=utf-8", "with parameters"},
        {"text/html  ;  charset=utf-8", "with parameters"},
        {"text/html;p=v", "with parameters"}
    };
  }

  @DataProvider
  public Object[][] invalidProvider() {
    return new Object[][] {
        {"", "Full type must not be empty"},
        {"text_html", "Full type must contain the '/' character"},
        {"/html", "Type must not be empty"},
        {"text/", "Subtype must not be empty"},
        {"text/html/html", "Subtype contains the invalid '/' character"},
        {"text/html<", "Subtype contains the invalid '<' character"},
        {" ", "Type must begin with ALPHA or DIGIT, but found ' '"},
        {"image/png; charset=utf-8", "Parameters are only allowed on text types"},
        {"text/html &", "Expected the ';' character but found '&' instead"},
    };
  }

  @Test(dataProvider = "validProvider")

  public void valid(String fullType, String description) {
    final MediaTypeValidator validator;
    validator = new MediaTypeValidator(fullType);

    validator.validate();
  }

  @Test(dataProvider = "invalidProvider")

  public void invalid(String fullType, String message) {
    try {
      final MediaTypeValidator validator;
      validator = new MediaTypeValidator(fullType);

      validator.validate();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), message);
    }
  }

}
