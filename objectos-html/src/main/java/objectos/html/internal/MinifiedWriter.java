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
import objectos.html.tmpl.AttributeName;
import objectos.html.tmpl.ElementName;

public final class MinifiedWriter implements HtmlTemplate.Visitor {

  public Appendable out;

  @Override
  public final void startTag(ElementName name) throws IOException {
    out.append('<');
    out.append(name.getName());
  }

  @Override
  public final void startTagEnd(ElementName name) throws IOException {
    out.append('>');
  }

  @Override
  public final void endTag(ElementName name) throws IOException {
    out.append('<');
    out.append('/');
    out.append(name.getName());
    out.append('>');
  }

  @Override
  public final void startTagEndSelfClosing() throws IOException {
    out.append('>');
  }

  @Override
  public final void attribute(AttributeName name, String value) throws IOException {
    out.append(' ');
    out.append(name.getName());
    out.append('=');
    out.append('"');
    HtmlEscape.to(value, out);
    out.append('"');
  }

}