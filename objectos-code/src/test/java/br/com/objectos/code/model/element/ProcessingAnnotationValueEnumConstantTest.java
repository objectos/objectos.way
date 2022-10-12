/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.model.element;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProcessingAnnotationValueEnumConstantTest
    extends
    AbstractProcessingAnnotationValueTest {

  @Test
  public void getDeclaredOrDefaultValue() {
    assertEquals(
        getDeclaredOrDefaultValue(defaults()),
        ThisEnum.DEFAULTS
    );

    assertEquals(
        getDeclaredOrDefaultValue(noDefaults()),
        ThisEnum.NO_DEFAULTS
    );

    assertEquals(
        getDeclaredOrDefaultValue(withDefaults()),
        ThisEnum.WITH_DEFAULTS
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
        getDeclaredValue(noDefaults()),
        ThisEnum.NO_DEFAULTS
    );

    assertEquals(
        getDeclaredValue(withDefaults()),
        ThisEnum.WITH_DEFAULTS
    );
  }

  @Test
  public void getDefaultValue() {
    assertEquals(
        getDefaultValue(defaults()),
        ThisEnum.DEFAULTS
    );

    try {
      getDefaultValue(noDefaults());
      Assert.fail();
    } catch (NoDefaultValueException expected) {
      assertTrue(expected.getMessage().contains("value"));
    }

    assertEquals(
        getDefaultValue(withDefaults()),
        ThisEnum.DEFAULTS
    );
  }

  private ProcessingAnnotation defaults() {
    return getDirectlyPresentAnnotation(Defaults.class, WithDefaults.class);
  }

  private ThisEnum getDeclaredOrDefaultValue(ProcessingAnnotation annotation) {
    ProcessingAnnotationValue value;
    value = getDeclaredOrDefaultValue(annotation, "value");

    return value.getEnum(ThisEnum.class);
  }

  private ThisEnum getDeclaredValue(ProcessingAnnotation annotation) {
    ProcessingAnnotationValue value;
    value = getDeclaredValue(annotation, "value");

    return value.getEnum(ThisEnum.class);
  }

  private ThisEnum getDefaultValue(ProcessingAnnotation annotation) {
    ProcessingAnnotationValue value;
    value = getDefaultValue(annotation, "value");

    return value.getEnum(ThisEnum.class);
  }

  private ProcessingAnnotation noDefaults() {
    return getDirectlyPresentAnnotation(Subject.class, NoDefaults.class);
  }

  private ProcessingAnnotation withDefaults() {
    return getDirectlyPresentAnnotation(Subject.class, WithDefaults.class);
  }

  @WithDefaults
  private static class Defaults {}

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  private @interface NoDefaults {
    ThisEnum value();
  }

  @NoDefaults(ThisEnum.NO_DEFAULTS)
  @WithDefaults(ThisEnum.WITH_DEFAULTS)
  private static class Subject {}

  private enum ThisEnum {
    DEFAULTS,
    NO_DEFAULTS,
    WITH_DEFAULTS
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  private @interface WithDefaults {
    ThisEnum value() default ThisEnum.DEFAULTS;
  }

}
