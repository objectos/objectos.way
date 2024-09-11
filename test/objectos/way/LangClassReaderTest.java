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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import objectos.util.set.GrowableSet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LangClassReaderTest {

  private Lang.ClassReader reader;

  @BeforeClass
  public void beforeClass() {
    reader = Lang.createClassReader(TestingNoteSink.INSTANCE);
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

    assertEquals(
        processStringConstants(Subject.class),

        Set.of("first", "second")
    );
  }

  private void consume(String string) {}

  private Set<String> processStringConstants(Class<?> clazz) throws IOException {
    String binaryName;
    binaryName = clazz.getName();

    byte[] bytes;
    bytes = loadBytes(clazz);

    reader.init(binaryName, bytes);

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