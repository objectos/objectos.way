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

import java.util.Arrays;
import org.testng.annotations.Test;

public class HtmlMarkupTest02Player {

  @Test(description = """
  <html></html>
  """)
  public void testCase00() {
    Html.Markup.OfHtml compiler;
    compiler = new Html.Markup.OfHtml();

    compiler.elementBegin(HtmlElementName.HTML);
    compiler.elementEnd();

    // document
    compiler.compile();

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_START,
        HtmlBytes.encodeInt0(0), HtmlBytes.encodeInt1(0), HtmlBytes.encodeInt2(0)
    );

    // document.nodes
    compiler.documentIterable();

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_ITERABLE,
        HtmlBytes.encodeInt0(0), HtmlBytes.encodeInt1(0), HtmlBytes.encodeInt2(0)
    );

    // document.nodes.iterator
    compiler.documentIterator();

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_ITERATOR,
        HtmlBytes.encodeInt0(0), HtmlBytes.encodeInt1(0), HtmlBytes.encodeInt2(0)
    );

    // document.nodes.hasNext
    assertEquals(compiler.documentHasNext(), true);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_HAS_NEXT,
        HtmlBytes.encodeInt0(0), HtmlBytes.encodeInt1(0), HtmlBytes.encodeInt2(0)
    );

    // document.nodes.next
    DomElement elem;
    elem = (DomElement) compiler.documentNext();

    assertEquals(elem.name, HtmlElementName.HTML);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),

        Html.Markup.OfHtml._ELEMENT_START,
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        len(4)
    );

    // element.attributes
    compiler.elementAttributes();

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),

        Html.Markup.OfHtml._ELEMENT_ATTRS_ITERABLE,
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        len(4)
    );

    // element.attributes.iterator
    compiler.elementAttributesIterator();

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),

        Html.Markup.OfHtml._ELEMENT_ATTRS_ITERATOR,
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        len(4)
    );

    // element.attributes.hasNext
    assertEquals(compiler.elementAttributesHasNext(HtmlElementName.HTML), false);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),

        Html.Markup.OfHtml._ELEMENT_ATTRS_EXHAUSTED,
        HtmlBytes.encodeInt0(5), HtmlBytes.encodeInt1(5), HtmlBytes.encodeInt2(5),
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        len(4)
    );

    // element.nodes
    compiler.elementNodes();

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),

        Html.Markup.OfHtml._ELEMENT_NODES_ITERABLE,
        HtmlBytes.encodeInt0(5), HtmlBytes.encodeInt1(5), HtmlBytes.encodeInt2(5),
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        len(4)
    );

    // element.nodes.iterator
    compiler.elementNodesIterator();

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),

        Html.Markup.OfHtml._ELEMENT_NODES_ITERATOR,
        HtmlBytes.encodeInt0(5), HtmlBytes.encodeInt1(5), HtmlBytes.encodeInt2(5),
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        len(4)
    );

    // element.nodes.hasNext
    assertEquals(compiler.elementNodesHasNext(), false);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),

        Html.Markup.OfHtml._ELEMENT_NODES_EXHAUSTED,
        HtmlBytes.encodeInt0(5), HtmlBytes.encodeInt1(5), HtmlBytes.encodeInt2(5),
        HtmlBytes.encodeInt0(5), HtmlBytes.encodeInt1(5), HtmlBytes.encodeInt2(5),
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        len(4)
    );

    // document.nodes.hasNext
    assertEquals(compiler.documentHasNext(), false);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_EXHAUSTED,
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8)
    );
  }

  @Test(description = """
  <html lang="pt-BR"></html>
  """)
  public void testCase01() {
    Html.Markup.OfHtml compiler;
    compiler = new Html.Markup.OfHtml();

    compiler.attr(HtmlAttributeName.LANG, "pt-BR");

    compiler.elementBegin(HtmlElementName.HTML);
    compiler.elementValue(Html.ATTRIBUTE);
    compiler.elementEnd();

    // document
    compiler.compile();

    compiler.documentIterable();
    compiler.documentIterator();
    assertEquals(compiler.documentHasNext(), true);
    compiler.documentNext();

    // html
    compiler.elementAttributes();

    // html.attrs.iterator
    compiler.elementAttributesIterator();

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),

        Html.Markup.OfHtml._ELEMENT_ATTRS_ITERATOR,
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        len(4)
    );

    // html.attrs.iterator.hasNext
    assertEquals(compiler.elementAttributesHasNext(HtmlElementName.HTML), true);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),

        Html.Markup.OfHtml._ELEMENT_ATTRS_HAS_NEXT,
        HtmlBytes.encodeInt0(10), HtmlBytes.encodeInt1(10), HtmlBytes.encodeInt2(10),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        len(4)
    );

    // html.attrs.iterator.next => lang="pt-BR"
    DomAttribute attr;
    attr = (DomAttribute) compiler.elementAttributesNext();

    assertEquals(attr.name(), "lang");
    assertEquals(attr.value, "pt-BR");

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),

        Html.Markup.OfHtml._ELEMENT_ATTRS_NEXT,
        HtmlBytes.encodeInt0(12), HtmlBytes.encodeInt1(12), HtmlBytes.encodeInt2(12),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        len(4)
    );

    // lang="pt-BR".values
    compiler.attributeValues();

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),

        Html.Markup.OfHtml._ATTRIBUTE_VALUES_ITERABLE,
        HtmlBytes.encodeInt0(12), HtmlBytes.encodeInt1(12), HtmlBytes.encodeInt2(12),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        len(4)
    );

    // lang="pt-BR".values.iterator
    compiler.attributeValuesIterator();

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),

        Html.Markup.OfHtml._ATTRIBUTE_VALUES_ITERATOR,
        HtmlBytes.encodeInt0(12), HtmlBytes.encodeInt1(12), HtmlBytes.encodeInt2(12),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        len(4)
    );

    // lang="pt-BR".values.iterator.hasNext
    assertEquals(compiler.attributeValuesHasNext(), true);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),

        Html.Markup.OfHtml._ATTRIBUTE_VALUES_HAS_NEXT,
        HtmlBytes.encodeInt0(12), HtmlBytes.encodeInt1(12), HtmlBytes.encodeInt2(12),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        len(4)
    );

    assertEquals(compiler.attributeValuesNext(attr.value), "pt-BR");

    attr.value = null;

    // lang="pt-BR".values.iterator.hasNext
    assertEquals(compiler.attributeValuesHasNext(), false);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),

        Html.Markup.OfHtml._ATTRIBUTE_VALUES_EXHAUSTED,
        HtmlBytes.encodeInt0(12), HtmlBytes.encodeInt1(12), HtmlBytes.encodeInt2(12),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        len(4)
    );

    // html.attributes.hasNext
    assertEquals(compiler.elementAttributesHasNext(HtmlElementName.HTML), false);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),

        Html.Markup.OfHtml._ELEMENT_ATTRS_EXHAUSTED,
        HtmlBytes.encodeInt0(12), HtmlBytes.encodeInt1(12), HtmlBytes.encodeInt2(12),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        len(4)
    );
  }

  @Test(description = """
  <html><head></head></html>
  """)
  public void testCase03() {
    Html.Markup.OfHtml compiler;
    compiler = new Html.Markup.OfHtml();

    compiler.elementBegin(HtmlElementName.HEAD);
    compiler.elementEnd();

    compiler.elementBegin(HtmlElementName.HTML);
    compiler.elementValue(Html.ELEMENT);
    compiler.elementEnd();

    // document
    compiler.compile();
    compiler.documentIterable();
    compiler.documentIterator();
    assertEquals(compiler.documentHasNext(), true);

    // html
    DomElement html;
    html = (DomElement) compiler.documentNext();

    assertEquals(html.name, HtmlElementName.HTML);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),

        Html.Markup.OfHtml._ELEMENT_START,
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),
        len(4)
    );

    // html.attributes
    compiler.elementAttributes();
    compiler.elementAttributesIterator();
    assertEquals(compiler.elementAttributesHasNext(HtmlElementName.HTML), false);

    // html.nodes
    compiler.elementNodes();

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),

        Html.Markup.OfHtml._ELEMENT_NODES_ITERABLE,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),
        len(4)
    );

    // html.nodes.iterator
    compiler.elementNodesIterator();

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),

        Html.Markup.OfHtml._ELEMENT_NODES_ITERATOR,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),
        len(4)
    );

    // html.nodes.hasNext
    assertEquals(compiler.elementNodesHasNext(), true);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),

        Html.Markup.OfHtml._ELEMENT_NODES_HAS_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(13), HtmlBytes.encodeInt1(13), HtmlBytes.encodeInt2(13),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),
        len(4)
    );

    // head
    DomElement head;
    head = (DomElement) compiler.elementNodesNext();

    assertEquals(head.name(), "head");

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),

        Html.Markup.OfHtml._ELEMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),
        len(4),

        Html.Markup.OfHtml._ELEMENT_START,
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        len(14)
    );

    // head.attributes
    compiler.elementAttributes();
    compiler.elementAttributesIterator();
    assertEquals(compiler.elementAttributesHasNext(HtmlElementName.HEAD), false);

    // head.nodes
    compiler.elementNodes();
    compiler.elementNodesIterator();
    assertEquals(compiler.elementNodesHasNext(), false);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),

        Html.Markup.OfHtml._ELEMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),
        len(4),

        Html.Markup.OfHtml._ELEMENT_NODES_EXHAUSTED,
        HtmlBytes.encodeInt0(5), HtmlBytes.encodeInt1(5), HtmlBytes.encodeInt2(5),
        HtmlBytes.encodeInt0(5), HtmlBytes.encodeInt1(5), HtmlBytes.encodeInt2(5),
        HtmlBytes.encodeInt0(3), HtmlBytes.encodeInt1(3), HtmlBytes.encodeInt2(3),
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        len(14)
    );

    // html.nodes.hasNext
    assertEquals(compiler.elementNodesHasNext(), false);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),

        Html.Markup.OfHtml._ELEMENT_NODES_EXHAUSTED,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),
        len(4)
    );

    // document.nodes.hasNext
    assertEquals(compiler.documentHasNext(), false);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_EXHAUSTED,
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18)
    );
  }

  @Test(description = """
  Ambiguous
  """)
  public void testCase16() {
    Html.Markup.OfHtml compiler;
    compiler = new Html.Markup.OfHtml();

    compiler.ambiguous(HtmlAmbiguous.TITLE, "element");

    compiler.elementBegin(HtmlElementName.HEAD);
    compiler.elementValue(Html.ELEMENT);
    compiler.elementEnd();

    compiler.ambiguous(HtmlAmbiguous.TITLE, "attribute");

    compiler.elementBegin(HtmlElementName.BODY);
    compiler.elementValue(Html.ELEMENT);
    compiler.elementEnd();

    compiler.elementBegin(HtmlElementName.HTML);
    compiler.elementValue(Html.ELEMENT);
    compiler.elementValue(Html.ELEMENT);
    compiler.elementEnd();

    // document
    compiler.compile();
    compiler.documentIterable();
    compiler.documentIterator();
    assertEquals(compiler.documentHasNext(), true);

    // html
    DomElement html;
    html = (DomElement) compiler.documentNext();
    assertEquals(html.name, HtmlElementName.HTML);

    // html.attributes
    compiler.elementAttributes();
    compiler.elementAttributesIterator();
    assertEquals(compiler.elementAttributesHasNext(HtmlElementName.HTML), false);

    // html.nodes
    compiler.elementNodes();
    compiler.elementNodesIterator();
    assertEquals(compiler.elementNodesHasNext(), true);

    // head
    DomElement head;
    head = (DomElement) compiler.elementNodesNext();
    assertEquals(head.name, HtmlElementName.HEAD);

    // head.attributes
    compiler.elementAttributes();
    compiler.elementAttributesIterator();
    assertEquals(compiler.elementAttributesHasNext(HtmlElementName.HEAD), false);

    // head.nodes
    compiler.elementNodes();
    compiler.elementNodesIterator();
    assertEquals(compiler.elementNodesHasNext(), true);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(42), HtmlBytes.encodeInt1(42), HtmlBytes.encodeInt2(42),

        Html.Markup.OfHtml._ELEMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(39), HtmlBytes.encodeInt1(39), HtmlBytes.encodeInt2(39),
        HtmlBytes.encodeInt0(37), HtmlBytes.encodeInt1(37), HtmlBytes.encodeInt2(37),
        HtmlBytes.encodeInt0(33), HtmlBytes.encodeInt1(33), HtmlBytes.encodeInt2(33),
        HtmlBytes.encodeInt0(42), HtmlBytes.encodeInt1(42), HtmlBytes.encodeInt2(42),
        len(4),

        Html.Markup.OfHtml._ELEMENT_NODES_HAS_NEXT,
        HtmlBytes.encodeInt0(12), HtmlBytes.encodeInt1(12), HtmlBytes.encodeInt2(12),
        HtmlBytes.encodeInt0(10), HtmlBytes.encodeInt1(10), HtmlBytes.encodeInt2(10),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(37), HtmlBytes.encodeInt1(37), HtmlBytes.encodeInt2(37),
        len(14)
    );

    // title
    DomElement title;
    title = (DomElement) compiler.elementNodesNext();
    assertEquals(title.name, HtmlElementName.TITLE);
  }

  @Test(description = """
  HtmlTemplate TC31

  - email input
  """)
  public void testCase31() {
    Html.Markup.OfHtml compiler;
    compiler = new Html.Markup.OfHtml();

    compiler.attr(HtmlAttributeName.TYPE, "email");
    compiler.attr(HtmlAttributeName.REQUIRED);

    compiler.elementBegin(HtmlElementName.INPUT);
    compiler.elementValue(Html.ATTRIBUTE);
    compiler.elementValue(Html.ATTRIBUTE);
    compiler.elementEnd();

    // document
    compiler.compile();
    compiler.documentIterable();
    compiler.documentIterator();
    assertEquals(compiler.documentHasNext(), true);

    // input
    DomElement input;
    input = (DomElement) compiler.documentNext();
    assertEquals(input.name(), "input");

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),

        Html.Markup.OfHtml._ELEMENT_START,
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),
        len(4)
    );

    // input.attrs
    compiler.elementAttributes();
    compiler.elementAttributesIterator();
    assertEquals(compiler.elementAttributesHasNext(HtmlElementName.INPUT), true);

    // input[type]
    DomAttribute type;
    type = (DomAttribute) compiler.elementAttributesNext();

    assertEquals(type.name(), "type");

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),

        Html.Markup.OfHtml._ELEMENT_ATTRS_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),
        len(4)
    );

    // input[type].values
    compiler.attributeValues();
    compiler.attributeValuesIterator();
    assertEquals(compiler.attributeValuesHasNext(), true);
    assertEquals(compiler.attributeValuesNext(type.value), "email");

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),

        Html.Markup.OfHtml._ATTRIBUTE_VALUES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),
        len(4)
    );

    // input[type].values.hasNext
    type.value = null;
    assertEquals(compiler.attributeValuesHasNext(), false);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),

        Html.Markup.OfHtml._ATTRIBUTE_VALUES_EXHAUSTED,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),
        len(4)
    );

    // input.attrs.hasNext
    assertEquals(compiler.elementAttributesHasNext(HtmlElementName.INPUT), true);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),

        Html.Markup.OfHtml._ELEMENT_ATTRS_HAS_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),
        len(4)
    );

    // input[required]
    DomAttribute required;
    required = (DomAttribute) compiler.elementAttributesNext();

    assertEquals(required.name(), "required");

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),

        Html.Markup.OfHtml._ELEMENT_ATTRS_NEXT,
        HtmlBytes.encodeInt0(17), HtmlBytes.encodeInt1(17), HtmlBytes.encodeInt2(17),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),
        len(4)
    );

    // input.attrs.hasNext
    assertEquals(compiler.elementAttributesHasNext(HtmlElementName.INPUT), false);

    testAux(
        compiler,

        Html.Markup.OfHtml._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),

        Html.Markup.OfHtml._ELEMENT_ATTRS_EXHAUSTED,
        HtmlBytes.encodeInt0(17), HtmlBytes.encodeInt1(17), HtmlBytes.encodeInt2(17),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),
        len(4)
    );
  }

  private byte len(int value) {
    return (byte) value;
  }

  private void testAux(Html.Markup.OfHtml compiler, byte... expected) {
    byte[] result;
    result = Arrays.copyOf(compiler.aux, compiler.auxIndex);

    if (result.length != expected.length) {
      throw new AssertionError(
          """
        Arrays don't have the same size.

        Actual  : %s
        Expected: %s
        """.formatted(Arrays.toString(result), Arrays.toString(expected))
      );
    }

    if (!Arrays.equals(result, expected)) {
      throw new AssertionError(
          """
        Arrays don't have the same content.

        Actual  : %s
        Expected: %s
        """.formatted(Arrays.toString(result), Arrays.toString(expected))
      );
    }
  }

}