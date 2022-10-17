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

import static br.com.objectos.code.java.declaration.ConstructorCode.constructor;
import static br.com.objectos.code.java.declaration.Modifiers._private;
import static br.com.objectos.code.java.declaration.Modifiers._protected;
import static br.com.objectos.code.java.declaration.Modifiers._public;
import static br.com.objectos.code.java.declaration.ParameterCode.param;
import static br.com.objectos.code.java.declaration.ParameterCode.params;
import static br.com.objectos.code.java.declaration.SuperConstructorInvocation._super;
import static br.com.objectos.code.java.declaration.ThisConstructorInvocation._this;
import static br.com.objectos.code.java.expression.Expressions.assign;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.statement.Statements.statements;
import static br.com.objectos.code.java.type.NamedTypes._int;
import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.expression.MethodInvocation;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.Arrays;
import java.util.List;
import org.testng.annotations.Test;

public class ConstructorCodeTest extends AbstractCodeJavaTest {

  class Public {
    public Public() {}
  }

  class Default {
    Default(String name) {}
  }

  class Protected {
    protected Protected(String name, int value) {}
  }

  class Private {
    private Private() {}
  }

  @Test(
      description = ""
          + "the constructor() shorthand without parameters should render "
          + "a default (package-private) constructor."
  )
  public void shorthandWithoutParameters() {
    test(constructor(),
        "Constructor() {}");
  }

  @Test(
      description = "Verify the correct rendering of all modifiers."
  )
  public void testAllModifiers() {
    test(constructor(_public()),
        "public Constructor() {}");
    test(constructor(_protected()),
        "protected Constructor() {}");
    test(constructor(_private()),
        "private Constructor() {}");
  }

  @Test(
      description = "Constructor with many modifiers: last modifier wins"
  )
  public void lastAccessModifierWins() {
    test(constructor(_private(), _protected(), _public()),
        "public Constructor() {}");
  }

  @Test(
      description = ""
          + "constructor() shorthand should honor param()."
          + "it should be rendered in a single line."
  )
  public void constructorWithParameter() {
    test(
        constructor(
            param(t(String.class), id("name"))
        ),
        "Constructor(java.lang.String name) {}"
    );
    test(
        constructor(
            param(t(String.class), id("name")),
            param(_int(), id("value"))
        ),
        "Constructor(java.lang.String name, int value) {}"
    );
    test(
        constructor(
            params(
                Arrays.asList(
                    param(t(String.class), id("name")),
                    param(_int(), id("value"))
                )
            )
        ),
        "Constructor(java.lang.String name, int value) {}"
    );
  }

  @Test
  public void constructorWithParametersAndBody() {
    Identifier name = id("name");
    Identifier value = id("value");

    test(
        constructor(
            param(t(String.class), name),
            param(_int(), value),
            assign(Keywords._this().id("name"), name),
            assign(Keywords._this().id("value"), value)
        ),
        "Constructor(java.lang.String name, int value) {",
        "  this.name = name;",
        "  this.value = value;",
        "}"
    );
  }

  @Test(description = ""
      + "_constructor() shorthand should honor _this() invocations."
      + "It must always be the first statement rendered.")
  public void constructorWithThisConstructorInvocation() {
    test(
        constructor(
            param(_int(), id("value")),
            _this()
        ),
        "Constructor(int value) {",
        "  this();",
        "}"
    );
    test(
        constructor(
            param(_int(), id("value")),
            _this(),
            invoke("abc")
        ),
        "Constructor(int value) {",
        "  this();",
        "  abc();",
        "}"
    );
    test(
        constructor(
            _this(l(0))
        ),
        "Constructor() {",
        "  this(0);",
        "}"
    );
    test(
        constructor(
            _this(l(0), l(1))
        ),
        "Constructor() {",
        "  this(0, 1);",
        "}"
    );
    test(
        constructor(
            _this(l(0), l(1), l(2))
        ),
        "Constructor() {",
        "  this(0, 1, 2);",
        "}"
    );
    test(
        constructor(
            _this(l(0), l(1), l(2), l(3))
        ),
        "Constructor() {",
        "  this(0, 1, 2, 3);",
        "}"
    );
  }

