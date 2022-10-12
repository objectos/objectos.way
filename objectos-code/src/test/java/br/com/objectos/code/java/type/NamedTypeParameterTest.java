/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.type;

import static org.testng.Assert.assertEquals;

import br.com.objectos.code.java.io.ImportSet;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.io.Closeable;
import java.io.InputStream;
import org.testng.annotations.Test;

public class NamedTypeParameterTest extends AbstractCodeJavaTest {

  @Test
  public void _toStringBoundedMany() {
    NamedTypeParameter res = NamedTypeParameter.named("E")
        .addBound(Object.class)
        .addBound(Runnable.class);
    assertEquals(res.toString(), "E extends java.lang.Object & java.lang.Runnable");
  }

  @Test
  public void _toStringBoundedSingle() {
    NamedTypeParameter res = NamedTypeParameter.named("E")
        .addBound(Number.class);
    assertEquals(res.toString(), "E extends java.lang.Number");
  }

  @Test
  public void _toStringUnbounded() {
    NamedTypeParameter res = NamedTypeParameter.named("E");
    assertEquals(res.toString(), "E");
  }

  @Test(description = ""
      + "a type parameter without bounds should NOT generate import decl."
      + "a type parameter with bounds should generate import decl. if necessary.")
  public void acceptJavaFileImportSet() {
    ImportSet set = ImportSet.forPackage(TESTING_CODE);

    NamedTypeParameter u;
    u = NamedTypeParameter.named("U");
    NamedTypeParameter b;
    b = NamedTypeParameter.named("B").addBound(InputStream.class);
    NamedTypeParameter i;
    i = NamedTypeParameter.named("I").addBound(InputStream.class).addBound(Closeable.class);

    assertEquals(set.get(u), "U");
    assertEquals(set.get(b), "B extends InputStream");
    assertEquals(set.get(i), "I extends InputStream & Closeable");

    testToString(
        set,
        "package testing.code;",
        "",
        "import java.io.Closeable;",
        "import java.io.InputStream;"
    );
  }

}