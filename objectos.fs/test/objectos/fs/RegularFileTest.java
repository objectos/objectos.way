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
import java.io.Writer;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import objectos.core.io.Read;
import objectos.core.io.Write;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class RegularFileTest extends AbstractObjectosFsTest {

  private long lastModified;

  private RegularFile lastModifiedSubject;

  private final byte[] MORE_BYTES = "more bytes".getBytes(UTF8);

  private final byte[] SOME_BYTES = "some bytes".getBytes(UTF8);

  @Test
  public void acceptPathNameVisitor() throws IOException {
    Directory root;
    root = createTempDir();

    RegularFile foo;
    foo = root.createRegularFile("foo");

    String result;
    result = foo.acceptPathNameVisitor(VISITOR, "bar");

    assertEquals(result, "bar:file:foo");
  }

  @Test
  public void compareTo() throws IOException {
    Directory root;
    root = createTempDir();

    List<RegularFile> files;
    files = new ArrayList<>();

    RegularFile f3;
    f3 = root.createRegularFile("f3");

    files.add(f3);

    RegularFile f1;
    f1 = root.createRegularFile("f1");

    files.add(f1);

    RegularFile f2;
    f2 = root.createRegularFile("f2");

    files.add(f2);

    Collections.sort(files);

    assertEquals(files.get(0), f1);
    assertEquals(files.get(1), f2);
    assertEquals(files.get(2), f3);
  }

  @Test
  public void delete() throws IOException {
    Directory root;
    root = createTempDir();

    RegularFile f;
    f = createTempFile(root);

    assertTrue(f.exists());

    f.delete();

    assertFalse(f.exists());
  }

  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void equalsTest() throws IOException {
    Directory root;
    root = createTempDir();

    RegularFile a;
    a = root.createRegularFile("a");

    RegularFile b;
    b = root.getRegularFile("a");

    RegularFile c;
    c = root.createRegularFile("c");

    assertTrue(a.equals(b));

    assertTrue(b.equals(a));

    ResolvedPath aPath;
    aPath = root.resolve("c", "..", "a");

    assertTrue(a.equals(aPath.toRegularFile()));

    assertFalse(a.equals(null));

    assertFalse(a.equals(c));

    assertFalse(a.equals("a"));
  }

  // actual getLastModifiedMillis test: workaround to be run at the end of the suite
  @AfterSuite(alwaysRun = true)
  public void getLastModifiedMillis() throws InterruptedException, IOException {
    byte[] bytes;
    bytes = Next.bytes(1 << 10);

    Write.byteArray(lastModifiedSubject, bytes);

    Directory parent;
    parent = lastModifiedSubject.getParent();

    RegularFile subject;
    subject = parent.getRegularFile(lastModifiedSubject.getName());

    long result;
    result = subject.getLastModifiedMillis();

    assertTrue(result > lastModified);
  }

  @BeforeSuite
  public void getLastModifiedMillisSetup() throws IOException {
    Directory lastModifiedDirectory;
    lastModifiedDirectory = InternalTestingFs.nextDirectory();

    lastModifiedSubject = lastModifiedDirectory.createRegularFile("last-modified-subject");

    lastModified = lastModifiedSubject.getLastModifiedMillis();
  }

  @Test
  public void getName() throws IOException {
    Directory root;
    root = createTempDir();

    RegularFile file;
    file = root.createRegularFile("file");

    assertEquals(file.getName(), "file");
  }

  @Test
  public void getParent() throws IOException {
    Directory root;
    root = createTempDir();

    RegularFile file;
    file = root.createRegularFile("file");

    assertEquals(file.getParent(), root);
  }

  @Test
  public void getPath() throws IOException {
    class Test implements OperatingSystemVisitor<String, Directory> {
      @Override
      public String visitLinux(Linux os, Directory root) {
        return root.getPath() + "/path-subject.txt";
      }

      @Override
      public final String visitUnsupportedOs(UnsupportedOperatingSystem os, Directory p) {
        return null;
      }
    }

    Directory root;
    root = createTempDir();

    RegularFile subject;
    subject = root.createRegularFile("path-subject.txt");

    OperatingSystem os;
    os = OperatingSystem.get();

    assertEquals(
        subject.getPath(),

        os.acceptOperatingSystemVisitor(new Test(), root)
    );
  }

  @Test
  public void is() throws IOException {
    Directory root;
    root = createTempDir();

    RegularFile file;
    file = root.createRegularFile("perms");

    assertTrue(file.is(Posix.ownerReadable()));

    assertTrue(file.is(Posix.ownerWritable()));

    assertFalse(file.is(Posix.ownerExecutable()));
  }

  @Test
  public void openInputStream() throws IOException {
    byte[] bytes;
    bytes = Next.bytes(1 << 14);

    Directory root;
    root = createTempDir();

    RegularFile f;
    f = root.createRegularFile("f");

    Write.byteArray(f, bytes);

    byte[] result;
    result = Read.byteArray(f);

    assertEquals(result, bytes);
  }

  @Test
  public void openOutputStream() throws IOException {
    byte[] firstBytes;
    firstBytes = Next.bytes(8192);

    Directory root;
    root = createTempDir();

    RegularFile file;
    file = root.createRegularFile("file");

    Write.byteArray(file, firstBytes);

    byte[] lastBytes;
    lastBytes = Next.bytes(8192);

    Write.byteArray(file, lastBytes);

    byte[] result;
    result = Read.byteArray(file);

    assertEquals(result.length, 8192 * 2);

    assertTrue(arraysEquals(result, 0, firstBytes));

    assertTrue(arraysEquals(result, 8192, lastBytes));
  }

  @Test
  public void openReadAndWriteChannel() throws IOException {
    Directory tempDir;
    tempDir = createTempDir();

    RegularFile notFound;
    notFound = tempDir.createRegularFile("writable");

    FileChannel channel;
    channel = notFound.openReadAndWriteChannel();

    byte[] expected;
    expected = new byte[128];

    try {
      ByteBuffer bb;
      bb = ByteBuffer.allocate(2);

      for (byte i = 0; i >= 0 && i < 128; i++) {
        bb.put(i);

        bb.flip();

        channel.write(bb);

        bb.compact();

        expected[i] = i;
      }

      RegularFile file;
      file = notFound;

      byte[] bytes;
      bytes = Read.byteArray(file);

      assertEquals(bytes, expected);

      channel.position(0);

      bb = ByteBuffer.allocate(128);

      channel.read(bb);

      assertEquals(bytes, bb.array());
    } finally {
      channel.close();
    }
  }

  @Test
  public void openReadChannel() throws IOException {
    Directory root;
    root = createTempDir();

    FileChannel channel;
    channel = null;

    try {
      RegularFile file;
      file = root.createRegularFile("file");

      Write.byteArray(file, SOME_BYTES);

      channel = file.openReadChannel();

      try {
        writeBytes(channel, MORE_BYTES);

        Assert.fail();
      } catch (NonWritableChannelException expected) {

      }

      ByteBuffer bytesBuffer;
      bytesBuffer = ByteBuffer.allocate(5);

      channel.position(5);

      channel.read(bytesBuffer);

      String bytesString;
      bytesString = new String(bytesBuffer.array());

      assertEquals(bytesString, "bytes");
    } finally {
      close(channel);
    }
  }

  @Test
  public void openWriteChannel() throws IOException {
    Directory root;
    root = createTempDir();

    RegularFile file;
    file = root.createRegularFile("write");

    try (var writable = file.openWriteChannel()) {
      ByteBuffer bytesBuffer;
      bytesBuffer = ByteBuffer.allocate(5);

      try {
        writable.read(bytesBuffer);

        Assert.fail();
      } catch (NonReadableChannelException expected) {

      }
    }

    byte[] firstBytes;
    firstBytes = Next.bytes(8192);

    try (var writable = file.openWriteChannel()) {
      writeBytes(writable, firstBytes);
    }

    byte[] lastBytes;
    lastBytes = Next.bytes(8192);

    try (var writable = file.openWriteChannel()) {
      writeBytes(writable, lastBytes);
    }

    byte[] result;
    result = Read.byteArray(file);

    assertEquals(result.length, 8192 * 2);

    assertTrue(arraysEquals(result, 0, firstBytes));

    assertTrue(arraysEquals(result, 8192, lastBytes));
  }

  @Test
  public void openWriter() throws IOException {
    Directory root;
    root = createTempDir();

    RegularFile file;
    file = root.createRegularFile("file");

    Charset charset;
    charset = UTF8;

    Writer out;
    out = file.openWriter(charset);

    try {
      out.write("initial text");
    } finally {
      out.close();
    }

    assertEquals(Read.string(file, charset), "initial text");

    out = file.openWriter(charset);

    try {
      out.write(" should append");
    } finally {
      out.close();
    }

    assertEquals(Read.string(file, charset), "initial text should append");

    file.truncate();

    out = file.openWriter(charset);

    try {
      out.write("after truncate");
    } finally {
      out.close();
    }

    assertEquals(Read.string(file, charset), "after truncate");
  }

  @Test
  public void size() throws IOException {
    byte[] bytes;
    bytes = Next.bytes(54321);

    Directory root;
    root = createTempDir();

    RegularFile f;
    f = root.createRegularFile("f");

    assertEquals(f.size(), 0);

    Write.byteArray(f, bytes);

    assertEquals(f.size(), bytes.length);

    f.truncate();

    assertEquals(f.size(), 0);

    f.delete();

    try {
      f.size();

      Assert.fail();
    } catch (NotRegularFileException expected) {

    }
  }

  @Test
  public void toFile() throws IOException {
    Directory root;
    root = createTempDir();

    RegularFile f;
    f = root.createRegularFile("f");

    File r0;
    r0 = new File(JAVA_IO_TMPDIR);

    File r1;
    r1 = new File(r0, root.getName());

    assertEquals(
        f.toFile(),

        new File(r1, "f")
    );
  }

  @Test
  public void toUri() throws IOException {
    Directory root;
    root = createTempDir();

    RegularFile f;
    f = root.createRegularFile("uri.txt");

    Write.string(f, UTF8, "uri");

    URI uri;
    uri = f.toUri();

    assertRegularFileUri(uri, root, "uri.txt");
  }

  private void writeBytes(WritableByteChannel channel, byte[] bytes) throws IOException {
    ByteBuffer bb;
    bb = ByteBuffer.wrap(bytes);

    channel.write(bb);
  }

}