/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.specgen.css.mdn;

import objectos.util.UnmodifiableList;
import objectos.specgen.css.Property;
import objectos.specgen.css.ValueType;
import objectos.util.GrowableList;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class PropertyPage extends Property.Builder {

  private final Elements codeElements;
  private final Element syntaxbox;
  private final Element title;

  public PropertyPage(Document document) {
    title = document.selectFirst("h1.title");
    syntaxbox = document.selectFirst("pre.syntaxbox");

    if (syntaxbox != null) {
      codeElements = syntaxbox.getElementsByTag("code");
    } else {
      codeElements = null;
    }
  }

  public final boolean isValid() {
    return syntaxbox != null;
  }

  @Override
  protected final String formal() {
    StringBuilder sb = new StringBuilder();

    for (Node child : syntaxbox.childNodes()) {
      if (hasTagName(child, "p")) {
        continue;
      }
      sb.append(getText(child));
    }

    return sb.toString();
  }

  @Override
  protected final String name() {
    return title.ownText();
  }

  @Override
  protected final UnmodifiableList<ValueType> valueTypes() {
    GrowableList<ValueType> vt = new GrowableList<>();

    for (Element code : codeElements) {
      valueTypes0(vt, code);
    }

    return vt.toUnmodifiableList();
  }

  private String getText(Node node) {
    if (node instanceof Element) {
      return ((Element) node).text();
    }

    if (node instanceof TextNode) {
      return ((TextNode) node).text();
    }

    throw new UnsupportedOperationException("Implement me");
  }

  private boolean hasTagName(Node node, String tagName) {
    if (node instanceof Element) {
      return ((Element) node).tagName().equals(tagName);
    }

    return false;
  }

  private void valueTypes0(GrowableList<ValueType> vt, Element code) {
    String name = null;
    StringBuilder formal = new StringBuilder();

    for (Node child : code.childNodes()) {
      if (hasTagName(child, "br")) {
        vt.add(new ValueType(name, formal.toString()));
        name = null;
        formal.setLength(0);
        continue;
      }

      String text = getText(child);

      if (hasTagName(child, "span")) {
        name = text;
        continue;
      }

      if (text.startsWith(" = ")) {
        text = text.substring(3);
      }

      formal.append(text);
    }

    if (name != null) {
      vt.add(new ValueType(name, formal.toString()));
    } else {
      System.out.println("name == null @ " + name());
    }
  }

}
