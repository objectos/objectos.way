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
package objectos.fs.zip;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import objectos.core.io.Read;
import objectos.core.io.Write;
import objectos.core.testing.Next;
import objectos.fs.Directory;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.fs.testing.TmpDir;
import org.testng.annotations.Test;

public final class FsZipTest {

  @Test
  public void zip() throws IOException {
    zip0(".");

    zip0("A", "B", "f1", "f2");
  }

  final void write(File file, String text) throws IOException {
    FileWriter w;
    w = new FileWriter(file);

    try {
      w.write(text);
    } finally {
      w.close();
    }
  }

  private void zip0(String... files) throws IOException {
    int size;
    size = 1024;

    byte[] bytes1;
    bytes1 = Next.bytes(size);

    byte[] bytes2;
    bytes2 = Next.bytes(size << 1);

    byte[] bytes3;
    bytes3 = Next.bytes(size << 2);

    byte[] bytes4;
    bytes4 = Next.bytes(size << 3);

    Directory root;
    root = TmpDir.create();

    Directory src;
    src = root.createDirectory("src");

    Directory srcA;
    srcA = src.createDirectory("A");

    src.createDirectory("B");

    Directory srcC;
    srcC = srcA.createDirectory("C");

    RegularFile f1;
    f1 = src.createRegularFile("f1");

    RegularFile f2;
    f2 = src.createRegularFile("f2");

    RegularFile f3;
    f3 = srcA.createRegularFile("f3");

    RegularFile f4;
    f4 = srcC.createRegularFile("f4");

    Write.byteArray(f1, bytes1);

    Write.byteArray(f2, bytes2);

    Write.byteArray(f3, bytes3);

    Write.byteArray(f4, bytes4);

    ResolvedPath srcZipPath;
    srcZipPath = root.resolve("src.zip");

    assertFalse(srcZipPath.exists());

    RegularFile srcZip;
    srcZip = Zip.zip(
        src,

        srcZipPath,

        Zip.recursePaths(),

        files
    );

    assertEquals(srcZip, srcZipPath);

    ZipFile zipFile;
    zipFile = new ZipFile(srcZip.toFile());

    Set<String> names;
    names = new TreeSet<String>();

    try {
      Enumeration<? extends ZipEntry> entries;
      entries = zipFile.entries();

      while (entries.hasMoreElements()) {
        ZipEntry entry;
        entry = entries.nextElement();

        String entryName;
        entryName = entry.getName();

        names.add(entryName);
      }
    } finally {
      zipFile.close();
    }

    assertEquals(names.size(), 7);
    assertTrue(names.contains("A/"));
    assertTrue(names.contains("A/C/"));
    assertTrue(names.contains("A/C/f4"));
    assertTrue(names.contains("A/f3"));
    assertTrue(names.contains("B/"));
    assertTrue(names.contains("f1"));
    assertTrue(names.contains("f2"));

    Directory dest;
    dest = root.createDirectory("foo");

    assertTrue(dest.isEmpty());

    Unzip.unzip(dest, srcZip);

    Directory destA;
    destA = dest.getDirectory("A");

    Directory destC;
    destC = destA.getDirectory("C");

    f1 = dest.getRegularFile("f1");

    f2 = dest.getRegularFile("f2");

    f3 = destA.getRegularFile("f3");

    f4 = destC.getRegularFile("f4");

    assertEquals(Read.byteArray(f1), bytes1);

    assertEquals(Read.byteArray(f2), bytes2);

    assertEquals(Read.byteArray(f3), bytes3);

    assertEquals(Read.byteArray(f4), bytes4);
  }

}