/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.element.Keywords._this;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class EqualityTest extends AbstractCodeJavaTest {

  public interface Adapter {

    EqualityExpression equalityExpression();

  }

  private final Adapter adapter;

  private EqualityTest(Adapter adapter) {
    this.adapter = adapter;
  }

  public static EqualityTest with(Adapter adapter) {
    return new EqualityTest(adapter);
  }

  @Test
  public void equalTo() {
    test(equalityExpression().eq(_this()), equalityExpression(" == this"));
  }

  @Test
  public void notEqualTo() {
    test(equalityExpression().ne(_this()), equalityExpression(" != this"));
  }

  private EqualityExpression equalityExpression() {
    return adapter.equalityExpression();
  }

  private String equalityExpression(String trailer) {
    return equalityExpression() + trailer;
  }

}
