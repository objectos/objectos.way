/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
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
