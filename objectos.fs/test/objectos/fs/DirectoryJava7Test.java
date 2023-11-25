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

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DirectoryJava7Test extends AbstractObjectosFsTest {

  private Path absolutePath;

  @BeforeClass
  public void _beforeClass() throws IOException {
    OperatingSystem os;
    os = OperatingSystem.get();

    absolutePath = os.acceptOperatingSystemVisitor(
        new OperatingSystemVisitor<Path, Void>() {
          @Override
          public Path visitLinux(Linux os, Void p) {
            return Paths.get("/absolute");
          }

          @Override
          public Path visitUnsupportedOs(UnsupportedOperatingSystem os, Void p) {
            throw new UnsupportedOperationException("Implement me: " + os.getOsName());
          }
        },
        null
    );
  }

  @Test
  public void resolve() throws IOException {
    Directory root;
    root = createTempDir();

    Directory a;
    a = root.createDirectory("a");

    assertEquals(root.resolve(Paths.get("")), root);
    assertEquals(root.resolve(Paths.get("a")), a);

    try {
      root.resolve(absolutePath);

      Assert.fail("expected exception was not thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "pathName is absolute");
    }

    try {
      a.resolve(Paths.get("..", "sibling"));

      Assert.fail("expected exception was not thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "pathName is not a descendant");
    }

    try {
      root.resolve(Paths.get("a", null, "c.txt"));

      Assert.fail("expected exception was not thrown");
    } catch (NullPointerException expected) {

    }
  }

  @Test
  public void toPath() throws IOException {
    Directory tempDir;
    tempDir = createTempDir();

    String name;
    name = tempDir.getName();

    Path path;
    path = Paths.get(JAVA_IO_TMPDIR, name);

    assertEquals(path, tempDir.toPath());
  }

}