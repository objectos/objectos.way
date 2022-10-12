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

import static br.com.objectos.code.java.element.NewLine.nextLine;

import br.com.objectos.code.annotations.Ignore;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.element.Symbols;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.io.Section;
import br.com.objectos.code.java.statement.Block;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedClassOrParameterized;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public final class EnumCode extends AbstractTypeCode {

  private final String simpleName;

  private EnumCode(String simpleName, CodeElement... elements) {
    super(elements);
    this.simpleName = simpleName;
  }

  public static EnumCode _enum(EnumCodeElement e1) {
    Check.notNull(e1, "e1 == null");
    Builder b = builder();
    e1.acceptEnumCodeBuilder(b);
    return b.build();
  }

  public static EnumCode _enum(EnumCodeElement... elements) {
    Check.notNull(elements, "elements == null");
    Builder b = builder();

    for (int i = 0; i < elements.length; i++) {
      EnumCodeElement e = Check.notNull(elements[i], "elements[" + i + "] == null");
      e.acceptEnumCodeBuilder(b);
    }

    return b.build();
  }

  public static EnumCode _enum(
      EnumCodeElement e1,
      EnumCodeElement e2) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Builder b = builder();
    e1.acceptEnumCodeBuilder(b);
    e2.acceptEnumCodeBuilder(b);
    return b.build();
  }

  public static EnumCode _enum(
      EnumCodeElement e1,
      EnumCodeElement e2,
      EnumCodeElement e3) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Builder b = builder();
    e1.acceptEnumCodeBuilder(b);
    e2.acceptEnumCodeBuilder(b);
    e3.acceptEnumCodeBuilder(b);
    return b.build();
  }

  public static EnumCode _enum(
      EnumCodeElement e1,
      EnumCodeElement e2,
      EnumCodeElement e3,
      EnumCodeElement e4) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Builder b = builder();
    e1.acceptEnumCodeBuilder(b);
    e2.acceptEnumCodeBuilder(b);
    e3.acceptEnumCodeBuilder(b);
    e4.acceptEnumCodeBuilder(b);
    return b.build();
  }

  public static EnumCode _enum(
      EnumCodeElement e1,
      EnumCodeElement e2,
      EnumCodeElement e3,
      EnumCodeElement e4,
      EnumCodeElement e5) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Builder b = builder();
    e1.acceptEnumCodeBuilder(b);
    e2.acceptEnumCodeBuilder(b);
    e3.acceptEnumCodeBuilder(b);
    e4.acceptEnumCodeBuilder(b);
    e5.acceptEnumCodeBuilder(b);
    return b.build();
  }

  public static EnumCode _enum(
      EnumCodeElement e1,
      EnumCodeElement e2,
      EnumCodeElement e3,
      EnumCodeElement e4,
      EnumCodeElement e5,
      EnumCodeElement e6) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Builder b = builder();
    e1.acceptEnumCodeBuilder(b);
    e2.acceptEnumCodeBuilder(b);
    e3.acceptEnumCodeBuilder(b);
    e4.acceptEnumCodeBuilder(b);
    e5.acceptEnumCodeBuilder(b);
    e6.acceptEnumCodeBuilder(b);
    return b.build();
  }

  public static EnumCode _enum(
      EnumCodeElement e1,
      EnumCodeElement e2,
      EnumCodeElement e3,
      EnumCodeElement e4,
      EnumCodeElement e5,
      EnumCodeElement e6,
      EnumCodeElement e7) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Builder b = builder();
    e1.acceptEnumCodeBuilder(b);
    e2.acceptEnumCodeBuilder(b);
    e3.acceptEnumCodeBuilder(b);
    e4.acceptEnumCodeBuilder(b);
    e5.acceptEnumCodeBuilder(b);
    e6.acceptEnumCodeBuilder(b);
    e7.acceptEnumCodeBuilder(b);
    return b.build();
  }

  public static EnumCode _enum(
      EnumCodeElement e1,
      EnumCodeElement e2,
      EnumCodeElement e3,
      EnumCodeElement e4,
      EnumCodeElement e5,
      EnumCodeElement e6,
      EnumCodeElement e7,
      EnumCodeElement e8) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Check.notNull(e8, "e8 == null");
    Builder b = builder();
    e1.acceptEnumCodeBuilder(b);
    e2.acceptEnumCodeBuilder(b);
    e3.acceptEnumCodeBuilder(b);
    e4.acceptEnumCodeBuilder(b);
    e5.acceptEnumCodeBuilder(b);
    e6.acceptEnumCodeBuilder(b);
    e7.acceptEnumCodeBuilder(b);
    e8.acceptEnumCodeBuilder(b);
    return b.build();
  }

  public static EnumCode _enum(
      EnumCodeElement e1,
      EnumCodeElement e2,
      EnumCodeElement e3,
      EnumCodeElement e4,
      EnumCodeElement e5,
      EnumCodeElement e6,
      EnumCodeElement e7,
      EnumCodeElement e8,
      EnumCodeElement e9) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Check.notNull(e8, "e8 == null");
    Check.notNull(e9, "e9 == null");
    Builder b = builder();
    e1.acceptEnumCodeBuilder(b);
    e2.acceptEnumCodeBuilder(b);
    e3.acceptEnumCodeBuilder(b);
    e4.acceptEnumCodeBuilder(b);
    e5.acceptEnumCodeBuilder(b);
    e6.acceptEnumCodeBuilder(b);
    e7.acceptEnumCodeBuilder(b);
    e8.acceptEnumCodeBuilder(b);
    e9.acceptEnumCodeBuilder(b);
    return b.build();
  }

  @Ignore("AggregatorGenProcessor")
  public static Builder builder() {
    return new Builder();
  }

  @Override
  public final String simpleName() {
    return simpleName;
  }

  public static class Builder {

    private Implements _implements;
    private final Implements.Builder _implementsBuilder = Implements.builder();
    private final GrowableList<AnnotationCode> annotations = new GrowableList<>();
    private final GrowableList<EnumBodyElement> bodyElements = new GrowableList<>();
    private final EnumConstantList.Builder constantListBuilder = EnumConstantList.builder();
    private final GrowableList<EnumModifier> modifiers = new GrowableList<>();
    private String simpleName = "Unnamed";

    private Builder() {}

    public final Builder addAnnotation(AnnotationCode annotation) {
      annotations.addWithNullMessage(annotation, "annotation == null");
      return this;
    }

    public final Builder addConstructor(ConstructorCode constructor) {
      bodyElements.addWithNullMessage(constructor, "constructor == null");
      return this;
    }

    public final Builder addEnumConstant(EnumConstantCode constant) {
      constantListBuilder.addEnumConstant(constant);
      return this;
    }

    public final Builder addEnumConstants(Iterable<? extends EnumConstantCode> constants) {
      this.constantListBuilder.addEnumConstants(constants);
      return this;
    }

    public final Builder addField(FieldCode field) {
      bodyElements.addWithNullMessage(field, "field == null");
      return this;
    }

    public final Builder addImplementedInterface(NamedClassOrParameterized iface) {
      _implementsBuilder.addWithNullMessage(iface, "iface == null");
      return this;
    }

    public final Builder addImplementedInterfaces(
        Iterable<? extends NamedClassOrParameterized> ifaces) {
      _implementsBuilder.addAll(ifaces);
      return this;
    }

    public final Builder addMethod(MethodCode method) {
      bodyElements.addWithNullMessage(method, "method == null");
      return this;
    }

    public final Builder addModifier(EnumModifier modifier) {
      modifiers.addWithNullMessage(modifier, "modifier == null");
      return this;
    }

    public final EnumCode build() {
      return new EnumCode(
          simpleName,
          newLineSeparated(annotations),
          annotations.isEmpty() ? noop() : nextLine(),
          spaceSeparated(modifiers),
          modifiers.isEmpty() ? noop() : space(),
          Keywords._enum(), space(),
          new SimpleNameCodeElement(simpleName),
          _implements != null ? _implements : _implementsBuilder.build(),
          space(),
          buildBody(),
          popSimpleName()
      );
    }

    public final Builder simpleName(Identifier simpleName) {
      Check.notNull(simpleName, "simpleName == null");
      this.simpleName = simpleName.name();
      return this;
    }

    public final Builder simpleName(NamedClass className) {
      Check.notNull(className, "className == null");
      this.simpleName = className.getSimpleName();
      return this;
    }

    public final Builder simpleName(String simpleName) {
      this.simpleName = Check.notNull(simpleName, "simpleName == null");
      return this;
    }

    final void setImplementsUnchecked(Implements _implements) {
      this._implements = _implements;
    }

    final void uncheckedAddEnumConstants(UnmodifiableList<EnumConstantCode> list) {
      constantListBuilder.uncheckedAddEnumConstants(list);
    }

    private CodeElement buildBody() {
      EnumConstantList constantList = constantListBuilder.build();
      return constantList.isEmpty() && bodyElements.isEmpty()
          ? Block.empty()
          : new EnumBody(constantList, bodyElements);
    }

  }

  private static class EnumBody implements CodeElement {

    private final GrowableList<EnumBodyElement> bodyElements;
    private final EnumConstantList enumConstantList;

    EnumBody(EnumConstantList enumConstantList, GrowableList<EnumBodyElement> bodyElements) {
      this.enumConstantList = enumConstantList;
      this.bodyElements = bodyElements;
    }

    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      w.write('{');
      w.beginSection(Section.BLOCK);

      w.writeCodeElement(enumConstantList);

      if (bodyElements.isEmpty()) {
        w.nextLine();
        w.nextLine();
      }

      for (EnumBodyElement element : bodyElements) {
        w.nextLine();
        w.nextLine();
        w.writeCodeElement(element);
      }

      if (!bodyElements.isEmpty()) {
        w.nextLine();
        w.nextLine();
      }

      w.endSection();
      w.writeCodeElement(Symbols.closeBrace());
      return w;
    }

  }

}
