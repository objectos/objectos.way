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

import java.util.Set;
import objectos.html.pseudom.DocumentProcessor;
import objectos.html.pseudom.HtmlAttribute;
import objectos.html.pseudom.HtmlDocument;
import objectos.html.pseudom.HtmlElement;
import objectos.html.tmpl.AttributeName;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;
import objectos.util.GrowableSet;

public final class DistinctClassNames implements HtmlTemplate.Visitor, DocumentProcessor {

  private final Set<String> names = new GrowableSet<>();

  private boolean collect;

  @Override
  public final void attribute(AttributeName name) {
    if (name == StandardAttributeName.CLASS) {
      collect = true;
    }
  }

  @Override
  public final void attributeFirstValue(String value) {
    if (collect) {
      names.add(value);
    }
  }

  @Override
  public final void attributeNextValue(String value) {
    attributeFirstValue(value);
  }

  @Override
  public final void attributeValueEnd() {
    collect = false;
  }

  public final void clear() {
    collect = false;

    names.clear();
  }

  public final boolean contains(String value) {
    return names.contains(value);
  }

  @Override
  public void doctype() {}

  @Override
  public void documentEnd() {}

  @Override
  public final void documentStart() {
    clear();
  }

  @Override
  public void endTag(StandardElementName name) {}

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
    for (var attribute : element.attributes()) {
      if (attribute.hasName(StandardAttributeName.CLASS)) {
        processClassAttribute(attribute);
      }
    }
  }

  private void processClassAttribute(HtmlAttribute attribute) {
    for (var value : attribute.values()) {
      names.add(value);
    }
  }

  @Override
  public void raw(String value) {}

  public final int size() {
    return names.size();
  }

  @Override
  public void startTag(StandardElementName name) {}

  @Override
  public void startTagEnd(StandardElementName name) {}

  @Override
  public void text(String value) {}

}