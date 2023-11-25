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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import objectos.core.io.Read;
import objectos.core.io.Write;
import objectos.util.list.GrowableList;
import objectos.util.list.UnmodifiableList;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DirectoryTest extends AbstractObjectosFsTest {

  private String absolutePath;

  @BeforeClass
  public void _beforeClass() throws IOException {
    OperatingSystem os;
    os = OperatingSystem.get();

    absolutePath = os.acceptOperatingSystemVisitor(
        new OperatingSystemVisitor<String, Void>() {
          @Override
          public String visitLinux(Linux os, Void p) {
            return "/absolute";
          }

          @Override
          public String visitUnsupportedOs(UnsupportedOperatingSystem os, Void p) {
            throw new UnsupportedOperationException("Implement me: " + os.getOsName());
          }
        },
        null
    );
  }

  @Test
  public void acceptPathNameVisitor() throws IOException {
    Directory root;
    root = createTempDir();

    Directory foo;
    foo = root.createDirectory("foo");

    String result;
    result = foo.acceptPathNameVisitor(VISITOR, "bar");

    assertEquals(result, "bar:directory:foo");
  }

  @Test(description = "copyTo(Directory) should complete normally")
  public void copyTo0() throws IOException {
    Directory src;
    src = createTempDir();

    Write.string(
        src.createRegularFile("f1"),
        UTF8, "f1"
    );

    Write.string(
        src.createRegularFile("f2"),
        UTF8, "f2"
    );

    Directory srcA;
    srcA = src.createDirectory("A");

    Write.string(
        srcA.createRegularFile("f3"),
        UTF8, "f3"
    );

    src.createDirectory("B");

    Directory srcC = srcA.createDirectory("C");

    Write.string(
        srcC.createRegularFile("f4"),
        UTF8, "f4"
    );

    Directory dest;
    dest = createTempDir();

    assertTrue(dest.isEmpty());

    src.copyTo(dest);

    RegularFile f1;
    f1 = dest.getRegularFile("f1");

    RegularFile f2;
    f2 = dest.getRegularFile("f2");

    Directory destA;
    destA = dest.getDirectory("A");

    RegularFile f3;
    f3 = destA.getRegularFile("f3");

    Directory destC;
    destC = destA.getDirectory("C");

    RegularFile f4;
    f4 = destC.getRegularFile("f4");

    assertEquals(Read.string(f1, UTF8), "f1");
    assertEquals(Read.string(f2, UTF8), "f2");
    assertEquals(Read.string(f3, UTF8), "f3");
    assertEquals(Read.string(f4, UTF8), "f4");
  }

  @Test(description = "copyTo(Directory) should fail with FoundException")
  public void copyTo1() throws IOException {
    Directory src;
    src = createTempDir();

    Write.string(
        src.createRegularFile("f1"),
        UTF8, "f1"
    );

    Write.string(
        src.createRegularFile("f2"),
        UTF8, "f2"
    );

    Directory srcA;
    srcA = src.createDirectory("A");

    Write.string(
        srcA.createRegularFile("f3"),
        UTF8, "f3"
    );

    Directory dest;
    dest = createTempDir();

    dest.createRegularFile("f2");

    try {
      src.copyTo(dest);

      Assert.fail("expected exception was not thrown");
    } catch (FoundException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("f2"));
    }

    dest = createTempDir();

    dest.createDirectory("A");

    try {
      src.copyTo(dest);

      Assert.fail("expected exception was not thrown");
    } catch (FoundException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("A"));
    }
  }

  @Test
  public void createDirectory() throws IOException {
    Directory root;
    root = createTempDir();

    ResolvedPath ra0;
    ra0 = root.resolve("newdir");

    assertFalse(ra0.exists());

    Directory a0;
    a0 = root.createDirectory("newdir");

    assertTrue(a0.exists());

    assertEquals(a0, ra0);

    try {
      root.createDirectory("newdir");

      Assert.fail("expected exception was not thrown");
    } catch (FoundException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("newdir"));
    }

    try {
      root.createDirectory("");

      Assert.fail("expected exception was not thrown");
    } catch (IllegalArgumentException expected) {
      String message;
      message = expected.getMessage();

      assertEquals(message, "directoryName is empty");
    }

    try {
      root.createDirectory(absolutePath);

      Assert.fail("expected exception was not thrown");
    } catch (IllegalArgumentException expected) {
      String message;
      message = expected.getMessage();

      assertEquals(message, "directoryName is absolute");
    }

    try {
      root.createDirectory(newRelativePath("newdir", "descendant"));

      Assert.fail("expected exception was not thrown");
    } catch (IllegalArgumentException expected) {
      String message;
      message = expected.getMessage();

      assertEquals(message, "directoryName does not resolve to a child of this directory");
    }
  }

  @Test
  public void createRegularFile() throws IOException {
    Directory root;
    root = createTempDir();

    RegularFile file;
    file = root.createRegularFile("test-file");

    assertFalse(file.is(Posix.ownerExecutable()));

    file = root.createRegularFile(
        "perms",

        Posix.ownerReadable(),
        Posix.ownerWritable(),
        Posix.ownerExecutable()
    );

    assertTrue(file.is(Posix.ownerExecutable()));

    try {
      root.createRegularFile("test-file");

      Assert.fail("expected exception was not thrown");
    } catch (FoundException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("test-file"));
    }

    try {
      root.createRegularFile("");

      Assert.fail("expected exception was not thrown");
    } catch (IllegalArgumentException expected) {
      String message;
      message = expected.getMessage();

      assertEquals(message, "fileName is empty");
    }

    try {
      root.createRegularFile(absolutePath);

      Assert.fail("expected exception was not thrown");
    } catch (IllegalArgumentException expected) {
      String message;
      message = expected.getMessage();

      assertEquals(message, "fileName is absolute");
    }

    try {
      root.createRegularFile(newRelativePath("newdir", "descendant"));

      Assert.fail("expected exception was not thrown");
    } catch (IllegalArgumentException expected) {
      String message;
      message = expected.getMessage();

      assertEquals(message, "fileName does not resolve to a child of this directory");
    }
  }

  @Test(description = ""
      + "it should fail when directory is not empty."
      + "it should succeed otherwise")
  public void delete() throws IOException {
    Directory root;
    root = createTempDir();

    RegularFile f1;
    f1 = root.createRegularFile("f1");

    Write.string(f1, UTF8, "f1");

    assertTrue(root.exists());

    assertTrue(f1.exists());

    try {
      root.delete();

      Assert.fail();
    } catch (IOException expected) {}

    f1.delete();

    root.delete();

    assertFalse(root.exists());
  }

  @Test
  public void deleteContents() throws IOException {
    Directory root;
    root = createTempDir();

    root.deleteContents();

    RegularFile f1;
    f1 = createTempFile(root);

    Write.string(f1, UTF8, "f1");

    Directory dirA;
    dirA = root.createDirectory("dirA");

    RegularFile f2;
    f2 = createTempFile(dirA);

    Write.string(f2, UTF8, "f2");

    Directory dirB;
    dirB = dirA.createDirectory("dirB");

    RegularFile f3;
    f3 = createTempFile(dirB);

    Write.string(f3, UTF8, "f3");

    root.deleteContents();

    assertTrue(root.exists());
    assertFalse(f1.exists());
    assertFalse(f2.exists());
    assertFalse(f3.exists());
    assertFalse(dirA.exists());
    assertFalse(dirB.exists());
  }

  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void equalsTest() throws IOException {
    Directory root;
    root = createTempDir();

    Directory a;
    a = root.createDirectory("a");

    Directory b;
    b = root.getDirectory("a");

    Directory c;
    c = root.createDirectory("c");

    assertTrue(a.equals(b));

    assertTrue(b.equals(a));

    ResolvedPath aPath;
    aPath = root.resolve("c", "..", "a");

    assertTrue(a.equals(aPath.toDirectory()));

    assertFalse(a.equals(null));

    assertFalse(a.equals(c));

    assertFalse(a.equals("a"));
  }

  @Test
  public void exists() throws IOException {
    Directory yes;
    yes = createTempDir();

    assertTrue(yes.exists());

    yes.delete();

    assertFalse(yes.exists());
  }

  @Test
  public void getDirectory() throws IOException {
    Directory root;
    root = createTempDir();

    Directory a0;
    a0 = root.createDirectory("a");

    assertEquals(root.getDirectory("a"), a0);

    try {
      root.getDirectory("do-not-exist");

      Assert.fail("expected exception was not thrown");
    } catch (NotFoundException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("do-not-exist"));
    }

    try {
      root.createRegularFile("is-file");

      root.getDirectory("is-file");

      Assert.fail("expected exception was not thrown");
    } catch (NotDirectoryException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("is-file"));
    }

    try {
      root.getDirectory("");

      Assert.fail("expected exception was not thrown");
    } catch (IllegalArgumentException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("is empty"), message);
    }

    try {
      root.getDirectory(absolutePath);

      Assert.fail("expected exception was not thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "directoryName is absolute");
    }

    try {
      root.getDirectory(newRelativePath("newdir", "descendant"));

      Assert.fail("expected exception was not thrown");
    } catch (IllegalArgumentException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("does not resolve to a child of this directory"), message);
    }
  }

  @Test
  public void getName() throws IOException {
    Directory root;
    root = createTempDir();

    Directory a;
    a = root.createDirectory("a");

    Directory b;
    b = root.createDirectory("b");

    Directory c;
    c = root.createDirectory("c");

    assertEquals(a.getName(), "a");
    assertEquals(b.getName(), "b");
    assertEquals(c.getName(), "c");
  }

  @Test
  public void getOrCreateRegularFile() throws IOException {
    Directory root;
    root = createTempDir();

    try {
      root.getRegularFile("do-not-exist");
    } catch (NotFoundException expected) {

    }

    RegularFile doNotExist;
    doNotExist = root.getOrCreateRegularFile("do-not-exist");

    assertTrue(doNotExist.exists());

    RegularFile exists;
    exists = root.createRegularFile("exists");

    assertEquals(
        root.getOrCreateRegularFile("exists"),
        exists
    );

    Directory subdir;
    subdir = root.createDirectory("subdir");

    assertTrue(subdir.exists());

    try {
      root.getOrCreateRegularFile("subdir");
    } catch (NotRegularFileException expected) {

    }
  }

  @Test
  public void getParent() throws IOException {
    Directory root;
    root = createTempDir();

    Directory a;
    a = root.createDirectory("a");

    assertEquals(a.getParent(), root);

    Directory b;
    b = a.createDirectory("b");

    assertEquals(b.getParent(), a);

    try {
      Directory p;
      p = root;

      for (int i = 0; i < 20; i++) {
        p = p.getParent();
      }

      Assert.fail();
    } catch (NotFoundException expected) {

    }
  }

  @Test
  public void getPath() throws IOException {
    class Test implements OperatingSystemVisitor<String, Directory> {
      @Override
      public String visitLinux(Linux os, Directory root) {
        return root.getPath() + "/a/b/c";
      }

      @Override
      public final String visitUnsupportedOs(UnsupportedOperatingSystem os, Directory p) {
        return null;
      }
    }

    Directory root;
    root = createTempDir();

    Directory a;
    a = root.createDirectory("a");

    Directory b;
    b = a.createDirectory("b");

    Directory c;
    c = b.createDirectory("c");

    OperatingSystem os;
    os = OperatingSystem.get();

    assertEquals(
        c.getPath(),

        os.acceptOperatingSystemVisitor(new Test(), root)
    );
  }

  @Test(description = ""
      + "it should only allow single element path"
      + "it should not allow empty string"
      + "it should not allow filename in a subdir"
      + "it should not allow absolute path")
  public void getRegularFile() throws IOException {
    Directory dir;
    dir = createTempDir();

    assertTrue(dir.exists());

    try {
      dir.getRegularFile("ok");

      Assert.fail("expected exception was not thrown");
    } catch (NotFoundException expected) {

    }

    RegularFile ok;
    ok = dir.createRegularFile("ok");

    Write.string(ok, UTF8, "hello");

    assertEquals(dir.getRegularFile("ok"), ok);

    try {
      dir.getRegularFile("");

      Assert.fail("expected exception was not thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "fileName is empty");
    }

    try {
      dir.getRegularFile("sub/ok");

      Assert.fail("expected exception was not thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "fileName does not resolve to a child of this directory");
    }

    try {
      dir.getRegularFile(absolutePath);

      Assert.fail("expected exception was not thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "fileName is absolute");
    }
  }

  @Test
  public void isEmpty() throws IOException {
    Directory dir;
    dir = createTempDir();

    assertTrue(dir.isEmpty());

    Directory a;
    a = dir.createDirectory("a");

    assertFalse(dir.isEmpty());

    a.delete();

    assertTrue(dir.isEmpty());
  }

  @Test
  public void resolve() throws IOException {
    Directory root;
    root = createTempDir();

    Directory a;
    a = root.createDirectory("a");

    Directory b;
    b = a.createDirectory("b");

    RegularFile bTxt;
    bTxt = a.createRegularFile("b.txt");

    Write.string(bTxt, UTF8, "b");

    RegularFile cTxt;
    cTxt = b.createRegularFile("c.txt");

    Write.string(cTxt, UTF8, "c");

    root.createDirectory("sibling");

    assertEquals(root.resolve(""), root);
    assertEquals(root.resolve("a"), a);
    assertEquals(root.resolve("a", "b"), b);
    assertEquals(root.resolve("a", "b.txt"), bTxt);
    assertEquals(root.resolve("a", "..", "a", "b"), b);
    assertEquals(root.resolve("a", "b", "c.txt"), cTxt);

    try {
      root.resolve(absolutePath);

      Assert.fail("expected exception was not thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "pathName is absolute");
    }

    try {
      a.resolve("..", "sibling");

      Assert.fail("expected exception was not thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "pathName is not a descendant");
    }

    try {
      root.resolve("a", null, "c.txt");

      Assert.fail("expected exception was not thrown");
    } catch (NullPointerException expected) {

    }
  }

  @Test
  public void toFile() throws IOException {
    Directory javaIoTmpdir;
    javaIoTmpdir = JavaIoTmpdir.get();

    assertEquals(
        javaIoTmpdir.toFile(),

        new File(JAVA_IO_TMPDIR)
    );
  }

  @Test
  public void toUri() throws IOException {
    Directory root;
    root = createTempDir();

    URI uri;
    uri = root.toUri();

    String result;
    result = uri.toString();

    assertTrue(result.contains(root.getPath()));

    assertTrue(result.endsWith("/"));

    assertFalse(result.endsWith("//"));

    OperatingSystem os;
    os = OperatingSystem.get();

    if (os instanceof Linux) {
      assertTrue(result.startsWith("file:///"), result);

      assertFalse(result.startsWith("file:////"), result);
    }
  }

  @Test
  public void visitContents() throws IOException {
    Directory root;
    root = createTempDir();

    RegularFile f1 = createTempFile(root);

    Write.string(f1, UTF8, "f1");

    Directory dirA = root.createDirectory("dirA");

    RegularFile f2 = createTempFile(dirA);

    Write.string(f2, UTF8, "f2");

    Directory dirB = dirA.createDirectory("dirB");

    RegularFile f3 = createTempFile(dirB);

    Write.string(f3, UTF8, "f3");

    RegularFile f4 = createTempFile(root);

    Write.string(f4, UTF8, "f4");

    CollectingNodeVisitor visitor = new CollectingNodeVisitor();
    root.visitContents(visitor);

    UnmodifiableList<Directory> directories = visitor.directories.toUnmodifiableList();
    assertEquals(directories.size(), 1);
    assertTrue(directories.contains(dirA));

    UnmodifiableList<RegularFile> files = visitor.files.toUnmodifiableList();
    assertEquals(files.size(), 2);
    assertTrue(files.contains(f1));
    assertTrue(files.contains(f4));
    assertFalse(files.contains(f2));
    assertFalse(files.contains(f3));
  }

  private static class CollectingNodeVisitor implements DirectoryContentsVisitor {

    private final GrowableList<Directory> directories = new GrowableList<>();

    private final GrowableList<RegularFile> files = new GrowableList<>();

    @Override
    public final void visitDirectory(Directory directory) {
      directories.add(directory);
    }

    @Override
    public final void visitRegularFile(RegularFile file) {
      files.add(file);
    }

  }

}