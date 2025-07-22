/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import java.util.List;

final class TomlName {

  // kind
  static final byte $KEY = 1;
  static final byte $HEADER = 2;
  static final byte $ARRAY = 3;

  // (current) part type

  static final byte $BARE = 1;
  static final byte $SINGLE = 2;
  static final byte $DOUBLE = 3;

  // list

  private final byte kind;

  @SuppressWarnings("unused")
  private byte current;

  private Object value;

  TomlName(byte kind) {
    this.kind = kind;
  }

  static TomlName bare() {
    return new TomlName($KEY);
  }

  static TomlName bareHeader() {
    return new TomlName($HEADER);
  }

  @SuppressWarnings("unchecked")
  final void append(String part) {
    final List<String> list;

    if (value == null) {
      list = new UtilList<>();

      value = list;
    } else {
      list = (List<String>) value;
    }

    list.add(part);
  }

  final boolean isArray() {
    return kind == $ARRAY;
  }

  final boolean isKey() {
    return kind == $KEY;
  }

  final boolean isHeader() {
    return kind == $HEADER;
  }

}