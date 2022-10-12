/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
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
