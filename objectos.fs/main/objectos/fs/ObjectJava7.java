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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.util.Iterator;
import objectos.lang.object.Check;

abstract class ObjectJava7 extends ObjectJavaAny {

  private static final OpenOption[] OPEN_READ = new OpenOption[] {
      StandardOpenOption.READ
  };

  private static final OpenOption[] OPEN_READ_AND_WRITE = new OpenOption[] {
      StandardOpenOption.READ, StandardOpenOption.WRITE
  };

  private static final OpenOption[] OPEN_WRITE_OPTIONS = new OpenOption[] {
      StandardOpenOption.WRITE, StandardOpenOption.APPEND, StandardOpenOption.CREATE
  };

  private final Path delegate;

  ObjectJava7(File file) {
    this(file.toPath());
  }

  ObjectJava7(Path delegate) {
    this.delegate = delegate;
  }

  @Override
  public final <R, P> R acceptPathNameVisitor(PathNameVisitor<R, P> visitor, P p) throws IOException {
    Check.notNull(visitor, "visitor == null");

    try {
      BasicFileAttributes attributes;
      attributes = Files.readAttributes(delegate, BasicFileAttributes.class);

      if (attributes.isDirectory()) {
        return visitor.visitDirectory(this, p);
      }

      if (attributes.isRegularFile()) {
        return visitor.visitRegularFile(this, p);
      }

      throw new IOException("Not a directory nor a regular file");
    } catch (NoSuchFileException notFound) {
      return visitor.visitNotFound(this, p);
    }
  }

  @Override
  public final Directory createDirectory() throws FoundException, IOException {
    try {
      Files.createDirectory(delegate);

      return this;
    } catch (FileAlreadyExistsException e) {
      throw new FoundException(e);
    }
  }

  @Override
  public final ResolvedPath createParents() throws IOException {
    Path parent;
    parent = delegate.getParent();

    if (parent != null) {
      Files.createDirectories(parent);
    }

    return this;
  }

  @Override
  public final RegularFile createRegularFile(RegularFileCreateOption... options) throws IOException {
    Check.notNull(options, "options == null");

    FileAttribute<?>[] attributes;

    switch (options.length) {
      case 0:
        attributes = PosixJava7.EMPTY_FILE_ATTRIBUTES;
        break;
      case 1:
        RegularFileCreateOption option;
        option = options[0];

        attributes = (FileAttribute<?>[]) option.createRegularFileGetValue();
        break;
      default:
        OperatingSystem os;
        os = OperatingSystem.get();

        FileAttributeBuilder builder;
        builder = FileAttributeBuilder.create(os);

        for (RegularFileCreateOption o : options) {
          o.createRegularFileSetValue(builder);
        }

        attributes = builder.build();
        break;
    }

    try {
      Files.createFile(delegate, attributes);

      return this;
    } catch (FileAlreadyExistsException e) {
      throw new FoundException(e);
    }
  }

  @Override
  public final void delete() throws IOException {
    Files.delete(delegate);
  }

  @Override
  public final void deleteContents() throws IOException {
    deleteContents0(delegate);
  }

  @Override
  public final boolean exists() {
    return Files.exists(delegate);
  }

  @Override
  public final long getLastModifiedMillis() throws IOException {
    FileTime time;
    time = Files.getLastModifiedTime(delegate);

    return time.toMillis();
  }

  @Override
  public final String getName() {
    Path fileName;
    fileName = delegate.getFileName();

    return fileName.toString();
  }

  @Override
  public final boolean is(RegularFileIsOption option) throws IOException {
    Check.notNull(option, "option == null");

    return option.is(delegate);
  }

  @Override
  public final InputStream openInputStream() throws IOException {
    return Files.newInputStream(delegate, OPEN_READ);
  }

  @Override
  public final OutputStream openOutputStream() throws IOException {
    return Files.newOutputStream(delegate, OPEN_WRITE_OPTIONS);
  }

  @Override
  public final FileChannel openReadAndWriteChannel() throws IOException {
    return FileChannel.open(delegate, OPEN_READ_AND_WRITE);
  }

  @Override
  public final FileChannel openReadChannel() throws IOException {
    return FileChannel.open(delegate, OPEN_READ);
  }

