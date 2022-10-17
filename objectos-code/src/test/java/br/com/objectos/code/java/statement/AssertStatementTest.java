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

import static br.com.objectos.code.java.expression.Expressions.eq;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.statement.AssertStatement._assert;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class AssertStatementTest extends AbstractCodeJavaTest {

  @Test
  public void singleExpression() {
    test(
        _assert(eq(id("something"), l(true))),
        "assert something == true"
    );
  }

  @Test
  public void withDetailMessage() {
    test(
        _assert(eq(id("something"), l(true)), invoke("createMessage")),
        "assert something == true : createMessage()");
  }

}