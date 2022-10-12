/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.declaration;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class InterfaceModifierTest extends AbstractCodeJavaTest {

  @Test
  public void _private_static_final() {
    InterfaceModifierSet res = InterfaceModifierSet._private()._static().build();
    test(res, "private static");
  }

  @Test
  public void merge() {
    InterfaceModifierSet res = InterfaceModifierSet._public()._static()
        .withModifier(InterfaceModifierSet._strictfp().build())
        .build();
    test(res, "public static strictfp");
  }

  private void test(InterfaceModifierSet mod, String... lines) {
    testToString(mod, lines);
  }

}