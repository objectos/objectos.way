/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.model.element;

import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.tools.Tools.compilationUnit;
import static br.com.objectos.tools.Tools.javac;
import static br.com.objectos.tools.Tools.patchModuleWithTestClasses;
import static br.com.objectos.tools.Tools.processor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.declaration.Modifiers;
import br.com.objectos.code.java.declaration.ParameterModifier;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedType;
import br.com.objectos.code.java.type.NamedTypeVariable;
import br.com.objectos.code.java.type.NamedTypes;
import br.com.objectos.code.processing.AbstractProcessingRoundProcessor;
import br.com.objectos.code.processing.ProcessingRound;
import br.com.objectos.code.processing.type.PTypeMirror;
import br.com.objectos.code.util.AbstractCodeCoreTest;
import br.com.objectos.code.util.TypeAnnotation;
import br.com.objectos.tools.Compilation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Set;
import objectos.util.UnmodifiableList;
import objectos.util.UnmodifiableSet;
import objectos.util.GrowableList;
import org.testng.annotations.Test;

public class ProcessingParameterTest extends AbstractCodeCoreTest {

  @Test
  public void getDirectlyPresentAnnotations() {
    UnmodifiableList<ProcessingParameter> subjectParameters;
    subjectParameters = getSubjectParameters(GetDirectlyPresentAnnotationsSubject.class);

    ProcessingParameter p0;
    p0 = subjectParameters.get(0);

    UnmodifiableList<ProcessingAnnotation> p0Annotations;
    p0Annotations = p0.getDirectlyPresentAnnotations();

    assertEquals(p0Annotations.size(), 0);

    ProcessingParameter p1;
    p1 = subjectParameters.get(1);

    UnmodifiableList<ProcessingAnnotation> p1Annotations;
    p1Annotations = p1.getDirectlyPresentAnnotations();

    UnmodifiableList<String> p1Values;
    p1Values = map(p1Annotations, getDirectlyPresentAnnotationsFunction());

    assertEquals(p1Values.size(), 2);
    assertTrue(p1Values.contains("a"));
    assertTrue(p1Values.contains("b"));
  }

  @Test(description = ""
      + "getModifiers() must return the `final` modifier if it is present. "
      + "The `final` modifier has a (analogous to) 'source-only' retention policy; "
      + "test was required to do a full compilation step.")
  public void getModifiers() {
    final GrowableList<ProcessingParameter> result = new GrowableList<>();

    class ThisProcessor extends AbstractProcessingRoundProcessor {
      @Override
      public final Set<String> getSupportedAnnotationTypes() {
        return supportedAnnotationTypes(TypeAnnotation.class);
      }

      @Override
      protected final boolean process(ProcessingRound round) {
        UnmodifiableSet<ProcessingType> types = round.getAnnotatedTypes();
        for (ProcessingType type : types) {
          process(round, type);
        }
        return round.claimTheseAnnotations();
      }

      private void process(ProcessingRound round, ProcessingType type) {
        ProcessingMethod subjectMethod;
        subjectMethod = getDeclaredMethod(type, "subject");

        UnmodifiableList<ProcessingParameter> parameters;
        parameters = subjectMethod.getParameters();

        result.addAll(parameters);
      }
    }

    Compilation compilation = javac(
        processor(new ThisProcessor()),
        patchModuleWithTestClasses("br.com.objectos.code"),
        compilationUnit(
            "package testing.code;",
            "@br.com.objectos.code.util.TypeAnnotation",
            "class ModifiersSubject {",
            "  void subject(int no, final int yes) {}",
            "}"
        )
    );

    assertTrue(compilation.wasSuccessful());

    UnmodifiableList<ProcessingParameter> subject;
    subject = result.toUnmodifiableList();

    assertEquals(subject.size(), 2);

    ProcessingParameter no;
    no = subject.get(0);

    assertFalse(no.hasModifier(Modifiers.FINAL));

    UnmodifiableSet<ParameterModifier> noModifiers;
    noModifiers = no.getModifiers();

    assertEquals(noModifiers.size(), 0);

    ProcessingParameter yes;
    yes = subject.get(1);

    assertTrue(yes.hasModifier(Modifiers.FINAL));

    UnmodifiableSet<ParameterModifier> yesModifiers;
    yesModifiers = yes.getModifiers();

    assertEquals(yesModifiers.size(), 1);
    assertTrue(yesModifiers.contains(Modifiers.FINAL));
  }

  @Test
  public void getName() {
    class GetName implements Function<ProcessingParameter, String> {
      @Override
      public final String apply(ProcessingParameter input) {
        return input.getName();
      }
    }

    GetName getName;
    getName = new GetName();

    UnmodifiableList<ProcessingParameter> subjectParameters;
    subjectParameters = getSubjectParameters(GetNameSubject.class);

    UnmodifiableList<String> subjectNames;
    subjectNames = map(subjectParameters, getName);

    assertEquals(subjectNames.size(), 3);
    assertEquals(subjectNames.get(0), "a");
    assertEquals(subjectNames.get(1), "b");
    assertEquals(subjectNames.get(2), "c");

    UnmodifiableList<ProcessingParameter> abstractParameters;
    abstractParameters = getSubjectParameters(GetNameAbstract.class);

    UnmodifiableList<String> abstractNames;
    abstractNames = map(abstractParameters, getName);

    assertEquals(abstractNames.size(), 3);
    assertNotEquals(abstractNames.get(0), "a");
    assertNotEquals(abstractNames.get(1), "b");
    assertNotEquals(abstractNames.get(2), "c");
  }