  @Test(description = ""
      + "_constructor() shorthand should honor _super() invocations."
      + "It must always be the first statement rendered.")
  public void constructorWithSuperConstructorInvocation() {
    test(
        constructor(
            param(_int(), id("value")),
            _super()
        ),
        "Constructor(int value) {",
        "  super();",
        "}"
    );
    test(
        constructor(
            param(_int(), id("value")),
            _super(),
            invoke("abc")
        ),
        "Constructor(int value) {",
        "  super();",
        "  abc();",
        "}"
    );
    test(
        constructor(
            _super(l(0))
        ),
        "Constructor() {",
        "  super(0);",
        "}"
    );
    test(
        constructor(
            _super(l(0), l(1))
        ),
        "Constructor() {",
        "  super(0, 1);",
        "}"
    );
    test(
        constructor(
            _super(l(0), l(1), l(2))
        ),
        "Constructor() {",
        "  super(0, 1, 2);",
        "}"
    );
    test(
        constructor(
            _super(l(0), l(1), l(2), l(3))
        ),
        "Constructor() {",
        "  super(0, 1, 2, 3);",
        "}"
    );
  }

  @Test(description = "constructor() shorthand should honor statements() shorthand")
  public void constructorWithStatementsShorthand() {
    MethodInvocation a = invoke("a");
    MethodInvocation b = invoke("b");
    MethodInvocation c = invoke("c");
    List<MethodInvocation> abc = Arrays.asList(a, b, c);
    test(
        constructor(            
            statements(abc)
        ),
        "Constructor() {",
        "  a();",
        "  b();",
        "  c();",
        "}"
    );
  }

  @Test
  public void arityTest() {
    MethodInvocation m0 = invoke("m0");
    MethodInvocation m1 = invoke("m1");
    MethodInvocation m2 = invoke("m2");
    MethodInvocation m3 = invoke("m3");
    MethodInvocation m4 = invoke("m4");
    MethodInvocation m5 = invoke("m5");
    MethodInvocation m6 = invoke("m6");
    MethodInvocation m7 = invoke("m7");
    MethodInvocation m8 = invoke("m8");
    MethodInvocation m9 = invoke("m9");
    test(
        constructor(m0),
        "Constructor() {",
        "  m0();",
        "}"
    );
    test(
        constructor(m0, m1),
        "Constructor() {",
        "  m0();",
        "  m1();",
        "}"
    );
    test(
        constructor(m0, m1, m2),
        "Constructor() {",
        "  m0();",
        "  m1();",
        "  m2();",
        "}"
    );
    test(
        constructor(m0, m1, m2, m3),
        "Constructor() {",
        "  m0();",
        "  m1();",
        "  m2();",
        "  m3();",
        "}"
    );
    test(
        constructor(m0, m1, m2, m3, m4),
        "Constructor() {",
        "  m0();",
        "  m1();",
        "  m2();",
        "  m3();",
        "  m4();",
        "}"
    );
    test(
        constructor(m0, m1, m2, m3, m4, m5),
        "Constructor() {",
        "  m0();",
        "  m1();",
        "  m2();",
        "  m3();",
        "  m4();",
        "  m5();",
        "}"
    );
    test(
        constructor(m0, m1, m2, m3, m4, m5, m6),
        "Constructor() {",
        "  m0();",
        "  m1();",
        "  m2();",
        "  m3();",
        "  m4();",
        "  m5();",
        "  m6();",
        "}"
    );
    test(
        constructor(m0, m1, m2, m3, m4, m5, m6, m7),
        "Constructor() {",
        "  m0();",
        "  m1();",
        "  m2();",
        "  m3();",
        "  m4();",
        "  m5();",
        "  m6();",
        "  m7();",
        "}"
    );
    test(
        constructor(m0, m1, m2, m3, m4, m5, m6, m7, m8),
        "Constructor() {",
        "  m0();",
        "  m1();",
        "  m2();",
        "  m3();",
        "  m4();",
        "  m5();",
        "  m6();",
        "  m7();",
        "  m8();",
        "}"
    );
    test(
        constructor(m0, m1, m2, m3, m4, m5, m6, m7, m8, m9),
        "Constructor() {",
        "  m0();",
        "  m1();",
        "  m2();",
        "  m3();",
        "  m4();",
        "  m5();",
        "  m6();",
        "  m7();",
        "  m8();",
        "  m9();",
        "}"
    );
  }

  private void test(ConstructorCode code, String... lines) {
    testToString(code, lines);
  }

}