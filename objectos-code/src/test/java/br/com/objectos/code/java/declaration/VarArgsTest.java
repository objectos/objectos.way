/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.declaration;

import static br.com.objectos.code.java.type.NamedTypes._int;
import static br.com.objectos.code.java.type.NamedTypes.a;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class VarArgsTest extends AbstractCodeJavaTest {

  @Test
  public void test() {
    test(VarArgs.of(a(_int())), "int...");
    test(VarArgs.of(a(a(_int()))), "int[]...");
    test(VarArgs.of(a(a(a(_int())))), "int[][]...");
  }

}
