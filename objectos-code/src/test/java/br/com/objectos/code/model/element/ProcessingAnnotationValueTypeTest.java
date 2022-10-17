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
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.processing.AbstractProcessingRoundProcessor;
import br.com.objectos.code.processing.ProcessingRound;
import br.com.objectos.code.processing.type.PTypeMirror;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;
import objectos.util.UnmodifiableSet;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProcessingAnnotationValueTypeTest extends AbstractProcessingAnnotationValueTest {

  @Test
  public void _errorType() {
    class ThisProcessor extends AbstractProcessingRoundProcessor {
      PTypeMirror declaredValue;
      PTypeMirror defaultValue;

      @Override
      public final Set<String> getSupportedAnnotationTypes() {
        return supportedAnnotationTypes(ErrorType.class);
      }

      @Override
      protected final boolean process(ProcessingRound round) {
        UnmodifiableSet<ProcessingType> annotatedTypes;
        annotatedTypes = round.getAnnotatedTypes();

        for (ProcessingType annotatedType : annotatedTypes) {
          process0(annotatedType);
        }

        return round.claimTheseAnnotations();
      }

      private void process0(ProcessingType annotatedType) {
        ProcessingAnnotation annotation;
        annotation = annotatedType.getDirectlyPresentAnnotation(ErrorType.class);

        declaredValue = annotation.getDeclaredValue("value").getType();

        defaultValue = annotation.getDefaultValue("value").getType();
      }
    }

    ThisProcessor processor = new ThisProcessor();

    javac(
        processor(processor),
        patchModuleWithTestClasses("br.com.objectos.code"),
        compilationUnit(
            "package testing.code;",
            "@" + ErrorType.class.getCanonicalName() + "(",
            "    testing.code.DoNotExists.class",
            ")",
            "class Subject {}"
        )
    );

    assertTrue(processor.declaredValue.isErrorType());
    assertTrue(processor.defaultValue.isDeclaredType());
    assertEquals(processor.defaultValue.toString(), "java.lang.Integer");
  }

  @Test
  public void getDeclaredOrDefaultValue() {
    assertEquals(
        getDeclaredOrDefaultValue(defaults()).getName(),
        cn(Void.class)
    );

    assertEquals(
        getDeclaredOrDefaultValue(noDefaults()).getName(),
        cn(Integer.class)
    );

    assertEquals(
        getDeclaredOrDefaultValue(withDefaults()).getName(),
        cn(Long.class)
    );
  }

  @Test
  public void getDeclaredValue() {
    try {
      getDeclaredValue(defaults());
      Assert.fail();
    } catch (NoDeclaredValueException expected) {
      assertTrue(expected.getMessage().contains("value"));
    }

    assertEquals(
        getDeclaredValue(noDefaults()).getName(),
        cn(Integer.class)
    );

    assertEquals(
        getDeclaredValue(withDefaults()).getName(),
        cn(Long.class)
    );
  }

  @Test
  public void getDefaultValue() {
    assertEquals(
        getDefaultValue(defaults()).getName(),
        cn(Void.class)
    );

    try {
      getDefaultValue(noDefaults());
      Assert.fail();
    } catch (NoDefaultValueException expected) {
      assertTrue(expected.getMessage().contains("value"));
    }

    assertEquals(
        getDefaultValue(withDefaults()).getName(),
        cn(Void.class)
    );
  }

  private NamedClass cn(Class<?> type) {
    return NamedClass.of(type);
  }

  private ProcessingAnnotation defaults() {
    return getDirectlyPresentAnnotation(Defaults.class, WithDefaults.class);
  }

  private PTypeMirror getDeclaredOrDefaultValue(ProcessingAnnotation annotation) {
    ProcessingAnnotationValue value;
    value = getDeclaredOrDefaultValue(annotation, "value");

    return value.getType();
  }

  private PTypeMirror getDeclaredValue(ProcessingAnnotation annotation) {
    ProcessingAnnotationValue value;
    value = getDeclaredValue(annotation, "value");

    return value.getType();
  }

  private PTypeMirror getDefaultValue(ProcessingAnnotation annotation) {
    ProcessingAnnotationValue value;
    value = getDefaultValue(annotation, "value");

    return value.getType();
  }

  private ProcessingAnnotation noDefaults() {
    return getDirectlyPresentAnnotation(Subject.class, NoDefaults.class);
  }

  private ProcessingAnnotation withDefaults() {
    return getDirectlyPresentAnnotation(Subject.class, WithDefaults.class);
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  public @interface ErrorType {
    Class<?> value() default Integer.class;
  }

  @WithDefaults
  private static class Defaults {}

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  private @interface NoDefaults {
    Class<?> value();
  }

  @NoDefaults(Integer.class)
  @WithDefaults(Long.class)
  private static class Subject {}

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  private @interface WithDefaults {
    Class<?> value() default Void.class;
  }

}
