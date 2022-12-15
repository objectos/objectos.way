/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class JavaSinkTest {

  private Path directory;

  private JavaSink sink;

  @BeforeClass
  public void _setUp() throws IOException {
    directory = Files.createTempDirectory("java-sink-test-");

    sink = JavaSink.ofDirectory(directory);
  }

  @AfterClass
  public void _tearDown() throws IOException {
    var rm = new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.delete(file);
        return FileVisitResult.CONTINUE;
      }
    };

    Files.walkFileTree(directory, rm);
  }

  @Test
  public void testCase01() throws IOException {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        _package("a.b");

        _class(
          _public(), id("Test")
        );
      }
    };

    sink.write(tmpl);

    var path = Path.of("a", "b", "Test.java");

    var file = directory.resolve(path);

    assertTrue(Files.isRegularFile(file));

    assertEquals(
      Files.readString(file),
      """
      package a.b;

      public class Test {}
      """
    );
  }

  @Test(dependsOnMethods = "testCase01")
  public void testCase02() {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        _package("a.b");

        _class(
          _public(), id("Test")
        );
      }
    };

    try {
      sink.write(tmpl);

      Assert.fail();
    } catch (IOException expected) {

    }
  }

  @Test(dependsOnMethods = "testCase02")
  public void testCase03() throws IOException {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        _package("a.b");

        _class(
          _public(), id("Test"),
          constructor(_private())
        );
      }
    };

    var path = Path.of("a", "b", "Test.java");

    var file = directory.resolve(path);

    assertTrue(Files.isRegularFile(file));

    var overwritingSink = JavaSink.ofDirectory(directory, JavaSink.overwriteExisting());

    overwritingSink.write(tmpl);

    assertEquals(
      Files.readString(file),

      """
      package a.b;

      public class Test {
        private Test() {}
      }
      """
    );
  }

  @Test
  public void testCase04() throws IOException {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        _package("test4");

        _enum(
          _public(), id("Test"),
          enumConstant(id("INSTANCE"))
        );
      }
    };

    sink.write(tmpl);

    var path = Path.of("test4", "Test.java");

    var file = directory.resolve(path);

    assertTrue(Files.isRegularFile(file));

    assertEquals(
      Files.readString(file),
      """
      package test4;

      public enum Test {
        INSTANCE;
      }
      """
    );
  }

  @Test
  public void testCase05() throws IOException {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        _package("test5");

        _interface(
          _public(), id("Test")
        );
      }
    };

    sink.write(tmpl);

    var path = Path.of("test5", "Test.java");

    var file = directory.resolve(path);

    assertTrue(Files.isRegularFile(file));

    var result = Files.readString(file);

    assertEquals(
      result,
      """
      package test5;

      public interface Test {}
      """
    );
  }

}