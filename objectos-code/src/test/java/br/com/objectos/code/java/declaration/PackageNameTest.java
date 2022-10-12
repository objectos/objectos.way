/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.declaration;

import static br.com.objectos.code.java.declaration.PackageName._package;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.nio.file.Path;
import org.testng.annotations.Test;

public class PackageNameTest extends AbstractCodeJavaTest {

  @Test
  public void nestedClass() {
    PackageName packageName = PackageNameFake.TESTING_CODE;
    NamedClass res = packageName.nestedClass("Subject");
    assertEquals(res.toString(), "testing.code.Subject");
  }

  @Test
  public void nestedPackage() {
    PackageName a = PackageName.named("a");
    PackageName b = a.nestedPackage("b");
    PackageName c = b.nestedPackage("c");
    testToString(a, "a");
    testToString(b, "a.b");
    testToString(c, "a.b.c");
    testToString(_package(c, "d"), "a.b.c.d");
  }

  @Test
  public void nestedPackageInvalid() {
    PackageName a = PackageName.named("a");
    try {
      a.nestedPackage("123");
      fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "a.123 is not a valid package name");
    }

    try {
      a.nestedPackage(".b");
      fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "a..b is not a valid package name");
    }
  }

  @Test
  public void ofClass() {
    PackageName res = PackageName.of(getClass());
    assertEquals(res.toString(), "br.com.objectos.code.java.declaration");
  }

  @Test
  public void ofPackage() {
    PackageName res = PackageName.of(getClass().getPackage());
    assertEquals(res.toString(), "br.com.objectos.code.java.declaration");
  }

  @Test
  public void packageShorthand() {
    testToString(_package("a"), "a");
    testToString(_package("a.b"), "a.b");
    testToString(_package("a.b.c"), "a.b.c");
    testToString(_package("_a_._b_._c_"), "_a_._b_._c_");
    testToString(_package("_123.four.five"), "_123.four.five");
  }

  @Test(description = "it should not accept invalid package names")
  public void packageShorthandInvalid() {
    try {
      _package("123");
      fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "123 is not a valid package name");
    }

    try {
      _package("a.break");
      fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "a.break is not a valid package name");
    }
  }

  @Test
  public void resolve() {
    var src = TMPDIR;

    var packageName = PackageName.unnamed();

    var unnamed = packageName.resolve(src);

    assertEquals(unnamed, src);

    packageName = PackageName._package("a.b");

    var result = packageName.resolve(src);

    assertEquals(result, TMPDIR.resolve(Path.of("a", "b")));
  }

  @Test
  public void simpleName() {
    assertEquals(
      PackageName.unnamed().getSimpleName(),
      ""
    );
    assertEquals(
      PackageName.named("simple").getSimpleName(),
      "simple"
    );
    assertEquals(
      PackageName.named("fully.qualified.name").getSimpleName(),
      "name"
    );
  }

}