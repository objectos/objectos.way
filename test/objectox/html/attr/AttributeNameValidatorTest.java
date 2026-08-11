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
package objectox.html.attr;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AttributeNameValidatorTest {

  @DataProvider
  public Object[][] validProvider() {
    return new Object[][] {
        {"a", "single lowercase letter"},
        {"Z", "single uppercase letter"},
        {"abc123", "digits are valid"},
        {"abc-def", "hyphen is valid"},
        {"abcDefGhi", "camel case is valid"}
    };
  }

  @Test(dataProvider = "validProvider")
  public void valid(String name, String description) {
    final AttributeNameValidator subject;
    subject = new AttributeNameValidator(name);

    assertEquals(subject.validate(), name);
  }

  @DataProvider
  public Object[][] iaeProvider() {
    return new Object[][] {
        {"", "Invalid attribute name: it must not be empty"},
        {"7abc", "Invalid attribute name: it must start with a letter"},
        {"-abc", "Invalid attribute name: it must start with a letter"},
        {"abc&123", "Invalid attribute name: it must not contain the character '&'"},
        {"a#", "Invalid attribute name: it must not contain the character '#'"}
    };
  }

  @Test(dataProvider = "iaeProvider")
  public void iae(String name, String expectedMsg) {
    final AttributeNameValidator subject;
    subject = new AttributeNameValidator(name);

    try {
      subject.validate();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, expectedMsg);
    }
  }

  @Test(
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "name == null")
  public void npe() {
    new AttributeNameValidator(null);
  }

}
