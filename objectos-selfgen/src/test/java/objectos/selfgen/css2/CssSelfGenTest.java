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
package objectos.selfgen.css2;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class CssSelfGenTest {

  @Test(description = """
  CssSelfGen selectors TC01

  - selectors should be sorted alphabetically
  """)
  public void selectors01() {
    var spec = new CssSelfGen() {
      @Override
      protected void definition() {
        selectors(
          "a", "pre", "body", "::after"
        );
      }
    }.compile();

    var selectors = spec.selectors();

    assertEquals(selectors.size(), 4);
    assertEquals(selectors.get(0), new Selector("__after", "::after"));
    assertEquals(selectors.get(1), new Selector("a", "a"));
    assertEquals(selectors.get(2), new Selector("body", "body"));
    assertEquals(selectors.get(3), new Selector("pre", "pre"));
  }

}