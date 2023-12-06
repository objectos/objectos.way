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

enum ObjectKind {

  BLOB(new byte[] {
      0x62, // b
      0x6C, // l
      0x6F, // o
      0x62, // b
      0x20 // SPACE
  }),

  COMMIT(new byte[] {
      0x63, // c
      0x6F, // o
      0x6D, // m
      0x6D, // m
      0x69, // i
      0x74, // t
      0x20 // SPACE
  }),

  TREE(new byte[] {
      0x74, // t
      0x72, // r
      0x65, // e
      0x65, // e
      0x20 // SPACE
  });

  static final byte BLOB_FIRST_BYTE = 0x62; // b

  static final byte COMMIT_FIRST_BYTE = 0x63; // c

  static final byte TREE_FIRST_BYTE = 0x74; // t

  final byte[] headerPrefix;

  private ObjectKind(byte[] headerPrefix) {
    this.headerPrefix = headerPrefix;
  }

}
