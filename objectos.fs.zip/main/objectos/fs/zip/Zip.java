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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import objectos.core.io.Copy;
import objectos.fs.Directory;
import objectos.fs.DirectoryContentsVisitor;
import objectos.fs.PathNameVisitor;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.lang.object.Check;

/**
 * @since 2
 */
public final class Zip implements DirectoryContentsVisitor, ZipOptionVisitor {

  private final byte[] buffer = new byte[8192];

  private final Closeable entryCloseable = new Closeable() {
    @Override
    public final void close() throws IOException {
      zip.closeEntry();
    }
  };

  private String prefix = "";

  private boolean recursePaths;

  private final Directory workingDirectory;

  private final ZipOutputStream zip;

  private final ResolvedPath zipFile;

  Zip(ResolvedPath zipFile, Directory workingDirectory, ZipOutputStream zip) {
    this.zipFile = zipFile;

    this.workingDirectory = workingDirectory;

    this.zip = zip;
  }

  public static ZipOption recursePaths() {
    return RecursePaths.INSTANCE;
  }

  public static RegularFile zip(
                                Directory workingDirectory, ResolvedPath zipFile,
                                ZipOption option,
                                String... files) throws IOException {
    Check.notNull(workingDirectory, "workingDirectory == null");
    Check.notNull(zipFile, "zipFile == null");
    Check.notNull(option, "option == null");
    Check.notNull(files, "files == null");

    Zip zip;
    zip = open(workingDirectory, zipFile);

    option.acceptZipOptionVisitor(zip);

    return zip.execute(files);
  }

  private static Zip open(
                          Directory workingDirectory, ResolvedPath zipFile)
                                                                            throws IOException {
    OutputStream writeStream;
    writeStream = zipFile.openOutputStream();

    ZipOutputStream zip;
    zip = new ZipOutputStream(writeStream);

    return new Zip(zipFile, workingDirectory, zip);
  }

  public final RegularFile execute(String[] files) throws IOException {
    try (zip) {
      execute0(files);
    }

    return zipFile.toRegularFile();
  }

  @Override
  public final void visitDirectory(Directory directory) throws IOException {
    String entryName;
    entryName = prefix + directory.getName() + '/';

    ZipEntry entry;
    entry = new ZipEntry(entryName);

    zip.putNextEntry(entry);

    zip.closeEntry();

    if (!recursePaths) {
      return;
    }

    String previousPrefix;
    previousPrefix = prefix;

    prefix = entryName;

    directory.visitContents(this);

    prefix = previousPrefix;
  }

  @Override
  public final void visitRecursePaths() {
    recursePaths = true;
  }

  @Override
  public final void visitRegularFile(RegularFile file) throws IOException {
    String entryName;
    entryName = prefix + file.getName();

    ZipEntry entry;
    entry = new ZipEntry(entryName);

    zip.putNextEntry(entry);

    try (InputStream in = file.openInputStream(); entryCloseable) {
      Copy.streams(in, zip, buffer);
    }
  }

  private void execute0(String[] files) throws IOException {
    ThisVisitor thisVisitor;
    thisVisitor = new ThisVisitor();

    for (int i = 0, length = files.length; i < length; i++) {
      String fileName;
      fileName = files[i];

      Check.notNull(fileName, "files[", i, "] == null");

      if (fileName.equals(".")) {
        workingDirectory.visitContents(this);

        continue;
      }

      ResolvedPath resolved;
      resolved = workingDirectory.resolve(fileName);

      resolved.acceptPathNameVisitor(thisVisitor, this);
    }
  }

  private static class ThisVisitor implements PathNameVisitor<Void, Zip> {
    @Override
    public final Void visitDirectory(Directory directory, Zip outer) throws IOException {
      outer.visitDirectory(directory);

      return null;
    }

    @Override
    public final Void visitNotFound(ResolvedPath notFound, Zip outer) throws IOException {
      return null;
    }

    @Override
    public final Void visitRegularFile(RegularFile file, Zip outer) throws IOException {
      outer.visitRegularFile(file);

      return null;
    }
  }

}