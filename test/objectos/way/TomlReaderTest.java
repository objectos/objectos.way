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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import org.testng.annotations.Test;

public class TomlReaderTest {

  @FunctionalInterface
  private interface ThrowingConsumer {
    void accept(Toml.Reader r) throws IOException;
  }

  private record Project(Coordinates coordinates) {}

  private record Coordinates(String group, String artifact, String version) {}

  @Test
  public void testCase01() {
    test(
        """
        [coordinates]
        group = "com.example.test"
        artifact = "some.artifact"
        version = "1.0.0-SNAPSHOT"
        """,

        r -> {
          assertEquals(
              r.readRecord(Project.class),

              new Project(
                  new Coordinates(
                      "com.example.test",
                      "some.artifact",
                      "1.0.0-SNAPSHOT"
                  )
              )
          );
        }
    );
  }

  private void test(String source, ThrowingConsumer test) {
    final Path file;
    file = Y.nextTempFile(source, StandardCharsets.UTF_8);

    try (Toml.Reader reader = Toml.Reader.create(opts -> {
      opts.file(file);

      opts.lookup(MethodHandles.lookup());
    })) {
      test.accept(reader);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}