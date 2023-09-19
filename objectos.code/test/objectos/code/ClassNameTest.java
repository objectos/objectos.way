/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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
package objectos.code;

import static org.testng.Assert.assertEquals;

import java.nio.file.Path;
import org.testng.annotations.Test;

public class ClassNameTest {

  @Test
  public void toPath() {
    ClassName className;
    className = ClassName.of("a.b", "Test");

    Path src;
    src = Path.of("src", "main", "java");

    Path file;
    file = className.toPath(src);

    assertEquals(file, src.resolve(Path.of("a", "b", "Test.java")));
  }

}