/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.declaration;

import static br.com.objectos.code.java.declaration.Modifiers.PRIVATE;
import static br.com.objectos.code.java.declaration.Modifiers.PROTECTED;
import static br.com.objectos.code.java.declaration.Modifiers.PUBLIC;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class ConstructorCodeBuilderTest extends AbstractCodeJavaTest {
  
  @Test
  public void modifiers() {
    test(
        ConstructorCode.builder().build(),
        "Constructor() {}"
    );
    test(
        ConstructorCode.builder()
            .addModifier(PUBLIC)
            .build(),
        "public Constructor() {}"
    );
    test(
        ConstructorCode.builder()
            .addModifier(PROTECTED)
            .build(),
        "protected Constructor() {}"
    );
    test(
        ConstructorCode.builder()
            .addModifier(PRIVATE)
            .build(),
        "private Constructor() {}"
    );
  }
  
}
