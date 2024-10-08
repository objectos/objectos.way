/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.util.Set;
import objectos.util.GrowableSet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LangClassReaderTest {

  private Lang.ClassReader reader;

  @BeforeClass
  public void beforeClass() {
    reader = Lang.createClassReader(TestingNoteSink.INSTANCE);
  }

  @Test
  public void isAnnotationPresent01() throws IOException {
    @App.DoNotReload
    class Subject {}

    init(Subject.class);

    assertFalse(reader.isAnnotationPresent(TestingAnnotations.RetentionClass.class));
    assertTrue(reader.isAnnotationPresent(App.DoNotReload.class));
  }

  @Test
  public void isAnnotationPresent02() throws IOException {
    @TestingAnnotations.ConstantValues(
        byteValue = 1, charValue = 'c', doubleValue = 2.34, floatValue = 7.89F,
        intValue = 345, longValue = 890, shortValue = 523, booleanValue = true,
        stringValue = "CONSTANT"
    )
    @SuppressWarnings("unused")
    class Subject {
      String filed = "abc";

      void method() {
        assert true != false;
      }
    }

    init(Subject.class);

    assertFalse(reader.isAnnotationPresent(TestingAnnotations.RetentionClass.class));
    assertTrue(reader.isAnnotationPresent(TestingAnnotations.ConstantValues.class));
  }

  @Test
  public void isAnnotationPresent03() throws IOException {
    @TestingAnnotations.EnumConstValue(ElementType.ANNOTATION_TYPE)
    @TestingAnnotations.ClassInfoValue(Lang.ClassReader.class)
    @TestingAnnotations.AnnotationValue(@TestingAnnotations.ClassInfoValue(Integer.class))
    class Subject {}

    init(Subject.class);

    assertFalse(reader.isAnnotationPresent(TestingAnnotations.RetentionClass.class));
    assertTrue(reader.isAnnotationPresent(TestingAnnotations.EnumConstValue.class));
    assertTrue(reader.isAnnotationPresent(TestingAnnotations.ClassInfoValue.class));
    assertTrue(reader.isAnnotationPresent(TestingAnnotations.AnnotationValue.class));
  }

  @Test
  public void isAnnotationPresent04() throws IOException {
    @TestingAnnotations.ArrayValue(
        constantArray = {"a", "b", "c"},
        enumArray = {ElementType.ANNOTATION_TYPE, ElementType.PACKAGE},
        classArray = {Lang.ClassReader.class},
        annotationArray = {
            @TestingAnnotations.AnnotationValue(@TestingAnnotations.ClassInfoValue(Integer.class)),
            @TestingAnnotations.AnnotationValue(@TestingAnnotations.ClassInfoValue(Long.class))
        }
    )
    class Subject {}

    init(Subject.class);

    assertFalse(reader.isAnnotationPresent(TestingAnnotations.RetentionClass.class));
    assertTrue(reader.isAnnotationPresent(TestingAnnotations.ArrayValue.class));
  }

  @Test
  public void processStringConstants01() throws IOException {
    @SuppressWarnings("unused")
    class Subject {
      final String A = "first";

      void foo() {
        consume("second");
      }
    }

    init(Subject.class);

    assertEquals(
        processStringConstants(),

        Set.of("first", "second")
    );
  }

  private void consume(String string) {}

  private void init(Class<?> clazz) throws IOException {
    String binaryName;
    binaryName = clazz.getName();

    byte[] bytes;
    bytes = loadBytes(clazz);

    reader.init(binaryName, bytes);
  }

  private Set<String> processStringConstants() {
    Set<String> set;
    set = new GrowableSet<>();

    reader.processStringConstants(set::add);

    return set;
  }

  private byte[] loadBytes(Class<?> clazz) throws IOException {
    String binaryName;
    binaryName = clazz.getName();

    String resourceName;
    resourceName = binaryName.replace('.', '/');

    resourceName += ".class";

    ClassLoader loader;
    loader = ClassLoader.getSystemClassLoader();

    InputStream in;
    in = loader.getResourceAsStream(resourceName);

    if (in == null) {
      throw new IllegalArgumentException("Class file not found: " + resourceName);
    }

    try (in; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      in.transferTo(out);

      return out.toByteArray();
    }
  }

}