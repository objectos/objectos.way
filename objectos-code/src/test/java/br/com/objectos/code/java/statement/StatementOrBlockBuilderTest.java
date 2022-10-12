/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.statement;

import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.statement.ReturnStatement._return;
import static br.com.objectos.code.java.statement.Statements._var;
import static br.com.objectos.code.java.type.NamedTypes._int;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class StatementOrBlockBuilderTest extends AbstractCodeJavaTest {

  @Test
  public void single_statement() {
    CodeElement result = new StatementOrBlockBuilder()
        .withStatement(_return("this"))
        .build();
    assertTrue(result instanceof Statement);
    test(result,
        "return this");
  }

  @Test
  public void single_statement_with_nls() {
    CodeElement result = new StatementOrBlockBuilder()
        .nl()
        .withStatement(_return("this"))
        .build();
    assertTrue(result instanceof Statement);
    test(result,
        "",
        "return this");
  }

  @Test
  public void single_non_statement_block_element_should_result_in_block() {
    CodeElement result = new StatementOrBlockBuilder()
        .withStatement(_var(_int(), "a"))
        .build();
    assertTrue(result instanceof Block);
    test(result,
        "{",
        "  int a;",
        "}");
  }

  @Test
  public void many_statements_should_result_in_block() {
    CodeElement result = new StatementOrBlockBuilder()
        .withStatement(invoke("a"))
        .withStatement(invoke("b"))
        .build();
    assertTrue(result instanceof Block);
    test(result,
        "{",
        "  a();",
        "  b();",
        "}");
  }

}