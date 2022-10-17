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
import static org.testng.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProcessingAnnotationValueAnnotationArrayTest
    extends
    AbstractProcessingAnnotationValueTest {

  @Test
  public void getDeclaredOrDefaultValue() {
    assertEquals(
        getDeclaredOrDefaultValue(defaults()),
        UnmodifiableList.of()
    );

    assertEquals(
        getDeclaredOrDefaultValue(noDefaults()),
        UnmodifiableList.of("A")
    );

    assertEquals(
        getDeclaredOrDefaultValue(withDefaults()),
        UnmodifiableList.of("1", "2")
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
        UnmodifiableList.of("A")
    );

    assertEquals(
        getDeclaredValue(withDefaults()),
        UnmodifiableList.of("1", "2")
    );
  }

  @Test
  public void getDefaultValue() {
    assertEquals(
        getDefaultValue(defaults()),
        UnmodifiableList.of()
    );

    try {
      getDefaultValue(noDefaults());
      Assert.fail();
    } catch (NoDefaultValueException expected) {
      assertTrue(expected.getMessage().contains("value"));
    }

    assertEquals(
        getDefaultValue(withDefaults()),
        UnmodifiableList.of()
    );
  }

  private ProcessingAnnotation defaults() {
    return getDirectlyPresentAnnotation(Defaults.class, WithDefaults.class);
  }

  private UnmodifiableList<String> getDeclaredOrDefaultValue(ProcessingAnnotation annotation) {
    ProcessingAnnotationValue value;
    value = getDeclaredOrDefaultValue(annotation, "value");

    return tn(value.getAnnotationArray());
  }

  private UnmodifiableList<String> getDeclaredValue(ProcessingAnnotation annotation) {
    ProcessingAnnotationValue value;
    value = getDeclaredValue(annotation, "value");

    return tn(value.getAnnotationArray());
  }

  private UnmodifiableList<String> getDefaultValue(ProcessingAnnotation annotation) {
    ProcessingAnnotationValue value;
    value = getDefaultValue(annotation, "value");

    return tn(value.getAnnotationArray());
  }

  private ProcessingAnnotation noDefaults() {
    return getDirectlyPresentAnnotation(Subject.class, NoDefaults.class);
  }

  private UnmodifiableList<String> tn(UnmodifiableList<ProcessingAnnotation> iter) {
    GrowableList<String> result;
    result = new GrowableList<>();

    for (int i = 0; i < iter.size(); i++) {
      ProcessingAnnotation ann;
      ann = iter.get(i);

      ProcessingAnnotationValue value;
      value = ann.getDeclaredValue("value");

      String s;
      s = value.getString();

      result.add(s);
    }

    return result.toUnmodifiableList();
  }

  private ProcessingAnnotation withDefaults() {
    return getDirectlyPresentAnnotation(Subject.class, WithDefaults.class);
  }

  @WithDefaults
  private static class Defaults {}

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  private @interface NoDefaults {
    Value[] value();
  }

  @NoDefaults({@Value("A")})
  @WithDefaults({@Value("1"), @Value("2")})
  private static class Subject {}

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.ANNOTATION_TYPE)
  private @interface Value {
    String value();
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  private @interface WithDefaults {
    Value[] value() default {};
  }

}