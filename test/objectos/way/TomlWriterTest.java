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
import java.nio.file.Files;
import java.nio.file.Path;
import org.testng.annotations.Test;

public class TomlWriterTest {

  @FunctionalInterface
  private interface ThrowingConsumer {
    void accept(Toml.Writer w) throws IOException;
  }

  @Test
  public void testCase01() {
    record Coordinates(String group, String artifact, String version) {}

    record Project(Coordinates coordinates) {}

    final Coordinates coordinates;
    coordinates = new Coordinates(
        "com.example.test",
        "some.artifact",
        "1.0.0-SNAPSHOT"
    );

    final Project project;
    project = new Project(coordinates);

    test(
        w -> w.writeRecord(project),

        """
        [coordinates]
        group = "com.example.test"
        artifact = "some.artifact"
        version = "1.0.0-SNAPSHOT"
        """
    );
  }

  private void test(ThrowingConsumer test, String expected) {
    final Path file;
    file = Y.nextTempFile();

    try (Toml.Writer w = Toml.Writer.ofFile(file)) {
      test.accept(w);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    try {
      assertEquals(Files.readString(file), expected);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}