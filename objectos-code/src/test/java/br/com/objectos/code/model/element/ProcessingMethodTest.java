/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.model.element;

import static br.com.objectos.code.java.type.NamedTypes.tvar;
import static br.com.objectos.tools.Tools.compilationUnit;
import static br.com.objectos.tools.Tools.javac;
import static br.com.objectos.tools.Tools.patchModuleWithTestClasses;
import static br.com.objectos.tools.Tools.processor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.declaration.AccessLevel;
import br.com.objectos.code.java.declaration.MethodModifier;
import br.com.objectos.code.java.declaration.Modifiers;
import br.com.objectos.code.java.type.NamedArray;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedParameterized;
import br.com.objectos.code.java.type.NamedType;
import br.com.objectos.code.java.type.NamedTypeParameter;
import br.com.objectos.code.java.type.NamedTypeVariable;
import br.com.objectos.code.java.type.NamedTypes;
import br.com.objectos.code.model.AbstractCodeModelTest;
import br.com.objectos.code.processing.AbstractProcessingRoundProcessor;
import br.com.objectos.code.processing.ProcessingRound;
import br.com.objectos.code.processing.type.PTypeMirror;
import br.com.objectos.code.testing.InheritedAnnotation;
import br.com.objectos.code.util.MethodAnnotation;
import br.com.objectos.code.util.TypeAnnotation;
import br.com.objectos.tools.Compilation;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.ExecutableElement;
import objectos.util.UnmodifiableList;
import objectos.util.UnmodifiableSet;
import objectos.util.GrowableList;
import org.testng.annotations.Test;

public class ProcessingMethodTest extends AbstractCodeModelTest {

  @Test
  public void getAccessLevel() {
    ProcessingType subject;
    subject = query(AccessLevelSubject.class);

    UnmodifiableList<ProcessingMethod> methods;
    methods = subject.getDeclaredMethods();

    assertEquals(methods.size(), 4);

    assertEquals(methods.get(0).getAccessLevel(), AccessLevel.PUBLIC);
    assertEquals(methods.get(1).getAccessLevel(), AccessLevel.PROTECTED);
    assertEquals(methods.get(2).getAccessLevel(), AccessLevel.DEFAULT);
    assertEquals(methods.get(3).getAccessLevel(), AccessLevel.PRIVATE);
  }

  @Test
  public void getDeclaringType() {
    class IsFromParent implements Predicate<ProcessingMethod> {
      @Override
      public final boolean test(ProcessingMethod o) {
        return o.hasName("fromParent");
      }
    }

    ProcessingType subject;
    subject = query(DeclaringTypeSubject.class);

    UnmodifiableList<ProcessingMethod> methods;
    methods = subject.getDeclaredOrInheritedMethods();

    ProcessingMethod fromParent;
    fromParent = filterAndGetOnly(methods, new IsFromParent());

    ProcessingType declaringType;
    declaringType = fromParent.getDeclaringType();

    assertEquals(declaringType.getName(), NamedClass.of(DeclaringTypeParent.class));
  }

  @Test
  public void getDirectlyPresentAnnotations() {
    ProcessingExecutableElement subject;
    subject = method(AnnotationsSubject.class, "subject");

    UnmodifiableList<ProcessingAnnotation> subjectAnnotations;
    subjectAnnotations = subject.getDirectlyPresentAnnotations();

    UnmodifiableList<NamedClass> subjectClassNames;
    subjectClassNames = annotationToClassName(subjectAnnotations);

    assertEquals(subjectClassNames.size(), 1);

    assertTrue(
        subjectClassNames.contains(NamedClass.of(MethodAnnotation.class))
    );
  }

  @Test
  public void getDocComment() {
    class ThisProcessor extends AbstractProcessingRoundProcessor {
      private final GrowableList<String> comments = new GrowableList<>();

      @Override
      public final Set<String> getSupportedAnnotationTypes() {
        return supportedAnnotationTypes(TypeAnnotation.class);
      }

      @Override
      protected final boolean process(ProcessingRound round) {
        UnmodifiableSet<ProcessingType> types;
        types = round.getAnnotatedTypes();

        for (ProcessingType type : types) {
          process0(round, type);
        }

        return round.claimTheseAnnotations();
      }

      private void process0(ProcessingRound round, ProcessingType type) {
        UnmodifiableList<ProcessingMethod> methods;
        methods = type.getDeclaredMethods();

        for (int i = 0, size = methods.size(); i < size; i++) {
          ProcessingMethod m;
          m = methods.get(i);

          comments.add(m.getDocComment());
        }
      }
    }

    ThisProcessor thisProcessor;
    thisProcessor = new ThisProcessor();

    Compilation compilation;
    compilation = javac(
        processor(thisProcessor),

        patchModuleWithTestClasses("br.com.objectos.code"),

        compilationUnit(
            "package testing;",
            "@br.com.objectos.code.util.TypeAnnotation",
            "abstract class Subject {",
            "  /**",
            "   * standard formatted Javadoc comment",
            "   */",
            "  abstract void a();",
            "  /**trimmed Javadoc comment*/",
            "  abstract void b();",
            "  /* not a Javadoc comment */",
            "  abstract void c();",
            "",
            "  abstract void d();",
            "}"
        )
    );

    compilation.assertWasSuccessful();

    UnmodifiableList<String> result;
    result = thisProcessor.comments.toUnmodifiableList();

    assertEquals(result.size(), 4);

    assertEquals(result.get(0), " standard formatted Javadoc comment\n");
    assertEquals(result.get(1), "trimmed Javadoc comment");
    assertEquals(result.get(2), "");
    assertEquals(result.get(3), "");
  }

