/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.git;

import java.io.IOException;
import objectos.fs.Directory;
import objectos.fs.NotDirectoryException;
import objectos.fs.NotRegularFileException;
import objectos.fs.PathNameVisitor;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;

final class MaterializeEntry extends AbstractGitEngineTask {

  static final byte _BLOB = 1;

  static final byte _TO_IO_BLOB = 2;

  static final byte _TREE = 3;

  static final byte IO_BLOB = 1;

  static final byte IO_TREE = 2;

  private Blob blob;

  private Entry entry;

  private RegularFile file;

  private ObjectReader objectReader;

  private Repository repository;

  private Directory target;

  private Directory tree;

  MaterializeEntry(GitInjector injector) {
    super(injector);
  }

  public final void setInput(Repository repository, Entry entry, Directory target) {
    checkSetInput();

    this.repository = repository;

    this.entry = entry;

    this.target = target;
  }

  @Override
  byte execute(byte state) {
    switch (state) {
      case _BLOB:
        return executeBlob();
      case _TO_IO_BLOB:
        return executeToIoBlob();
      case _TREE:
        return executeTree();
      default:
        return super.execute(state);
    }
  }

  @Override
  byte executeFinally() {
    blob = null;

    entry = null;

    objectReader = injector.putObjectReader(objectReader);

    repository = null;

    target = null;

    tree = null;

    return super.executeFinally();
  }

  @Override
  final void executeIo(byte task) throws IOException {
    switch (task) {
      case IO_BLOB:
        ioBlob();
        break;
      case IO_TREE:
        ioTree();
        break;
      default:
        super.executeIo(task);
        break;
    }
  }

  @Override
  final byte executeStart() {
    super.executeStart();

    if (entry.isTree()) {
      return toIo(IO_TREE, _TREE, _FINALLY);
    } else {
      objectReader = injector.getObjectReader();

      ReadBlob readBlob;
      readBlob = injector.getReadBlob();

      ObjectId id;
      id = entry.getObjectId();

      readBlob.set(repository, id);

      objectReader.set(readBlob);

      return toSubTask(objectReader, _TO_IO_BLOB);
    }
  }

  final void setTree(Directory directory) {
    this.tree = directory;
  }

  final void writeBlobNotFound(ResolvedPath notFound) throws IOException {
    RegularFile file;
    file = entry.createRegularFile(notFound);

    writeBlobRegularFile(file);
  }

  final void writeBlobRegularFile(RegularFile file) throws IOException {
    blob.writeTo(file);

    this.file = file;
  }

  final void writeTreeNotFound(ResolvedPath notFound) throws IOException {
    Directory directory;
    directory = notFound.createDirectory();

    setTree(directory);
  }

  private byte executeBlob() {
    setResult(
        new MaterializedBlob(file)
    );

    return _FINALLY;
  }

  private byte executeToIoBlob() {
    blob = getSubTaskResult();

    return toIo(IO_BLOB, _BLOB, _FINALLY);
  }

  private byte executeTree() {
    setResult(
        new MaterializedTree(tree, entry.name, entry.object)
    );

    return _FINALLY;
  }

  private void ioBlob() throws IOException {
    String name;
    name = entry.getName();

    ResolvedPath path;
    path = target.resolve(name);

    path.acceptPathNameVisitor(ResolveBlob.INSTANCE, this);
  }

  private void ioTree() throws IOException {
    String name;
    name = entry.getName();

    ResolvedPath path;
    path = target.resolve(name);

    path.acceptPathNameVisitor(ResolveTree.INSTANCE, this);
  }

  private enum ResolveBlob implements PathNameVisitor<Void, MaterializeEntry> {
    INSTANCE;

    @Override
    public final Void visitDirectory(Directory directory, MaterializeEntry p) throws IOException {
      p.catchThrowable(
          new NotRegularFileException(directory)
      );

      return null;
    }

    @Override
    public final Void visitNotFound(ResolvedPath notFound, MaterializeEntry p) throws IOException {
      p.writeBlobNotFound(notFound);

      return null;
    }

    @Override
    public final Void visitRegularFile(RegularFile file, MaterializeEntry p) throws IOException {
      p.writeBlobRegularFile(file);

      return null;
    }
  }

  private enum ResolveTree implements PathNameVisitor<Void, MaterializeEntry> {
    INSTANCE;

    @Override
    public final Void visitDirectory(Directory directory, MaterializeEntry p) throws IOException {
      p.setTree(directory);

      return null;
    }

    @Override
    public final Void visitNotFound(ResolvedPath notFound, MaterializeEntry p) throws IOException {
      p.writeTreeNotFound(notFound);

      return null;
    }

    @Override
    public final Void visitRegularFile(RegularFile file, MaterializeEntry p) throws IOException {
      p.catchThrowable(
          new NotDirectoryException(file)
      );

      return null;
    }
  }

}