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
package br.com.objectos.css.sheet;

import static org.testng.Assert.assertEquals;

import br.com.objectos.css.keyword.Keywords;
import br.com.objectos.css.property.StandardPropertyName;
import br.com.objectos.css.select.TypeSelectors;
import br.com.objectos.css.sheet.ex.TestCase00;
import br.com.objectos.css.sheet.ex.TestCase01;
import br.com.objectos.css.sheet.ex.TestCase08;
import br.com.objectos.css.sheet.ex.TestCase09;
import br.com.objectos.css.sheet.ex.TestCase17;
import br.com.objectos.css.sheet.ex.TestCase25;
import br.com.objectos.css.sheet.ex.TestCase27;
import br.com.objectos.css.sheet.ex.TestCase32;
import java.util.Arrays;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Pass0Test {

  private final Pass0 pass = new Pass0();

  @BeforeMethod
  public void _beforeMethod() {
    pass.close();
  }

  @Test
  public void testCase00() {
    dsl(new TestCase00());

    testProtos(
      ByteProto.RULE_END,
      TypeSelectors.body.getCode(),
      ByteProto.SELECTOR_TYPE_OBJ,
      ByteProto.RULE_START
    );
  }

  @Test
  public void testCase01() {
    dsl(new TestCase01());

    testChars(
      "myid"
    );

    testProtos(
      0,
      ByteProto.SELECTOR_ID,
      ByteProto.RULE_END,
      ByteProto.SELECTOR_ID_MARK,
      ByteProto.RULE_START
    );
  }

  @Test
  public void testCase08() {
    dsl(new TestCase08());

    testProtos(
      ByteProto.DECLARATION_END,
      Keywords.block.getCode(),
      ByteProto.VALUE_KEYWORD,
      StandardPropertyName.DISPLAY.getCode(),
      ByteProto.DECLARATION_START,

      ByteProto.RULE_END,
      ByteProto.DECLARATION_MARK,
      ByteProto.SELECTOR_UNIVERSAL_OBJ,
      ByteProto.RULE_START
    );
  }

  @Test
  public void testCase09() {
    dsl(new TestCase09());

    testProtos(
      ByteProto.DECLARATION_END,
      -300,
      ByteProto.VALUE_INT,
      StandardPropertyName.Z_INDEX.getCode(),
      ByteProto.DECLARATION_START,

      ByteProto.RULE_END,
      ByteProto.DECLARATION_MARK,
      ByteProto.SELECTOR_UNIVERSAL_OBJ,
      ByteProto.RULE_START
    );
  }

  @Test
  public void testCase17() {
    dsl(new TestCase17());

    testProtos(
      2, ByteProto.VALUE_INT_DSL,
      0, ByteProto.VALUE_DOUBLE_DSL,

      ByteProto.DECLARATION_END,
      ByteProto.VALUE_DOUBLE_MARK,
      ByteProto.VALUE_INT_MARK,
      StandardPropertyName.FLEX.getCode(),
      ByteProto.DECLARATION_START,

      ByteProto.RULE_END,
      ByteProto.DECLARATION_MARK,
      ByteProto.SELECTOR_UNIVERSAL_OBJ,
      ByteProto.RULE_START
    );
  }

  @Test
  public void testCase25() {
    dsl(new TestCase25());

    testProtos(
      ByteProto.DECLARATION_END,
      Keywords.sansSerif.getCode(),
      ByteProto.VALUE_KEYWORD,
      StandardPropertyName.FONT_FAMILY.getCode(),
      ByteProto.DECLARATION_START,

      ByteProto.DECLARATION_END,
      0,
      ByteProto.VALUE_STRING,
      StandardPropertyName.FONT_FAMILY.getCode(),
      ByteProto.DECLARATION_START,

      ByteProto.DECLARATION_MULTI_END,
      ByteProto.DECLARATION_MULTI_ELEMENT_MARK,
      ByteProto.DECLARATION_MULTI_ELEMENT_MARK,
      StandardPropertyName.FONT_FAMILY.getCode(),
      ByteProto.DECLARATION_MULTI_START,

      ByteProto.RULE_END,
      ByteProto.DECLARATION_MARK,
      ByteProto.SELECTOR_UNIVERSAL_OBJ,
      ByteProto.RULE_START
    );
  }

  @Test
  public void testCase27() {
    dsl(new TestCase27());

    testProtos(
      100,
      ByteProto.VALUE_PERCENTAGE_INT,

      ByteProto.DECLARATION_END,
      ByteProto.VALUE_PERCENTAGE_INT_MARK,
      StandardPropertyName.WIDTH.getCode(),
      ByteProto.DECLARATION_START,

      ByteProto.RULE_END,
      ByteProto.DECLARATION_MARK,
      TypeSelectors.section.getCode(),
      ByteProto.SELECTOR_TYPE_OBJ,
      ByteProto.RULE_START,

      ByteProto.AT_MEDIA_END,
      ByteProto.RULE_MARK,
      MediaType.SCREEN.getCode(),
      ByteProto.MEDIA_TYPE,
      ByteProto.AT_MEDIA_START
    );
  }

  @Test
  public void testCase32() {
    dsl(new TestCase32());

    testProtos(
      0,
      ByteProto.SELECTOR_CLASS,
      ByteProto.DECLARATION_END,
      0,
      ByteProto.VALUE_INT,
      StandardPropertyName.Z_INDEX.getCode(),
      ByteProto.DECLARATION_START,

      ByteProto.RULE_END,
      ByteProto.DECLARATION_MARK,
      ByteProto.SELECTOR_CLASS_MARK,
      ByteProto.RULE_START,

      1,
      ByteProto.SELECTOR_CLASS,
      ByteProto.DECLARATION_END,
      1,
      ByteProto.VALUE_INT,
      StandardPropertyName.Z_INDEX.getCode(),
      ByteProto.DECLARATION_START,

      ByteProto.RULE_END,
      ByteProto.DECLARATION_MARK,
      ByteProto.SELECTOR_CLASS_MARK,
      ByteProto.RULE_START,

      2,
      ByteProto.SELECTOR_CLASS,
      ByteProto.DECLARATION_END,
      2,
      ByteProto.VALUE_INT,
      StandardPropertyName.Z_INDEX.getCode(),
      ByteProto.DECLARATION_START,

      ByteProto.RULE_END,
      ByteProto.DECLARATION_MARK,
      ByteProto.SELECTOR_CLASS_MARK,
      ByteProto.RULE_START
    );
  }

  private void dsl(StyleSheet sheet) {
    sheet.eval(pass);
  }

  private void testChars(String expected) {
    assertEquals(pass.strings.join(), expected);
  }

  private void testProtos(int... expected) {
    int[] protos = pass.getProtos();

    try {
      assertEquals(protos, expected);
    } catch (AssertionError e) {
      System.err.println(Arrays.toString(protos));
      System.err.println(Arrays.toString(expected));
      throw e;
    }
  }

}