  @Test
  public void getModifiers() {
    ProcessingType subject;
    subject = query(ModifiersSubject.class);

    UnmodifiableList<ProcessingMethod> methods;
    methods = subject.getDeclaredMethods();

    assertEquals(methods.size(), 1);

    UnmodifiableSet<MethodModifier> m0;
    m0 = methods.get(0).getModifiers();

    assertEquals(m0.size(), 2);
    assertTrue(m0.contains(Modifiers.PUBLIC));
    assertTrue(m0.contains(Modifiers.FINAL));
  }

  @Test
  public void getName() {
    ProcessingType subject;
    subject = query(NameSubject.class);

    UnmodifiableList<ProcessingMethod> methods;
    methods = subject.getDeclaredMethods();

    assertEquals(methods.size(), 1);
    assertEquals(methods.get(0).getName(), "m0");
  }

  @Test
  public void getParameters() {
    ProcessingType subject;
    subject = query(ParametersSubject.class);

    UnmodifiableList<ProcessingMethod> methods;
    methods = subject.getDeclaredMethods();

    assertEquals(methods.size(), 2);

    ProcessingExecutableElement m0;
    m0 = methods.get(0);

    UnmodifiableList<ProcessingParameter> m0Parameters;
    m0Parameters = m0.getParameters();

    assertEquals(m0Parameters.size(), 2);
    assertEquals(m0Parameters.get(0).getName(), "name");
    assertEquals(m0Parameters.get(1).getName(), "value");

    ProcessingExecutableElement m1;
    m1 = methods.get(1);

    UnmodifiableList<ProcessingParameter> m1Parameters;
    m1Parameters = m1.getParameters();

    assertEquals(m1Parameters.size(), 0);
  }

  @Test
  public void getReturnType() {
    ProcessingType subject;
    subject = query(ReturnTypeSubject.class);

    UnmodifiableList<ProcessingMethod> methods;
    methods = subject.getDeclaredMethods();

    GrowableList<NamedType> result;
    result = new GrowableList<>();

    for (int i = 0; i < methods.size(); i++) {
      ProcessingMethod method;
      method = methods.get(i);

      PTypeMirror returnType;
      returnType = method.getReturnType();

      NamedType name;
      name = returnType.getName();

      result.add(name);
    }

    assertEquals(result.size(), 7);
    assertEquals(result.get(0), NamedTypes._void());
    assertEquals(result.get(1), NamedTypes._int());
    assertEquals(result.get(2), NamedArray.of(NamedTypes._int()));
    assertEquals(result.get(3), NamedClass.of(String.class));
    assertEquals(result.get(4), NamedArray.of(NamedClass.of(String.class)));
    assertEquals(
        result.get(5),
        NamedParameterized.of(NamedClass.of(List.class), NamedClass.of(Integer.class))
    );
    assertEquals(result.get(6), NamedTypeVariable.of("E"));
  }

  @Test
  public void getThrownTypes() {
    class Get implements Function<PTypeMirror, NamedType> {
      private final Class<?> type;

      Get(Class<?> type) {
        this.type = type;
      }

      @Override
      public final NamedType apply(PTypeMirror input) {
        return input.getName();
      }

      public final UnmodifiableList<NamedType> get(String name) {
        ProcessingMethod method;
        method = method(type, name);

        GrowableList<NamedType> result;
        result = new GrowableList<>();

        UnmodifiableList<PTypeMirror> types;
        types = method.getThrownTypes();

        for (PTypeMirror type : types) {
          result.add(type.getName());
        }

        return result.toUnmodifiableList();
      }
    }

    Get get;
    get = new Get(ThrownTypesSubject.class);

    UnmodifiableList<NamedType> m0;
    m0 = get.get("m0");

    assertEquals(m0.size(), 0);

    UnmodifiableList<NamedType> m1;
    m1 = get.get("m1");

    assertEquals(m1.size(), 1);
    assertEquals(m1.get(0), NamedClass.of(IOException.class));

    UnmodifiableList<NamedType> m2;
    m2 = get.get("m2");

    assertEquals(m2.size(), 2);
    assertEquals(m2.get(0), NamedClass.of(IOException.class));
    assertEquals(m2.get(1), NamedClass.of(InterruptedException.class));

    UnmodifiableList<NamedType> m3;
    m3 = get.get("m3");

    assertEquals(m3.size(), 1);
    assertEquals(m3.get(0), NamedTypeVariable.of("E"));

    UnmodifiableList<NamedType> m4;
    m4 = get.get("m4");

    assertEquals(m4.size(), 1);
    assertEquals(m4.get(0), NamedTypeVariable.of("X"));
  }

