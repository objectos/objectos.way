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

public class MethodModifierTest extends AbstractCodeJavaTest {

  @Test
  public void _public_static_final() {
    MethodModifierSet res = MethodModifierSet._public()._static()._final().build();
    test(res, "public static final");
  }

  @Test
  public void merge() {
    MethodModifierSet res = MethodModifierSet._public()._static()
        .withModifier(MethodModifierSet._final().build())
        .build();
    test(res, "public static final");
  }

  private void test(MethodModifierSet mod, String... lines) {
    testToString(mod, lines);
  }

}