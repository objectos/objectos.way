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

import objectos.html.pseudom.HtmlAttribute;
import objectos.html.pseudom.HtmlDocument;
import objectos.html.pseudom.HtmlDocumentType;
import objectos.html.pseudom.HtmlElement;

public final class PrettyPrintWriter2 extends Writer2 {

  @Override
  public final void process(HtmlDocument document) {
    for (var node : document.nodes()) {
      if (node instanceof HtmlDocumentType) {
        documentType();
      } else if (node instanceof HtmlElement element) {
        topLevelElement(element);
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

    write(' ');
    write(name);

    if (attribute.isBoolean()) {
      return;
    }

    var values = attribute.values();

    var valuesIter = values.iterator();

    if (valuesIter.hasNext()) {
      write(valuesIter.next());

      while (valuesIter.hasNext()) {
        write(' ');
        write(valuesIter.next());
      }
    }
  }

  private void documentType() {
    write("<!DOCTYPE html>");

    nl();
  }

  private void nl() {
    write(System.lineSeparator());
  }

  private void startTag(HtmlElement element) {
    var name = element.name();

    write('<');
    write(name);

    for (var attribute : element.attributes()) {
      attribute(attribute);
    }

    write('>');
  }

  private void topLevelElement(HtmlElement element) {
    element(element);

    nl();
  }

  private void element(HtmlElement element) {
    startTag(element);

    if (element.isVoid()) {
      return;
    }

    for (var node : element.nodes()) {
      var type = node.getClass();

      throw new UnsupportedOperationException(
        "Implement me :: type=" + type
      );
    }

    endTag(element);
  }

  private void endTag(HtmlElement element) {
    write('<');
    write('/');
    write(element.name());
    write('>');
  }

}