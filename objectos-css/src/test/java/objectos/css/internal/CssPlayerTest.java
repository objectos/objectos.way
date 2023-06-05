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

import objectos.css.IdSelector;
import objectos.css.pseudom.PRule.PStyleRule;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CssPlayerTest {

  private final CssPlayer player = new CssPlayer();

  @BeforeMethod
  public void _beforeMethod() {
    player.objectIndex = CssPlayer.OBJECT_INDEX;
  }

  @Test(description = """
  CssPlayer

  - style(BODY);
  """)
  public void testCase00() {
    putProto(
      // [0]: BODY
      ByteProto.MARKED, 5,
      TypeSelector.BODY.ordinal(),
      0, ByteProto.TYPE_SELECTOR,

      // [5]: style()
      ByteProto.STYLE_RULE, 13,
      ByteProto.TYPE_SELECTOR, 0,
      ByteProto.STYLE_RULE_END,
      0, 5, ByteProto.STYLE_RULE,

      // [13]: ROOT
      ByteProto.ROOT,
      ByteProto.STYLE_RULE, 5,
      ByteProto.ROOT_END, 13
    );

    var sheet = player.pseudoStyleSheet().init();
    assertEquals(sheet.protoIndex, 13);
    var rules = sheet.rules().iterator();

    // rule[0]
    var rule = (PStyleRule) rules.next();
    var selector = rule.selector().elements().iterator();
    assertEquals(selector.next(), TypeSelector.BODY);
    assertEquals(selector.hasNext(), false);
    var declarations = rule.declarations().iterator();
    assertEquals(declarations.hasNext(), false);

    assertEquals(rules.hasNext(), false);
  }

  @Test(description = """
  CssPlayer

  style(
    id("myid")
  );
  """)
  public void testCase01() {
    int id = player.addObject("myid");

    putProto(
      // [0]: ID "myid"
      ByteProto.MARKED, 5,
      id,
      0, ByteProto.ID_SELECTOR,

      // [5]: style()
      ByteProto.STYLE_RULE, 13,
      ByteProto.ID_SELECTOR, 0,
      ByteProto.STYLE_RULE_END,
      0, 5, ByteProto.STYLE_RULE,

      // [13]: ROOT
      ByteProto.ROOT,
      ByteProto.STYLE_RULE, 5,
      ByteProto.ROOT_END, 13
    );

    var sheet = player.pseudoStyleSheet().init();
    assertEquals(sheet.protoIndex, 13);
    var rules = sheet.rules().iterator();

    // rule[0]
    var rule = (PStyleRule) rules.next();
    var selector = rule.selector().elements().iterator();
    assertEquals(selector.next(), new IdSelector("myid"));
    assertEquals(selector.hasNext(), false);
    var declarations = rule.declarations().iterator();
    assertEquals(declarations.hasNext(), false);

    assertEquals(rules.hasNext(), false);
  }

  @Test(description = """
  CssPlayer

  style(
    A, id("myid")
  );
  """)
  public void testCase02() {
    int id = player.addObject("myid");

    putProto(
      // [0]: ID "myid"
      ByteProto.MARKED, 5,
      id,
      0, ByteProto.ID_SELECTOR,

      // [5]: A
      ByteProto.MARKED, 10,
      TypeSelector.A.ordinal(),
      5, ByteProto.TYPE_SELECTOR,

      // [10]: style()
      ByteProto.STYLE_RULE, 20,
      ByteProto.TYPE_SELECTOR, 5,
      ByteProto.ID_SELECTOR, 0,
      ByteProto.STYLE_RULE_END,
      0, 10, ByteProto.STYLE_RULE,

      // [20]: ROOT
      ByteProto.ROOT,
      ByteProto.STYLE_RULE, 10,
      ByteProto.ROOT_END, 20
    );

    var sheet = player.pseudoStyleSheet().init();
    assertEquals(sheet.protoIndex, 20);
    var rules = sheet.rules().iterator();

    // rule[0]
    var rule = (PStyleRule) rules.next();
    var selector = rule.selector().elements().iterator();
    assertEquals(selector.next(), TypeSelector.A);
    assertEquals(selector.next(), new IdSelector("myid"));
    assertEquals(selector.hasNext(), false);
    var declarations = rule.declarations().iterator();
    assertEquals(declarations.hasNext(), false);

    assertEquals(rules.hasNext(), false);
  }

  private void putProto(int... values) {
    System.arraycopy(values, 0, player.protoArray, 0, values.length);

    player.protoIndex = values.length;
  }

}