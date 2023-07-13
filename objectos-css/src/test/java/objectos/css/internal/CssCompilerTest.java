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
package objectos.css.internal;

import static org.testng.Assert.assertEquals;

import objectos.css.StyleSheet;
import org.testng.annotations.Test;

public class CssCompilerTest {

  @Test
  public void testCase01() {
    CssCompiler compiler;
    compiler = new CssCompiler();

    compiler.compilationStart();

    // border-color: currentcolor;
    compiler.declarationStart(StandardName.BORDER_COLOR);
    compiler.declarationValue(StandardName.currentcolor);
    compiler.declarationEnd();

    // label { \{previous} }
    compiler.styleRuleStart();
    compiler.styleRuleElement(StandardName.label);
    compiler.styleRuleElement(InternalInstruction.DECLARATION);
    compiler.styleRuleEnd();

    compiler.compilationEnd();

    StyleSheet result;
    result = compiler.compile();

    assertEquals(
      result.toString(),

      """
      label {
        border-color: currentcolor;
      }
      """
    );
  }

}