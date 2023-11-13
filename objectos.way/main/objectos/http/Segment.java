/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.http;

import objectos.lang.object.Check;
import objectox.http.SegmentExact;
import objectox.http.SegmentKind;

public sealed interface Segment permits SegmentExact, SegmentKind {

  static Segment of() {
    return SegmentExact.EMPTY;
  }

  static Segment of(String value) {
    Check.notNull(value, "value == null");

    for (int i = 0, len = value.length(); i < len; i++) {
      char c;
      c = value.charAt(i);

      switch (c) {
        case '/', ' ' -> {
          throw new IllegalArgumentException("Segment must not contain the '" + c + "' character");
        }
      }
    }

    return new SegmentExact(value);
  }

  static Segment ofAny() {
    return SegmentKind.ALWAYS_TRUE;
  }

}