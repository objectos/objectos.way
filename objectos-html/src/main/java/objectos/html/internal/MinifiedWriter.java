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

public final class MinifiedWriter extends HtmlSink.Writer {

  private boolean firstValue;

  @Override
  public final void attributeEnd() {
    if (!firstValue) {
      write('"');
    }
  }

  @Override
  public final void attributeStart(String name) {
    write(' ');
    write(name);

    firstValue = true;
  }

  @Override
  public final void attributeValue(String value) {
    if (firstValue) {
      write('=');
      write('"');

      firstValue = false;
    } else {
      write(' ');
    }

    escaped(value);
  }

  @Override
  public final void doctype() {
    write("<!doctype html>");
  }

  @Override
  public final void endTag(String name) {
    write('<');
    write('/');
    write(name);
    write('>');
  }

  @Override
  public final void raw(String value) {
    write(value);
  }

  @Override
  public final void selfClosingEnd() {
    write('>');
  }

  @Override
  public final void startTag(String name) {
    write('<');
    write(name);
  }

  @Override
  public final void startTagEnd(String name) {
    write('>');
  }

  @Override
  public final void text(String value) {
    escaped(value);
  }

}