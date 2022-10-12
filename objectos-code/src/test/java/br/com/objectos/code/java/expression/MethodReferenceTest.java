/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.expression;

import static br.com.objectos.code.java.element.Keywords._super;
import static br.com.objectos.code.java.element.Keywords._this;
import static br.com.objectos.code.java.expression.Expressions.hint;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.expression.Expressions.l;
import static br.com.objectos.code.java.expression.Expressions.parens;
import static br.com.objectos.code.java.expression.Expressions.ref;
import static br.com.objectos.code.java.type.NamedTypes._int;
import static br.com.objectos.code.java.type.NamedTypes.t;
import static br.com.objectos.code.java.type.NamedTypes.tvar;

import br.com.objectos.code.java.type.NamedArray;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.Arrays;
import java.util.List;
import org.testng.annotations.Test;

public class MethodReferenceTest extends AbstractCodeJavaTest {

  @Test
  public void refTestWithExpressionName() {
    test(ref(id("a").id("b"), "c"),
        "a.b::c");
    test(ref(id("a").id("b"), hint(t(String.class)), "c"),
        "a.b::<java.lang.String> c");
  }

  @Test
  public void refTestWithKeyword() {
    test(ref(_this(), "foo"), "this::foo");
    test(ref(_this(), hint(t(Integer.class)), "foo"), "this::<java.lang.Integer> foo");
    test(ref(_super(), "toString"), "super::toString");
  }

  @Test
  public void refTestWithPrimary() {
    test(ref(id("a"), "b"),
        "a::b");
    test(ref(id("a").aget(l(0)), "b"),
        "a[0]::b");
    test(ref(invoke("a").id("b"), "c"),
        "a().b::c");
    test(ref(parens(
        id("test").ternary(
            id("list").invoke("replaceAll", ref(t(String.class), "trim")),
            id("list")
        )), "iterator"),
        "(test ? list.replaceAll(java.lang.String::trim) : list)::iterator");
  }

  @Test
  public void refTestWithReferenceType() {
    test(ref(t(String.class), "length"),
        "java.lang.String::length");
    test(ref(t(t(List.class), t(String.class)), "size"),
        "java.util.List<java.lang.String>::size");
    test(ref(NamedArray.of(_int()), "clone"),
        "int[]::clone");
    test(ref(tvar("T"), "tvarMember"),
        "T::tvarMember");
    test(ref(t(Arrays.class), hint(t(String.class)), "sort"),
        "java.util.Arrays::<java.lang.String> sort");
  }

}
