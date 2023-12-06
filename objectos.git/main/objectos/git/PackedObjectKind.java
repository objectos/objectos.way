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

enum PackedObjectKind {

  BLOB(3),

  COMMIT(1),

  OFS_DELTA(6),

  REF_DELTA(7),

  TAG(4),

  TREE(2);

  final byte value;

  private PackedObjectKind(int value) {
    this.value = (byte) value;
  }

  static PackedObjectKind ofByte(byte type) {
    switch (type) {
      case 1:
        return COMMIT;
      case 2:
        return TREE;
      case 3:
        return BLOB;
      case 4:
        return TAG;
      case 6:
        return OFS_DELTA;
      case 7:
        return REF_DELTA;
      default:
        throw new IllegalArgumentException("Invalid type: " + type);
    }
  }

  final boolean isNonDeltified() {
    switch (this) {
      case BLOB:
      case COMMIT:
      case TAG:
      case TREE:
        return true;
      default:
        return false;
    }
  }

  final boolean matches(ObjectKind expectedKind) {
    switch (this) {
      case BLOB:
        return expectedKind == ObjectKind.BLOB;
      case COMMIT:
        return expectedKind == ObjectKind.COMMIT;
      case TREE:
        return expectedKind == ObjectKind.TREE;
      default:
        throw new IllegalArgumentException("Invalid type: " + name());
    }
  }

  final ObjectKind toObjectKind() {
    switch (this) {
      case BLOB:
        return ObjectKind.BLOB;
      case COMMIT:
        return ObjectKind.COMMIT;
      case TREE:
        return ObjectKind.TREE;
      default:
        throw new IllegalArgumentException("Invalid type: " + name());
    }
  }

}