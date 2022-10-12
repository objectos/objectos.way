/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.type.NamedTypes._int;
import static br.com.objectos.code.java.type.NamedTypes.t;
import static br.com.objectos.code.java.type.NamedTypes.tvar;

import br.com.objectos.code.java.type.NamedPrimitive;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.List;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class CastExpressionTest extends AbstractCodeJavaTest
    implements
    AdditiveTest.Adapter,
    MultiplicativeTest.Adapter,
    RelationalTest.Adapter {

  @Override
  public final AdditiveExpression additiveExpression() {
    return multiplicativeExpression();
  }

  @Test
  public void cast_primitive() {
    test(Expressions.cast(NamedPrimitive._int(), id("foo")),
        "(int) foo");
  }

  @Test
  public void cast_type() {
    test(Expressions.cast(t(t(List.class), tvar("E")), id("foo")),
        "(java.util.List<E>) foo");
  }

  @Override
  public final MultiplicativeExpression multiplicativeExpression() {
    return Expressions.cast(_int(), id("mul"));
  }

  @Override
  public final RelationalExpression relationalExpression() {
    return Expressions.cast(_int(), id("longVal"));
  }

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        AdditiveTest.with(this),
        MultiplicativeTest.with(this),
        RelationalTest.with(this)
    };
  }

}
