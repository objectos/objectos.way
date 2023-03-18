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

import objectos.html.doc.HtmlAttribute;
import objectos.html.doc.HtmlElement;
import objectos.html.doc.HtmlIterable;
import objectos.html.doc.HtmlNode;

public final class AstHtmlElement implements HtmlElement {

  @Override
  public final String name() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final HtmlIterable<HtmlAttribute> attributes() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final boolean isVoid() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final HtmlIterable<HtmlNode> nodes() {
    throw new UnsupportedOperationException("Implement me");
  }

}