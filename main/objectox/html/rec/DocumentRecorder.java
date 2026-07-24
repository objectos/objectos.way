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

import objectos.html.AttributeName;
import objectos.html.ElementName;
import objectos.html.rec.Fragment0;
import objectos.html.rec.Fragment1;
import objectos.html.rec.Fragment2;
import objectos.html.rec.Fragment3;
import objectos.html.rec.Fragment4;
import objectos.html.rec.Instruction;

final class DocumentRecorder {

  private final AttributeRecorder attributeRecorder;

  private final BooleanAttributeRecorder booleanAttributeRecorder;

  private final ElementRecorder elementRecorder;

  private final FragmentRecorder fragmentRecorder;

  private final RawRecorder rawRecorder;

  private final TextRecorder textRecorder;

  DocumentRecorder(
      AttributeRecorder attributeRecorder,

      BooleanAttributeRecorder booleanAttributeRecorder,

      ElementRecorder elementRecorder,

      FragmentRecorder fragmentRecorder,

      RawRecorder rawRecorder,

      TextRecorder textRecorder
  ) {
    this.attributeRecorder = attributeRecorder;

    this.booleanAttributeRecorder = booleanAttributeRecorder;

    this.elementRecorder = elementRecorder;

    this.fragmentRecorder = fragmentRecorder;

    this.rawRecorder = rawRecorder;

    this.textRecorder = textRecorder;
  }

  public static DocumentRecorder of(HtmlSink sink) {
    return new DocumentRecorder(
        new AttributeRecorder(sink),

        new BooleanAttributeRecorder(sink),

        new ElementRecorder(sink),

        new FragmentRecorder(sink),

        new RawRecorder(sink),

        new TextRecorder(sink)
    );
  }

  public final AttributePointer attribute(AttributeName name) {
    return booleanAttributeRecorder.record(name);
  }

  public final AttributePointer attribute(AttributeName name, String value) {
    return attributeRecorder.record(name, value);
  }

  public final ElementPointer element(ElementName name, Instruction... contents) {
    return elementRecorder.record(name, contents);
  }

  public final FragmentPointer fragment(Fragment0 fragment) {
    return fragmentRecorder.record(fragment);
  }

  public final <T1> FragmentPointer fragment(Fragment1<T1> fragment, T1 arg1) {
    return fragmentRecorder.record(fragment, arg1);
  }

  public final <T1, T2> FragmentPointer fragment(Fragment2<T1, T2> fragment, T1 arg1, T2 arg2) {
    return fragmentRecorder.record(fragment, arg1, arg2);
  }

  public final <T1, T2, T3> FragmentPointer fragment(Fragment3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3) {
    return fragmentRecorder.record(fragment, arg1, arg2, arg3);
  }

  public final <T1, T2, T3, T4> FragmentPointer fragment(Fragment4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
    return fragmentRecorder.record(fragment, arg1, arg2, arg3, arg4);
  }

  public final RawPointer raw(String value) {
    return rawRecorder.record(value);
  }

  public final TextPointer text(String value) {
    return textRecorder.record(value);
  }

}
