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

import java.io.IOException;
import objectos.html.HtmlTemplate;
import objectos.html.io.HtmlEscape;

public final class MinifiedWriter implements HtmlTemplate.Visitor {

  public Appendable out;

  private boolean firstValue;

  @Override
  public final void attributeEnd() throws IOException {
    if (!firstValue) {
      out.append('"');
    }
  }

  @Override
  public final void attributeStart(String name) throws IOException {
    out.append(' ');
    out.append(name);

    firstValue = true;
  }

  @Override
  public final void attributeValue(String value) throws IOException {
    if (firstValue) {
      out.append('=');
      out.append('"');

      firstValue = false;
    } else {
      out.append(' ');
    }

    HtmlEscape.to(value, out);
  }

  @Override
  public final void doctype() throws IOException {
    out.append("<!doctype html>");
  }

  @Override
  public final void endTag(String name) throws IOException {
    out.append('<');
    out.append('/');
    out.append(name);
    out.append('>');
  }

  @Override
  public final void raw(String value) throws IOException {
    out.append(value);
  }

  @Override
  public final void selfClosingEnd() throws IOException {
    out.append('>');
  }

  @Override
  public final void startTag(String name) throws IOException {
    out.append('<');
    out.append(name);
  }

  @Override
  public final void startTagEnd(String name) throws IOException {
    out.append('>');
  }

  @Override
  public final void text(String value) throws IOException {
    HtmlEscape.to(value, out);
  }

}