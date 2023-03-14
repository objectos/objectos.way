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

  private static final int START = 0,
      ATTR_NAME = 1,
      ATTR_VALUE = 2;

  int state;

  @Override
  public void attributeEnd() {
    if (state == ATTR_VALUE) {
      write('"');
    }

    state = START;
  }

  @Override
  public void attributeStart(AttributeName name) {
    write(' ');
    write(name.getName());

    state = ATTR_NAME;
  }

  @Override
  public void attributeValue(String value) {
    if (state == ATTR_NAME) {
      write('=');
      write('"');
    } else {
      write(' ');
    }

    escaped(value);

    state = ATTR_VALUE;
  }

  @Override
  public void doctype() {
    write("<!doctype html>");
  }

  @Override
  public void documentEnd() {}

  @Override
  public void documentStart() {
    state = START;
  }

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