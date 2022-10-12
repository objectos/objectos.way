/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.declaration;

import static br.com.objectos.code.java.declaration.ParameterCode.param;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.type.NamedTypes._int;
import static br.com.objectos.code.java.type.NamedTypes.a;
import static br.com.objectos.code.java.type.NamedTypes.t;
import static org.testng.Assert.assertEquals;

import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import br.com.objectos.code.util.GetOnly;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.testng.annotations.Test;

public class ParameterCodeTest extends AbstractCodeJavaTest {

  abstract class Subject {
    void isVarArgs(int... values) {}

    void notVarArgs(int[] values) {}
    void type(String name) {}
  }

  @Test
  public void ofFromVariableElement() {
    assertEquals(
      ParameterCode.of(onlyParameterOfMethod("type")).toString(),
      "java.lang.String name"
    );
    assertEquals(
      ParameterCode.of(onlyParameterOfMethod("isVarArgs")).toString(),
      "int... values"
    );
    assertEquals(
      ParameterCode.of(onlyParameterOfMethod("notVarArgs")).toString(),
      "int[] values"
    );
  }

  @Test(description = ""
      + "Verify that query methods return the correct values.")
  public void queryMethods() {
    NamedClass stringType = t(String.class);
    Identifier name = id("name");

    ParameterCode param = param(stringType, name);
    assertEquals(param.type(), stringType);
    assertEquals(param.name(), name);
  }

  @Test(
      description = "The simplest parameter is the one with type and name only."
  )
  public void simpleParameter() {
    test(param(t(String.class), id("name")),
      "java.lang.String name");
    test(param(_int(), id("value")),
      "int value");
    test(param(VarArgs.of(a(_int())), id("values")),
      "int... values");
  }

  private ExecutableElement method(String methodName) {
    return getMethodElement(Subject.class, methodName);
  }

  private VariableElement onlyParameterOfMethod(String methodName) {
    ExecutableElement method;
    method = method(methodName);

    List<? extends VariableElement> parameters;
    parameters = method.getParameters();

    return GetOnly.of(parameters);
  }

}