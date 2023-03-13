/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import objectos.html.tmpl.AnyElementValue;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HtmlSinkTest {

  private final DistinctClassNames classNames = new DistinctClassNames();

  private Path directory;

  private HtmlSink sink;

  @BeforeClass
  public void _setUp() throws IOException {
    directory = Files.createTempDirectory("html-sink-test-");

    sink = new HtmlSink();
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

  @Test(description = """
  HtmlSink toDirectory TC01

  - index.html
  """)
  public void toDirectory01() throws IOException {
    var tmpl = new HtmlTemplate() {
      @Override
      protected final void definition() {
        pathName("index.html");

        p("Test Case 01");
      }
    };

    sink.toDirectory(tmpl, directory);

    var path = Path.of("index.html");

    var file = directory.resolve(path);

    assertTrue(Files.isRegularFile(file));

    assertEquals(
      Files.readString(file),

      """
      <p>Test Case 01</p>
      """
    );
  }

  @Test(description = """
  HtmlSink toDirectory TC02

  - /a/b/index.html
  """)
  public void toDirectory02() throws IOException {
    var tmpl = new HtmlTemplate() {
      @Override
      protected final void definition() {
        pathName("a/b/index.html");

        a(href("index.html"), t("Test case 02"));
      }
    };

    sink.toDirectory(tmpl, directory);

    var path = Path.of("a", "b", "index.html");

    var file = directory.resolve(path);

    assertTrue(Files.isRegularFile(file));

    assertEquals(
      Files.readString(file),

      """
      <a href="../../index.html">Test case 02</a>
      """
    );
  }

  @Test(description = """
  HtmlSink toDirectory TC03

  - no path name defined should throw
  """)
  public void toDirectory03() throws IOException {
    var tmpl = new HtmlTemplate() {
      @Override
      protected final void definition() {
        p("Test Case 03");
      }
    };

    try {
      sink.toDirectory(tmpl, directory);

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      var msg = expected.getMessage();

      assertTrue(msg.contains("no pathname was defined"));
    }
  }

  @Test(description = """
  HtmlSink toDirectory TC01

  - collect distinct class names use-case
  """)
  public void toVisitor01() {
    var tmpl = new HtmlTemplate() {
      private final AnyElementValue abc = new TestClassSelector("abc");
      private final AnyElementValue def = new TestClassSelector("def");
      private final AnyElementValue ghi = new TestClassSelector("ghi");

      @Override
      protected final void definition() {
        div(
          abc, ghi, _class("c01"), def,
          p(abc, def, t("Test case01"))
        );
      }
    };

    classNames.clear();

    sink.toVisitor(tmpl, classNames);

    assertEquals(classNames.size(), 4);
    assertTrue(classNames.contains("abc"));
    assertTrue(classNames.contains("def"));
    assertTrue(classNames.contains("ghi"));
    assertTrue(classNames.contains("c01"));
  }

}