/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.fs;

import static org.testng.Assert.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FilesTest extends AbstractObjectosFsTest {

  @Test
  public void readAttributes() throws IOException {
    Path path;
    path = Paths.get("i-do-not-exist");

    assertFalse(Files.exists(path));

    try {
      Files.readAttributes(path, BasicFileAttributes.class);

      Assert.fail("expected exception was not thrown");
    } catch (NoSuchFileException expected) {

    }
  }

}