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
import br.com.objectos.code.java.expression.Expressions;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.expression.LambdaParameter;
import br.com.objectos.code.java.type.NamedArray;
import br.com.objectos.code.java.type.NamedType;
import br.com.objectos.code.java.type.NamedVoid;
import br.com.objectos.code.util.SimpleElementVisitor;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;

public class ParameterCode extends AbstractImmutableCodeElement
    implements
    LambdaParameter,
    ConstructorCodeElement,
    MethodCodeElement {

  private final Identifier name;
  private final ParameterTypeName type;

  private ParameterCode(Builder builder) {
    super(builder.elements());
    name = builder.name;
    type = builder.type;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Ignore("AggregatorGenProcessor")
  public static ParameterCode of(VariableElement element) {
    Check.notNull(element, "element == null");
    Element enclosingElement = element.getEnclosingElement();
    return enclosingElement.accept(FromVariableElementFactory.INSTANCE, element);
  }

  public static ParameterCode param(ParameterTypeName type, Identifier name) {
    Builder b = builder();
    b.setType(type);
    b.setName(name);
    return b.build();
  }

  public static ParamsShorthand params(Iterable<ParameterCode> parameters) {
    UnmodifiableList<ParameterCode> copy = UnmodifiableList.copyOf(parameters);

    return new ParamsShorthand(copy);
  }

  static ParameterCode ofUnchecked(ParameterTypeName typeName, String name) {
    Builder b = builder();
    b.setTypeUnchecked(typeName);
    b.setNameUnchecked(Expressions.id(name));
    return b.build();
  }

  @Override
  public final void acceptConstructorCodeBuilder(ConstructorCode.Builder builder) {
    builder.addParameter(this);
  }

  @Override
  public final void acceptMethodCodeBuilder(MethodCode.Builder builder) {
    builder.addParameter(this);
  }

  public final Identifier name() {
    return name;
  }

  public final ParameterTypeName type() {
    return type;
  }

  public static class Builder {

    private Identifier name = Declarations.unnamed();
    private ParameterTypeName type = NamedVoid._void();

    private Builder() {}

    public final ParameterCode build() {
      return new ParameterCode(this);
    }

    public UnmodifiableList<CodeElement> elements() {
      return UnmodifiableList.of(type, space(), name);
    }

    public final Builder setName(Identifier name) {
      return setNameUnchecked(Check.notNull(name, "name == null"));
    }

    public final Builder setName(String name) {
      return setNameUnchecked(Expressions.id(name));
    }

    public final Builder setType(ParameterTypeName type) {
      return setTypeUnchecked(Check.notNull(type, "type == null"));
    }

    final Builder setNameUnchecked(Identifier name) {
      this.name = name;
      return this;
    }

    final Builder setTypeUnchecked(ParameterTypeName type) {
      this.type = type;
      return this;
    }

  }

  public static class ParamsShorthand implements ConstructorCodeElement, MethodCodeElement {

    private final UnmodifiableList<ParameterCode> parameters;

    private ParamsShorthand(UnmodifiableList<ParameterCode> parameters) {
      this.parameters = parameters;
    }

    @Override
    public final void acceptConstructorCodeBuilder(ConstructorCode.Builder builder) {
      builder.addParameters(parameters);
    }

    @Override
    public final void acceptMethodCodeBuilder(MethodCode.Builder builder) {
      builder.addParameters(parameters);
    }

  }

  private static class FromVariableElementFactory
      extends SimpleElementVisitor<ParameterCode, VariableElement> {

    static FromVariableElementFactory INSTANCE = new FromVariableElementFactory();

    @Override
    public final ParameterCode visitExecutable(ExecutableElement e, VariableElement p) {
      return e.isVarArgs() ? visitExecutableVarArgs(e, p) : visitExecutableStandard(e, p);
    }

    @Override
    protected final ParameterCode defaultAction(Element e, VariableElement p) {
      throw new UnsupportedOperationException(
          "Cannot instantiate ParameterCode from " + e.getKind()
      );
    }

    private ParameterCode visitExecutableStandard(ExecutableElement e, VariableElement p) {
      TypeMirror type;
      type = p.asType();

      NamedType named;
      named = NamedType.of(type);

      return ofUnchecked(named, p.getSimpleName().toString());
    }

    private ParameterCode visitExecutableVarArgs(ExecutableElement e, VariableElement p) {
      List<? extends VariableElement> parameters;
      parameters = e.getParameters();

      VariableElement last;
      last = parameters.get(parameters.size() - 1);

      if (!last.equals(p)) {
        return visitExecutableStandard(e, p);
      }

      TypeMirror type;
      type = p.asType();

      NamedType named;
      named = NamedType.of(type);

      NamedArray namedArray = (NamedArray) named;

      return ofUnchecked(
          VarArgs.of(namedArray),
          p.getSimpleName().toString()
      );
    }

  }

}