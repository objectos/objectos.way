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

import java.util.Arrays;
import org.testng.annotations.Test;

public class HtmlCompiler02TestDocument {

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
    compiler.bootstrap();

    testAux(
        compiler,

        HtmlCompiler02._DOCUMENT_START,
        Bytes.encodeInt0(0), Bytes.encodeInt1(0), Bytes.encodeInt2(0)
    );

    // document.nodes
    compiler.documentIterable();

    testAux(
        compiler,

        HtmlCompiler02._DOCUMENT_NODES_ITERABLE,
        Bytes.encodeInt0(0), Bytes.encodeInt1(0), Bytes.encodeInt2(0)
    );

    // document.nodes.iterator
    compiler.documentIterator();

    testAux(
        compiler,

        HtmlCompiler02._DOCUMENT_NODES_ITERATOR,
        Bytes.encodeInt0(0), Bytes.encodeInt1(0), Bytes.encodeInt2(0)
    );

    // document.nodes.hasNext
    assertEquals(compiler.documentHasNext(), true);

    testAux(
        compiler,

        HtmlCompiler02._DOCUMENT_NODES_HAS_NEXT,
        Bytes.encodeInt0(0), Bytes.encodeInt1(0), Bytes.encodeInt2(0)
    );

    // document.nodes.next
    PseudoHtmlElement elem;
    elem = (PseudoHtmlElement) compiler.documentNext();

    assertEquals(elem.name, StandardElementName.HTML);

    testAux(
        compiler,

        HtmlCompiler02._ELEMENT_START,
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3), // @ ByteProto.STD_NAME
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),

        HtmlCompiler02._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8)
    );

    // element.attributes
    compiler.elementAttributes();

    testAux(
        compiler,

        HtmlCompiler02._ELEMENT_ATTRS_ITERABLE,
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(0), Bytes.encodeInt1(0), Bytes.encodeInt2(0),

        HtmlCompiler02._ELEMENT_START,
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),

        HtmlCompiler02._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8)
    );

    // element.attributes.iterator
    compiler.elementAttributesIterator();

    testAux(
        compiler,

        HtmlCompiler02._ELEMENT_ATTRS_ITERATOR,
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(0), Bytes.encodeInt1(0), Bytes.encodeInt2(0),

        HtmlCompiler02._ELEMENT_START,
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3), // @ ByteProto.STD_NAME
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),

        HtmlCompiler02._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8)
    );

    // element.attributes.hasNext
    assertEquals(compiler.elementAttributesHasNext(StandardElementName.HTML), false);

    testAux(
        compiler,

        HtmlCompiler02._ELEMENT_START,
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3), // @ ByteProto.STD_NAME
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),

        HtmlCompiler02._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8)
    );

    // element.nodes
    compiler.elementNodes();

    testAux(
        compiler,

        HtmlCompiler02._ELEMENT_NODES_ITERABLE,
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),

        HtmlCompiler02._ELEMENT_START,
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3), // @ ByteProto.STD_NAME
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),

        HtmlCompiler02._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8)
    );

    // element.nodes.iterator
    compiler.elementNodesIterator();

    testAux(
        compiler,

        HtmlCompiler02._ELEMENT_NODES_ITERATOR,
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3),

        HtmlCompiler02._ELEMENT_START,
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3), // @ ByteProto.STD_NAME
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),

        HtmlCompiler02._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8)
    );

    // element.nodes.hasNext
    assertEquals(compiler.elementNodesHasNext(StandardElementName.HTML), false);

    testAux(
        compiler,

        HtmlCompiler02._ELEMENT_START,
        Bytes.encodeInt0(3), Bytes.encodeInt1(3), Bytes.encodeInt2(3), // @ ByteProto.STD_NAME
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8),

        HtmlCompiler02._DOCUMENT_NODES_NEXT,
        Bytes.encodeInt0(8), Bytes.encodeInt1(8), Bytes.encodeInt2(8)
    );

    assertEquals(compiler.documentHasNext(), false);
  }

  private void testAux(HtmlCompiler02 compiler, byte... expected) {
    byte[] aux;
    aux = compiler.aux;

    int length;
    length = compiler.auxIndex + 1;

    byte[] result;
    result = new byte[length];

    for (int i = 0; i < length; i++) {
      result[i] = aux[length - i - 1];
    }

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