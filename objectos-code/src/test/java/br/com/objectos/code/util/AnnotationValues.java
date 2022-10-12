/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AnnotationValues {

  char charValue() default '\0';

  Letter[] enumArray() default {};

  Letter enumValue() default Letter.Z;

  int intValue() default -1;
  
  int[] intArrayValue() default {};

  String string() default "default";

  String[] stringArrayValue() default {};

  Class<?> typeMirror() default Object.class;

  StringValueAnnotation stringValueAnnotation() default @StringValueAnnotation("");

}