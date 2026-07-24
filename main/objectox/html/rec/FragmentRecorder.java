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
import objectos.html.rec.Fragment0;
import objectos.html.rec.Fragment1;
import objectos.html.rec.Fragment2;
import objectos.html.rec.Fragment3;
import objectos.html.rec.Fragment4;

final class FragmentRecorder {

  private final HtmlSink sink;

  FragmentRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final FragmentPointer record(Fragment0 fragment) {
    final Fragment0 f;
    f = Objects.requireNonNull(fragment, "fragment == null");

    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.STARTFRAG);

    f.invoke();

    sink.addByte(HtmlBytes.ENDFRAG);

    return new FragmentPointer(startIndex);
  }

  public final <T1> FragmentPointer record(Fragment1<T1> fragment, T1 arg1) {
    final Fragment1<T1> f;
    f = Objects.requireNonNull(fragment, "fragment == null");

    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.STARTFRAG);

    f.invoke(arg1);

    sink.addByte(HtmlBytes.ENDFRAG);

    return new FragmentPointer(startIndex);
  }

  public final <T1, T2> FragmentPointer record(Fragment2<T1, T2> fragment, T1 arg1, T2 arg2) {
    final Fragment2<T1, T2> f;
    f = Objects.requireNonNull(fragment, "fragment == null");

    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.STARTFRAG);

    f.invoke(arg1, arg2);

    sink.addByte(HtmlBytes.ENDFRAG);

    return new FragmentPointer(startIndex);
  }

  public final <T1, T2, T3> FragmentPointer record(Fragment3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3) {
    final Fragment3<T1, T2, T3> f;
    f = Objects.requireNonNull(fragment, "fragment == null");

    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.STARTFRAG);

    f.invoke(arg1, arg2, arg3);

    sink.addByte(HtmlBytes.ENDFRAG);

    return new FragmentPointer(startIndex);
  }

  public final <T1, T2, T3, T4> FragmentPointer record(Fragment4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
    final Fragment4<T1, T2, T3, T4> f;
    f = Objects.requireNonNull(fragment, "fragment == null");

    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.STARTFRAG);

    f.invoke(arg1, arg2, arg3, arg4);

    sink.addByte(HtmlBytes.ENDFRAG);

    return new FragmentPointer(startIndex);
  }

}
