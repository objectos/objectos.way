/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.type;

import static br.com.objectos.code.java.type.NamedTypes.tvar;
import static org.testng.Assert.assertEquals;

import br.com.objectos.code.java.expression.MethodReferenceReferenceExpression;
import br.com.objectos.code.java.expression.MethodReferenceReferenceExpressionTest;
import br.com.objectos.code.java.io.ImportSet;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class NamedTypeVariableTest extends AbstractCodeJavaTest
    implements
    MethodReferenceReferenceExpressionTest.Adapter {

  @Test(description = "A NamedTypeVariable should never generate an import declaration.")
  public void acceptJavaFileImportSet() {
    ImportSet set = ImportSet.forPackage(TESTING_CODE);
    NamedTypeVariable e = NamedTypeVariable.of("E");

    assertEquals(set.get(e), "E");

    test(
        set,
        "package testing.code;"
    );
  }

  @Test
  public void getArrayCreationExpressionName() {
    NamedTypeVariable e;
    e = NamedTypeVariable.of("E");

    assertEquals(e.getArrayCreationExpressionName(), NamedClass.of(Object.class));
  }

  @Override
  public final MethodReferenceReferenceExpression methodReferenceReferenceExpression() {
    return tvar("X");
  }

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        MethodReferenceReferenceExpressionTest.with(this)
    };
  }

  @Test
  public void toNamedArray() {
    NamedTypeVariable e;
    e = NamedTypeVariable.of("E");

    NamedArray a1 = e.toNamedArray();
    NamedArray a2 = a1.toNamedArray();
    NamedArray a3 = a2.toNamedArray();

    test(a1, "E[]");
    test(a2, "E[][]");
    test(a3, "E[][][]");
  }

}
