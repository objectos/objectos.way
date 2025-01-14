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

    compiler.compilationBegin();

    compiler.elementBegin(HtmlElementName.HTML);
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
    HtmlMarkup compiler;
    compiler = new HtmlMarkup();

    compiler.compilationBegin();

    compiler.attribute0(HtmlAttributeName.LANG, "pt-BR");

    compiler.elementBegin(HtmlElementName.HTML);
    compiler.elementValue(Html.ATTRIBUTE);
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

  private String test(HtmlMarkup compiler) {
    StringBuilder out;
    out = new StringBuilder();

    HtmlDom document;
    document = compiler.compile();

    Lang.IterableOnce<Html.Dom.Node> nodes;
    nodes = document.nodes();

    Iterator<Html.Dom.Node> nodesIter;
    nodesIter = nodes.iterator();

    while (nodesIter.hasNext()) {
      Html.Dom.Node node;
      node = nodesIter.next();

      switch (node) {
        case HtmlDomDocumentType type -> throw new UnsupportedOperationException("Implement me");

        case HtmlDomElement element -> element(out, element);

        default -> {
          Class<? extends Html.Dom.Node> type;
          type = node.getClass();

          throw new UnsupportedOperationException(
              "Implement me :: type=" + type
          );
        }
      }
    }

    return out.toString();
  }

  private void element(StringBuilder out, HtmlDomElement element) {
    String elementName;
    elementName = element.name();

    out.append('<');
    out.append(elementName);

    Lang.IterableOnce<Html.Dom.Attribute> attrs;
    attrs = element.attributes();

    Iterator<Html.Dom.Attribute> attrsIter;
    attrsIter = attrs.iterator();

    while (attrsIter.hasNext()) {
      Html.Dom.Attribute attr;
      attr = attrsIter.next();

      attribute(out, attr);
    }

    out.append('>');
    out.append('\n');

    if (element.isVoid()) {
      return;
    }

    Lang.IterableOnce<Html.Dom.Node> nodes;
    nodes = element.nodes();

    Iterator<Html.Dom.Node> nodesIter;
    nodesIter = nodes.iterator();

    while (nodesIter.hasNext()) {
      Html.Dom.Node node;
      node = nodesIter.next();

      switch (node) {
        case HtmlDomElement child -> element(out, child);

        default -> {
          Class<? extends Html.Dom.Node> type;
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

  private void attribute(StringBuilder out, Html.Dom.Attribute attribute) {
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