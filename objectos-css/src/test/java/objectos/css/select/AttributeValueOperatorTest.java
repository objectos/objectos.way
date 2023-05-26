/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.select;

import static objectos.css.select.AttributeValueOperator.CONTAINS;
import static objectos.css.select.AttributeValueOperator.ENDS_WITH;
import static objectos.css.select.AttributeValueOperator.EQUALS;
import static objectos.css.select.AttributeValueOperator.HYPHEN;
import static objectos.css.select.AttributeValueOperator.STARTS_WITH;
import static objectos.css.select.AttributeValueOperator.WS_LIST;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AttributeValueOperatorTest {

  @DataProvider
  public Object[][] matchesProvider() {
    return new Object[][] {
        { EQUALS, "a", "a", true },
        { EQUALS, "A", "a", false },
        { EQUALS, "a", "b", false },
        { EQUALS, "a", "aaa", false },

        { WS_LIST, "en-us", "", false },
        { WS_LIST, "en-us", "en-us", true },
        { WS_LIST, "en-us", "en-gb", false },
        { WS_LIST, "en-us", "en-us en-gb", true },
        { WS_LIST, "en-us", "en-gb en-us", true },
        { WS_LIST, "en-us", "en-gb en-us", true },
        { WS_LIST, "en-us", "en-gb        en-us  en-au", true },

        { STARTS_WITH, "a", "a", true },
        { STARTS_WITH, "a", "", false },
        { STARTS_WITH, "a", "abc", true },
        { STARTS_WITH, "a", "xaa", false },

        { ENDS_WITH, ".br", "whatever.com.br", true },
        { ENDS_WITH, ".br", "whatever.com.ar", false },
        { ENDS_WITH, ".br", "", false },
        { ENDS_WITH, ".br", "br.com", false },

        { CONTAINS, "example", "www.example.com", true },
        { CONTAINS, "example", "example.com", true },
        { CONTAINS, "example", "example", true },
        { CONTAINS, "example", "www.example", true },
        { CONTAINS, "example", "ex", false },

        { HYPHEN, "en", "", false },
        { HYPHEN, "en", "en-us", true },
        { HYPHEN, "en", "en-gb", true },
        { HYPHEN, "en", "pt-br", false }
    };
  }

  @Test(dataProvider = "matchesProvider")
  public void matches(
      AttributeValueOperator op, String expression, String actual, boolean expected) {
    assertEquals(op.matches(expression, actual), expected);
  }

}