  @Override
  public final Reader openReader(Charset charset) throws IOException {
    return Files.newBufferedReader(delegate, charset);
  }

  @Override
  public final FileChannel openWriteChannel() throws IOException {
    return FileChannel.open(delegate, OPEN_WRITE_OPTIONS);
  }

  @Override
  public final Writer openWriter(Charset charset) throws IOException {
    return Files.newBufferedWriter(delegate, charset, OPEN_WRITE_OPTIONS);
  }

  @Override
  public final WatchKey register(WatchService service, Kind<?>... events) throws IOException {
    return delegate.register(service, events);
  }

  @Override
  public final ResolvedPath resolve(Path pathName) {
    Check.notNull(pathName, "pathName == null");

    Check.argument(!pathName.isAbsolute(), "pathName is absolute");

    Path resolved;
    resolved = delegate.resolve(pathName);

    return newDescendant(resolved);
  }

  @Override
  public final long size() throws IOException {
    try {
      return Files.size(delegate);
    } catch (NoSuchFileException e) {
      throw new NotRegularFileException(e);
    }
  }

  @Override
  public final File toFile() {
    return delegate.toFile();
  }

  @Override
  public final Path toPath() {
    return delegate;
  }

  @Override
  public final URI toUri() {
    return delegate.toUri();
  }

  @Override
  public final void visitContents(DirectoryContentsVisitor visitor) throws IOException {
    Check.notNull(visitor, "visitor == null");

    try (DirectoryStream<Path> entries = Files.newDirectoryStream(delegate)) {
      for (Path entry : entries) {
        ObjectJava7 object;
        object = new ObjectImpl(entry);

        object.acceptPathNameVisitor(DirectoryContentsVisitorAdapter.INSTANCE, visitor);
      }
    }
  }

  @Override
  final boolean equals0(ObjectJavaAny that) {
    return that instanceof ObjectJava7 && equalsImpl((ObjectJava7) that);
  }

  @Override
  final Path getDelegate() {
    return delegate;
  }

  @Override
  final Directory getParent0() throws NotFoundException {
    Path parent;
    parent = delegate.getParent();

    if (parent == null) {
      throw new NotFoundException("No parent directory for " + delegate);
    }

    return new ObjectImpl(parent);
  }

  @Override
  final ObjectJavaAny resolve0(String firstName, String[] rest) {
    Check.notNull(firstName, "firstName == null");

    Path path;
    path = Paths.get(firstName, rest);

    Check.argument(!path.isAbsolute(), "pathName is absolute");

    Path resolved;
    resolved = delegate.resolve(path);

    return newDescendant(resolved);
  }

  @Override
  final ObjectJavaAny resolveChild0(String fileName, String variableName) {
    Check.notNull(fileName, variableName, " == null");

    Check.argument(!fileName.equals(""), variableName, " is empty");

    Path path;
    path = Paths.get(fileName);

    Check.argument(!path.isAbsolute(), variableName, " is absolute");

    Path resolved;
    resolved = delegate.resolve(path);

    resolved = resolved.normalize();

    Path parent;
    parent = resolved.getParent();

    Check.argument(
        delegate.equals(parent), variableName,
        " does not resolve to a child of this directory"
    );

    return new ObjectImpl(resolved);
  }

  private void clear1(Path path) throws IOException {
    if (Files.isDirectory(path)) {
      deleteContents0(path);
    }

    Files.delete(path);
  }

  private void deleteContents0(Path path) throws IOException {
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
      Iterator<Path> iterator;
      iterator = stream.iterator();

      while (iterator.hasNext()) {
        Path next;
        next = iterator.next();

        clear1(next);
      }
    }
  }

  private boolean equalsImpl(ObjectJava7 that) {
    Path thisPath;
    thisPath = delegate;

    Path thatPath;
    thatPath = that.delegate;

    Path thisNormalized;
    thisNormalized = thisPath.normalize();

    Path thatNormalized;
    thatNormalized = thatPath.normalize();

    return thisNormalized.equals(thatNormalized);
  }

  private ObjectJavaAny newDescendant(Path resolved) {
    resolved = resolved.normalize();

    Check.argument(resolved.startsWith(delegate), "pathName is not a descendant");

    return new ObjectImpl(resolved);
  }

}