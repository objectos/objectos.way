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

import objectos.core.io.Charsets;

/**
 * Represents a tree entry file mode.
 *
 * @since 1
 */
public enum EntryMode {

  /**
   * The file mode of a regular file that is executable.
   */
  EXECUTABLE_FILE("100755"),

  /**
   * The file mode of a regular file.
   */
  REGULAR_FILE("100644"),

  /**
   * The file mode of a tree object (a directory).
   */
  TREE("40000");

  private final byte[] byteArray;

  private EntryMode(String mode) {
    this.byteArray = mode.getBytes(Charsets.utf8());
  }

  static EntryMode fromInt(int mode) throws GitStubException {
    switch (mode) {
      case 0100644:
        return REGULAR_FILE;
      case 0100755:
        return EXECUTABLE_FILE;
      case 040000:
        return TREE;
      default:
        throw new GitStubException("mode = " + Integer.toOctalString(mode));
    }
  }

  /**
   * Returns {@code true} if this mode denotes a blob object.
   *
   * @return {@code true} if this mode denotes a blob object
   */
  public final boolean isBlob() {
    return this == EXECUTABLE_FILE || this == REGULAR_FILE;
  }

  /**
   * Returns {@code true} if this mode denotes a tree object.
   *
   * @return {@code true} if this mode denotes a tree object
   */
  public final boolean isTree() {
    return this == TREE;
  }

  final Entry createEntry(String name, String sha1) throws InvalidObjectIdFormatException {
    ObjectId object;
    object = ObjectId.parse(sha1);

    return new Entry(this, name, object);
  }

  final byte[] getBytes() {
    return byteArray;
  }

  final String printMode() {
    switch (this) {
      case EXECUTABLE_FILE:
        return "100755";
      case REGULAR_FILE:
        return "100644";
      case TREE:
        return "040000";
      default:
        throw new UnsupportedOperationException("Implement me @ " + name());
    }
  }

  final String printType() {
    switch (this) {
      case EXECUTABLE_FILE:
      case REGULAR_FILE:
        return "blob";
      case TREE:
        return "tree";
      default:
        throw new UnsupportedOperationException("Implement me @ " + name());
    }
  }

}