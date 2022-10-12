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

import br.com.objectos.code.annotations.Ignore;
import br.com.objectos.code.java.element.AbstractImmutableCodeElement;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.expression.Arguments;
import br.com.objectos.code.java.expression.Identifier;
import objectos.lang.Check;

public class EnumConstantCode
    extends AbstractImmutableCodeElement
    implements
    EnumCodeElement {

  private EnumConstantCode(CodeElement... elements) {
    super(elements);
  }

  @Ignore("AggregatorGenProcessor")
  public static Builder builder() {
    return new Builder();
  }

  public static EnumConstantCode enumConstant(
      EnumConstantCodeElement e1) {
    Check.notNull(e1, "e1 == null");
    Builder b = builder();
    e1.acceptEnumConstantCodeBuilder(b);
    return b.build();
  }

  public static EnumConstantCode enumConstant(
      EnumConstantCodeElement e1,
      EnumConstantCodeElement e2) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Builder b = builder();
    e1.acceptEnumConstantCodeBuilder(b);
    e2.acceptEnumConstantCodeBuilder(b);
    return b.build();
  }

  @Override
  public final void acceptEnumCodeBuilder(EnumCode.Builder builder) {
    builder.addEnumConstant(this);
  }

  public static class Builder {

    private CodeElement arguments = noop();
    private Identifier simpleName = Declarations.UNNAMED_CONSTANT;

    private Builder() {}

    public final Builder arguments(Arguments arguments) {
      this.arguments = Check.notNull(arguments, "arguments == null");
      return this;
    }

    public final EnumConstantCode build() {
      return new EnumConstantCode(simpleName, arguments);
    }

    public final Builder simpleName(Identifier name) {
      this.simpleName = Check.notNull(name, "name == null");
      return this;
    }

  }

}
