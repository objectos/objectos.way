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

import static org.testng.Assert.assertEquals;

import java.util.Iterator;
import objectos.html.pseudom.HtmlAttribute;
import objectos.html.pseudom.HtmlIterable;
import objectos.html.pseudom.HtmlNode;
import org.testng.annotations.Test;

public class HtmlCompiler02TestProcessor {

  @Test(description = """
  <html></html>
  """)
  public void testCase00() {
    HtmlCompiler02 compiler;
    compiler = new HtmlCompiler02();

    compiler.compilationBegin();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementEnd();

    compiler.compilationEnd();

    // document
    PseudoHtmlDocument document;
    document = compiler.bootstrap();

    HtmlIterable<HtmlNode> nodes;
    nodes = document.nodes();

    Iterator<HtmlNode> documentNodes;
    documentNodes = nodes.iterator();

    assertEquals(documentNodes.hasNext(), true);

    // <html>
    HtmlNode node;
    node = documentNodes.next();

    PseudoHtmlElement html;
    html = (PseudoHtmlElement) node;

    assertEquals(html.name(), "html");

    // <html attrs>
    HtmlIterable<HtmlAttribute> attrs;
    attrs = html.attributes();

    Iterator<HtmlAttribute> attrsIter;
    attrsIter = attrs.iterator();

    assertEquals(attrsIter.hasNext(), false);

    // <html>children
    nodes = html.nodes();

    Iterator<HtmlNode> htmlNodes;
    htmlNodes = nodes.iterator();

    assertEquals(htmlNodes.hasNext(), false);

    // document end
    assertEquals(documentNodes.hasNext(), false);
  }

}