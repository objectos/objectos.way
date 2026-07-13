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

import java.util.Objects;
import objectos.html.Fragment0;
import objectos.way.Html;
import objectox.html.ByteArray;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlInstruction;

final class FragmentRecorder {

  private final ByteArray main;

  private final ForwardOffsetRecorder forwardOffsetRecorder;

  private final ReverseOffsetRecorder reverseOffsetRecorder;

  FragmentRecorder(ByteArray main, ForwardOffsetRecorder forwardOffsetRecorder, ReverseOffsetRecorder reverseOffsetRecorder) {
    this.main = main;

    this.forwardOffsetRecorder = forwardOffsetRecorder;

    this.reverseOffsetRecorder = reverseOffsetRecorder;
  }

  public final Html.Instruction record(Fragment0 fragment) {
    final Fragment0 f;
    f = Objects.requireNonNull(fragment, "fragment == null");

    final int index;
    index = fragmentBegin();

    f.invoke();

    fragmentEnd(index);

    return HtmlInstruction.FRAGMENT;
  }

  private int fragmentBegin() {
    // we mark:
    // 1) the start of the contents of the current declaration
    final int startIndex;
    startIndex = main.size();

    main.add(
        HtmlByteProto.FRAGMENT,

        // length takes 3 bytes
        HtmlByteProto.NULL,
        HtmlByteProto.NULL,
        HtmlByteProto.NULL
    );

    return startIndex;
  }

  private void fragmentEnd(int startIndex) {
    reverseOffsetRecorder.record(startIndex);

    forwardOffsetRecorder.three(startIndex);
  }

}
