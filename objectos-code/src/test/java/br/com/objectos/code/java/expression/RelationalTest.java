/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.ArrayList;
import org.testng.annotations.Test;

public class RelationalTest extends AbstractCodeJavaTest {

  public interface Adapter {

    RelationalExpression relationalExpression();

  }

  private final Adapter adapter;

  private RelationalTest(Adapter adapter) {
    this.adapter = adapter;
  }

  public static RelationalTest with(Adapter adapter) {
    return new RelationalTest(adapter);
  }

  @Test
  public void numericalComparisons() {
    test(relationalExpression().lt(id("y")), relationalExpression(" < y"));
    test(relationalExpression().gt(id("y")), relationalExpression(" > y"));
    test(relationalExpression().le(id("y")), relationalExpression(" <= y"));
    test(relationalExpression().ge(id("y")), relationalExpression(" >= y"));
  }
  
  @Test
  public void instanceOf() {
    test(relationalExpression().instanceOf(t(ArrayList.class)),
        relationalExpression(
            " instanceof java.util.ArrayList"
        ));
  }
  
  private RelationalExpression relationalExpression() {
    return adapter.relationalExpression();
  }
  
  private String relationalExpression(String trailer) {
    return adapter.relationalExpression() + trailer;
  }

}
