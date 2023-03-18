/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html.internal;

import objectos.html.HtmlSink;
import objectos.html.tmpl.AttributeName;
import objectos.html.tmpl.StandardElementName;

public class MinifiedWriter extends HtmlSink.Writer {

  @Override
  public final void attribute(AttributeName name) {
    write(' ');

    write(name.getName());
  }

  @Override
  public final void attributeFirstValue(String value) {
    write("=\"");

    escaped(value);
  }

  @Override
  public final void attributeNextValue(String value) {
    write(' ');

    escaped(value);
  }

  @Override
  public final void attributeValueEnd() {
    write('"');
  }

  @Override
  public void doctype() {
    write("<!doctype html>");
  }

  @Override
  public void documentEnd() {}

  @Override
  public void documentStart() {}

  @Override
  public void endTag(StandardElementName name) {
    write('<');
    write('/');
    write(name.getName());
    write('>');
  }

  @Override
  public void raw(String value) {
    write(value);
  }

  @Override
  public void selfClosingEnd() {
    write('>');
  }

  @Override
  public void startTag(StandardElementName name) {
    write('<');
    write(name.getName());
  }

  @Override
  public void startTagEnd(StandardElementName name) {
    write('>');
  }

  @Override
  public void text(String value) {
    escaped(value);
  }

}