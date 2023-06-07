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

import java.util.Arrays;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CssRecorderTest {

  private final CssRecorder recorder = new CssRecorder();

  @Test(description = """
  CssRecorder

  - style(BODY);
  """)
  public void testCase00() {
    executeBefore();

    recorder.addRule(TypeSelector.BODY);

    executeAfter();

    testProto(
      // [0]: style()
      ByteProto.STYLE_RULE, 8,
      ByteProto.TYPE_SELECTOR, TypeSelector.BODY.ordinal(),
      ByteProto.STYLE_RULE_END,
      0, 0, ByteProto.STYLE_RULE,

      // [8]: ROOT
      ByteProto.ROOT,
      ByteProto.STYLE_RULE, 0,
      ByteProto.ROOT_END, 8
    );
  }

  @Test(description = """
  CssRecorder

  style(
    id("myid")
  );
  """)
  public void testCase01() {
    executeBefore();

    int id = recorder.addObject("myid");

    recorder.addRule(
      recorder.addInternal(ByteProto.ID_SELECTOR, id)
    );

    executeAfter();

    testProto(
      // [0]: ID "myid"
      ByteProto.MARKED, 5,
      id,
      0, ByteProto.ID_SELECTOR,

      // [5]: style()
      ByteProto.STYLE_RULE, 13,
      ByteProto.ID_SELECTOR, id,
      ByteProto.STYLE_RULE_END,
      0, 5, ByteProto.STYLE_RULE,

      // [13]: ROOT
      ByteProto.ROOT,
      ByteProto.STYLE_RULE, 5,
      ByteProto.ROOT_END, 13
    );
  }

  @Test(description = """
  CssRecorder

  style(
    A, id("myid")
  );
  """)
  public void testCase02() {
    executeBefore();

    int id = recorder.addObject("myid");

    recorder.addRule(
      TypeSelector.A,
      recorder.addInternal(ByteProto.ID_SELECTOR, id)
    );

    executeAfter();

    testProto(
      // [0]: ID "myid"
      ByteProto.MARKED, 5,
      id,
      0, ByteProto.ID_SELECTOR,

      // [5]: style()
      ByteProto.STYLE_RULE, 15,
      ByteProto.TYPE_SELECTOR, TypeSelector.A.ordinal(),
      ByteProto.ID_SELECTOR, id,
      ByteProto.STYLE_RULE_END,
      0, 5, ByteProto.STYLE_RULE,

      // [15]: ROOT
      ByteProto.ROOT,
      ByteProto.STYLE_RULE, 5,
      ByteProto.ROOT_END, 15
    );
  }

  @Test(description = """
  CssRecorder

  style(
    attr("type")
  );
  """)
  public void testCase07A() {
    executeBefore();

    int name = recorder.addObject("type");

    recorder.addRule(
      recorder.addInternal(ByteProto.ATTR_NAME_SELECTOR, name)
    );

    executeAfter();

    testProto(
      // [0]: attr("type")
      ByteProto.MARKED, 5,
      name,
      0, ByteProto.ATTR_NAME_SELECTOR,

      // [5]: style()
      ByteProto.STYLE_RULE, 13,
      ByteProto.ATTR_NAME_SELECTOR, 0,
      ByteProto.STYLE_RULE_END,
      0, 5, ByteProto.STYLE_RULE,

      // [13]: ROOT
      ByteProto.ROOT,
      ByteProto.STYLE_RULE, 5,
      ByteProto.ROOT_END, 13
    );
  }

  @Test(description = """
  CssRecorder

  style(
    attr("type", eq("input"))
  );
  """)
  public void testCase07B() {
    executeBefore();

    int equals = AttributeValueOperator.EQUALS.ordinal();
    int value = recorder.addObject("input");

    var instruction = recorder.addInternal(
      ByteProto.ATTR_VALUE_ELEMENT, equals, value
    );

    int name = recorder.addObject("type");

    instruction = recorder.addAttribute(name, instruction);

    testProto(
      // [0]: eq("input")
      ByteProto.MARKED, 6,
      equals, value,
      0, ByteProto.ATTR_VALUE_ELEMENT,

      // [6]: attr("type", ...)
      ByteProto.ATTR_VALUE_SELECTOR, 12,
      name, 0,
      0, ByteProto.ATTR_VALUE_SELECTOR
    );

    recorder.addRule(instruction);

    executeAfter();

    testProto(
      // [0]: eq("input")
      ByteProto.MARKED, 6,
      equals, value,
      0, ByteProto.ATTR_VALUE_ELEMENT,

      // [6]: attr("type", ...)
      ByteProto.MARKED, 12,
      name, 0,
      0, ByteProto.ATTR_VALUE_SELECTOR,

      // [12]: style()
      ByteProto.STYLE_RULE, 20,
      ByteProto.ATTR_VALUE_SELECTOR, 6,
      ByteProto.STYLE_RULE_END,
      0, 12, ByteProto.STYLE_RULE,

      // [20]: ROOT
      ByteProto.ROOT,
      ByteProto.STYLE_RULE, 12,
      ByteProto.ROOT_END, 20
    );
  }

  @Test(description = """
  CssRecorder

  style(
    ANY, display(block)
  );
  """)
  public void testCase08() {
    executeBefore();

    recorder.addRule(
      UniversalSelector.INSTANCE,
      recorder.addDeclaration(Property.DISPLAY, Keyword.BLOCK)
    );

    executeAfter();

    testProto(
      // [0]: display(block)
      ByteProto.MARKED, 9,
      Property.DISPLAY.ordinal(),
      ByteProto.KEYWORD, Keyword.BLOCK.ordinal(),
      ByteProto.DECLARATION_END,
      0, 0, ByteProto.DECLARATION,

      // [9]: style()
      ByteProto.STYLE_RULE, 18,
      ByteProto.UNIVERSAL_SELECTOR,
      ByteProto.DECLARATION, 0,
      ByteProto.STYLE_RULE_END,
      0, 9, ByteProto.STYLE_RULE,

      // [18]: ROOT
      ByteProto.ROOT,
      ByteProto.STYLE_RULE, 9,
      ByteProto.ROOT_END, 18
    );
  }

  @Test(description = """
  CssRecorder

  style(
    ANY, zIndex(-300)
  );
  """)
  public void testCase09() {
    executeBefore();

    recorder.addRule(
      UniversalSelector.INSTANCE,
      recorder.addDeclaration(Property.Z_INDEX, -300)
    );

    executeAfter();

    testProto(
      // [0]: display(block)
      ByteProto.MARKED, 9,
      Property.Z_INDEX.ordinal(),
      ByteProto.INT_VALUE, -300,
      ByteProto.DECLARATION_END,
      0, 0, ByteProto.DECLARATION,

      // [9]: style()
      ByteProto.STYLE_RULE, 18,
      ByteProto.UNIVERSAL_SELECTOR,
      ByteProto.DECLARATION, 0,
      ByteProto.STYLE_RULE_END,
      0, 9, ByteProto.STYLE_RULE,

      // [18]: ROOT
      ByteProto.ROOT,
      ByteProto.STYLE_RULE, 9,
      ByteProto.ROOT_END, 18
    );
  }

  @Test(description = """
  CssRecorder

  style(
    ANY, minHeight(px(160))
  );
  """)
  public void testCase12() {
    executeBefore();

    recorder.addRule(
      UniversalSelector.INSTANCE,
      recorder.addDeclaration(
        Property.MIN_HEIGHT,
        recorder.addValue(ByteProto.PX1, 160)
      )
    );

    executeAfter();

    testProto(
      // [0]: px(160)
      ByteProto.MARKED3,
      160,
      ByteProto.PX1,

      // [3]: minHeight(px(160))
      ByteProto.MARKED, 12,
      Property.MIN_HEIGHT.ordinal(),
      ByteProto.PX1, 160,
      ByteProto.DECLARATION_END,
      0, 3, ByteProto.DECLARATION,

      // [12]: style()
      ByteProto.STYLE_RULE, 21,
      ByteProto.UNIVERSAL_SELECTOR,
      ByteProto.DECLARATION, 3,
      ByteProto.STYLE_RULE_END,
      0, 12, ByteProto.STYLE_RULE,

      // [21]: ROOT
      ByteProto.ROOT,
      ByteProto.STYLE_RULE, 12,
      ByteProto.ROOT_END, 21
    );
  }

  private void executeAfter() {
    recorder.executeRecorderAfter();
  }

  private void executeBefore() {
    recorder.executeRecorderBefore();
  }

  private void testProto(int... expected) {
    int[] protos = Arrays.copyOf(recorder.protoArray, recorder.protoIndex);

    if (protos.length != expected.length) {
      Assert.fail("""
      protos length differ

      actual  : %s
      expected: %s
      """.formatted(Arrays.toString(protos), Arrays.toString(expected)));
    }

    assertEquals(protos, expected);
  }

}