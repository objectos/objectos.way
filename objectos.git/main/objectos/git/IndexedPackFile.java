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

import objectos.fs.RegularFile;
import objectos.lang.object.ToString;

final class IndexedPackFile extends PackFile {

  private final RegularFile indexFile;

  private final int objectCount;

  private final ObjectId objectId;

  private final RegularFile packFile;

  private final long packFileSize;

  private final int version;

  IndexedPackFile(RegularFile indexFile,
                  int objectCount,
                  ObjectId objectId,
                  RegularFile packFile,
                  long packFileSize,
                  int version) {
    this.indexFile = indexFile;
    this.objectCount = objectCount;
    this.objectId = objectId;
    this.packFile = packFile;
    this.packFileSize = packFileSize;
    this.version = version;
  }

  @Override
  public final void formatToString(StringBuilder toString, int level) {
    ToString.format(
        toString, level, this,
        "", objectId
    );
  }

  @Override
  public final int getObjectCount() {
    return objectCount;
  }

  @Override
  public final ObjectId getObjectId() {
    return objectId;
  }

  @Override
  public final long getPackFileSize() {
    return packFileSize;
  }

  @Override
  public final int getVersion() {
    return version;
  }

  @Override
  final RegularFile getIndexFile() {
    return indexFile;
  }

  @Override
  final RegularFile getPackFile() {
    return packFile;
  }

}