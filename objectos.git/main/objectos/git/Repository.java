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
import objectos.fs.RegularFile;
import objectos.fs.ResolvedPath;
import objectos.lang.object.Check;
import objectos.lang.object.ToString;
import objectos.util.list.UnmodifiableList;

/**
 * Represents a Git repository.
 *
 * @since 1
 */
public final class Repository extends GitRepository implements ToString.Formattable {

  private final Directory gitDirectory;

  private final Directory objectsDirectory;

  private final UnmodifiableList<PackFile> packFiles;

  Repository(Directory gitDirectory,
             Directory objectsDirectory,
             UnmodifiableList<PackFile> packFiles) {
    this.gitDirectory = gitDirectory;

    this.objectsDirectory = objectsDirectory;

    this.packFiles = packFiles;
  }

  @Override
  public final void formatToString(StringBuilder sb, int depth) {
    ToString.format(
        sb, depth, this,
        "gitDirectory", gitDirectory,
        "packFiles", packFiles
    );
  }

  /**
   * Returns {@code true} if this repository is a bare repository.
   *
   * @return {@code true} if this repository is a bare repository
   */
  public boolean isBare() {
    return true;
  }

  @Override
  public final String toString() {
    return ToString.of(this);
  }

  final RegularFile createRegularFile(ObjectId objectId) throws IOException {
    String directoryName;
    directoryName = objectId.getDirectoryName();

    ResolvedPath resolved;
    resolved = objectsDirectory.resolve(directoryName);

    Directory sub;
    sub = resolved.toDirectoryCreateIfNotFound();

    String regularFileName;
    regularFileName = objectId.getRegularFileName();

    return sub.createRegularFile(regularFileName);
  }

  final RegularFile getLooseObjectFile(ObjectId objectId) throws IOException {
    String directoryName;
    directoryName = objectId.getDirectoryName();

    Directory sub;
    sub = objectsDirectory.getDirectory(directoryName);

    String regularFileName;
    regularFileName = objectId.getRegularFileName();

    return sub.getRegularFile(regularFileName);
  }

  @Override
  final PackFile getPackFile(int index) {
    return packFiles.get(index);
  }

  @Override
  final int getPackFileCount() {
    return packFiles.size();
  }

  final ResolvedPath resolve(String first, String... more) {
    return objectsDirectory.resolve(first, more);
  }

  final ResolvedPath resolveLoose(RefName ref) throws IOException {
    return ref.resolveLoose(gitDirectory);
  }

  @Override
  final ResolvedPath resolveLooseObject(ObjectId objectId) throws IOException {
    String directoryName;
    directoryName = objectId.getDirectoryName();

    String regularFileName;
    regularFileName = objectId.getRegularFileName();

    return objectsDirectory.resolve(directoryName, regularFileName);
  }

  final ResolvedPath resolvePackedRefs() throws IOException {
    return gitDirectory.resolve("packed-refs");
  }

  final void update(RefName refName, ObjectId value) throws IOException {
    Check.notNull(refName, "refName == null");

    refName.update(gitDirectory, value);
  }

}