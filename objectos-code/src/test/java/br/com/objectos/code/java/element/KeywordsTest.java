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
package br.com.objectos.code.java.element;

import static br.com.objectos.code.java.declaration.MethodCode.method;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.statement.Block.block;
import static br.com.objectos.code.java.statement.ForStatement._for;
import static br.com.objectos.code.java.statement.IfStatement._if;
import static br.com.objectos.code.java.statement.ReturnStatement._return;
import static br.com.objectos.code.java.statement.WhileStatement._while;
import static br.com.objectos.code.java.type.NamedTypes._int;
import static br.com.objectos.code.java.type.NamedTypes._void;
import static br.com.objectos.code.java.type.NamedTypes.t;
import static br.com.objectos.code.java.type.NamedTypes.tvar;

import br.com.objectos.code.java.type.NamedParameterized;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.Iterator;
import org.testng.annotations.Test;

public class KeywordsTest extends AbstractCodeJavaTest {

  @Test
  public void breakTest() {
    test(
        _while(invoke("alwaysTrue"),
            block(
                Keywords._break()
            )
        ),
        "while (alwaysTrue()) {",
        "  break;",
        "}"
    );
  }

  @Test
  public void continueTest() {
    test(
        _for(tvar("E"), id("e"), id("elements"),
            block(
                _if(invoke("cond", id("e")),
                    Keywords._continue()
                )
            )
        ),
        "for (E e : elements) {",
        "  if (cond(e)) {",
        "    continue;",
        "  }",
        "}"
    );
  }

  @Test
  public void returnTest() {
    test(
        method(_void(), id("doNothing"),
            Keywords._return()
        ),
        "void doNothing() {",
        "  return;",
        "}"
    );
  }

  @Test
  public void superTest() {
    test(
        method(_int(), id("returnSuperField"),
            _return(Keywords._super().id("FIELD"))
        ),
        "int returnSuperField() {",
        "  return super.FIELD;",
        "}"
    );
  }

  @Test
  public void thisTest() {
    NamedParameterized it = t(t(Iterator.class), tvar("E"));

    test(
        method(it, id("self"),
            _return(Keywords._this())
        ),
        "java.util.Iterator<E> self() {",
        "  return this;",
        "}"
    );
  }

}
