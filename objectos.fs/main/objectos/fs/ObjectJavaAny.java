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

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import objectos.core.io.Copy;
import objectos.lang.object.ToString;

abstract class ObjectJavaAny
    implements
    Directory,
    RegularFile,
    ResolvedPath {

  private Directory parent;

  ObjectJavaAny() {}

  @Override
  public final int compareTo(PathName o) {
    if (o == null) {
      return 1;
    }

    String thisPath;
    thisPath = getPath();

    String thatPath;
    thatPath = o.getPath();

    return thisPath.compareTo(thatPath);
  }

  @Override
  public final void copyTo(final Directory dest) throws IOException {
    visitContents(new DirectoryContentsVisitor() {
      @Override
      public final void visitDirectory(Directory directory) throws IOException {
        String directoryName;
        directoryName = directory.getName();

        Directory subdir;
        subdir = dest.createDirectory(directoryName);

        directory.copyTo(subdir);
      }

      @Override
      public final void visitRegularFile(RegularFile file) throws IOException {
        String fileName;
        fileName = file.getName();

        RegularFile destFile;
        destFile = dest.createRegularFile(fileName);

        Copy.sources(file, destFile);
      }
    });
  }

  @Override
  public final Directory createDirectory(String directoryName) throws FoundException, IOException {
    ObjectJavaAny object;
    object = resolveChild0(directoryName, "directoryName");

    return object.createDirectory();
  }

  @Override
  public final RegularFile createRegularFile(
                                             String fileName, RegularFileCreateOption... options) throws IOException {
    ObjectJavaAny object;
    object = resolveChild0(fileName, "fileName");

    return object.createRegularFile(options);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this
        || obj instanceof ObjectJavaAny && equals0((ObjectJavaAny) obj);
  }

  @Override
  public final void formatToString(StringBuilder sb, int depth) {
    ToString.format(
        sb, depth, this,
        "", getPath()
    );
  }

  @Override
  public final Directory getDirectory(String directoryName)
                                                            throws NotFoundException, NotDirectoryException, IOException {
    ObjectJavaAny object;
    object = resolveChild0(directoryName, "directoryName");

    return object.toDirectory();
  }

  @Override
  public final RegularFile getOrCreateRegularFile(String fileName) throws IOException {
    ObjectJavaAny object;
    object = resolveChild0(fileName, "fileName");

    if (object.exists()) {
      return object.toRegularFile();
    } else {
      return object.createRegularFile();
    }
  }

  @Override
  public final Directory getParent() throws NotFoundException {
    if (parent == null) {
      parent = getParent0();
    }

    return parent;
  }

  @Override
  public final String getPath() {
    Object delegate;
    delegate = getDelegate();

    return delegate.toString();
  }

  @Override
  public final RegularFile getRegularFile(String fileName)
                                                           throws NotFoundException, NotRegularFileException, IOException {
    ObjectJavaAny object;
    object = resolveChild0(fileName, "fileName");

    return object.toRegularFile();
  }

  @Override
  public final int hashCode() {
    Object delegate;
    delegate = getDelegate();

    return delegate.hashCode();
  }

  @Override
  public final boolean isEmpty() {
    File file;
    file = toFile();

    String[] list;
    list = file.list();

    return list.length == 0;
  }

  @Override
  public final ResolvedPath resolve(String firstName, String... rest) {
    return resolve0(firstName, rest);
  }

  @Override
  public final Directory toDirectory() throws IOException {
    return acceptPathNameVisitor(ToDirectory.INSTANCE, null);
  }

  @Override
  public final Directory toDirectoryCreateIfNotFound() throws NotDirectoryException, IOException {
    return acceptPathNameVisitor(ToDirectoryCreateIfNotFound.INSTANCE, null);
  }

  @Override
  public final RegularFile toRegularFile() throws IOException {
    return acceptPathNameVisitor(ToRegularFile.INSTANCE, null);
  }

  /**
   * Returns a string representation of this file system object.
   */
  @Override
  public final String toString() {
    return ToString.of(this);
  }

  /**
   * Truncates this regular file. The size of this regular file will be zero
   * after this method returns.
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  @Override
  public final void truncate() throws IOException {
    try (FileChannel channel = openWriteChannel()) {
      channel.truncate(0);
    }
  }

  abstract boolean equals0(ObjectJavaAny that);

  abstract Object getDelegate();

  abstract Directory getParent0() throws NotFoundException;

  abstract ObjectJavaAny resolve0(String firstName, String[] rest);

  abstract ObjectJavaAny resolveChild0(String value, String variableName);

}