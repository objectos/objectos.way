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
import static org.testng.Assert.assertTrue;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;

abstract class AbstractObjectosFsTest {

  static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

  static final String JAVA_IO_TMPDIR = System.getProperty("java.io.tmpdir");

  static final Charset UTF8 = Charset.forName("UTF-8");

  static final PathNameVisitor<String, String> VISITOR = new PathNameVisitor<String, String>() {
    @Override
    public final String visitDirectory(Directory directory, String p) throws IOException {
      return p + ":directory:" + directory.getName();
    }

    @Override
    public final String visitNotFound(ResolvedPath notFound, String p) throws IOException {
      return p + ":notfound:" + notFound.getName();
    }

    @Override
    public final String visitRegularFile(RegularFile file, String p) throws IOException {
      return p + ":file:" + file.getName();
    }
  };

  final boolean arraysEquals(byte[] a, int offset, byte[] b) {
    byte[] copy;
    copy = new byte[b.length];

    System.arraycopy(a, offset, copy, 0, copy.length);

    return Arrays.equals(copy, b);
  }

  final void assertRegularFileUri(URI uri, Directory dir, String fileName) {
    String result;
    result = uri.toString();

    assertTrue(result.contains(dir.getPath()));

    assertTrue(result.endsWith(fileName));

    OperatingSystem os = OperatingSystem.get();

    os.acceptOperatingSystemVisitor(new OperatingSystemVisitor<Void, String>() {
      @Override
      public final Void visitLinux(Linux os, String p) {
        assertTrue(p.startsWith("file:///"));

        assertFalse(p.startsWith("file:////"));

        return null;
      }

      @Override
      public final Void visitUnsupportedOs(UnsupportedOperatingSystem os, String p) {
        return null;
      }
    }, result);
  }

  final void close(Closeable out) throws IOException {
    if (out != null) {
      out.close();
    }
  }

  final Directory createTempDir() throws IOException {
    return InternalTestingFs.nextDirectory();
  }

  final RegularFile createTempFile(Directory directory) throws IOException {
    String randomName;
    randomName = Next.string(10);

    return directory.createRegularFile(randomName);
  }

  final String newRelativePath(final String first, final String second) {
    return first + File.separatorChar + second;
  }

}