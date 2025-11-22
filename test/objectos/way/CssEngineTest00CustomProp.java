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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CssEngineTest00CustomProp {

  @DataProvider
  public Object[][] testProvider() {
    return new Object[][] {
        {"--foo", true},
        {"--foo-bar", true},
        {"-foo", false},
        {"foo", false},
        {"--123foo", false}
    };
  }

  @Test(dataProvider = "testProvider")
  public void test(
      String input,
      boolean expected) {
    final CssEngine.CustomProp prop;
    prop = new CssEngine.CustomProp();

    prop.set(input);

    final boolean result;
    result = prop.test();

    assertEquals(result, expected);
  }

}