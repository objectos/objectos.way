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

import static br.com.objectos.code.java.element.Keywords._continue;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.statement.ContinueStatement._continue;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class ContinueStatementTest extends AbstractCodeJavaTest {

  @Test
  public void keywordOnly() {
    test(_continue(), "continue");
  }

  @Test
  public void with_label() {
    test(_continue(id("outer")), "continue outer");
  }

}
