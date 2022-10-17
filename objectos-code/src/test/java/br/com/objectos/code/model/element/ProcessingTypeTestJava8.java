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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.model.AbstractCodeModelTest;
import br.com.objectos.code.testing.InheritedAnnotation;
import br.com.objectos.code.testing.NonInheritedAnnotation;
import br.com.objectos.code.util.ContainerAnnotation;
import br.com.objectos.code.util.InheritedContainerAnnotation;
import br.com.objectos.code.util.InheritedRepeatableAnnotation;
import br.com.objectos.code.util.Marker1;
import br.com.objectos.code.util.Marker2;
import br.com.objectos.code.util.NonInheritedContainerAnnotation;
import br.com.objectos.code.util.NonInheritedRepeatableAnnotation;
import br.com.objectos.code.util.RepeatableAnnotation;
import br.com.objectos.code.util.TypeAnnotation;
import java.util.NoSuchElementException;
import objectos.util.UnmodifiableList;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProcessingTypeTestJava8 extends AbstractCodeModelTest {

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

    assertNotNull(
      subject.getDirectlyPresentAnnotation(ContainerAnnotation.class)
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

    try {
      subject.getDirectlyPresentAnnotation(InheritedRepeatableAnnotation.class);
      Assert.fail();
    } catch (NoSuchElementException expected) {
      String canonicalName;
      canonicalName = InheritedRepeatableAnnotation.class.getCanonicalName();

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

    assertEquals(subjectClassNames.size(), 4);
    assertTrue(subjectClassNames.contains(NamedClass.of(Marker1.class)));
    assertTrue(subjectClassNames.contains(NamedClass.of(Marker2.class)));
    assertTrue(subjectClassNames.contains(NamedClass.of(TypeAnnotation.class)));
    assertTrue(subjectClassNames.contains(NamedClass.of(ContainerAnnotation.class)));
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

    assertNotNull(
      subject.getDirectlyPresentOrInheritedAnnotation(InheritedContainerAnnotation.class)
    );

    try {
      subject.getDirectlyPresentOrInheritedAnnotation(NonInheritedAnnotation.class);
      Assert.fail();
    } catch (NoSuchElementException expected) {
      String canonicalName;
      canonicalName = NonInheritedAnnotation.class.getCanonicalName();

      assertEquals(expected.getMessage(), canonicalName);
    }

    try {
      subject.getDirectlyPresentOrInheritedAnnotation(NonInheritedContainerAnnotation.class);
      Assert.fail();
    } catch (NoSuchElementException expected) {
      String canonicalName;
      canonicalName = NonInheritedContainerAnnotation.class.getCanonicalName();

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

    assertEquals(subjectClassNames.size(), 6);
    assertTrue(subjectClassNames.contains(NamedClass.of(Marker1.class)));
    assertTrue(subjectClassNames.contains(NamedClass.of(Marker2.class)));
    assertTrue(subjectClassNames.contains(NamedClass.of(TypeAnnotation.class)));
    assertTrue(subjectClassNames.contains(NamedClass.of(InheritedAnnotation.class)));
    assertTrue(subjectClassNames.contains(NamedClass.of(ContainerAnnotation.class)));
    assertTrue(subjectClassNames.contains(NamedClass.of(InheritedContainerAnnotation.class)));
  }

  @InheritedAnnotation
  @NonInheritedAnnotation
  @InheritedRepeatableAnnotation(0)
  @InheritedRepeatableAnnotation(1)
  @NonInheritedRepeatableAnnotation(2)
  @NonInheritedRepeatableAnnotation(4)
  private static class AnnotationsParent {}

  @Marker1
  @Marker2
  @TypeAnnotation
  @RepeatableAnnotation(0)
  @RepeatableAnnotation(1)
  private static class AnnotationsSubject extends AnnotationsParent {}

}
