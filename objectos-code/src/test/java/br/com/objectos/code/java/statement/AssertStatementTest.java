/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
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