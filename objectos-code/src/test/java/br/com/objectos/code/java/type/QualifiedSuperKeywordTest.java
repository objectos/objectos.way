/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.type;

import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.java.element.SuperKeywordTest;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class QualifiedSuperKeywordTest extends AbstractCodeJavaTest {

  @Test
  public void fieldAccess() {
    test(
        t(SuperKeywordTest.class)._super().id("FIELD"),
        "br.com.objectos.code.java.element.SuperKeywordTest.super.FIELD"
    );
  }
  
  @Test
  public void methodInvocation() {
    test(
        t(SuperKeywordTest.class)._super().invoke("method"),
        "br.com.objectos.code.java.element.SuperKeywordTest.super.method()"
    );
  }

}
