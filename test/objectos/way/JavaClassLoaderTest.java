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

import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.testng.annotations.Test;

public class JavaClassLoaderTest {

  @Test
  public void getResourceAsStream() throws IOException {
    ClassLoader loader;
    loader = ClassLoader.getSystemClassLoader();

    try (InputStream stream = loader.getResourceAsStream("objectos/way");
        Reader intermediate = new InputStreamReader(stream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(intermediate)) {
      List<String> lines = reader.lines().toList();

      assertTrue(lines.contains(getClass().getSimpleName() + ".class"));
    }
  }

}