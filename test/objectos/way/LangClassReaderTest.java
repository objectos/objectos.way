/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
import org.testng.Assert;
import org.testng.annotations.Test;

public class LangClassReaderTest {

  private final Lang.ClassReader reader = Lang.createClassReader(TestingNoteSink.INSTANCE);

  @Test
  public void annotatedWith01() throws Exception {
    @App.DoNotReload
    class Subject {}

    final byte[] bytes;
    bytes = loadBytes(Subject.class);

    reader.init(bytes);

    assertFalse(reader.annotatedWith(TestingAnnotations.RetentionClass.class));
    assertTrue(reader.annotatedWith(App.DoNotReload.class));
  }

  @Test
  public void annotatedWith02() throws Exception {
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

    final byte[] bytes;
    bytes = loadBytes(Subject.class);

    reader.init(bytes);

    assertFalse(reader.annotatedWith(TestingAnnotations.RetentionClass.class));
    assertTrue(reader.annotatedWith(TestingAnnotations.ConstantValues.class));
  }

  @Test
  public void annotatedWith03() throws Exception {
    @TestingAnnotations.EnumConstValue(ElementType.ANNOTATION_TYPE)
    @TestingAnnotations.ClassInfoValue(Lang.ClassReader.class)
    @TestingAnnotations.AnnotationValue(@TestingAnnotations.ClassInfoValue(Integer.class))
    class Subject {}

    final byte[] bytes;
    bytes = loadBytes(Subject.class);

    reader.init(bytes);

    assertFalse(reader.annotatedWith(TestingAnnotations.RetentionClass.class));
    assertTrue(reader.annotatedWith(TestingAnnotations.EnumConstValue.class));
    assertTrue(reader.annotatedWith(TestingAnnotations.ClassInfoValue.class));
    assertTrue(reader.annotatedWith(TestingAnnotations.AnnotationValue.class));
  }

  @Test
  public void isAnnotationPresent04() throws Exception {
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

    final byte[] bytes;
    bytes = loadBytes(Subject.class);

    reader.init(bytes);

    assertFalse(reader.annotatedWith(TestingAnnotations.RetentionClass.class));
    assertTrue(reader.annotatedWith(TestingAnnotations.ArrayValue.class));
  }

  @Test
  public void visitStrings01() throws Exception {
    @SuppressWarnings("unused")
    class Subject {
      final String A = "first";

      void foo() {
        consume("second");
      }

      private void consume(String string) {}
    }

    final byte[] bytes;
    bytes = loadBytes(Subject.class);

    reader.init(bytes);

    Set<String> set;
    set = Util.createSet();

    reader.visitStrings(set::add);

    assertEquals(
        set,

        Set.of("first", "second")
    );
  }

  @Test
  public void visitStrings02() throws Exception {
    @SuppressWarnings("unused")
    @TestingAnnotations.RetentionClass
    class Subject {
      final String a = "a";
      final String b = "b";
      final String c = "c";
    }

    final byte[] bytes;
    bytes = loadBytes(Subject.class);

    reader.init(bytes);

    if (reader.annotatedWith(TestingAnnotations.RetentionClass.class)) {
      Set<String> set;
      set = Util.createSet();

      reader.visitStrings(set::add);

      assertEquals(
          set,

          Set.of("a", "b", "c")
      );
    } else {
      Assert.fail();
    }
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