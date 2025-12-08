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

import java.util.function.Function;
import objectos.way.Script.JsElement;
import org.testng.annotations.Test;

public class ScriptJsObjectTest {

  @Test
  public void prop0() {
    test(
        lo -> {
          var classList = lo.prop("Element", "classList");

          return classList.invoke("DOMTokenList", "toggle", "foo");
        },

        """
        [["LO","TT"],["RP","Element","classList"],["IV","DOMTokenList","toggle",["JS","foo"]]]""",

        """
        [["LO","ID","foo"],["RP","Element","classList"],["IV","DOMTokenList","toggle",["JS","foo"]]]"""
    );
  }

  private void test(Function<Script.JsObject, Script.Action> f, String target, String foo) {
    final JsElement tt;
    tt = Script.target();

    final Script.Action action;
    action = f.apply(tt);

    final String result;
    result = action.toJsonString();

    assertEquals(result, target);
  }

}
