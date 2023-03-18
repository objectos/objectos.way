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

import objectos.html.doc.DocumentProcessor;
import objectos.html.doc.HtmlAttribute;
import objectos.html.doc.HtmlDocument;
import objectos.html.doc.HtmlDocumentType;
import objectos.html.doc.HtmlElement;

public final class PrettyPrintWriter2 implements DocumentProcessor {

  @Override
  public final void process(HtmlDocument document) {
    for (var node : document.nodes()) {
      if (node instanceof HtmlDocumentType doctype) {
        doctype(doctype);
      } else if (node instanceof HtmlElement element) {
        element(element);
      } else {
        var type = node.getClass();

        throw new UnsupportedOperationException(
          "Implement me :: type=" + type
        );
      }
    }
  }

  private void attribute(HtmlAttribute attribute) {
    var name = attribute.name();

    write(name);

    if (attribute.isBoolean()) {
      return;
    }

    var values = attribute.values();

    var valueIterator = values.iterator();

    if (valueIterator.hasNext()) {
      write("=\"");
      write(valueIterator.next());

      while (valueIterator.hasNext()) {
        write(" ");
        write(valueIterator.next());
      }

      write("\"");
    }
  }

  private void doctype(HtmlDocumentType doctype) {}

  private void element(HtmlElement element) {
    var name = element.name();

    write("<");
    write(name);

    for (var attribute : element.attributes()) {
      write(" ");

      attribute(attribute);
    }

    write(">");

    if (element.isVoid()) {
      return;
    }

    for (var node : element.nodes()) {
      if (node instanceof HtmlElement child) {
        element(child);
      } else {
        var type = node.getClass();

        throw new UnsupportedOperationException(
          "Implement me :: type=" + type
        );
      }
    }

    write("</");
    write(name);
    write(">");
  }

  private void write(String s) {

  }

}