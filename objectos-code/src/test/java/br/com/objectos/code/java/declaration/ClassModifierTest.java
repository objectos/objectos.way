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

public class ClassModifierTest extends AbstractCodeJavaTest {

  @Test
  public void _private_static_final() {
    ClassModifierSet res = ClassModifierSet._private()._static()._final().build();
    test(res, "private static final");
  }

  @Test
  public void merge() {
    ClassModifierSet res = ClassModifierSet._public()._static()
        .withModifier(ClassModifierSet._final().build())
        .build();
    test(res, "public static final");
  }

  private void test(ClassModifierSet mod, String... lines) {
    testToString(mod, lines);
  }

}