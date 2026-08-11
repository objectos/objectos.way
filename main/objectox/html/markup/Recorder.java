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

import objectos.html.ElementName;
import objectos.html.markup.ElementValue;
import objectox.html.HtmlPojo;

public final class Recorder {

  private final DoctypeRecorder doctypeRecorder;

  private final DocumentCompiler documentCompiler;

  private final ElementRecorder elementRecorder;

  Recorder(
      DoctypeRecorder doctypeRecorder,

      DocumentCompiler documentCompiler,

      ElementRecorder elementRecorder
  ) {
    this.doctypeRecorder = doctypeRecorder;

    this.documentCompiler = documentCompiler;

    this.elementRecorder = elementRecorder;
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

        new ElementRecorder(
            sink,

            new ElementAttributesRecorder(
                attributeRecorder,

                booleanAttributeRecorder
            ),

            new ElementChildrenRecorder(
                new ChildAmbiguousRecorder(sink),

                new ChildElementMarker(sink),

                new ChildElementRecorder(sink),

                new ChildFragmentRecorder(sink),

                new ChildRawRecorder(sink),

                new ChildTextRecorder(sink)
            )
        )
    );
  }

  public final HtmlPojo compile() {
    return documentCompiler.compile();
  }

  public final void doctype() {
    doctypeRecorder.record();
  }

  public final ElementPointer element(ElementName name, ElementValue... contents) {
    return elementRecorder.record(name, contents);
  }

}
