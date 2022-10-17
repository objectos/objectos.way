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

import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.model.AbstractCodeModelTest;
import br.com.objectos.code.processing.AbstractProcessingRoundProcessor;
import br.com.objectos.code.processing.ProcessingRound;
import br.com.objectos.code.processing.type.PTypeMirror;
import br.com.objectos.code.util.AnnotationValues;
import br.com.objectos.code.util.Letter;
import br.com.objectos.code.util.StringValueAnnotation;
import br.com.objectos.code.util.TypeAnnotation;
import br.com.objectos.tools.Compilation;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Types;
import objectos.util.UnmodifiableList;
import objectos.util.UnmodifiableSet;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProcessingAnnotationTest extends AbstractCodeModelTest {

  @Test
  public void className() {
    ProcessingAnnotation query = getDirectlyPresentAnnotation(TypeAnnotation.class);
    assertEquals(query.className(), NamedClass.of(TypeAnnotation.class));
  }

  @Test
  public void compilationError() {
    class ThisProcessor extends AbstractProcessingRoundProcessor {
      @Override
      public final Set<String> getSupportedAnnotationTypes() {
        return supportedAnnotationTypes(TypeAnnotation.class);
      }

      @Override
      protected final boolean process(ProcessingRound round) {
        UnmodifiableSet<ProcessingType> types = round.getAnnotatedTypes();
        for (ProcessingType type : types) {
          process0(round, type);
        }
        return round.claimTheseAnnotations();
      }

      private Predicate<ProcessingMethod> isSomeMethod() {
        return new Predicate<ProcessingMethod>() {
          @Override
          public boolean test(ProcessingMethod o) {
            return o.hasName("someMethod");
          }
        };
      }

      private void process0(ProcessingRound round, ProcessingType type) {
        ProcessingAnnotation typeAnn;
        typeAnn = type.getDirectlyPresentAnnotation(TypeAnnotation.class);

        typeAnn.compilationError("Hello world!");

        UnmodifiableList<ProcessingMethod> methods;
        methods = type.getDeclaredMethods();

        ProcessingExecutableElement someMethod;
        someMethod = filterAndGetOnly(methods, isSomeMethod());

        ProcessingAnnotation values;
        values = someMethod.getDirectlyPresentAnnotation(AnnotationValues.class);

        ProcessingAnnotationValue value;
        value = values.getDeclaredValue("string");

        value.compilationError(value.getString());
      }
    }

    Compilation compilation = javac(
        processor(new ThisProcessor()),
        patchModuleWithTestClasses("br.com.objectos.code"),
        compilationUnit(
            "package testing;",
            "@br.com.objectos.code.util.TypeAnnotation",
            "abstract class Subject {",
            "  @br.com.objectos.code.util.AnnotationValues(",
            "      intValue = 123,",
            "      string = \"a string value\"",
            "  )",
            "  abstract void someMethod();",
            "}"
        )
    );

    assertFalse(compilation.wasSuccessful());

    String message;
    message = compilation.getMessage();

    assertTrue(message.contains("Hello world!"));
    assertTrue(message.contains("Subject.java"));
    assertTrue(message.contains("a string value"));
  }

  @Test
  public void getDeclaredValue() {
    ProcessingAnnotation annotationValues;
    annotationValues = getDirectlyPresentAnnotation(AnnotationValues.class);

    assertNotNull(annotationValues.getDeclaredValue("string"));

    try {
      annotationValues.getDeclaredValue("i do not exist");
      Assert.fail();
    } catch (NoSuchElementException expected) {
      assertTrue(expected.getMessage().contains("i do not exist"));
    }
  }

  @Test
  public void getType() {
    ProcessingAnnotation annotationValues;
    annotationValues = getDirectlyPresentAnnotation(AnnotationValues.class);

    Types types;
    types = processingEnv.getTypeUtils();

    DeclaredType annotationType;
    annotationType = types.getDeclaredType(getTypeElement(AnnotationValues.class));

    assertEquals(
        annotationValues.getType(),
        PTypeMirror.adapt(processingEnv, annotationType)
    );
  }

  @Test
  public void hasQualifiedName() {
    ProcessingAnnotation typeAnnotation;
    typeAnnotation = getDirectlyPresentAnnotation(TypeAnnotation.class);

    String qname = "br.com.objectos.code.util.TypeAnnotation";

    assertTrue(typeAnnotation.hasQualifiedName(qname));
    assertFalse(typeAnnotation.hasQualifiedName(""));
    assertFalse(typeAnnotation.hasQualifiedName("TypeAnnotation"));

    ProcessingAnnotation innerAnnotation;
    innerAnnotation = getDirectlyPresentAnnotation(InnerAnnotation.class);

    qname = "br.com.objectos.code.model.element.ProcessingAnnotationTest.InnerAnnotation";

    assertTrue(innerAnnotation.hasQualifiedName(qname));
    assertFalse(innerAnnotation.hasQualifiedName(""));
    assertFalse(typeAnnotation.hasQualifiedName("InnerAnnotation"));
  }

  private ProcessingAnnotation getDirectlyPresentAnnotation(
      Class<? extends Annotation> annotationType) {
    ProcessingType annotated;
    annotated = query(Annotated.class);

    return annotated.getDirectlyPresentAnnotation(annotationType);
  }

  @TypeAnnotation
  @AnnotationValues(
      charValue = 'j',
      enumArray = {Letter.A, Letter.B, Letter.C},
      enumValue = Letter.A,
      intValue = 123,
      string = "I am a string",
      stringArrayValue = {"1", "2", "3"},
      typeMirror = Number.class,
      stringValueAnnotation = @StringValueAnnotation("Hello!")
  )
  @InnerAnnotation
  public static class Annotated {}

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  private @interface InnerAnnotation {}

}