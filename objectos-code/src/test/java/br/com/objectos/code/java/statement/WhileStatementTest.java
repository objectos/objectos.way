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

import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.statement.Block.block;
import static br.com.objectos.code.java.statement.WhileStatement._while;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class WhileStatementTest extends AbstractCodeJavaTest {

  @Test
  public void whileStatement() {
    test(
        _while(invoke("condition"),
            block(
                invoke("doSomething")
            )
        ),
        "while (condition()) {",
        "  doSomething();",
        "}"
    );
    test(
        _while(invoke("condition"),
            invoke("doSomething")
        ),
        "while (condition()) doSomething();"
    );
  }

}