  @Test(
      description = ""
          + "getTypeParameters() method should return an UnmodifiableList of TypeParameterName "
          + "instances in declaration order."
  )
  public void getTypeParameters() {
    ProcessingExecutableElement m0;
    m0 = method(TypeParametersSubject.class, "typeParam0");

    UnmodifiableList<NamedTypeParameter> tp0;
    tp0 = m0.getTypeParameters();

    assertEquals(tp0.size(), 0);

    ProcessingExecutableElement m1;
    m1 = method(TypeParametersSubject.class, "typeParam1");

    UnmodifiableList<NamedTypeParameter> tp1;
    tp1 = m1.getTypeParameters();

    assertEquals(tp1.size(), 1);
    assertEquals(tp1.get(0), NamedTypeParameter.named("U"));

    ProcessingExecutableElement m2;
    m2 = method(TypeParametersSubject.class, "typeParam2");

    UnmodifiableList<NamedTypeParameter> tp2;
    tp2 = m2.getTypeParameters();

    assertEquals(tp2.size(), 2);
    assertEquals(tp2.get(0), NamedTypeParameter.named("U"));
    assertEquals(tp2.get(1), NamedTypeParameter.named("B").addBound(tvar("U")));
  }

  @Test
  public void hasAccessLevel() {
    ProcessingType subject;
    subject = query(AccessLevelSubject.class);

    UnmodifiableList<ProcessingMethod> methods;
    methods = subject.getDeclaredMethods();

    assertEquals(methods.size(), 4);

    ProcessingMethod m0 = methods.get(0);

    assertTrue(m0.hasAccessLevel(AccessLevel.PUBLIC));
    assertFalse(m0.hasAccessLevel(AccessLevel.PROTECTED));
    assertFalse(m0.hasAccessLevel(AccessLevel.DEFAULT));
    assertFalse(m0.hasAccessLevel(AccessLevel.PRIVATE));

    ProcessingMethod m1 = methods.get(1);

    assertFalse(m1.hasAccessLevel(AccessLevel.PUBLIC));
    assertTrue(m1.hasAccessLevel(AccessLevel.PROTECTED));
    assertFalse(m1.hasAccessLevel(AccessLevel.DEFAULT));
    assertFalse(m1.hasAccessLevel(AccessLevel.PRIVATE));

    ProcessingMethod m2 = methods.get(2);

    assertFalse(m2.hasAccessLevel(AccessLevel.PUBLIC));
    assertFalse(m2.hasAccessLevel(AccessLevel.PROTECTED));
    assertTrue(m2.hasAccessLevel(AccessLevel.DEFAULT));
    assertFalse(m2.hasAccessLevel(AccessLevel.PRIVATE));

    ProcessingMethod m3 = methods.get(3);

    assertFalse(m3.hasAccessLevel(AccessLevel.PUBLIC));
    assertFalse(m3.hasAccessLevel(AccessLevel.PROTECTED));
    assertFalse(m3.hasAccessLevel(AccessLevel.DEFAULT));
    assertTrue(m3.hasAccessLevel(AccessLevel.PRIVATE));
  }

  @Test
  public void hasName() {
    ProcessingType subject;
    subject = query(NameSubject.class);

    UnmodifiableList<ProcessingMethod> methods;
    methods = subject.getDeclaredMethods();

    assertEquals(methods.size(), 1);

    ProcessingMethod m0 = methods.get(0);

    assertTrue(m0.hasName("m0"));
    assertFalse(m0.hasName("m1"));
  }

  @Test
  public void isAbstract() {
    assertTrue(method(IsSubject.class, "isAbstract").isAbstract());
    assertFalse(method(IsSubject.class, "isFinal").isAbstract());
    assertFalse(method(IsSubject.class, "isPublic").isAbstract());
    assertFalse(method(IsSubject.class, "isProtected").isAbstract());
    assertFalse(method(IsSubject.class, "isPrivate").isAbstract());
    assertFalse(method(IsSubject.class, "isStatic").isAbstract());
  }

