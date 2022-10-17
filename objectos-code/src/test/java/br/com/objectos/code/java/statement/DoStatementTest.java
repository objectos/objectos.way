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
import static br.com.objectos.code.java.statement.DoStatement._do;
import static br.com.objectos.code.java.statement.DoStatement._while;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class DoStatementTest extends AbstractCodeJavaTest {

  @Test
  public void fromCode() {
    test(
        _do(
            block(
                invoke("a")
            ),
            _while(invoke("condition"))
        ),
        "do {",
        "  a();",
        "} while (condition())"
    );
    test(
        _do(
            invoke("a"),
            _while(invoke("condition"))
        ),
        "do a(); while (condition())"
    );
  }

}