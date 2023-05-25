/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.css.select;

import static org.testng.Assert.assertEquals;

import objectos.selfgen.css.spec.Prefix;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PrefixTest {

  @DataProvider
  public Object[][] parseProvider() {
    return new Object[][] {
        { "", Prefix.STANDARD },
        { "-webkit-", Prefix.WEBKIT },
        { "-moz-", Prefix.MOZILLA },
        { "-ms-", Prefix.MICROSOFT },
        { "-mso-", Prefix.MICROSOFT },
        { "-xpto-", Prefix.UNKNOWN }
    };
  }

  @Test(dataProvider = "parseProvider")
  public void parse(String text, Prefix expected) {
    Prefix res = Prefix.parse(text);
    assertEquals(res, expected);
  }

}