  @Test
  public void isFinal() {
    assertFalse(method(IsSubject.class, "isAbstract").isFinal());
    assertTrue(method(IsSubject.class, "isFinal").isFinal());
    assertFalse(method(IsSubject.class, "isPublic").isFinal());
    assertFalse(method(IsSubject.class, "isProtected").isFinal());
    assertFalse(method(IsSubject.class, "isPrivate").isFinal());
    assertFalse(method(IsSubject.class, "isStatic").isFinal());
  }

  @Test
  public void isPrivate() {
    assertFalse(method(IsSubject.class, "isAbstract").isPrivate());
    assertFalse(method(IsSubject.class, "isFinal").isPrivate());
    assertFalse(method(IsSubject.class, "isPublic").isPrivate());
    assertFalse(method(IsSubject.class, "isProtected").isPrivate());
    assertTrue(method(IsSubject.class, "isPrivate").isPrivate());
    assertFalse(method(IsSubject.class, "isStatic").isPrivate());
  }

  @Test
  public void isProtected() {
    assertFalse(method(IsSubject.class, "isAbstract").isProtected());
    assertFalse(method(IsSubject.class, "isFinal").isProtected());
    assertFalse(method(IsSubject.class, "isPublic").isProtected());
    assertTrue(method(IsSubject.class, "isProtected").isProtected());
    assertFalse(method(IsSubject.class, "isPrivate").isProtected());
    assertFalse(method(IsSubject.class, "isStatic").isProtected());
  }

  @Test
  public void isPublic() {
    assertFalse(method(IsSubject.class, "isAbstract").isPublic());
    assertFalse(method(IsSubject.class, "isFinal").isPublic());
    assertTrue(method(IsSubject.class, "isPublic").isPublic());
    assertFalse(method(IsSubject.class, "isProtected").isPublic());
    assertFalse(method(IsSubject.class, "isPrivate").isPublic());
    assertFalse(method(IsSubject.class, "isStatic").isPublic());
  }

  @Test
  public void isStatic() {
    assertFalse(method(IsSubject.class, "isAbstract").isStatic());
    assertFalse(method(IsSubject.class, "isFinal").isStatic());
    assertFalse(method(IsSubject.class, "isPublic").isStatic());
    assertFalse(method(IsSubject.class, "isProtected").isStatic());
    assertFalse(method(IsSubject.class, "isPrivate").isStatic());
    assertTrue(method(IsSubject.class, "isStatic").isStatic());
  }

  private final ProcessingMethod method(Class<?> type, String name) {
    ExecutableElement el = getMethodElement(type, name);
    return ProcessingMethod.adapt(processingEnv, el);
  }

  @SuppressWarnings("unused")
  private abstract static class AccessLevelSubject {
    public void m0() {}
    protected void m1() {}
    void m2() {}
    private void m3() {}
  }

  private interface AnnotationsInterface {
    @InheritedAnnotation
    void interfaceMethod();
  }

  private abstract static class AnnotationsSubject
      implements
      AnnotationsInterface {

    @Override
    public final void interfaceMethod() {}

    @MethodAnnotation
    abstract void subject();

  }

  @SuppressWarnings("unused")
  private abstract static class DeclaringTypeParent {
    void fromParent() {}
  }

  @SuppressWarnings("unused")
  private static class DeclaringTypeSubject extends DeclaringTypeParent {
    void fromSubject() {}
  }

  @SuppressWarnings("unused")
  private abstract static class IsSubject {
    static void isStatic() {}
    public void isPublic() {};
    protected void isProtected() {};
    abstract void isAbstract();;
    final void isFinal() {};
    private void isPrivate() {};
  }

  @SuppressWarnings("unused")
  private abstract static class ModifiersSubject {
    public final void m0() {}
  }

  @SuppressWarnings("unused")
  private abstract static class NameSubject {
    public final void m0() {}
  }

  @SuppressWarnings("unused")
  private abstract static class ParametersSubject {
    public void m0(String name, int value) {}
    void m1() {}
  }

  private abstract static class ReturnTypeSubject<E> {
    abstract void m0();
    abstract int m1();
    abstract int[] m2();
    abstract String m3();
    abstract String[] m4();
    abstract List<Integer> m5();
    abstract E m6();
  }

  private abstract static class ThrownTypesSubject<E extends Exception> {
    abstract void m0();
    abstract void m1() throws IOException;
    abstract void m2() throws IOException, InterruptedException;
    abstract void m3() throws E;
    abstract <X extends Exception> void m4() throws X;
  }

  private abstract static class TypeParametersSubject {
    abstract void typeParam0();
    abstract <U> void typeParam1();
    abstract <U, B extends U> void typeParam2();
  }

}
