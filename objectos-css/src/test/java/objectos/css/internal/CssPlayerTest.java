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

import objectos.css.pseudom.PRule.PStyleRule;
import objectos.css.tmpl.TypeSelector;
import objectos.util.UnmodifiableList;
import org.testng.annotations.Test;

public class CssPlayerTest {

  private final CssPlayer player = new CssPlayer();

  @Test(description = """
  CssPlayer

  - style(BODY);
  """)
  public void testCase00() {
    putProto(
      // [0]: BODY
      ByteProto.MARKED,
      ByteProto.NULL,
      TypeSelector.BODY.ordinal(),
      0,
      ByteProto.TYPE_SELECTOR,

      // [5]: style()
      ByteProto.STYLE_RULE,
      13,
      ByteProto.TYPE_SELECTOR,
      0,
      ByteProto.STYLE_RULE_END,
      0, // contents
      5, // start
      ByteProto.STYLE_RULE,

      // [13]: ROOT
      ByteProto.ROOT,
      ByteProto.STYLE_RULE,
      5,
      ByteProto.ROOT_END,
      13
    );

    var sheet = player.pseudoStyleSheet().init();

    assertEquals(sheet.protoIndex, 13);

    var rules = UnmodifiableList.copyOf(sheet.rules());

    assertEquals(rules.size(), 1);

    var rule = (PStyleRule) rules.get(0);

    var selector = rule.selector();

    var elements = UnmodifiableList.copyOf(selector.elements());

    assertEquals(elements.size(), 1);
    assertEquals(elements.get(0), TypeSelector.BODY);

    var declarations = UnmodifiableList.copyOf(rule.declarations());

    assertEquals(declarations.size(), 0);
  }

  private void putProto(int... values) {
    System.arraycopy(values, 0, player.protoArray, 0, values.length);

    player.protoIndex = values.length;
  }

}