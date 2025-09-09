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

public class HtmlCompilerTestInternal {

  @Test(description = """
  <html></html>
  """)
  public void testCase00() {
    HtmlMarkup compiler;
    compiler = new HtmlMarkup();

    compiler.elementBegin(HtmlElementName.HTML);
    compiler.elementEnd();

    // document
    compiler.compile();

    testAux(
        compiler,

        HtmlMarkup._DOCUMENT_START,
        HtmlBytes.encodeInt0(0), HtmlBytes.encodeInt1(0), HtmlBytes.encodeInt2(0)
    );

    // document.nodes
    compiler.documentIterable();

    testAux(
        compiler,

        HtmlMarkup._DOCUMENT_NODES_ITERABLE,
        HtmlBytes.encodeInt0(0), HtmlBytes.encodeInt1(0), HtmlBytes.encodeInt2(0)
    );

    // document.nodes.iterator
    compiler.documentIterator();

    testAux(
        compiler,

        HtmlMarkup._DOCUMENT_NODES_ITERATOR,
        HtmlBytes.encodeInt0(0), HtmlBytes.encodeInt1(0), HtmlBytes.encodeInt2(0)
    );

    // document.nodes.hasNext
    assertEquals(compiler.documentHasNext(), true);

    testAux(
        compiler,

        HtmlMarkup._DOCUMENT_NODES_HAS_NEXT,
        HtmlBytes.encodeInt0(0), HtmlBytes.encodeInt1(0), HtmlBytes.encodeInt2(0)
    );

    // document.nodes.next
    HtmlDomElement elem;
    elem = (HtmlDomElement) compiler.documentNext();

    assertEquals(elem.name, HtmlElementName.HTML);

    testAux(
        compiler,

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),

        HtmlMarkup._ELEMENT_START,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),

