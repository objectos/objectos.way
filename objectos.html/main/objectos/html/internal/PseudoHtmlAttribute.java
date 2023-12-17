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

import java.util.Iterator;
import objectos.html.pseudom.HtmlAttribute;
import objectos.html.pseudom.HtmlIterable;

public final class PseudoHtmlAttribute
    implements HtmlAttribute, HtmlIterable<String>, Iterator<String> {

  AttributeName name;

  @Override
  public final String name() {
    return name.getName();
  }

  @Override
  public final boolean isBoolean() {
    return name.getKind() == AttributeKind.BOOLEAN;
  }

  @Override
  public final HtmlIterable<String> values() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final boolean hasNext() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final String next() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final Iterator<String> iterator() {
    throw new UnsupportedOperationException("Implement me");
  }

}