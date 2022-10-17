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
package br.com.objectos.code.java.statement;

import static br.com.objectos.code.java.expression.Expressions.hint;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.NewClass._new;
import static br.com.objectos.code.java.statement.ReturnStatement._return;
import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.ArrayList;
import org.testng.annotations.Test;

public class ReturnStatementTest extends AbstractCodeJavaTest {

  @Test
  public void returnStatement() {
    test(_return("name"),
        "return name");
  }

  @Test
  public void returnStatement_expression() {
    test(_return(_new(t(ArrayList.class), hint())),
        "return new java.util.ArrayList<>()");
  }

  @Test
  public void returnStatement_expressionWithNewLines() {
    Identifier that = id("that");
    test(
        _return(
            id("first").invoke("equals", that.invoke("first")).nl()
                .and(id("second").invoke("equals", that.invoke("second"))).nl()
                .and(id("third").invoke("equals", that.invoke("third")))
        ),
        "return first.equals(that.first())",
        "    && second.equals(that.second())",
        "    && third.equals(that.third())");
  }

}
