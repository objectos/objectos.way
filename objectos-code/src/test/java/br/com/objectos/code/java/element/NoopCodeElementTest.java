/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
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
