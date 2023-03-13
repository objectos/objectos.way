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
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;

public class MinifiedWriter extends HtmlSink.Writer {

  static final int FIRST_VALUE = 1 << 0;

  private int flags;

  @Override
  public void attributeEnd() {
    if (!isSet(FIRST_VALUE)) {
      write('"');
    }
  }

  @Override
  public void attributeStart(StandardAttributeName name) {
    write(' ');
    write(name.getName());

    setTrue(FIRST_VALUE);
  }

  @Override
  public void attributeValue(String value) {
    if (isSet(FIRST_VALUE)) {
      write('=');
      write('"');

      setFalse(FIRST_VALUE);
    } else {
      write(' ');
    }

    escaped(value);
  }

  @Override
  public void doctype() {
    write("<!doctype html>");
  }

  @Override
  public void documentEnd() {}

  @Override
  public void documentStart() {
    flags = 0;
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
  public void startTagEnd() {
    write('>');
  }

  @Override
  public void text(String value) {
    escaped(value);
  }

  final boolean isSet(int flag) {
    return (flags & flag) != 0;
  }

  final void setFalse(int flag) {
    flags &= ~flag;
  }

  final void setTrue(int flag) {
    flags |= flag;
  }

}