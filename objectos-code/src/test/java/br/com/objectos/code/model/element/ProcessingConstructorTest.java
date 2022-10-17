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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.declaration.AccessLevel;
import br.com.objectos.code.java.declaration.ConstructorModifier;
import br.com.objectos.code.java.declaration.Modifiers;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedTypeParameter;
import br.com.objectos.code.model.AbstractCodeModelTest;
import br.com.objectos.code.util.Marker1;
import java.util.NoSuchElementException;
import objectos.util.UnmodifiableList;
import objectos.util.UnmodifiableSet;
import org.testng.annotations.Test;

public class ProcessingConstructorTest extends AbstractCodeModelTest {

  @Test
  public void getAccessLevel() {
    ProcessingType subject;
    subject = query(Subject.class);

    UnmodifiableList<ProcessingConstructor> constructors;
    constructors = subject.getDeclaredConstructors();

    assertEquals(constructors.size(), 4);
    assertEquals(constructors.get(0).getAccessLevel(), AccessLevel.PUBLIC);
    assertEquals(constructors.get(1).getAccessLevel(), AccessLevel.DEFAULT);
    assertEquals(constructors.get(2).getAccessLevel(), AccessLevel.PRIVATE);
    assertEquals(constructors.get(3).getAccessLevel(), AccessLevel.PROTECTED);
  }

  @Test
  public void getDirectlyPresentAnnotations() {
    ProcessingConstructor defaultConstructor;
    defaultConstructor = getConstructorByAccessLevel(Subject.class, AccessLevel.DEFAULT);

    UnmodifiableList<ProcessingAnnotation> defaultAnnotations;
    defaultAnnotations = defaultConstructor.getDirectlyPresentAnnotations();

    assertEquals(defaultAnnotations.size(), 0);

    ProcessingConstructor privateConstructor;
    privateConstructor = getConstructorByAccessLevel(Subject.class, AccessLevel.PRIVATE);

    UnmodifiableList<ProcessingAnnotation> privateAnnotations;
    privateAnnotations = privateConstructor.getDirectlyPresentAnnotations();

    UnmodifiableList<NamedClass> privateAnnotationClassNames;
    privateAnnotationClassNames = annotationToClassName(privateAnnotations);

    assertEquals(privateAnnotationClassNames.size(), 1);
    assertEquals(
        privateAnnotationClassNames.get(0),
        NamedClass.of(Marker1.class)
    );
  }

  @Test
  public void getModifiers() {
    ProcessingConstructor defaultConstructor;
    defaultConstructor = getConstructorByAccessLevel(Subject.class, AccessLevel.DEFAULT);

    UnmodifiableSet<ConstructorModifier> defaultModifiers;
    defaultModifiers = defaultConstructor.getModifiers();

    assertEquals(defaultModifiers.size(), 0);

    ProcessingConstructor privateConstructor;
    privateConstructor = getConstructorByAccessLevel(Subject.class, AccessLevel.PRIVATE);

    UnmodifiableSet<ConstructorModifier> privateModifiers;
    privateModifiers = privateConstructor.getModifiers();

    assertEquals(privateModifiers.size(), 1);
    assertTrue(privateModifiers.contains(Modifiers.PRIVATE));
  }

  @Test
  public void getParameters() {
    ProcessingConstructor defaultSubject;
    defaultSubject = getConstructorByAccessLevel(ParametersSubject.class, AccessLevel.DEFAULT);

    UnmodifiableList<ProcessingParameter> defaultParameters;
    defaultParameters = defaultSubject.getParameters();

    assertEquals(defaultParameters.size(), 0);

    ProcessingConstructor publicSubject;
    publicSubject = getConstructorByAccessLevel(ParametersSubject.class, AccessLevel.PUBLIC);

    UnmodifiableList<ProcessingParameter> publicParameters;
    publicParameters = publicSubject.getParameters();

    assertEquals(publicParameters.size(), 2);
    assertEquals(publicParameters.get(0).getName(), "name");
    assertEquals(publicParameters.get(1).getName(), "value");
  }

