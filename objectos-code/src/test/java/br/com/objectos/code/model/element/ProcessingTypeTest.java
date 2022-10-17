/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.objectos.code.model.element;

import static br.com.objectos.tools.Tools.compilationUnit;
import static br.com.objectos.tools.Tools.javac;
import static br.com.objectos.tools.Tools.patchModuleWithTestClasses;
import static br.com.objectos.tools.Tools.processor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.declaration.Modifiers;
import br.com.objectos.code.java.declaration.TypeModifier;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedTypeParameter;
import br.com.objectos.code.model.AbstractCodeModelTest;
import br.com.objectos.code.processing.AbstractProcessingRoundProcessor;
import br.com.objectos.code.processing.ProcessingRound;
import br.com.objectos.code.testing.InheritedAnnotation;
import br.com.objectos.code.testing.NonInheritedAnnotation;
import br.com.objectos.code.util.Marker1;
import br.com.objectos.code.util.Marker2;
import br.com.objectos.code.util.TypeAnnotation;
import br.com.objectos.tools.Compilation;
import java.io.Closeable;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.lang.model.element.ElementKind;
import objectos.util.UnmodifiableList;
import objectos.util.UnmodifiableSet;
import objectos.util.GrowableList;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProcessingTypeTest extends AbstractCodeModelTest {

  @Test
  public void getBinaryName() {
    ProcessingType outer;
    outer = query(ProcessingTypeTest.class);

    assertEquals(
      outer.getBinaryName(),
      ProcessingTypeTest.class.getCanonicalName()
    );

    ProcessingType subject;
    subject = query(getBinaryNameSubject.class);

    assertEquals(
      subject.getBinaryName(),
      outer.getBinaryName() + "$" + subject.getSimpleName()
    );
  }

  @Test
  public void getCanonicalName() {
    ProcessingType hello;
    hello = query(GetCanonicalNameSubject.Hello.class);

    assertEquals(
      hello.getCanonicalName(),
      ProcessingTypeTest.class.getCanonicalName() + ".GetCanonicalNameParent.Hello"
    );

    ProcessingType string;
    string = query(String.class);

    assertEquals(
      string.getCanonicalName(),
      "java.lang.String"
    );

    ProcessingType mapEntry;
    mapEntry = query(Map.Entry.class);

    assertEquals(
      mapEntry.getCanonicalName(),
      "java.util.Map.Entry"
    );
  }

  @Test
  public void getDeclaredConstructors() {
    ProcessingType subject;
    subject = query(GetDeclaredConstructorSubject.class);

    UnmodifiableList<ProcessingConstructor> explicitlyDeclared;
    explicitlyDeclared = subject.getDeclaredConstructors();

    assertEquals(explicitlyDeclared.size(), 2);

    ProcessingConstructor c0 = explicitlyDeclared.get(0);
    ProcessingConstructor c1 = explicitlyDeclared.get(1);

    assertEquals(c0.toString(), "GetDeclaredConstructorSubject(int,float)");
    assertEquals(c1.toString(), "private GetDeclaredConstructorSubject(int)");

    ProcessingType compilerProvidedSubject;
    compilerProvidedSubject = query(GetDeclaredConstructorCompilerSubject.class);

    UnmodifiableList<ProcessingConstructor> compilerProvidedConstructors;
    compilerProvidedConstructors = compilerProvidedSubject.getDeclaredConstructors();

    assertEquals(compilerProvidedConstructors.size(), 1);

    ProcessingConstructor compilerProvidedConstructor;
    compilerProvidedConstructor = compilerProvidedConstructors.get(0);

    assertEquals(compilerProvidedConstructor.toString(), "GetDeclaredConstructorCompilerSubject()");
  }

  @Test
  public void getDeclaredFields() {
    ProcessingType subject;
    subject = query(GetFieldsSubject.class);

    UnmodifiableList<ProcessingField> fields;
    fields = subject.getDeclaredFields();

    assertEquals(fields.size(), 3);

    ProcessingField f0 = fields.get(1);
    ProcessingField f2 = fields.get(2);
    ProcessingField f4 = fields.get(0);

    assertEquals(f0.toString(), "private int f0");
    assertEquals(f2.toString(), "private short f2");
    assertEquals(f4.toString(), "float f4");
  }

  @Test
  public void getDeclaredMethods() {
    ProcessingType subject;
    subject = query(GetMethodsSubject.class);

    UnmodifiableList<ProcessingMethod> methods;
    methods = subject.getDeclaredMethods();

    assertEquals(methods.size(), 4);

    ProcessingExecutableElement m0 = methods.get(0);
    ProcessingExecutableElement m1 = methods.get(1);
    ProcessingExecutableElement m2 = methods.get(2);
    ProcessingExecutableElement m4 = methods.get(3);

    assertEquals(m0.toString(), "public final void m0()");
    assertEquals(m1.toString(), "final void m1()");
    assertEquals(m2.toString(), "abstract void m2(int)");
    assertEquals(m4.toString(), "abstract void m4()");
  }

  @Test
  public void getDeclaredOrInheritedFields() {
    ProcessingType subject;
    subject = query(GetFieldsSubject.class);

    UnmodifiableList<ProcessingField> fields;
    fields = subject.getDeclaredOrInheritedFields();

    assertEquals(fields.size(), 7);

    UnmodifiableList<String> fieldStrings;
    fieldStrings = map(fields, toStringFunction());

    assertTrue(fieldStrings.contains("protected short f2"));
    assertTrue(fieldStrings.contains("protected byte f3"));
    assertTrue(fieldStrings.contains("public static final int f0"));
    assertTrue(fieldStrings.contains("public static final double f1"));
    assertTrue(fieldStrings.contains("private int f0"));
    assertTrue(fieldStrings.contains("private short f2"));
    assertTrue(fieldStrings.contains("float f4"));
  }

  @Test
  public void getDeclaredOrInheritedMethods() {
    ProcessingType subject;
    subject = query(GetMethodsSubject.class);

    UnmodifiableList<ProcessingMethod> methods;
    methods = subject.getDeclaredOrInheritedMethods();

    GrowableList<String> methodsToString;
    methodsToString = new GrowableList<>();

    for (int i = 0; i < methods.size(); i++) {
      ProcessingMethod method;
      method = methods.get(i);

      ProcessingType methodEnclosingElement;
      methodEnclosingElement = method.getDeclaringType();

      NamedClass methodEnclosingElementClassName;
      methodEnclosingElementClassName = methodEnclosingElement.getName();

      if (methodEnclosingElementClassName.isJavaLangObject()) {
        continue;
      }

      methodsToString.add(method.toString());
    }

    UnmodifiableList<String> result;
    result = methodsToString.toUnmodifiableList();

    assertEquals(result.size(), 7);
    assertTrue(result.contains("public final void m0()"));
    assertTrue(result.contains("final void m1()"));
    assertTrue(result.contains("abstract void m2()"));
    assertTrue(result.contains("abstract void m2(int)"));
    assertTrue(result.contains("final void m3()"));
    assertTrue(result.contains("abstract void m4()"));
    assertTrue(result.contains("public abstract int m6()"));
  }

  @Test
  public void getDeclaredOrInheritedTypes() {
    ProcessingType subject;
    subject = query(GetDeclaredOrInheritedTypesSubject.class);

    UnmodifiableList<ProcessingType> types;
    types = subject.getDeclaredOrInheritedTypes();

    assertEquals(types.size(), 3);

    ProcessingType inner0 = types.get(0);
    ProcessingType inner1 = types.get(1);
    ProcessingType inner2 = types.get(2);

    assertEquals(inner0.getSimpleName(), "DefaultInnerClass");
    assertEquals(inner1.getSimpleName(), "PublicInnerInterface");
    assertEquals(inner2.getSimpleName(), "ProtectedInnerAnnotation");
  }

  @Test
  public void getDeclaredTypes() {
    ProcessingType subject;
    subject = query(GetDeclaredOrInheritedTypesSubject.class);

    UnmodifiableList<ProcessingType> types;
    types = subject.getDeclaredTypes();

    assertEquals(types.size(), 1);

    ProcessingType inner0;
    inner0 = types.get(0);

    assertEquals(inner0.getSimpleName(), "ProtectedInnerAnnotation");
  }

  @Test
  public void getDirectlyPresentAnnotation() {
    ProcessingType subject;
    subject = query(AnnotationsSubject.class);

    assertNotNull(
      subject.getDirectlyPresentAnnotation(Marker1.class)
    );

    assertNotNull(
      subject.getDirectlyPresentAnnotation(Marker2.class.getCanonicalName())
    );

    try {
      subject.getDirectlyPresentAnnotation(Deprecated.class);
      Assert.fail();
    } catch (NoSuchElementException expected) {
      assertEquals(expected.getMessage(), "java.lang.Deprecated");
    }

    try {
      subject.getDirectlyPresentAnnotation(InheritedAnnotation.class);
      Assert.fail();
    } catch (NoSuchElementException expected) {
      String canonicalName;
      canonicalName = InheritedAnnotation.class.getCanonicalName();

      assertEquals(expected.getMessage(), canonicalName);
    }
  }

  @Test
  public void getDirectlyPresentAnnotations() {
    ProcessingType subject;
    subject = query(AnnotationsSubject.class);

    UnmodifiableList<ProcessingAnnotation> subjectAnnotations;
    subjectAnnotations = subject.getDirectlyPresentAnnotations();

    UnmodifiableList<NamedClass> subjectClassNames;
    subjectClassNames = annotationToClassName(subjectAnnotations);

    assertEquals(subjectClassNames.size(), 3);
    assertTrue(subjectClassNames.contains(NamedClass.of(Marker1.class)));
    assertTrue(subjectClassNames.contains(NamedClass.of(Marker2.class)));
    assertTrue(subjectClassNames.contains(NamedClass.of(TypeAnnotation.class)));
  }

  @Test
  public void getDirectlyPresentOrInheritedAnnotation() {
    ProcessingType subject;
    subject = query(AnnotationsSubject.class);

    assertNotNull(
      subject.getDirectlyPresentOrInheritedAnnotation(Marker1.class)
    );

    assertNotNull(
      subject.getDirectlyPresentOrInheritedAnnotation(InheritedAnnotation.class)
    );

    try {
      subject.getDirectlyPresentOrInheritedAnnotation(NonInheritedAnnotation.class);
      Assert.fail();
    } catch (NoSuchElementException expected) {
      String canonicalName;
      canonicalName = NonInheritedAnnotation.class.getCanonicalName();

      assertEquals(expected.getMessage(), canonicalName);
    }
  }

  @Test
  public void getDirectlyPresentOrInheritedAnnotations() {
    ProcessingType subject;
    subject = query(AnnotationsSubject.class);

    UnmodifiableList<ProcessingAnnotation> subjectAnnotations;
    subjectAnnotations = subject.getDirectlyPresentOrInheritedAnnotations();

    UnmodifiableList<NamedClass> subjectClassNames;
    subjectClassNames = annotationToClassName(subjectAnnotations);

    assertEquals(subjectClassNames.size(), 4);
    assertTrue(subjectClassNames.contains(NamedClass.of(Marker1.class)));
    assertTrue(subjectClassNames.contains(NamedClass.of(Marker2.class)));
    assertTrue(subjectClassNames.contains(NamedClass.of(TypeAnnotation.class)));
    assertTrue(subjectClassNames.contains(NamedClass.of(InheritedAnnotation.class)));
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
        comments.add(type.getDocComment());
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
        "/**",
        " * standard formatted Javadoc comment",
        " */",
        "@br.com.objectos.code.util.TypeAnnotation",
        "class Subject {}"
      )
    );

    compilation.assertWasSuccessful();

    UnmodifiableList<String> result;
    result = thisProcessor.comments.toUnmodifiableList();

    assertEquals(result.size(), 1);

    assertEquals(result.get(0), " standard formatted Javadoc comment\n");
  }

  @Test
  public void getModifiers() {
    ProcessingType subject0;
    subject0 = query(ModifiersSubject0.class);

    UnmodifiableSet<TypeModifier> modifiers0;
    modifiers0 = subject0.getModifiers();

    assertEquals(modifiers0.size(), 3);
    assertTrue(modifiers0.contains(Modifiers.PROTECTED));
    assertTrue(modifiers0.contains(Modifiers.STATIC));
    assertTrue(modifiers0.contains(Modifiers.FINAL));

    ProcessingType subject1;
    subject1 = query(ModifiersSubject1.class);

    UnmodifiableSet<TypeModifier> modifiers1;
    modifiers1 = subject1.getModifiers();

    assertEquals(modifiers1.size(), 2);
    assertTrue(modifiers1.contains(Modifiers.PRIVATE));
    assertTrue(modifiers1.contains(Modifiers.ABSTRACT));
  }

  @Test
  public void getName() {
    ProcessingType subject;
    subject = query(GetNameSubject.class);

    assertEquals(
      subject.getName().getCanonicalName(),
      GetNameSubject.class.getCanonicalName()
    );

    ProcessingType inner;
    inner = query(GetNameSubject.Inner.class);

    assertEquals(
      inner.getName().getCanonicalName(),
      GetNameSubject.Inner.class.getCanonicalName()
    );
  }

  @Test
  public void getSimpleName() {
    ProcessingType subject;
    subject = query(GetSimpleNameSubject.class);

    assertEquals(
      subject.getSimpleName(),
      GetSimpleNameSubject.class.getSimpleName()
    );
  }

  @Test
  public void getTypeParameters() {
    ProcessingType query;
    query = query(Generic.class);

    assertEquals(
      query.getTypeParameters(),
      UnmodifiableList.of(
        NamedTypeParameter.named("U"),
        NamedTypeParameter.named("B").addBound(InputStream.class),
        NamedTypeParameter.named("I").addBound(InputStream.class).addBound(Closeable.class)
      )
    );
  }

  @Test
  public void instanceOf() {
    ProcessingType query;
    query = query(InstanceOf.class);

    assertTrue(query.instanceOf(Basic.class));
    assertTrue(query.instanceOf(Serializable.class));
    assertFalse(query.instanceOf(Comparable.class));
  }

  @Test
  public void typeElementKindQuery() {
    TypeElementKindQuery query = query(Basic.class);
    assertFalse(query.isAnnotation());
    assertTrue(query.isClass());
    assertFalse(query.isEnum());
    assertFalse(query.isInterface());
  }

  @Test
  public void typeElementKindQueryWhenAnnotation() {
    TypeElementKindQuery query = query(Test.class);
    assertTrue(query.isAnnotation());
    assertFalse(query.isClass());
    assertFalse(query.isEnum());
    assertFalse(query.isInterface());
  }

  @Test
  public void typeElementKindQueryWhenEnum() {
    TypeElementKindQuery query = query(ElementKind.class);
    assertFalse(query.isAnnotation());
    assertFalse(query.isClass());
    assertTrue(query.isEnum());
    assertFalse(query.isInterface());
  }

  @Test
  public void typeElementKindQueryWhenInterface() {
    TypeElementKindQuery query = query(List.class);
    assertFalse(query.isAnnotation());
    assertFalse(query.isClass());
    assertFalse(query.isEnum());
    assertTrue(query.isInterface());
  }

  protected static final class ModifiersSubject0 {}

  static class Fields extends FieldsSuper implements FieldsIface {
    int b;
    int c;
  }

  static interface FieldsIface {
    int d = 0;
  }

  static class FieldsSuper {
    int a;
  }

  @InheritedAnnotation
  @NonInheritedAnnotation
  private static class AnnotationsParent {}

  @Marker1
  @Marker2
  @TypeAnnotation
  private static class AnnotationsSubject extends AnnotationsParent {}

  private class getBinaryNameSubject {}

  private class GetCanonicalNameParent {
    class Hello {}
  }

  private class GetCanonicalNameSubject extends GetCanonicalNameParent {}

  @SuppressWarnings("unused")
  private static class GetDeclaredOrInheritedTypesSubject extends GetDeclaredTypesSubject {
    protected @interface ProtectedInnerAnnotation {}
  }

  @SuppressWarnings("unused")
  private static class GetDeclaredTypesSubject {
    final PrivateInnerClass inner = new PrivateInnerClass();

    public interface PublicInnerInterface {}
    static class DefaultInnerClass {}
    private static class PrivateInnerClass {}
  }

  @SuppressWarnings("unused")
  private interface GetFieldsInterface {
    int f0 = 0;
    double f1 = 1d;
  }

  @SuppressWarnings("unused")
  private abstract static class GetFieldsParent {
    protected short f2;
    protected byte f3;
  }

  @SuppressWarnings("unused")
  private static class GetFieldsSubject
      extends GetFieldsParent
      implements GetFieldsInterface {

    float f4;

    // shadows FieldsInterface.f0
    private int f0;

    // shadows FieldsParent.f2
    private short f2;
  }

  private interface GetMethodsInterface {
    void m0();
    int m6();
  }

  @SuppressWarnings("unused")
  private abstract static class GetMethodsParent {
    abstract void m1();
    abstract void m2();
    final void m3() {}
    private void m4() {}
  }

  private static abstract class GetMethodsSubject
      extends GetMethodsParent
      implements GetMethodsInterface {

    @Override
    public final void m0() {}

    @Override
    final void m1() {}

    // super: abstract void m2();

    abstract void m2(int i);

    // super: final void m3();

    // shadows: private void m4()

    abstract void m4();

    // super: public abstract int m6()

  }

  private class GetNameSubject {
    class Inner {}
  }

  private class GetSimpleNameSubject {}

  private abstract class ModifiersSubject1 {}

}