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

import static br.com.objectos.code.java.element.Keywords._break;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.statement.CaseSwitchElement._case;
import static br.com.objectos.code.java.statement.DefaultSwitchElement._default;
import static br.com.objectos.code.java.statement.SwitchStatement._switch;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class SwitchStatementTest extends AbstractCodeJavaTest {

  @Test
  public void switchStatement_onInt() {
    test(
        _switch(id("onInt"),
            _case(0),
            _case(1,
                invoke("doSomething"),
                _break()
            ),
            _default(
                invoke("doDefault"),
                _break()
            )
        ),
        "switch (onInt) {",
        "  case 0:",
        "  case 1:",
        "    doSomething();",
        "    break;",
        "  default:",
        "    doDefault();",
        "    break;",
        "}"
    );
  }
  
  @Test
  public void emptyDefaultSwitchElement() {
    test(
        _switch(id("empty"),
            _default(),
            _case(0,
                invoke("doSomething"),
                _break()
            ),
            _case(1,
                invoke("doDefault"),
                _break()
            )
        ),
        "switch (empty) {",
        "  default:",
        "  case 0:",
        "    doSomething();",
        "    break;",
        "  case 1:",
        "    doDefault();",
        "    break;",
        "}"
    );
  }

  @Test
  public void switchStatement_onStringLiteral() {
    test(
        _switch(id("onString"),
            _case("0",
                invoke("doZero"),
                _break()
            ),
            _case("1"),
            _default(
                invoke("doOne"),
                _break()
            )
        ),
        "switch (onString) {",
        "  case \"0\":",
        "    doZero();",
        "    break;",
        "  case \"1\":",
        "  default:",
        "    doOne();",
        "    break;",
        "}"
    );
  }

  @Test
  public void switchStatement_onEnum() {
    test(
        _switch(id("onEnum"),
            _case(id("ZERO"),
                invoke("doZero"),
                _break()
            ),
            _case(id("ONE")),
            _default(
                invoke("doOne"),
                _break()
            )
        ),
        "switch (onEnum) {",
        "  case ZERO:",
        "    doZero();",
        "    break;",
        "  case ONE:",
        "  default:",
        "    doOne();",
        "    break;",
        "}");
  }

}
