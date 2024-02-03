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

import static org.testng.Assert.assertEquals;

import java.util.Iterator;
import objectos.html.pseudom.HtmlAttribute;
import objectos.html.pseudom.HtmlDocumentType;
import objectos.html.pseudom.HtmlElement;
import objectos.html.pseudom.HtmlIterable;
import objectos.html.pseudom.HtmlNode;
import org.testng.annotations.Test;

public class HtmlTestProcessor {

  @Test(description = """
  <html></html>
  """)
  public void testCase00() {
    Html compiler;
    compiler = new Html();

    compiler.compilationBegin();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementEnd();

    compiler.compilationEnd();

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
    Html compiler;
    compiler = new Html();

    compiler.compilationBegin();

    compiler.attribute(StandardAttributeName.LANG, "pt-BR");

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(Api.ATTRIBUTE);
    compiler.elementEnd();

    compiler.compilationEnd();

    assertEquals(
        test(compiler),

        """
        <html lang="pt-BR">
        </html>
        """
    );
  }

  private String test(Html compiler) {
    StringBuilder out;
    out = new StringBuilder();

    PseudoHtmlDocument document;
    document = (PseudoHtmlDocument) compiler.compile();

    HtmlIterable<HtmlNode> nodes;
    nodes = document.nodes();

    Iterator<HtmlNode> nodesIter;
    nodesIter = nodes.iterator();

    while (nodesIter.hasNext()) {
      HtmlNode node;
      node = nodesIter.next();

      switch (node) {
        case HtmlDocumentType type -> throw new UnsupportedOperationException("Implement me");

        case HtmlElement element -> element(out, element);

        default -> {
          Class<? extends HtmlNode> type;
          type = node.getClass();

          throw new UnsupportedOperationException(
              "Implement me :: type=" + type
          );
        }
      }
    }

    return out.toString();
  }

  private void element(StringBuilder out, HtmlElement element) {
    String elementName;
    elementName = element.name();

    out.append('<');
    out.append(elementName);

    HtmlIterable<HtmlAttribute> attrs;
    attrs = element.attributes();

    Iterator<HtmlAttribute> attrsIter;
    attrsIter = attrs.iterator();

    while (attrsIter.hasNext()) {
      HtmlAttribute attr;
      attr = attrsIter.next();

      attribute(out, attr);
    }

    out.append('>');
    out.append('\n');

    if (element.isVoid()) {
      return;
    }

    HtmlIterable<HtmlNode> nodes;
    nodes = element.nodes();

    Iterator<HtmlNode> nodesIter;
    nodesIter = nodes.iterator();

    while (nodesIter.hasNext()) {
      HtmlNode node;
      node = nodesIter.next();

      switch (node) {
        case HtmlElement child -> element(out, child);

        default -> {
          Class<? extends HtmlNode> type;
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

  private void attribute(StringBuilder out, HtmlAttribute attribute) {
    String name;
    name = attribute.name();

    out.append(' ');
    out.append(name);

    if (attribute.isBoolean()) {
      return;
    }

    HtmlIterable<String> values;
    values = attribute.values();

    Iterator<String> valuesIter;
    valuesIter = values.iterator();

    if (valuesIter.hasNext()) {
      out.append('=');
      out.append('\"');
      out.append(valuesIter.next());

      while (valuesIter.hasNext()) {
        out.append(' ');
        out.append(valuesIter.next());
      }

      out.append('\"');
    }
  }

}