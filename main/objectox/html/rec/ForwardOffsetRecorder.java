/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.html.rec;

import objectox.html.ByteArray;

final class ForwardOffsetRecorder {

  private final ByteArray main;

  ForwardOffsetRecorder(ByteArray main) {
    this.main = main;
  }

  public final void two(int startIndex) {
    final int mainIndex;
    mainIndex = main.size();

    int offset;

    // set the end index of the declaration
    offset = mainIndex - startIndex;

    // skip ByteProto.FOO + len0 + len1
    offset -= 3;

    // we skip the first byte proto
    main.setInt16(startIndex + 1, offset);
  }

  public final void three(int startIndex) {
    final int mainIndex;
    mainIndex = main.size();

    int offset;

    // set the end index of the declaration
    offset = mainIndex - startIndex;

    // skip ByteProto.FOO + len0 + len1 + len2
    offset -= 4;

    // we skip the first byte proto
    main.setInt24(startIndex + 1, offset);
  }

}
