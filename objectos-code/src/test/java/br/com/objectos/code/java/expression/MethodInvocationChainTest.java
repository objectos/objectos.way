/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.element.NewLine.nl;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.expression.MethodInvocationChain.chain;
import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.util.AbstractCodeJavaTest;
import objectos.util.UnmodifiableList;
import org.testng.annotations.Test;

public class MethodInvocationChainTest extends AbstractCodeJavaTest {

  @Test
  public void chainTest() {
    test(
        chain(
            id("foo"),
            invoke("builder"), nl(),
            invoke("withString", l("abc")), nl(),
            invoke("withClass", l(Number.class)), nl(),
            invoke("withClassFromClassName", l(t(String.class))), nl(),
            invoke("withIterableAsArgs", UnmodifiableList.of(l(0), l(1))), nl(),
            invoke("build")
        ),
        "foo.builder()",
        "    .withString(\"abc\")",
        "    .withClass(java.lang.Number.class)",
        "    .withClassFromClassName(java.lang.String.class)",
        "    .withIterableAsArgs(0, 1)",
        "    .build()"
    );
  }

}
