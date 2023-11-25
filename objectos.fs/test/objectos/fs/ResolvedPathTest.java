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

import java.io.IOException;
import java.util.Arrays;
import objectos.core.io.Charsets;
import objectos.core.io.Read;
import objectos.core.io.Write;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ResolvedPathTest extends AbstractObjectosFsTest {

  @Test
  public void createDirectory() throws IOException {
    Directory root;
    root = createTempDir();

    ResolvedPath subject;
    subject = root.resolve("subject");

    assertFalse(subject.exists());

    Directory created;
    created = subject.createDirectory();

    assertTrue(subject.exists());

    assertEquals(created, root.getDirectory("subject"));

    try {
      subject.createDirectory();

      Assert.fail("expected exception was not thrown");
    } catch (FoundException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("subject"));
    }

    created.delete();

    root.createRegularFile("subject");

    try {
      subject.createDirectory();

      Assert.fail("expected exception was not thrown");
    } catch (FoundException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("subject"));
    }
  }

  @Test
  public void createParents() throws IOException {
    Directory root;
    root = createTempDir();

    ResolvedPath a;
    a = root.resolve("a");

    ResolvedPath ab;
    ab = root.resolve("a", "b");

    ResolvedPath abc;
    abc = root.resolve("a", "b", "c");

    assertFalse(a.exists());

    assertFalse(ab.exists());

    assertFalse(abc.exists());

    try {
      abc.createDirectory();

      Assert.fail("expected exception was not thrown");
    } catch (IOException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("c"), message);
    }

    abc.createParents();

    assertTrue(a.exists());

    assertTrue(ab.exists());

    assertFalse(abc.exists());
  }

  @Test
  public void createRegularFile() throws IOException {
    Directory root;
    root = createTempDir();

    ResolvedPath subject;
    subject = root.resolve("subject");

    assertFalse(subject.exists());

    RegularFile created;
    created = subject.createRegularFile();

    assertTrue(subject.exists());

    assertEquals(created, root.getRegularFile("subject"));

    try {
      subject.createRegularFile();

      Assert.fail("expected exception was not thrown");
    } catch (FoundException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("subject"));
    }

    created.delete();

    root.createDirectory("subject");

    try {
      subject.createRegularFile();

      Assert.fail("expected exception was not thrown");
    } catch (FoundException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("subject"));
    }
  }

  @Test
  public void openOutputStream() throws IOException {
    byte[] randomBytes;
    randomBytes = Next.bytes(8192);

    Directory root;
    root = createTempDir();

    ResolvedPath path;
    path = root.resolve("file");

    assertFalse(path.exists());

    Write.byteArray(path, randomBytes);

    assertTrue(path.exists());

    RegularFile file = path.toRegularFile();

    byte[] result;
    result = Read.byteArray(file);

    assertEquals(result.length, 8192);

    assertTrue(Arrays.equals(result, randomBytes));
  }

  @Test
  public void openWriter() throws IOException {
    String randomString;
    randomString = Next.string(8192);

    Directory root;
    root = createTempDir();

    ResolvedPath path;
    path = root.resolve("file");

    assertFalse(path.exists());

    Write.string(path, Charsets.utf8(), randomString);

    assertTrue(path.exists());

    RegularFile file;
    file = path.toRegularFile();

    String result;
    result = Read.string(file, Charsets.utf8());

    assertEquals(result, randomString);
  }

  @Test
  public void toDirectory() throws IOException {
    Directory root;
    root = createTempDir();

    ResolvedPath subject;
    subject = root.resolve("subject");

    try {
      subject.toDirectory();

      Assert.fail("expected exception was not thrown");
    } catch (NotFoundException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("subject"));
    }

    RegularFile file;
    file = root.createRegularFile("subject");

    try {
      subject.toDirectory();

      Assert.fail("expected exception was not thrown");
    } catch (NotDirectoryException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("subject"));
    }

    file.delete();

    Directory directory;
    directory = root.createDirectory("subject");

    assertEquals(directory, subject.toDirectory());
  }

  @Test
  public void toDirectoryCreateIfNotFound() throws IOException {
    Directory root;
    root = createTempDir();

    ResolvedPath subject;
    subject = root.resolve("subject");

    RegularFile file;
    file = root.createRegularFile("subject");

    try {
      subject.toDirectoryCreateIfNotFound();

      Assert.fail("expected exception was not thrown");
    } catch (NotDirectoryException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("subject"));
    }

    file.delete();

    Directory directory;
    directory = subject.toDirectoryCreateIfNotFound();

    assertTrue(subject.exists());

    assertEquals(directory, root.getDirectory("subject"));

    directory = subject.toDirectoryCreateIfNotFound();

    assertEquals(directory, root.getDirectory("subject"));
  }

  @Test
  public void toRegularFile() throws IOException {
    Directory root;
    root = createTempDir();

    ResolvedPath subject;
    subject = root.resolve("subject");

    try {
      subject.toRegularFile();

      Assert.fail("expected exception was not thrown");
    } catch (NotFoundException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("subject"));
    }

    Directory dir;
    dir = root.createDirectory("subject");

    try {
      subject.toRegularFile();

      Assert.fail("expected exception was not thrown");
    } catch (NotRegularFileException expected) {
      String message;
      message = expected.getMessage();

      assertTrue(message.endsWith("subject"));
    }

    dir.delete();

    RegularFile file;
    file = root.createRegularFile("subject");

    assertEquals(file, subject.toRegularFile());
  }

}