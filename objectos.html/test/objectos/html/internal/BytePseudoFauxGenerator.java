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

public class BytePseudoFauxGenerator extends FauxGenerator {
  public static void main(String[] args) {
    var gen = new BytePseudoFauxGenerator();

    gen.execute();
  }

  @Override
  public final void execute() {
    comment("document");

    value("DOCUMENT_ITERABLE");
    value("DOCUMENT_ITERATOR");
    value("DOCUMENT_HAS_NEXT");
    value("DOCUMENT_EXHAUSTED");

    comment("instructions");

    value("DOCTYPE");
    value("NULL");
    value("RETURN");

    comment("element");

    value("ELEMENT");
    value("ELEMENT_START");
    value("ELEMENT_ATTRS_ITERABLE");
    value("ELEMENT_ATTRS_ITERATOR");
    value("ELEMENT_ATTRS_HAS_NEXT");
    value("ELEMENT_ATTRS_EXHAUSTED");
    value("ELEMENT_NODES_ITERABLE");
    value("ELEMENT_NODES_ITERATOR");
    value("ELEMENT_NODES_HAS_NEXT");
    value("ELEMENT_NODES_EXHAUSTED");

    comment("attribute");

    value("ATTR_NAME");
    value("ATTR_HAS_VALUE");
    value("ATTR_EXHAUSTED");

    comment("text");

    value("TEXT");
    value("RAW");
  }
}