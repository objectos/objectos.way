/*
 * Copyright (C) 2015-2025 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;

import java.util.Iterator;
import org.testng.annotations.Test;

public class HtmlCompilerTestProcessor {

  @Test(description = """
  <html></html>
  """)
  public void testCase00() {
    HtmlMarkup compiler;
    compiler = new HtmlMarkup();

    compiler.elementBegin(HtmlElementName.HTML);
    compiler.elementEnd();

    assertEquals(
        test(compiler),

        """
        <html>
        </html>
        """
    );
  }

  @Test(description = """
  <html lang="pt-BR"></html>
  """)
  public void testCase01() {
    HtmlMarkup compiler;
    compiler = new HtmlMarkup();

    compiler.attribute0(HtmlAttributeName.LANG, "pt-BR");

    compiler.elementBegin(HtmlElementName.HTML);
    compiler.elementValue(Html.ATTRIBUTE);
    compiler.elementEnd();

    assertEquals(
        test(compiler),

        """
        <html lang="pt-BR">
        </html>
        """
    );
  }

  private String test(HtmlMarkup compiler) {
    StringBuilder out;
    out = new StringBuilder();

    Dom.Document document;
    document = compiler.compile();

    Lang.IterableOnce<Dom.Node> nodes;
    nodes = document.nodes();

    Iterator<Dom.Node> nodesIter;
    nodesIter = nodes.iterator();

    while (nodesIter.hasNext()) {
      Dom.Node node;
      node = nodesIter.next();

      switch (node) {
        case Dom.Document.Type type -> throw new UnsupportedOperationException("Implement me");

        case DomElement element -> element(out, element);

        default -> {
          Class<? extends Dom.Node> type;
          type = node.getClass();

          throw new UnsupportedOperationException(
              "Implement me :: type=" + type
          );
        }
      }
    }

    return out.toString();
  }

  private void element(StringBuilder out, DomElement element) {
    String elementName;
    elementName = element.name();

    out.append('<');
    out.append(elementName);

    Lang.IterableOnce<Dom.Attribute> attrs;
    attrs = element.attributes();

    Iterator<Dom.Attribute> attrsIter;
    attrsIter = attrs.iterator();

    while (attrsIter.hasNext()) {
      Dom.Attribute attr;
      attr = attrsIter.next();

      attribute(out, attr);
    }

    out.append('>');
    out.append('\n');

    if (element.voidElement()) {
      return;
    }

    Lang.IterableOnce<Dom.Node> nodes;
    nodes = element.nodes();

    Iterator<Dom.Node> nodesIter;
    nodesIter = nodes.iterator();

    while (nodesIter.hasNext()) {
      Dom.Node node;
      node = nodesIter.next();

      switch (node) {
        case DomElement child -> element(out, child);

        default -> {
          Class<? extends Dom.Node> type;
          type = node.getClass();

          throw new UnsupportedOperationException(
              "Implement me :: type=" + type
          );
        }
      }
    }

    out.append('<');
    out.append('/');
    out.append(elementName);
    out.append('>');
    out.append('\n');
  }

  private void attribute(StringBuilder out, Dom.Attribute attribute) {
    String name;
    name = attribute.name();

    out.append(' ');
    out.append(name);

    if (attribute.booleanAttribute()) {
      return;
    }

    out.append('=');

    out.append('\"');

    out.append(attribute.value());

    out.append('\"');
  }

}