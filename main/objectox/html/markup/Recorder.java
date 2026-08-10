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
package objectox.html.markup;

import objectos.html.AttributeName;
import objectos.html.ElementName;
import objectos.html.Html;
import objectos.html.markup.ElementValue;
import objectos.html.markup.Fragment0;
import objectos.html.markup.Fragment1;
import objectos.html.markup.Fragment2;
import objectos.html.markup.Fragment3;
import objectos.html.markup.Fragment4;
import objectos.html.markup.Instruction;
import objectox.html.AmbiguousName;

public final class Recorder {

  private final DoctypeRecorder doctypeRecorder;

  private final DocumentCompiler documentCompiler;

  private final ElementRecorder elementRecorder;

  private final FlattenRecorder flattenRecorder;

  private final FragmentRecorder fragmentRecorder;

  private final RawRecorder rawRecorder;

  private final TextRecorder textRecorder;

  Recorder(
      DoctypeRecorder doctypeRecorder,

      DocumentCompiler documentCompiler,

      ElementRecorder elementRecorder,

      FlattenRecorder flattenRecorder,

      FragmentRecorder fragmentRecorder,

      RawRecorder rawRecorder,

      TextRecorder textRecorder
  ) {
    this.doctypeRecorder = doctypeRecorder;

    this.documentCompiler = documentCompiler;

    this.elementRecorder = elementRecorder;

    this.flattenRecorder = flattenRecorder;

    this.fragmentRecorder = fragmentRecorder;

    this.rawRecorder = rawRecorder;

    this.textRecorder = textRecorder;
  }

  public static Recorder create() {
    return of(new HtmlSink());
  }

  public static Recorder of(HtmlSink sink) {
    final AttributeRecorder attributeRecorder;
    attributeRecorder = new AttributeRecorder(sink);

    final BooleanAttributeRecorder booleanAttributeRecorder;
    booleanAttributeRecorder = new BooleanAttributeRecorder(sink);

    return new Recorder(
        new DoctypeRecorder(sink),

        new DocumentCompiler(sink),

        new ElementRecorder(sink, contentsPrecorder, contentsRecorder),

        new FlattenRecorder(sink, contentsPrecorder, contentsRecorder),

        new FragmentRecorder(sink),

        new RawRecorder(sink),

        new TextRecorder(sink)
    );
  }

  public final Html compile() {
    return documentCompiler.compile();
  }

  public final void doctype() {
    doctypeRecorder.record();
  }

  public final ElementPointer element(ElementName name, ElementValue... contents) {
    return elementRecorder.record(name, contents);
  }

  public final FlattenPointer flatten(Instruction... contents) {
    return flattenRecorder.record(contents);
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
