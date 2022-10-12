/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.type.NamedTypes.t;
import static br.com.objectos.code.java.type.NamedTypes.tvar;

import br.com.objectos.code.java.type.NamedType;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import objectos.util.UnmodifiableList;
import org.testng.annotations.Test;

public class TypeWitnessTest extends AbstractCodeJavaTest {

  @Test
  public void witness0() {
    test(TypeWitness.witness0(), "<>");
    test(TypeWitness.witness0(t(String.class)), "<java.lang.String>");
    test(TypeWitness.witness0(tvar("X"), tvar("Y")), "<X, Y>");
    test(TypeWitness.witness0(tvar("X"), tvar("Y"), tvar("Z")), "<X, Y, Z>");
  }

  @Test
  public void witness0WithIterable() {
    Iterable<? extends NamedType> types
        = UnmodifiableList.of(tvar("E1"), tvar("E2"), tvar("E3"), tvar("E4"));
    test(TypeWitness.witness0(types), "<E1, E2, E3, E4>");
  }

}
