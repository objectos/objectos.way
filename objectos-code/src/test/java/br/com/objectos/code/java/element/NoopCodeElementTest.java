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

import static br.com.objectos.code.java.declaration.ClassCode._class;
import static br.com.objectos.code.java.declaration.Modifiers._final;
import static br.com.objectos.code.java.declaration.Modifiers._public;
import static br.com.objectos.code.java.element.NoopCodeElement.noop;
import static br.com.objectos.code.java.expression.Expressions.id;

import br.com.objectos.code.java.declaration.MethodCode;
import br.com.objectos.code.util.AbstractCodeCoreTest;
import org.testng.annotations.Test;

@SuppressWarnings("unused")
public class NoopCodeElementTest extends AbstractCodeCoreTest {

  @Test
  public void classCode() {
    testToString(
        _class(
            true ? noop() : _public(),
            id("Noop")
        ),
        "class Noop {}"
    );
  }

  @Test
  public void methodCode() {
    testToString(
        MethodCode.method(
            true ? noop() : _public(), _final(),
            id("noop")
        ),
        "final void noop();"
    );
  }

}
