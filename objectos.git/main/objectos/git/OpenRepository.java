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
import objectos.fs.DirectoryContentsVisitor;
import objectos.fs.PathNameVisitor;
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.notes.Note1;
import objectos.util.list.GrowableList;
import objectos.util.list.UnmodifiableList;

/**
 * Opens a Git repository.
 *
 * @since 1
 */
final class OpenRepository extends AbstractGitEngineTask {

  private static final Note1<Directory> ESTART;

  private static final Note1<Repository> ESUCCESS;

  static {
    Class<?> source;
    source = OpenRepository.class;

    ESTART = Note1.debug(source, "Start");

    ESUCCESS = Note1.debug(source, "Success");
  }

  private static final byte _MAYBE_PACKS = 1;

  private static final byte _MAYBE_WORK_TREE = 2;

  private static final byte _NEXT_PACK_FILE = 3;

  private static final byte _PACK_FILE = 4;

  private static final byte _PACK_NAMES = 5;

  private static final byte IO_BARE = 1;

  private static final int PACK_NAME_LENGTH;

  private static final int PACK_PREFIX_LENGTH;

  static {
    PACK_PREFIX_LENGTH = "pack-".length();

    PACK_NAME_LENGTH = PACK_PREFIX_LENGTH + ObjectId.CHAR_LENGTH + ".pack".length();
  }

  private Directory directory;

  private Directory gitDirectory;

  private GrowableList<String> maybePackNames;

  private final MaybePackNamesGetter maybePackNamesGetter = new MaybePackNamesGetter();

  private Directory objectsDirectory;

  private GrowableList<PackFile> packFiles;

  private GrowableList<ObjectId> packIds;

  private int packIndex;

  OpenRepository(GitInjector injector) {
    super(injector);

  }

  public final void setInput(Directory directory) {
    checkSetInput();

    this.directory = directory;
  }

  @Override
  final byte execute(byte state) {
    switch (state) {
      case _MAYBE_PACKS:
        return executeMaybePacks();
      case _MAYBE_WORK_TREE:
        return executeMaybeWorkTree();
      case _NEXT_PACK_FILE:
        return executeNextPackFile();
      case _PACK_FILE:
        return executePackFile();
      case _PACK_NAMES:
        return executePackNames();
      default:
        return super.execute(state);
    }
  }

  @Override
  final byte executeFinally() {
    directory = null;

    gitDirectory = null;

    objectsDirectory = null;

    maybePackNames = injector.putGrowableList(maybePackNames);

    packIndex = 0;

    packFiles = injector.putGrowableList(packFiles);

    packIds = injector.putGrowableList(packIds);

    return super.executeFinally();
  }

  @Override
  final void executeIo(byte ioTask) throws IOException {
    switch (ioTask) {
      case IO_BARE:
        ioBare();
        break;
      default:
        super.executeIo(ioTask);
        break;
    }
  }

  @Override
  final byte executeStart() {
    super.executeStart();

    maybePackNames = injector.getGrowableList();

    packFiles = injector.getGrowableList();

    packIds = injector.getGrowableList();

    send(ESTART, directory);

    return toIo(IO_BARE, _MAYBE_PACKS, _FINALLY);
  }

  final void setObjectsDirectory(Directory directory) throws IOException {
    objectsDirectory = directory;

    ResolvedPath maybePackDirectory;
    maybePackDirectory = objectsDirectory.resolve("pack");

    maybePackDirectory.acceptPathNameVisitor(MaybeObjectsPack.INSTANCE, this);
  }

  final void setObjectsPackDirectory(Directory directory) throws IOException {
    directory.visitContents(maybePackNamesGetter);
  }

  private byte executeMaybePacks() {
    for (int i = 0, size = maybePackNames.size(); i < size; i++) {
      String maybe;
      maybe = maybePackNames.get(i);

      int length;
      length = maybe.length();

      if (length != PACK_NAME_LENGTH) {
        continue;
      }

      boolean packSuffix;
      packSuffix = true
          && maybe.charAt(--length) == 'k'
          && maybe.charAt(--length) == 'c'
          && maybe.charAt(--length) == 'a'
          && maybe.charAt(--length) == 'p';

      if (!packSuffix) {
        continue;
      }

      try {
        ObjectId oid;
        oid = ObjectId.parse(maybe, PACK_PREFIX_LENGTH);

        packIds.add(oid);
      } catch (InvalidObjectIdFormatException e) {
        // do not add
      }
    }

    if (packIds.size() > 0) {
      return executeNextPackFile();
    } else {
      return executeNewResult();
    }
  }

  private byte executeMaybeWorkTree() {
    throw new UnsupportedOperationException("Implement me");
  }

  private byte executeNewResult() {
    UnmodifiableList<PackFile> packs;
    packs = packFiles.toUnmodifiableList();

    Repository result;
    result = new Repository(gitDirectory, objectsDirectory, packs);

    setResult(result);

    send(ESUCCESS, result);

    return _FINALLY;
  }

  private byte executeNextPackFile() {
    if (packIndex < packIds.size()) {
      ObjectId name;
      name = packIds.get(packIndex);

      packIndex++;

      OpenPackFile sub;
      sub = injector.getOpenPackFile();

      sub.set(objectsDirectory, name);

      return toSubTask(sub, _PACK_FILE);
    } else {
      return executeNewResult();
    }
  }

  private byte executePackFile() {
    PackFile packFile;
    packFile = getSubTaskResult();

    packFiles.add(packFile);

    return execute(_NEXT_PACK_FILE);
  }

  private byte executePackNames() {
    packIds = getSubTaskResult();

    packIndex = 0;

    return executeNextPackFile();
  }

  private void ioBare() throws IOException {
    gitDirectory = directory;

    ResolvedPath maybeObjects;
    maybeObjects = directory.resolve("objects");

    maybeObjects.acceptPathNameVisitor(MaybeBare.INSTANCE, this);
  }

  private enum MaybeBare implements PathNameVisitor<Void, OpenRepository> {

    INSTANCE;

    @Override
    public final Void visitDirectory(Directory directory, OpenRepository p) throws IOException {
      p.setObjectsDirectory(directory);

      return null;
    }

    @Override
    public final Void visitNotFound(ResolvedPath notFound, OpenRepository p) throws IOException {
      p.state = _MAYBE_WORK_TREE;

      return null;
    }

    @Override
    public final Void visitRegularFile(RegularFile file, OpenRepository p) throws IOException {
      p.toIoException(file.getPath() + " is a regular file");

      return null;
    }

  }

  private enum MaybeObjectsPack implements PathNameVisitor<Void, OpenRepository> {

    INSTANCE;

    @Override
    public final Void visitDirectory(Directory directory, OpenRepository p) throws IOException {
      p.setObjectsPackDirectory(directory);

      return null;
    }

    @Override
    public final Void visitNotFound(ResolvedPath notFound, OpenRepository p) throws IOException {
      // noop

      return null;
    }

    @Override
    public final Void visitRegularFile(RegularFile file, OpenRepository p) throws IOException {
      p.toIoException(file.getPath() + " is a regular file");

      return null;
    }

  }

  private class MaybePackNamesGetter implements DirectoryContentsVisitor {

    @Override
    public final void visitDirectory(Directory directory) throws IOException {}

    @Override
    public final void visitRegularFile(RegularFile file) throws IOException {
      String name;
      name = file.getName();

      maybePackNames.add(name);
    }

  }

}