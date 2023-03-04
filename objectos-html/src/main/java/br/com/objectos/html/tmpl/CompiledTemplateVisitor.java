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
package br.com.objectos.html.tmpl;

import br.com.objectos.html.attribute.AttributeName;
import br.com.objectos.html.element.ElementName;
import objectos.util.UnmodifiableList;

public interface CompiledTemplateVisitor {

  void visitAttribute(AttributeName name);

  void visitAttribute(AttributeName name, UnmodifiableList<String> values);

  void visitAttribute(AttributeName name, String value);

  void visitAttribute(String name);

  void visitAttribute(String name, String value);

  void visitEndTag(ElementName name);

  void visitEndTag(String name);

  void visitRaw(String raw);

  void visitStartTag(ElementName name);

  void visitStartTag(String name);

  void visitStartTagEnd();

  void visitStartTagEndSelfClosing();

  void visitText(String text);

}