  @Test
  public void getType() {
    UnmodifiableList<ProcessingParameter> subjectParameters;
    subjectParameters = getSubjectParameters(GetTypeSubject.class);

    GrowableList<NamedType> result;
    result = new GrowableList<>();

    for (int i = 0; i < subjectParameters.size(); i++) {
      ProcessingParameter parameter;
      parameter = subjectParameters.get(i);

      PTypeMirror type;
      type = parameter.getType();

      NamedType name;
      name = type.getName();

      result.add(name);
    }

    assertEquals(result.size(), 3);
    assertEquals(result.get(0), NamedTypes._int());
    assertEquals(result.get(1), NamedClass.of(String.class));
    assertEquals(result.get(2), NamedTypeVariable.of("E").toNamedArray());
  }

  @Test
  public void hasName() {
    UnmodifiableList<ProcessingParameter> subjectParameters;
    subjectParameters = getSubjectParameters(GetNameSubject.class);

    ProcessingParameter a;
    a = subjectParameters.get(0);

    assertTrue(a.hasName("a"));
    assertFalse(a.hasName("x"));

    ProcessingParameter b;
    b = subjectParameters.get(1);

    assertTrue(b.hasName("b"));
    assertFalse(b.hasName("x"));

    ProcessingParameter c;
    c = subjectParameters.get(2);

    assertTrue(c.hasName("c"));
    assertFalse(c.hasName("x"));
  }

  @Test(
      description = ""
          + "Verify that the return of toIdentifier() is correct."
          + "Also that multiple calls return the exact same instance."
  )
  public void toIdentifier() {
    UnmodifiableList<ProcessingParameter> subjectParameters;
    subjectParameters = getSubjectParameters(ToIdentifierSubject.class);

    ProcessingParameter a = subjectParameters.get(0);
    ProcessingParameter b = subjectParameters.get(1);
    ProcessingParameter c = subjectParameters.get(2);

    Identifier aname = a.toIdentifier();
    Identifier bname = b.toIdentifier();
    Identifier cname = c.toIdentifier();

    assertEquals(aname, id("a"));
    assertEquals(bname, id("b"));
    assertEquals(cname, id("c"));

    assertSame(aname, a.toIdentifier());
    assertSame(bname, b.toIdentifier());
    assertSame(cname, c.toIdentifier());
  }

  @Test
  public void toParameterCode() {
    UnmodifiableList<ProcessingParameter> subjectParameters;
    subjectParameters = getSubjectParameters(ToParameterCodeSubject.class);

    ProcessingParameter p0;
    p0 = subjectParameters.get(0);

    testToString(
        p0.toParameterCode(),
        "java.lang.String p0"
    );

    ProcessingParameter pN;
    pN = subjectParameters.get(1);

    testToString(
        pN.toParameterCode(),
        "int... pN"
    );
  }

  private Function<ProcessingAnnotation, String> getDirectlyPresentAnnotationsFunction() {
    return new Function<ProcessingAnnotation, String>() {
      @Override
      public final String apply(ProcessingAnnotation input) {
        return getDirectlyPresentAnnotationsFunction0(input);
      }
    };
  }

  private String getDirectlyPresentAnnotationsFunction0(ProcessingAnnotation annotation) {
    ProcessingAnnotationValue value;
    value = annotation.getDeclaredValue("value");

    return value.getString();
  }

  private UnmodifiableList<ProcessingParameter> getSubjectParameters(Class<?> type) {
    ProcessingMethod subject;
    subject = getDeclaredMethod(type, "subject");

    return subject.getParameters();
  }

  private <F, T> UnmodifiableList<T> map(List<F> list, Function<F, T> f) {
    GrowableList<T> result;
    result = new GrowableList<>();

    for (F e : list) {
      T apply = f.apply(e);

      result.add(apply);
    }

    return result.toUnmodifiableList();
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.PARAMETER)
  private @interface GetDirectlyPresentAnnotationsA {
    String value();
  }

  @Retention(RetentionPolicy.CLASS)
  @Target(ElementType.PARAMETER)
  private @interface GetDirectlyPresentAnnotationsB {
    String value();
  }

  @SuppressWarnings("unused")
  private class GetDirectlyPresentAnnotationsSubject {
    void subject(
        int p0,
        @GetDirectlyPresentAnnotationsA("a") @GetDirectlyPresentAnnotationsB("b") int p1
    ) {};
  }

  private abstract class GetNameAbstract {
    abstract void subject(int a, int b, int c);
  }

  @SuppressWarnings("unused")
  private class GetNameSubject {
    void subject(int a, int b, int c) {};
  }

  @SuppressWarnings("unused")
  private class GetTypeSubject<E> {
    void subject(
        int p0,
        String p1,
        @SuppressWarnings("unchecked") E... p2
    ) {};
  }

  @SuppressWarnings("unused")
  private class ToIdentifierSubject {
    void subject(int a, int b, int c) {};
  }

  @SuppressWarnings("unused")
  private class ToParameterCodeSubject {
    void subject(String p0, int... pN) {};
  }

}