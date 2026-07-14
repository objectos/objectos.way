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
import objectos.html.Fragment1;
import objectos.html.Fragment2;
import objectos.html.Fragment3;
import objectos.html.Fragment4;
import objectos.way.Html;
import objectos.way.Html.Instruction.OfFragment;
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

  public final Html.Instruction.OfFragment record(Fragment0 fragment) {
    final Fragment0 f;
    f = Objects.requireNonNull(fragment, "fragment == null");

    final int index;
    index = fragmentBegin();

    f.invoke();

    fragmentEnd(index);

    return HtmlInstruction.FRAGMENT;
  }

  public final <T1> OfFragment record(Fragment1<T1> fragment, T1 arg1) {
    final Fragment1<T1> f;
    f = Objects.requireNonNull(fragment, "fragment == null");

    final int index;
    index = fragmentBegin();

    f.invoke(arg1);

    fragmentEnd(index);

    return HtmlInstruction.FRAGMENT;
  }

  public final <T1, T2> OfFragment record(Fragment2<T1, T2> fragment, T1 arg1, T2 arg2) {
    final Fragment2<T1, T2> f;
    f = Objects.requireNonNull(fragment, "fragment == null");

    final int index;
    index = fragmentBegin();

    f.invoke(arg1, arg2);

    fragmentEnd(index);

    return HtmlInstruction.FRAGMENT;
  }

  public final <T1, T2, T3> OfFragment record(Fragment3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3) {
    final Fragment3<T1, T2, T3> f;
    f = Objects.requireNonNull(fragment, "fragment == null");

    final int index;
    index = fragmentBegin();

    f.invoke(arg1, arg2, arg3);

    fragmentEnd(index);

    return HtmlInstruction.FRAGMENT;
  }

  public final <T1, T2, T3, T4> OfFragment record(Fragment4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
    final Fragment4<T1, T2, T3, T4> f;
    f = Objects.requireNonNull(fragment, "fragment == null");

    final int index;
    index = fragmentBegin();

    f.invoke(arg1, arg2, arg3, arg4);

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
