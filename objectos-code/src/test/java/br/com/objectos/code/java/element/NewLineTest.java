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

import static br.com.objectos.code.java.declaration.FieldCode.field;
import static br.com.objectos.code.java.declaration.Modifiers._final;
import static br.com.objectos.code.java.declaration.Modifiers._private;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.type.NamedTypes._int;

import br.com.objectos.code.java.declaration.ClassCode;
import br.com.objectos.code.java.io.BodyFormatter;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class NewLineTest extends AbstractCodeJavaTest {

  @Test
  public void single() {
    testToString(
        ClassCode.builder()
            .addNewLine(NewLine.nl())
            .addField(field(_private(), _final(), _int(), id("value")))
            .addNewLine(NewLine.nl())
            .buildWith(BodyFormatter.unformatted()),
        "class Unnamed {",
        "",
        "  private final int value;",
        "",
        "}");
  }

}