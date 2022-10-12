/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.type;

import static br.com.objectos.code.java.type.NamedTypes.t;
import static br.com.objectos.code.java.type.NamedTypes.tvar;
import static org.testng.Assert.assertEquals;

import br.com.objectos.code.java.expression.MethodReferenceReferenceExpression;
import br.com.objectos.code.java.expression.MethodReferenceReferenceExpressionTest;
import br.com.objectos.code.java.io.ImportSet;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.Arrays;
import java.util.List;
import objectos.util.UnmodifiableList;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class NamedParameterizedTest extends AbstractCodeJavaTest
    implements
    MethodReferenceReferenceExpressionTest.Adapter {

  @Test(description = "A NamedParameterized should generate import for raw and arguments.")
  public void acceptJavaFileImportSet() {
    ImportSet set;
    set = ImportSet.forPackage(TESTING_CODE);

    NamedClass raw = NamedClass.of(TESTING_OTHER, "Raw");
    NamedClass e1 = NamedClass.of(TESTING_OTHER, "E1");
    NamedClass e2 = NamedClass.of(TESTING_OTHER, "E2");

    NamedParameterized parameterized = t(raw, e1, e2);

    assertEquals(
        set.get(parameterized),
        "Raw<E1, E2>"
    );

    test(
        set,
        "package testing.code;",
        "",
        "import testing.other.E1;",
        "import testing.other.E2;",
        "import testing.other.Raw;"
    );
  }

  @Test
  public void getArrayCreationExpressionName() {
    NamedClass list = NamedClass.of(List.class);
    NamedParameterized listOfE = t(list, tvar("E"));
    assertEquals(listOfE.getArrayCreationExpressionName(), list);
  }

  @Override
  public final MethodReferenceReferenceExpression methodReferenceReferenceExpression() {
    return t(t(List.class), tvar("A"));
  }

  @Test
  public void of() {
    NamedClass raw = NamedClass.of(TESTING_CODE, "Raw");
    NamedTypeVariable e1 = NamedTypeVariable.of("E1");
    NamedTypeVariable e2 = NamedTypeVariable.of("E2");
    NamedTypeVariable e3 = NamedTypeVariable.of("E3");
    NamedTypeVariable e4 = NamedTypeVariable.of("E4");
    NamedTypeVariable e5 = NamedTypeVariable.of("E5");

    test(
        NamedParameterized.of(raw, e1),
        "testing.code.Raw<E1>"
    );
    test(
        NamedParameterized.of(raw, e1, e2),
        "testing.code.Raw<E1, E2>"
    );
    test(
        NamedParameterized.of(raw, e1, e2, e3),
        "testing.code.Raw<E1, E2, E3>"
    );
    test(
        NamedParameterized.of(raw, e1, e2, e3, e4),
        "testing.code.Raw<E1, E2, E3, E4>"
    );
    test(
        NamedParameterized.of(raw, e1, e2, e3, e4, e5),
        "testing.code.Raw<E1, E2, E3, E4, E5>"
    );
    test(
        NamedParameterized.of(raw, Arrays.asList(e1, e2, e3, e4, e5)),
        "testing.code.Raw<E1, E2, E3, E4, E5>"
    );
    test(
        NamedParameterized.of(raw, UnmodifiableList.of(e1, e2, e3, e4, e5)),
        "testing.code.Raw<E1, E2, E3, E4, E5>"
    );
  }

  @Factory
  public Object[] testFactory() {
    return new Object[] {
        MethodReferenceReferenceExpressionTest.with(this)
    };
  }

  @Test
  public void toNamedArray() {
    NamedClass list = NamedClass.of(List.class);
    NamedTypeVariable e = NamedTypeVariable.of("E");

    NamedParameterized listOfE;
    listOfE = NamedParameterized.of(list, e);

    NamedArray a1 = listOfE.toNamedArray();
    NamedArray a2 = a1.toNamedArray();
    NamedArray a3 = a2.toNamedArray();

    test(a1, "java.util.List<E>[]");
    test(a2, "java.util.List<E>[][]");
    test(a3, "java.util.List<E>[][][]");
  }

}
