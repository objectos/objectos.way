/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.declaration;

import static br.com.objectos.code.java.declaration.EnumConstantCode.enumConstant;
import static br.com.objectos.code.java.expression.Arguments.args;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class EnumConstantCodeTest extends AbstractCodeJavaTest {
  
  @Test
  public void simpleName() {
    test(
        enumConstant(id("INSTANCE")),
        "INSTANCE"
    );
  }
  
  @Test
  public void arguments() {
    test(
        enumConstant(id("A"), args(l("a"))),
        "A(\"a\")"
    );
    test(
        enumConstant(id("MAX"), args(t(Integer.class).id("MAX_VALUE"), l("x"))),
        "MAX(java.lang.Integer.MAX_VALUE, \"x\")"
    );
  }

}
