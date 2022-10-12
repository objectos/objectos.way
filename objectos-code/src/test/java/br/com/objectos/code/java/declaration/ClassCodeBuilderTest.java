/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.declaration;

import static br.com.objectos.code.java.declaration.Modifiers.FINAL;
import static br.com.objectos.code.java.declaration.Modifiers.PUBLIC;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.io.InputStream;
import objectos.util.UnmodifiableList;
import org.testng.annotations.Test;

public class ClassCodeBuilderTest extends AbstractCodeJavaTest {

  @Test
  public void modifiers() {
    test(
        ClassCode.builder()
            .addModifier()
            .build(),
        "class Unnamed {}"
    );
    test(
        ClassCode.builder()
            .addModifier(PUBLIC)
            .build(),
        "public class Unnamed {}"
    );
    test(
        ClassCode.builder()
            .addModifier(PUBLIC, FINAL)
            .build(),
        "public final class Unnamed {}"
    );
    test(
        ClassCode.builder()
            .addModifiers(UnmodifiableList.of(PUBLIC, FINAL))
            .build(),
        "public final class Unnamed {}"
    );
  }

  @Test
  public void simpleName() {
    test(
        ClassCode.builder()
            .simpleName("FromString")
            .build(),
        "class FromString {}"
    );
    test(
        ClassCode.builder()
            .simpleName(TESTING_CODE.nestedClass("FromClassName"))
            .build(),
        "class FromClassName {}"
    );
  }

  @Test
  public void superclass() {
    test(
        ClassCode.builder()
            .simpleName("Something")
            .superclass(TESTING_CODE.nestedClass("Else"))
            .build(),
        "class Something extends testing.code.Else {}"
    );
    test(
        ClassCode.builder()
            .simpleName("OfClass")
            .superclass(InputStream.class)
            .build(),
        "class OfClass extends java.io.InputStream {}"
    );
  }

}
