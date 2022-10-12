/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.statement;

import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.statement.BreakStatement._break;

import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class BreakStatementTest extends AbstractCodeJavaTest {

  @Test
  public void keywordOnly() {
    test(Keywords._break(), "break");
  }

  @Test
  public void with_label() {
    test(_break(id("outer")), "break outer");
  }

}