        HtmlMarkup._ELEMENT_ATTRS_ITERABLE,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),

        HtmlMarkup._ELEMENT_ATTRS_ITERATOR,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),

        HtmlMarkup._ELEMENT_ATTRS_EXHAUSTED,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),

        HtmlMarkup._ELEMENT_NODES_ITERABLE,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),

        HtmlMarkup._ELEMENT_NODES_ITERATOR,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),

        HtmlMarkup._ELEMENT_NODES_EXHAUSTED,
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

        HtmlMarkup._DOCUMENT_NODES_EXHAUSTED,
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8)
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),

        HtmlMarkup._ELEMENT_ATTRS_ITERATOR,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),

        HtmlMarkup._ELEMENT_ATTRS_HAS_NEXT,
        HtmlBytes.encodeInt0(10), HtmlBytes.encodeInt1(10), HtmlBytes.encodeInt2(10),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        len(4)
    );

    // html.attrs.iterator.next => lang="pt-BR"
    HtmlDomAttribute attr;
    attr = compiler.elementAttributesNext();

    assertEquals(attr.name(), "lang");
    assertEquals(attr.value, "pt-BR");

    testAux(
        compiler,

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),

        HtmlMarkup._ELEMENT_ATTRS_NEXT,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),

        HtmlMarkup._ATTRIBUTE_VALUES_ITERABLE,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),

        HtmlMarkup._ATTRIBUTE_VALUES_ITERATOR,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),

        HtmlMarkup._ATTRIBUTE_VALUES_HAS_NEXT,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),

        HtmlMarkup._ATTRIBUTE_VALUES_EXHAUSTED,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),

        HtmlMarkup._ELEMENT_ATTRS_EXHAUSTED,
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
    HtmlMarkup compiler;
    compiler = new HtmlMarkup();

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
    HtmlDomElement html;
    html = (HtmlDomElement) compiler.documentNext();

    assertEquals(html.name, HtmlElementName.HTML);

    testAux(
        compiler,

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),

        HtmlMarkup._ELEMENT_START,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),

        HtmlMarkup._ELEMENT_NODES_ITERABLE,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),

        HtmlMarkup._ELEMENT_NODES_ITERATOR,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),

        HtmlMarkup._ELEMENT_NODES_HAS_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(13), HtmlBytes.encodeInt1(13), HtmlBytes.encodeInt2(13),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),
        len(4)
    );

    // head
    HtmlDomElement head;
    head = (HtmlDomElement) compiler.elementNodesNext();

    assertEquals(head.name(), "head");

    testAux(
        compiler,

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),

        HtmlMarkup._ELEMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),
        len(4),

        HtmlMarkup._ELEMENT_START,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),

        HtmlMarkup._ELEMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),
        len(4),

        HtmlMarkup._ELEMENT_NODES_EXHAUSTED,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18),

        HtmlMarkup._ELEMENT_NODES_EXHAUSTED,
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

        HtmlMarkup._DOCUMENT_NODES_EXHAUSTED,
        HtmlBytes.encodeInt0(18), HtmlBytes.encodeInt1(18), HtmlBytes.encodeInt2(18)
    );
  }

  @Test(description = """
  Ambiguous
  """)
  public void testCase16() {
    HtmlMarkup compiler;
    compiler = new HtmlMarkup();

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
    HtmlDomElement html;
    html = (HtmlDomElement) compiler.documentNext();
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
    HtmlDomElement head;
    head = (HtmlDomElement) compiler.elementNodesNext();
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(42), HtmlBytes.encodeInt1(42), HtmlBytes.encodeInt2(42),

        HtmlMarkup._ELEMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(39), HtmlBytes.encodeInt1(39), HtmlBytes.encodeInt2(39),
        HtmlBytes.encodeInt0(37), HtmlBytes.encodeInt1(37), HtmlBytes.encodeInt2(37),
        HtmlBytes.encodeInt0(33), HtmlBytes.encodeInt1(33), HtmlBytes.encodeInt2(33),
        HtmlBytes.encodeInt0(42), HtmlBytes.encodeInt1(42), HtmlBytes.encodeInt2(42),
        len(4),

        HtmlMarkup._ELEMENT_NODES_HAS_NEXT,
        HtmlBytes.encodeInt0(12), HtmlBytes.encodeInt1(12), HtmlBytes.encodeInt2(12),
        HtmlBytes.encodeInt0(10), HtmlBytes.encodeInt1(10), HtmlBytes.encodeInt2(10),
        HtmlBytes.encodeInt0(8), HtmlBytes.encodeInt1(8), HtmlBytes.encodeInt2(8),
        HtmlBytes.encodeInt0(37), HtmlBytes.encodeInt1(37), HtmlBytes.encodeInt2(37),
        len(14)
    );

    // title
    HtmlDomElement title;
    title = (HtmlDomElement) compiler.elementNodesNext();
    assertEquals(title.name, HtmlElementName.TITLE);
  }

  @Test(description = """
  HtmlTemplate TC31

  - email input
  """)
  public void testCase31() {
    HtmlMarkup compiler;
    compiler = new HtmlMarkup();

    compiler.attribute0(HtmlAttributeName.TYPE, "email");
    compiler.attribute0(HtmlAttributeName.REQUIRED);

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
    HtmlDomElement input;
    input = (HtmlDomElement) compiler.documentNext();
    assertEquals(input.name(), "input");

    testAux(
        compiler,

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),

        HtmlMarkup._ELEMENT_START,
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
    HtmlDomAttribute type;
    type = compiler.elementAttributesNext();
    assertEquals(type.name(), "type");

    testAux(
        compiler,

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),

        HtmlMarkup._ELEMENT_ATTRS_NEXT,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),

        HtmlMarkup._ATTRIBUTE_VALUES_NEXT,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),

        HtmlMarkup._ATTRIBUTE_VALUES_EXHAUSTED,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),

        HtmlMarkup._ELEMENT_ATTRS_HAS_NEXT,
        HtmlBytes.encodeInt0(15), HtmlBytes.encodeInt1(15), HtmlBytes.encodeInt2(15),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(11), HtmlBytes.encodeInt1(11), HtmlBytes.encodeInt2(11),
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),
        len(4)
    );

    // input[required]
    HtmlDomAttribute required;
    required = compiler.elementAttributesNext();
    assertEquals(required.name(), "required");

    testAux(
        compiler,

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),

        HtmlMarkup._ELEMENT_ATTRS_NEXT,
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

        HtmlMarkup._DOCUMENT_NODES_NEXT,
        HtmlBytes.encodeInt0(20), HtmlBytes.encodeInt1(20), HtmlBytes.encodeInt2(20),

        HtmlMarkup._ELEMENT_ATTRS_EXHAUSTED,
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

  private void testAux(HtmlMarkup compiler, byte... expected) {
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