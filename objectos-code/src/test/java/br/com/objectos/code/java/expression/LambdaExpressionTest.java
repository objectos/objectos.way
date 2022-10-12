/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.declaration.ParameterCode.param;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.expression.Expressions.lambda;
import static br.com.objectos.code.java.statement.Block.block;
import static br.com.objectos.code.java.statement.ReturnStatement._return;
import static br.com.objectos.code.java.statement.Statements._var;
import static br.com.objectos.code.java.type.NamedTypes._int;
import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.java.declaration.ParameterCode;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.Arrays;
import org.testng.annotations.Test;

public class LambdaExpressionTest extends AbstractCodeJavaTest {

  @Test
  public void lambdaTest() {
    Identifier a = id("a");
    Identifier b = id("b");
    Identifier c = id("c");
    Identifier d = id("d");
    Identifier e = id("e");
    Identifier f = id("f");
    Identifier g = id("g");
    Literal l42 = l(42);
    test(lambda(l42),
        "() -> 42");
    test(lambda(a, a.add(l42)),
        "a -> a + 42");
    test(lambda(a, b, a.add(b).add(l42)),
        "(a, b) -> a + b + 42");
    test(lambda(a, b, c, a.add(b).add(c).add(l42)),
        "(a, b, c) -> a + b + c + 42");
    test(lambda(a, b, c, d, a.add(b).add(c).add(d).add(l42)),
        "(a, b, c, d) -> a + b + c + d + 42");
    test(lambda(Arrays.asList(a, b, c, d, e, f, g),
        a.add(b).add(c).add(d).add(e).add(f).add(g).add(l42)),
        "(a, b, c, d, e, f, g) -> a + b + c + d + e + f + g + 42");
  }

  @Test
  public void lambdaTestWithFormalParams() {
    ParameterCode p1 = param(_int(), id("p1"));
    ParameterCode p2 = param(_int(), id("p2"));
    ParameterCode p3 = param(_int(), id("p3"));
    ParameterCode p4 = param(_int(), id("p4"));
    ParameterCode p5 = param(_int(), id("p5"));
    ParameterCode p6 = param(_int(), id("p6"));
    MethodInvocation noop = invoke("noop");
    test(lambda(p1, noop),
        "(int p1) -> noop()");
    test(lambda(p1, p2, noop),
        "(int p1, int p2) -> noop()");
    test(lambda(p1, p2, p3, noop),
        "(int p1, int p2, int p3) -> noop()");
    test(lambda(p1, p2, p3, p4, noop),
        "(int p1, int p2, int p3, int p4) -> noop()");
    test(lambda(Arrays.asList(p1, p2, p3, p4, p5, p6), noop),
        "(int p1, int p2, int p3, int p4, int p5, int p6) -> noop()");
  }

  @Test
  public void lambdaTestWithBlock() {
    test(lambda(block(_return(l(42)))),
        "() -> {",
        "  return 42;",
        "}");
    test(lambda(param(t(String.class), id("s")), block(
        _var(_int(), "l", id("s").invoke("length")),
        _return("l")
    )),
        "(java.lang.String s) -> {",
        "  int l = s.length();",
        "  return l;",
        "}");
  }

}
