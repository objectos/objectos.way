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

import objectos.way.dev.DevScriptProp0;
import objectos.way.dev.DevScriptTarget;
import org.testng.annotations.Test;

public class ScriptJsObjectTestTarget {

  @Test(description = """
  IV: invoke virtual
  - locator = target
  - args = empty
  """)
  public void invoke0() {
    test(
        DevScriptTarget.INVOKE0,

        """
        [["LO","TT"],["IV","Element","remove",[]]]"""
    );
  }

  @Test(description = """
  IV: invoke virtual
  - locator = target
  - args = 1
  """)
  public void invoke1() {
    test(
        DevScriptTarget.INVOKE1,

        """
        [["LO","TT"],["IV","Element","removeAttribute",[["JS","style"]]]]"""
    );
  }

  @Test
  public void prop0() {
    test(
        DevScriptProp0.SCRIPT,

        """
        [["LO","TT"],["RP","Element","classList"],["IV","DOMTokenList","toggle",["JS","background-color:gray-200"]]]"""
    );
  }

  private void test(Script.Action action, String expected) {
    final String result;
    result = action.toString();

    assertEquals(result, expected);
  }

}
