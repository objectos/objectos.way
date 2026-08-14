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

import objectos.html.markup.AmbiguousMarkup;
import objectos.html.markup.ElementMarkup;
import objectos.html.markup.RegularElementValue;
import objectos.script.JsAction;
import objectox.html.AmbiguousName;
import objectox.html.HtmlPojo;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;

public final class Recorder {

  private final AmbiguousRecorder ambiguousRecorder;

  private final AttributeRecorder attributeRecorder;

  private final BooleanAttributeRecorder booleanAttributeRecorder;

  private final DoctypeRecorder doctypeRecorder;

  private final DocumentCompiler documentCompiler;

  private final FragmentRecorder fragmentRecorder;

  private final RawRecorder rawRecorder;

  private final RegularElementRecorder regularElementRecorder;

  private final TextRecorder textRecorder;

  private final VoidElementRecorder voidElementRecorder;

  Recorder(
      AmbiguousRecorder ambiguousRecorder,
      AttributeRecorder attributeRecorder,
      BooleanAttributeRecorder booleanAttributeRecorder,
      DoctypeRecorder doctypeRecorder,
      DocumentCompiler documentCompiler,
      FragmentRecorder fragmentRecorder,
      RawRecorder rawRecorder,
      RegularElementRecorder regularElementRecorder,
      TextRecorder textRecorder,
      VoidElementRecorder voidElementRecorder
  ) {
    this.ambiguousRecorder = ambiguousRecorder;
    this.attributeRecorder = attributeRecorder;
    this.booleanAttributeRecorder = booleanAttributeRecorder;
    this.doctypeRecorder = doctypeRecorder;
    this.documentCompiler = documentCompiler;
    this.fragmentRecorder = fragmentRecorder;
    this.rawRecorder = rawRecorder;
    this.regularElementRecorder = regularElementRecorder;
    this.textRecorder = textRecorder;
    this.voidElementRecorder = voidElementRecorder;
  }

  public static Recorder create() {
    return of(new HtmlSink());
  }

  public static Recorder of(HtmlSink sink) {
    final AmbiguousAttributeRecorder ambiguousAttributeRecorder;
    ambiguousAttributeRecorder = new AmbiguousAttributeRecorder(sink);

    final ValueRecorder valueRecorder;
    valueRecorder = new ValueRecorder(sink);

    final FragmentAttributeRecorder fragmentAttributeRecorder;
    fragmentAttributeRecorder = new FragmentAttributeRecorder(sink, ambiguousAttributeRecorder, valueRecorder);

    final ElementAttributesRecorder elementAttributesRecorder;
    elementAttributesRecorder = new ElementAttributesRecorder(ambiguousAttributeRecorder, fragmentAttributeRecorder, valueRecorder);

    final AmbiguousChildRecorder ambiguousChildRecorder;
    ambiguousChildRecorder = new AmbiguousChildRecorder(sink);

    final FragmentChildRecorder fragmentChildRecorder;
    fragmentChildRecorder = new FragmentChildRecorder(sink, ambiguousChildRecorder, valueRecorder);

    return new Recorder(
        new AmbiguousRecorder(sink),

        new AttributeRecorder(sink),

        new BooleanAttributeRecorder(sink),

        new DoctypeRecorder(sink),

        new DocumentCompiler(sink),

        new FragmentRecorder(sink),

        new RawRecorder(sink),

        new RegularElementRecorder(
            sink,

            elementAttributesRecorder,

            new ElementChildrenRecorder(
                ambiguousChildRecorder,

                fragmentChildRecorder,

                valueRecorder
            )
        ),

        new TextRecorder(sink),

        new VoidElementRecorder(sink, elementAttributesRecorder)
    );
  }

  public final AmbiguousMarkup ambiguous(AmbiguousName name, String text) {
    return ambiguousRecorder.record(name, text);
  }

  public final AttributePointer attr(AttributeName name, JsAction value) {
    return attributeRecorder.record(name, value);
  }

  public final AttributePointer attr(AttributeName name, String value) {
    return attributeRecorder.record(name, value);
  }

  public final AttributePointer boolAttr(AttributeName name) {
    return booleanAttributeRecorder.record(name);
  }

  public final HtmlPojo compile() {
    return documentCompiler.compile();
  }

  public final void doctype() {
    doctypeRecorder.record();
  }

  public final ElementMarkup elem(ElementName name, RegularElementValue... values) {
    return regularElementRecorder.record(name, values);
  }

}