  @Test
  public void getTypeParameters() {
    ProcessingConstructor publicSubject;
    publicSubject = getConstructorByAccessLevel(TypeParametersSubject.class, AccessLevel.PUBLIC);

    UnmodifiableList<NamedTypeParameter> publicParameters;
    publicParameters = publicSubject.getTypeParameters();

    assertEquals(publicParameters.size(), 0);

    ProcessingConstructor privateSubject;
    privateSubject = getConstructorByAccessLevel(TypeParametersSubject.class, AccessLevel.PRIVATE);

    UnmodifiableList<NamedTypeParameter> privateParameters;
    privateParameters = privateSubject.getTypeParameters();

    assertEquals(privateParameters.size(), 2);
    assertEquals(privateParameters.get(0).toString(), "E1");
    assertEquals(privateParameters.get(1).toString(), "E2");
  }

  @Test
  public void hasAccessLevel() {
    ProcessingConstructor protectedConstructor;
    protectedConstructor = getConstructorByAccessLevel(Subject.class, AccessLevel.PROTECTED);

    assertTrue(protectedConstructor.hasAccessLevel(AccessLevel.PROTECTED));

    assertFalse(protectedConstructor.hasAccessLevel(AccessLevel.PRIVATE));
    assertFalse(protectedConstructor.hasAccessLevel(AccessLevel.DEFAULT));
    assertFalse(protectedConstructor.hasAccessLevel(AccessLevel.PUBLIC));
  }

  @Test(description = "verify isPublic(), isProtected(), isPrivate()")
  public void isPublicOrProtectedOrPrivate() {
    ProcessingConstructor publicConstructor;
    publicConstructor = getConstructorByAccessLevel(Subject.class, AccessLevel.PUBLIC);

    assertTrue(publicConstructor.isPublic());
    assertFalse(publicConstructor.isProtected());
    assertFalse(publicConstructor.isPrivate());

    ProcessingConstructor defaultConstructor;
    defaultConstructor = getConstructorByAccessLevel(Subject.class, AccessLevel.DEFAULT);

    assertFalse(defaultConstructor.isPublic());
    assertFalse(defaultConstructor.isProtected());
    assertFalse(defaultConstructor.isPrivate());

    ProcessingConstructor protectedConstructor;
    protectedConstructor = getConstructorByAccessLevel(Subject.class, AccessLevel.PROTECTED);

    assertFalse(protectedConstructor.isPublic());
    assertTrue(protectedConstructor.isProtected());
    assertFalse(protectedConstructor.isPrivate());

    ProcessingConstructor privateConstructor;
    privateConstructor = getConstructorByAccessLevel(Subject.class, AccessLevel.PRIVATE);

    assertFalse(privateConstructor.isPublic());
    assertFalse(privateConstructor.isProtected());
    assertTrue(privateConstructor.isPrivate());
  }

  private ProcessingConstructor getConstructorByAccessLevel(
      Class<?> type, AccessLevel accessLevel) {
    ProcessingType processingType;
    processingType = query(type);

    UnmodifiableList<ProcessingConstructor> constructors;
    constructors = processingType.getDeclaredConstructors();

    for (int i = 0; i < constructors.size(); i++) {
      ProcessingConstructor constructor;
      constructor = constructors.get(i);

      if (constructor.hasAccessLevel(accessLevel)) {
        return constructor;
      }
    }

    throw new NoSuchElementException(accessLevel.name());
  }

  abstract static class ParametersSubject {
    public ParametersSubject(String name, int value) {}

    ParametersSubject() {}
  }

  abstract static class Subject {
    public Subject(byte c0) {}

    Subject(short c1) {}

    @Marker1
    private Subject(int c2) {}
    
    protected Subject(long c3) {}
  }

  @SuppressWarnings("unused")
  private abstract static class TypeParametersSubject {
    public TypeParametersSubject() {}

    private <E1, E2> TypeParametersSubject(E1 e1, E2 e2) {}
  }

}