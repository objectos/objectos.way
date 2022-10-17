/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.declaration;

import static br.com.objectos.code.java.declaration.FieldCode.field;
import static br.com.objectos.code.java.declaration.FieldCode.init;
import static br.com.objectos.code.java.declaration.Modifiers._final;
import static br.com.objectos.code.java.declaration.Modifiers._private;
import static br.com.objectos.code.java.declaration.Modifiers._protected;
import static br.com.objectos.code.java.declaration.Modifiers._public;
import static br.com.objectos.code.java.declaration.Modifiers._static;
import static br.com.objectos.code.java.declaration.Modifiers._transient;
import static br.com.objectos.code.java.declaration.Modifiers._volatile;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.type.NamedTypes._int;
import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import objectos.util.UnmodifiableSet;
import org.testng.annotations.Test;

public class FieldCodeTest extends AbstractCodeJavaTest {

  @Test(description = ""
      + "explicit Builder usage should be allowed.")
  public void builder() {
    UnmodifiableSet<FieldModifier> modifiers;
    modifiers = UnmodifiableSet.<FieldModifier> of(_static(), _final());

    test(
        FieldCode.builder()
            .addModifier(_public())
            .addModifiers(modifiers)
            .type(_int())
            .addDeclarator(id("a"), l(0))
            .build(),
        "public static final int a = 0;"
    );
  }

  @Test(description = ""
      + "more than one modifier should be rendered in insertion order")
  public void moreThanOneModifier() {
    test(field(_public(), _final(), _int(), id("f")),
        "public final int f;");
    test(field(_private(), _static(), _final(), _int(), id("f")),
        "private static final int f;");
    test(field(_static(), _private(), _final(), _int(), id("f")),
        "static private final int f;");
  }

  @Test(description = ""
      + "a simple field should be rendered in a single line.")
  public void simpleField() {
    test(
        field(t(String.class), id("name")),
        "java.lang.String name;"
    );
  }

  @Test(description = ""
      + "shorthand should honor init. still rendered in a single line.")
  public void singleDeclaratorWithInit() {
    test(
        field(
            _private(), _final(), t(String.class), init(id("name"), l("I am initialized"))
        ),
        "private final java.lang.String name = \"I am initialized\";"
    );
    test(
        field(
            _private(), _final(), t(String.class), init(id("name"), invoke("init"))
        ),
        "private final java.lang.String name = init();"
    );
  }

  @Test(description = ""
      + "test if all allowed modifiers are correctly rendered.")
  public void testAllModifiers() {
    test(field(_public(), _int(), id("f")),
        "public int f;");
    test(field(_private(), _int(), id("f")),
        "private int f;");
    test(field(_protected(), _int(), id("f")),
        "protected int f;");
    test(field(_final(), _int(), id("f")),
        "final int f;");
    test(field(_static(), _int(), id("f")),
        "static int f;");
    test(field(_transient(), _int(), id("f")),
        "transient int f;");
    test(field(_volatile(), _int(), id("f")),
        "volatile int f;");
  }

  private void test(FieldCode code, String... lines) {
    testToString(code, lines);
  }

}