/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.util;

import br.com.objectos.code.java.declaration.PackageName;
import br.com.objectos.code.java.declaration.PackageNameFake;
import java.io.Closeable;
import java.io.InputStream;

public abstract class AbstractCodeJavaTest extends AbstractCodeCoreTest {

  protected static final PackageName TESTING_CODE = PackageNameFake.TESTING_CODE;
  protected static final PackageName TESTING_OTHER = PackageNameFake.TESTING_OTHER;

  protected abstract class Empty {}

  protected abstract class Generic<//
      U /* U meaning Unbounded */, //
      B extends InputStream /* B meaning Bounded */, //
      I extends InputStream & Closeable /* I as Intersection */> {
    Generic() {}
  }

  protected final void test(Object el, String... lines) {
    testToString(el, lines);
  }

}