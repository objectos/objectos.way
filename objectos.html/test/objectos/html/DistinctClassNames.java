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
package objectos.html;

import java.util.HashSet;
import java.util.Set;
import objectos.html.pseudom.DocumentProcessor;
import objectos.html.pseudom.HtmlAttribute;
import objectos.html.pseudom.HtmlDocument;
import objectos.html.pseudom.HtmlElement;

final class DistinctClassNames implements DocumentProcessor {

  private final Set<String> names = new HashSet<>();

  public final void clear() {
    names.clear();
  }

  public final boolean contains(String value) {
    return names.contains(value);
  }

  @Override
  public final void process(HtmlDocument document) {
    names.clear();

    for (var node : document.nodes()) {
      if (node instanceof HtmlElement element) {
        processElement(element);
      }
    }
  }

  private void processElement(HtmlElement element) {
    for (HtmlAttribute attribute : element.attributes()) {
      if (attribute.hasName("class")) {
        processClassAttribute(attribute);
      }
    }
  }

  private void processClassAttribute(HtmlAttribute attribute) {
    for (var value : attribute.values()) {
      names.add(value);
    }
  }

  public final int size() {
    return names.size();
  }

}