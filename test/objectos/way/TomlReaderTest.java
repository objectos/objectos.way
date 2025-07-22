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

import org.testng.annotations.Test;

public class TomlReaderTest {

  @Test
  public void testCase01() {
    test(
        """
        [coordinates]
        group = "com.example.test"
        artifact = "some.artifact"
        version = "1.0.0-SNAPSHOT"
        """,

        """
        TableHeader[coordinates]
        StringProperty[group, com.example.test]
        StringProperty[artifact, some.artifact]
        StringProperty[version, 1.0.0-SNAPSHOT]
        """
    );
  }

  private void test(String source, String expected) {
  }

}