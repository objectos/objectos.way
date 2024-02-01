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

import java.util.Arrays;
import objectos.html.BaseTypes.AttributeInstruction;
import objectos.html.BaseTypes.ElementInstruction;
import objectos.html.internal.Ambiguous;
import objectos.html.internal.Bytes;
import objectos.html.internal.StandardAttributeName;
import objectos.html.internal.StandardElementName;
import org.testng.annotations.Test;

public class HtmlTestInternal {

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

    // document
    compiler.compile();

    testAux(
        compiler,

        Html._DOCUMENT_START,
        Bytes.encodeInt0(0), Bytes.encodeInt1(0), Bytes.encodeInt2(0)
    );

    // document.nodes
    compiler.documentIterable();

    testAux(
        compiler,

        Html._DOCUMENT_NODES_ITERABLE,
        Bytes.encodeInt0(0), Bytes.encodeInt1(0), Bytes.encodeInt2(0)
    );

    // document.nodes.iterator
    compiler.documentIterator();

    testAux(
        compiler,

        Html._DOCUMENT_NODES_ITERATOR,
        Bytes.encodeInt0(0), Bytes.encodeInt1(0), Bytes.encodeInt2(0)
    );

    // document.nodes.hasNext
    assertEquals(compiler.documentHasNext(), true);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_HAS_NEXT,
        Bytes.encodeInt0(0), Bytes.encodeInt1(0), Bytes.encodeInt2(0)
    );

    // document.nodes.next
    PseudoHtmlElement elem;
    elem = (PseudoHtmlElement) compiler.documentNext();

    assertEquals(elem.name, StandardElementName.HTML);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),

        Html._ELEMENT_START,
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        len(4)
    );

    // element.attributes
    compiler.elementAttributes();

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),

        Html._ELEMENT_ATTRS_ITERABLE,
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        len(4)
    );

    // element.attributes.iterator
    compiler.elementAttributesIterator();

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),

        Html._ELEMENT_ATTRS_ITERATOR,
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        len(4)
    );

    // element.attributes.hasNext
    assertEquals(compiler.elementAttributesHasNext(StandardElementName.HTML), false);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),

        Html._ELEMENT_ATTRS_EXHAUSTED,
        Bytes.encodeInt0(5), Bytes.encodeInt1(5), Bytes.encodeInt2(5),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        len(4)
    );

    // element.nodes
    compiler.elementNodes();

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),

        Html._ELEMENT_NODES_ITERABLE,
        Bytes.encodeInt0(5), Bytes.encodeInt1(5), Bytes.encodeInt2(5),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        len(4)
    );

    // element.nodes.iterator
    compiler.elementNodesIterator();

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),

        Html._ELEMENT_NODES_ITERATOR,
        Bytes.encodeInt0(5), Bytes.encodeInt1(5), Bytes.encodeInt2(5),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        len(4)
    );

    // element.nodes.hasNext
    assertEquals(compiler.elementNodesHasNext(), false);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),

        Html._ELEMENT_NODES_EXHAUSTED,
        Bytes.encodeInt0(5), Bytes.encodeInt1(5), Bytes.encodeInt2(5),
        Bytes.encodeInt0(5), Bytes.encodeInt1(5), Bytes.encodeInt2(5),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        len(4)
    );

    // document.nodes.hasNext
    assertEquals(compiler.documentHasNext(), false);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_EXHAUSTED,
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8)
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
    compiler.elementValue(AttributeInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

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

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),

        Html._ELEMENT_ATTRS_ITERATOR,
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        len(4)
    );

    // html.attrs.iterator.hasNext
    assertEquals(compiler.elementAttributesHasNext(StandardElementName.HTML), true);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),

        Html._ELEMENT_ATTRS_HAS_NEXT,
        Bytes.encodeInt0(10), Bytes.encodeInt1(10), Bytes.encodeInt2(10),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        len(4)
    );

    // html.attrs.iterator.next => lang="pt-BR"
    PseudoHtmlAttribute attr;
    attr = (PseudoHtmlAttribute) compiler.elementAttributesNext();

    assertEquals(attr.name(), "lang");
    assertEquals(attr.value, "pt-BR");

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),

        Html._ELEMENT_ATTRS_NEXT,
        Bytes.encodeInt0(12), Bytes.encodeInt1(12), Bytes.encodeInt2(12),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        len(4)
    );

    // lang="pt-BR".values
    compiler.attributeValues();

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),

        Html._ATTRIBUTE_VALUES_ITERABLE,
        Bytes.encodeInt0(12), Bytes.encodeInt1(12), Bytes.encodeInt2(12),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        len(4)
    );

    // lang="pt-BR".values.iterator
    compiler.attributeValuesIterator();

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),

        Html._ATTRIBUTE_VALUES_ITERATOR,
        Bytes.encodeInt0(12), Bytes.encodeInt1(12), Bytes.encodeInt2(12),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        len(4)
    );

    // lang="pt-BR".values.iterator.hasNext
    assertEquals(compiler.attributeValuesHasNext(), true);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),

        Html._ATTRIBUTE_VALUES_HAS_NEXT,
        Bytes.encodeInt0(12), Bytes.encodeInt1(12), Bytes.encodeInt2(12),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        len(4)
    );

    assertEquals(compiler.attributeValuesNext(attr.value), "pt-BR");

    attr.value = null;

    // lang="pt-BR".values.iterator.hasNext
    assertEquals(compiler.attributeValuesHasNext(), false);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),

        Html._ATTRIBUTE_VALUES_EXHAUSTED,
        Bytes.encodeInt0(12), Bytes.encodeInt1(12), Bytes.encodeInt2(12),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        len(4)
    );

    // html.attributes.hasNext
    assertEquals(compiler.elementAttributesHasNext(StandardElementName.HTML), false);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),

        Html._ELEMENT_ATTRS_EXHAUSTED,
        Bytes.encodeInt0(12), Bytes.encodeInt1(12), Bytes.encodeInt2(12),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        len(4)
    );
  }

  @Test(description = """
  <html><head></head></html>
  """)
  public void testCase03() {
    Html compiler;
    compiler = new Html();

    compiler.compilationBegin();

    compiler.elementBegin(StandardElementName.HEAD);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(ElementInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    // document
    compiler.compile();
    compiler.documentIterable();
    compiler.documentIterator();
    assertEquals(compiler.documentHasNext(), true);

    // html
    PseudoHtmlElement html;
    html = (PseudoHtmlElement) compiler.documentNext();

    assertEquals(html.name, StandardElementName.HTML);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(18), Bytes.encodeInt1(18), Bytes.encodeInt2(18),

        Html._ELEMENT_START,
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(18), Bytes.encodeInt1(18), Bytes.encodeInt2(18),
        len(4)
    );

    // html.attributes
    compiler.elementAttributes();
    compiler.elementAttributesIterator();
    assertEquals(compiler.elementAttributesHasNext(StandardElementName.HTML), false);

    // html.nodes
    compiler.elementNodes();

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(18), Bytes.encodeInt1(18), Bytes.encodeInt2(18),

        Html._ELEMENT_NODES_ITERABLE,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(18), Bytes.encodeInt1(18), Bytes.encodeInt2(18),
        len(4)
    );

    // html.nodes.iterator
    compiler.elementNodesIterator();

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(18), Bytes.encodeInt1(18), Bytes.encodeInt2(18),

        Html._ELEMENT_NODES_ITERATOR,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(18), Bytes.encodeInt1(18), Bytes.encodeInt2(18),
        len(4)
    );

    // html.nodes.hasNext
    assertEquals(compiler.elementNodesHasNext(), true);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(18), Bytes.encodeInt1(18), Bytes.encodeInt2(18),

        Html._ELEMENT_NODES_HAS_NEXT,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        Bytes.encodeInt0(13), Bytes.encodeInt1(13), Bytes.encodeInt2(13),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(18), Bytes.encodeInt1(18), Bytes.encodeInt2(18),
        len(4)
    );

    // head
    PseudoHtmlElement head;
    head = (PseudoHtmlElement) compiler.elementNodesNext();

    assertEquals(head.name(), "head");

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(18), Bytes.encodeInt1(18), Bytes.encodeInt2(18),

        Html._ELEMENT_NODES_NEXT,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(18), Bytes.encodeInt1(18), Bytes.encodeInt2(18),
        len(4),

        Html._ELEMENT_START,
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        len(14)
    );

    // head.attributes
    compiler.elementAttributes();
    compiler.elementAttributesIterator();
    assertEquals(compiler.elementAttributesHasNext(StandardElementName.HEAD), false);

    // head.nodes
    compiler.elementNodes();
    compiler.elementNodesIterator();
    assertEquals(compiler.elementNodesHasNext(), false);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(18), Bytes.encodeInt1(18), Bytes.encodeInt2(18),

        Html._ELEMENT_NODES_NEXT,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(18), Bytes.encodeInt1(18), Bytes.encodeInt2(18),
        len(4),

        Html._ELEMENT_NODES_EXHAUSTED,
        Bytes.encodeInt0(5), Bytes.encodeInt1(5), Bytes.encodeInt2(5),
        Bytes.encodeInt0(5), Bytes.encodeInt1(5), Bytes.encodeInt2(5),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        len(14)
    );

    // html.nodes.hasNext
    assertEquals(compiler.elementNodesHasNext(), false);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(18), Bytes.encodeInt1(18), Bytes.encodeInt2(18),

        Html._ELEMENT_NODES_EXHAUSTED,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(18), Bytes.encodeInt1(18), Bytes.encodeInt2(18),
        len(4)
    );

    // document.nodes.hasNext
    assertEquals(compiler.documentHasNext(), false);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_EXHAUSTED,
        Bytes.encodeInt0(18), Bytes.encodeInt1(18), Bytes.encodeInt2(18)
    );
  }

  @Test(description = """
  Ambiguous
  """)
  public void testCase16() {
    Html compiler;
    compiler = new Html();

    compiler.compilationBegin();

    compiler.ambiguous(Ambiguous.TITLE, "element");

    compiler.elementBegin(StandardElementName.HEAD);
    compiler.elementValue(ElementInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.ambiguous(Ambiguous.TITLE, "attribute");

    compiler.elementBegin(StandardElementName.BODY);
    compiler.elementValue(ElementInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.elementBegin(StandardElementName.HTML);
    compiler.elementValue(ElementInstruction.INSTANCE);
    compiler.elementValue(ElementInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    // document
    compiler.compile();
    compiler.documentIterable();
    compiler.documentIterator();
    assertEquals(compiler.documentHasNext(), true);

    // html
    PseudoHtmlElement html;
    html = (PseudoHtmlElement) compiler.documentNext();
    assertEquals(html.name, StandardElementName.HTML);

    // html.attributes
    compiler.elementAttributes();
    compiler.elementAttributesIterator();
    assertEquals(compiler.elementAttributesHasNext(StandardElementName.HTML), false);

    // html.nodes
    compiler.elementNodes();
    compiler.elementNodesIterator();
    assertEquals(compiler.elementNodesHasNext(), true);

    // head
    PseudoHtmlElement head;
    head = (PseudoHtmlElement) compiler.elementNodesNext();
    assertEquals(head.name, StandardElementName.HEAD);

    // head.attributes
    compiler.elementAttributes();
    compiler.elementAttributesIterator();
    assertEquals(compiler.elementAttributesHasNext(StandardElementName.HEAD), false);

    // head.nodes
    compiler.elementNodes();
    compiler.elementNodesIterator();
    assertEquals(compiler.elementNodesHasNext(), true);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(42), Bytes.encodeInt1(42), Bytes.encodeInt2(42),

        Html._ELEMENT_NODES_NEXT,
        Bytes.encodeInt0(39), Bytes.encodeInt1(39), Bytes.encodeInt2(39),
        Bytes.encodeInt0(37), Bytes.encodeInt1(37), Bytes.encodeInt2(37),
        Bytes.encodeInt0(33), Bytes.encodeInt1(33), Bytes.encodeInt2(33),
        Bytes.encodeInt0(42), Bytes.encodeInt1(42), Bytes.encodeInt2(42),
        len(4),

        Html._ELEMENT_NODES_HAS_NEXT,
        Bytes.encodeInt0(12), Bytes.encodeInt1(12), Bytes.encodeInt2(12),
        Bytes.encodeInt0(10), Bytes.encodeInt1(10), Bytes.encodeInt2(10),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),
        Bytes.encodeInt0(37), Bytes.encodeInt1(37), Bytes.encodeInt2(37),
        len(14)
    );

    // title
    PseudoHtmlElement title;
    title = (PseudoHtmlElement) compiler.elementNodesNext();
    assertEquals(title.name, StandardElementName.TITLE);
  }

  @Test(description = """
  HtmlTemplate TC31

  - email input
  """)
  public void testCase31() {
    Html compiler;
    compiler = new Html();

    compiler.compilationBegin();

    compiler.attribute(StandardAttributeName.TYPE, "email");
    compiler.attribute(StandardAttributeName.REQUIRED);

    compiler.elementBegin(StandardElementName.INPUT);
    compiler.elementValue(AttributeInstruction.INSTANCE);
    compiler.elementValue(AttributeInstruction.INSTANCE);
    compiler.elementEnd();

    compiler.compilationEnd();

    // document
    compiler.compile();
    compiler.documentIterable();
    compiler.documentIterator();
    assertEquals(compiler.documentHasNext(), true);

    // input
    PseudoHtmlElement input;
    input = (PseudoHtmlElement) compiler.documentNext();
    assertEquals(input.name(), "input");

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(20), Bytes.encodeInt1(20), Bytes.encodeInt2(20),

        Html._ELEMENT_START,
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(20), Bytes.encodeInt1(20), Bytes.encodeInt2(20),
        len(4)
    );

    // input.attrs
    compiler.elementAttributes();
    compiler.elementAttributesIterator();
    assertEquals(compiler.elementAttributesHasNext(StandardElementName.INPUT), true);

    // input[type]
    PseudoHtmlAttribute type;
    type = (PseudoHtmlAttribute) compiler.elementAttributesNext();
    assertEquals(type.name(), "type");

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(20), Bytes.encodeInt1(20), Bytes.encodeInt2(20),

        Html._ELEMENT_ATTRS_NEXT,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(20), Bytes.encodeInt1(20), Bytes.encodeInt2(20),
        len(4)
    );

    // input[type].values
    compiler.attributeValues();
    compiler.attributeValuesIterator();
    assertEquals(compiler.attributeValuesHasNext(), true);
    assertEquals(compiler.attributeValuesNext(type.value), "email");

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(20), Bytes.encodeInt1(20), Bytes.encodeInt2(20),

        Html._ATTRIBUTE_VALUES_NEXT,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(20), Bytes.encodeInt1(20), Bytes.encodeInt2(20),
        len(4)
    );

    // input[type].values.hasNext
    type.value = null;
    assertEquals(compiler.attributeValuesHasNext(), false);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(20), Bytes.encodeInt1(20), Bytes.encodeInt2(20),

        Html._ATTRIBUTE_VALUES_EXHAUSTED,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(20), Bytes.encodeInt1(20), Bytes.encodeInt2(20),
        len(4)
    );

    // input.attrs.hasNext
    assertEquals(compiler.elementAttributesHasNext(StandardElementName.INPUT), true);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(20), Bytes.encodeInt1(20), Bytes.encodeInt2(20),

        Html._ELEMENT_ATTRS_HAS_NEXT,
        Bytes.encodeInt0(15), Bytes.encodeInt1(15), Bytes.encodeInt2(15),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(20), Bytes.encodeInt1(20), Bytes.encodeInt2(20),
        len(4)
    );

    // input[required]
    PseudoHtmlAttribute required;
    required = (PseudoHtmlAttribute) compiler.elementAttributesNext();
    assertEquals(required.name(), "required");

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(20), Bytes.encodeInt1(20), Bytes.encodeInt2(20),

        Html._ELEMENT_ATTRS_NEXT,
        Bytes.encodeInt0(17), Bytes.encodeInt1(17), Bytes.encodeInt2(17),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(20), Bytes.encodeInt1(20), Bytes.encodeInt2(20),
        len(4)
    );

    // input.attrs.hasNext
    assertEquals(compiler.elementAttributesHasNext(StandardElementName.INPUT), false);

    testAux(
        compiler,

        Html._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(20), Bytes.encodeInt1(20), Bytes.encodeInt2(20),

        Html._ELEMENT_ATTRS_EXHAUSTED,
        Bytes.encodeInt0(17), Bytes.encodeInt1(17), Bytes.encodeInt2(17),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(11), Bytes.encodeInt1(11), Bytes.encodeInt2(11),
        Bytes.encodeInt0(20), Bytes.encodeInt1(20), Bytes.encodeInt2(20),
        len(4)
    );
  }

  private byte len(int value) {
    return (byte) value;
  }

  private void testAux(Html compiler, byte... expected) {
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