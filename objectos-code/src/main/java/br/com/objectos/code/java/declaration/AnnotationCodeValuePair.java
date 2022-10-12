/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.declaration;

import br.com.objectos.code.java.declaration.AnnotationCode.Builder;
import br.com.objectos.code.java.element.AbstractCodeElement;
import br.com.objectos.code.java.expression.Expressions;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.io.CodeWriter;
import objectos.lang.Check;

public class AnnotationCodeValuePair extends AbstractCodeElement implements AnnotationCodeElement {

  private final Identifier name;
  private final AnnotationCodeValue value;

  private AnnotationCodeValuePair(Identifier name, AnnotationCodeValue value) {
    this.name = name;
    this.value = value;
  }

  public static AnnotationCodeValuePair value(String name, AnnotationCodeValue value) {
    Identifier id = Expressions.id(name);
    Check.notNull(value, "value == null");
    return new AnnotationCodeValuePair(id, value);
  }

  @Override
  public final void acceptAnnotationCodeBuilder(Builder builder) {
    builder.addUnchecked(this);
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writeCodeElement(name);
    w.writeCodeElement(space());
    w.writeCodeElement(equals());
    w.writeCodeElement(space());
    w.writeCodeElement(value);
    return w;
  }

  public final String name() {
    return name.name();
  }

}