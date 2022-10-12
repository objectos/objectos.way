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
import br.com.objectos.code.java.io.NewLineFormatting.NewLineFormattingAction;
import br.com.objectos.code.java.statement.VariableInitializer;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedType;
import br.com.objectos.code.java.type.NamedVoid;
import java.util.List;
import objectos.lang.Check;
import objectos.util.GrowableList;

public final class FieldCode extends AbstractImmutableCodeElement
    implements
    ClassBodyElement,
    EnumBodyElement,
    InterfaceBodyElement {

  private FieldCode(CodeElement... elements) {
    super(elements);
  }

  @Ignore("AggregatorGenProcessor")
  public static Builder builder() {
    return new Builder();
  }

  public static FieldCode field(
      FieldCodeElement e1,
      FieldCodeElement e2) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Builder b = FieldCode.builder();
    e1.acceptFieldCodeBuilder(b);
    e2.acceptFieldCodeBuilder(b);
    return b.build();
  }

  public static FieldCode field(
      FieldCodeElement e1,
      FieldCodeElement e2,
      FieldCodeElement e3) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Builder b = FieldCode.builder();
    e1.acceptFieldCodeBuilder(b);
    e2.acceptFieldCodeBuilder(b);
    e3.acceptFieldCodeBuilder(b);
    return b.build();
  }

  public static FieldCode field(
      FieldCodeElement e1,
      FieldCodeElement e2,
      FieldCodeElement e3,
      FieldCodeElement e4) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Builder b = FieldCode.builder();
    e1.acceptFieldCodeBuilder(b);
    e2.acceptFieldCodeBuilder(b);
    e3.acceptFieldCodeBuilder(b);
    e4.acceptFieldCodeBuilder(b);
    return b.build();
  }

  public static FieldCode field(
      FieldCodeElement e1,
      FieldCodeElement e2,
      FieldCodeElement e3,
      FieldCodeElement e4,
      FieldCodeElement e5) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Builder b = FieldCode.builder();
    e1.acceptFieldCodeBuilder(b);
    e2.acceptFieldCodeBuilder(b);
    e3.acceptFieldCodeBuilder(b);
    e4.acceptFieldCodeBuilder(b);
    e5.acceptFieldCodeBuilder(b);
    return b.build();
  }

  public static FieldCodeDeclarator init(Identifier name, VariableInitializer initializer) {
    Check.notNull(name, "name == null");
    Check.notNull(initializer, "initializer == null");
    return FieldCodeDeclarator.init0(name, initializer);
  }

  @Override
  public final void acceptClassCodeBuilder(ClassCode.Builder builder) {
    builder.addField(this);
  }

  @Override
  public final void acceptEnumCodeBuilder(EnumCode.Builder builder) {
    builder.addField(this);
  }

  @Override
  public final void acceptInterfaceCodeBuilder(InterfaceCode.Builder builder) {
    builder.addField(this);
  }

  @Override
  public final void acceptNewLineFormattingAction(NewLineFormattingAction action) {
    action.propagateElement(this);
  }

  @Override
  public final Kind kind() {
    return Kind.FIELD;
  }

  public static class Builder {

    private final List<FieldCodeDeclarator> declarators = new GrowableList<>();

    private final GrowableList<CodeElement> modifierSet = new GrowableList<>();

    private final List<String> names = new GrowableList<>();

    private NamedType typeName = NamedVoid._void();

    private Builder() {}

    public final Builder _final() {
      return addModifier(Modifiers._final());
    }

    public final Builder _private() {
      return addModifier(Modifiers._private());
    }

    public final Builder _protected() {
      return addModifier(Modifiers._protected());
    }

    public final Builder _public() {
      return addModifier(Modifiers._public());
    }

    public final Builder _static() {
      return addModifier(Modifiers._static());
    }

    public final Builder _transient() {
      return addModifier(Modifiers._transient());
    }

    public final Builder _volatile() {
      return addModifier(Modifiers._volatile());
    }

    public final Builder addDeclarator(FieldCodeDeclarator declarator) {
      Check.notNull(declarator, "declarator == null");
      return addDeclarator0(declarator);
    }

    public final Builder addDeclarator(Identifier name) {
      Check.notNull(name, "name == null");
      return addDeclarator0(FieldCodeDeclarator.init0(name));
    }

    public final Builder addDeclarator(Identifier name, VariableInitializer initializer) {
      Check.notNull(name, "name == null");
      Check.notNull(initializer, "initializer == null");
      return addDeclarator0(FieldCodeDeclarator.init0(name, initializer));
    }

    public final Builder addDeclarator(String name) {
      return addDeclarator(Expressions.id(name));
    }

    public final Builder addModifier(FieldModifier modifier) {
      modifierSet.addWithNullMessage(modifier, "modifier == null");
      return this;
    }

    public final Builder addModifiers(Iterable<FieldModifier> modifiers) {
      modifierSet.addAllIterable(modifiers);
      return this;
    }

    public final FieldCode build() {
      return new FieldCode(
          spaceSeparated(modifierSet),
          modifierSet.isEmpty() ? noop() : space(),
          typeName,
          space(),
          commaSeparated(declarators),
          semicolon()
      );
    }

    public final Builder type(Class<?> type) {
      typeName = NamedClass.of(type);
      return this;
    }

    public final Builder type(NamedType typeName) {
      this.typeName = Check.notNull(typeName, "typeName == null");
      return this;
    }

    private Builder addDeclarator0(FieldCodeDeclarator declarator) {
      String key = declarator.name();

      if (!names.add(key)) {
        throw new IllegalArgumentException(
            "A declarator with name " + key + " was already defined");
      }

      declarators.add(declarator);

      return this;
    }

  }

}