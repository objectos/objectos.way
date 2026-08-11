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
package objectox.html;

import objectos.html.Attribute;
import objectos.html.ElementName;
import objectos.html.Html;
import objectos.html.Markup;
import objectos.html.markup.ElementMarkup;
import objectos.html.markup.ElementValue;
import objectos.html.markup.Fragment0;
import objectos.html.markup.Fragment1;
import objectos.html.markup.Fragment2;
import objectos.html.markup.Fragment3;
import objectos.html.markup.Fragment4;
import objectos.html.markup.FragmentMarkup;
import objectos.html.markup.NoOpMarkup;
import objectox.html.markup.AttributeNamePojo;
import objectox.html.markup.AttributePojo;
import objectox.html.markup.NoOpInstructionPojo;
import objectox.html.markup.Recorder;

public sealed abstract class MarkupPojo
    extends MarkupGenerated
    implements Markup
    permits Markup.OfHtml {

  private Recorder recorder;

  public MarkupPojo() {
    recorder = Recorder.create();
  }

  @Override
  public final Html compile() {
    return recorder.compile();
  }

  // ##################################################################
  // # END: Public API
  // ##################################################################

  // ##################################################################
  // # BEGIN: Way Support
  // ##################################################################

  @Override
  public final FragmentMarkup f(Fragment0 fragment) {
    final Recorder previous;
    previous = recorder;

    final Recorder current;
    current = Recorder.create();

    recorder = current;

    fragment.invoke();

    final HtmlPojo html;
    html = current.compile();

    recorder = previous;

    return html;
  }

  @Override
  public final <T1> FragmentMarkup f(Fragment1<T1> fragment, T1 arg1) {
    final Recorder previous;
    previous = recorder;

    final Recorder current;
    current = Recorder.create();

    recorder = current;

    fragment.invoke(arg1);

    final HtmlPojo html;
    html = current.compile();

    recorder = previous;

    return html;
  }

  @Override
  public final <T1, T2> FragmentMarkup f(Fragment2<T1, T2> fragment, T1 arg1, T2 arg2) {
    final Recorder previous;
    previous = recorder;

    final Recorder current;
    current = Recorder.create();

    recorder = current;

    fragment.invoke(arg1, arg2);

    final HtmlPojo html;
    html = current.compile();

    recorder = previous;

    return html;
  }

  @Override
  public final <T1, T2, T3> FragmentMarkup f(Fragment3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3) {
    final Recorder previous;
    previous = recorder;

    final Recorder current;
    current = Recorder.create();

    recorder = current;

    fragment.invoke(arg1, arg2, arg3);

    final HtmlPojo html;
    html = current.compile();

    recorder = previous;

    return html;
  }

  @Override
  public final <T1, T2, T3, T4> FragmentMarkup f(Fragment4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
    final Recorder previous;
    previous = recorder;

    final Recorder current;
    current = Recorder.create();

    recorder = current;

    fragment.invoke(arg1, arg2, arg3, arg4);

    final HtmlPojo html;
    html = current.compile();

    recorder = previous;

    return html;
  }

  @Override
  public final Attribute css(String value) {
    final StringBuilder sb;
    sb = new StringBuilder();

    final String formatted;
    formatted = AttributeNamePojo.formatAttrValue(value, sb);

    return AttributePojo.of0(AttributeNamePojo.CLASS, formatted);
  }

  @Override
  public final NoOpMarkup noop() {
    return NoOpInstructionPojo.INSTANCE;
  }

  // ##################################################################
  // # END: Way Support
  // ##################################################################

  // ##################################################################
  // # BEGIN: Elements
  // ##################################################################

  @Override
  public final void doctype() {
    recorder.doctype();
  }

  // ##################################################################
  // # END: Elements
  // ##################################################################

  // ##################################################################
  // # BEGIN: Testable
  // ##################################################################

  @Override
  public final String testableCell(String value, int width) {
    return value;
  }

  @Override
  public final String testableField(String name, String value) {
    return value;
  }

  @Override
  public final String testableFieldName(String name) {
    return name;
  }

  @Override
  public final String testableFieldValue(String value) {
    return value;
  }

  @Override
  public final String testableH1(String value) {
    return value;
  }

  @Override
  public final String testableH2(String value) {
    return value;
  }

  @Override
  public final String testableH3(String value) {
    return value;
  }

  @Override
  public final String testableH4(String value) {
    return value;
  }

  @Override
  public final String testableH5(String value) {
    return value;
  }

  @Override
  public final String testableH6(String value) {
    return value;
  }

  @Override
  public final NoOpMarkup testableNewLine() {
    return noop();
  }

  // ##################################################################
  // # END: Testable
  // ##################################################################

  @Override
  final ElementMarkup elem0(ElementName name, ElementValue... contents) {
    return recorder.element(name, contents);
  }

  @Override
  final ElementMarkup elem0(ElementName name, String text) {
    return recorder.element(name, text(text));
  }

}