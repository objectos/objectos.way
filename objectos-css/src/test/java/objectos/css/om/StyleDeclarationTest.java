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
package objectos.css.om;

import static org.testng.Assert.assertEquals;

import objectos.css.internal.om.Keyword;
import objectos.css.internal.om.Property;
import org.testng.annotations.Test;

public class StyleDeclarationTest {

  @Test(description = """
  StyleDeclaration: Keyword
  """)
  public void testCase01() {
    var decl = StyleDeclaration.create(
      Property.DISPLAY,
      Keyword.BLOCK
    );

    assertEquals(
      decl.toString(),
      """
      display: block"""
    );
  }

}