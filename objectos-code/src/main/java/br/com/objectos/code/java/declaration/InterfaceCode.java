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
import br.com.objectos.code.java.element.NewLine;
import br.com.objectos.code.java.io.BodyFormatter;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedClassOrParameterized;
import java.lang.annotation.Annotation;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public final class InterfaceCode extends AbstractTypeCode {

  private final String simpleName;

  private InterfaceCode(String simpleName, CodeElement... elements) {
    super(elements);
    this.simpleName = simpleName;
  }

  public static InterfaceCode _interface(
      InterfaceCodeElement e1) {
    Check.notNull(e1, "e1 == null");
    Builder b = builder();
    e1.acceptInterfaceCodeBuilder(b);
    return b.build();
  }

  public static InterfaceCode _interface(InterfaceCodeElement... elements) {
    Check.notNull(elements, "elements == null");
    Builder b = builder();

    for (int i = 0; i < elements.length; i++) {
      InterfaceCodeElement element = Check.notNull(elements[i], "elements[" + i + "] == null");
      element.acceptInterfaceCodeBuilder(b);
    }

    return b.build();
  }

  public static InterfaceCode _interface(
      InterfaceCodeElement e1,
      InterfaceCodeElement e2) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Builder b = builder();
    e1.acceptInterfaceCodeBuilder(b);
    e2.acceptInterfaceCodeBuilder(b);
    return b.build();
  }

  public static InterfaceCode _interface(
      InterfaceCodeElement e1,
      InterfaceCodeElement e2,
      InterfaceCodeElement e3) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Builder b = builder();
    e1.acceptInterfaceCodeBuilder(b);
    e2.acceptInterfaceCodeBuilder(b);
    e3.acceptInterfaceCodeBuilder(b);
    return b.build();
  }

  public static InterfaceCode _interface(
      InterfaceCodeElement e1,
      InterfaceCodeElement e2,
      InterfaceCodeElement e3,
      InterfaceCodeElement e4) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Builder b = builder();
    e1.acceptInterfaceCodeBuilder(b);
    e2.acceptInterfaceCodeBuilder(b);
    e3.acceptInterfaceCodeBuilder(b);
    e4.acceptInterfaceCodeBuilder(b);
    return b.build();
  }

  public static InterfaceCode _interface(
      InterfaceCodeElement e1,
      InterfaceCodeElement e2,
      InterfaceCodeElement e3,
      InterfaceCodeElement e4,
      InterfaceCodeElement e5) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Builder b = builder();
    e1.acceptInterfaceCodeBuilder(b);
    e2.acceptInterfaceCodeBuilder(b);
    e3.acceptInterfaceCodeBuilder(b);
    e4.acceptInterfaceCodeBuilder(b);
    e5.acceptInterfaceCodeBuilder(b);
    return b.build();
  }

  public static InterfaceCode _interface(
      InterfaceCodeElement e1,
      InterfaceCodeElement e2,
      InterfaceCodeElement e3,
      InterfaceCodeElement e4,
      InterfaceCodeElement e5,
      InterfaceCodeElement e6) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Builder b = builder();
    e1.acceptInterfaceCodeBuilder(b);
    e2.acceptInterfaceCodeBuilder(b);
    e3.acceptInterfaceCodeBuilder(b);
    e4.acceptInterfaceCodeBuilder(b);
    e5.acceptInterfaceCodeBuilder(b);
    e6.acceptInterfaceCodeBuilder(b);
    return b.build();
  }

  public static InterfaceCode _interface(
      InterfaceCodeElement e1,
      InterfaceCodeElement e2,
      InterfaceCodeElement e3,
      InterfaceCodeElement e4,
      InterfaceCodeElement e5,
      InterfaceCodeElement e6,
      InterfaceCodeElement e7) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Builder b = builder();
    e1.acceptInterfaceCodeBuilder(b);
    e2.acceptInterfaceCodeBuilder(b);
    e3.acceptInterfaceCodeBuilder(b);
    e4.acceptInterfaceCodeBuilder(b);
    e5.acceptInterfaceCodeBuilder(b);
    e6.acceptInterfaceCodeBuilder(b);
    e7.acceptInterfaceCodeBuilder(b);
    return b.build();
  }

  public static InterfaceCode _interface(
      InterfaceCodeElement e1,
      InterfaceCodeElement e2,
      InterfaceCodeElement e3,
      InterfaceCodeElement e4,
      InterfaceCodeElement e5,
      InterfaceCodeElement e6,
      InterfaceCodeElement e7,
      InterfaceCodeElement e8) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Check.notNull(e8, "e8 == null");
    Builder b = builder();
    e1.acceptInterfaceCodeBuilder(b);
    e2.acceptInterfaceCodeBuilder(b);
    e3.acceptInterfaceCodeBuilder(b);
    e4.acceptInterfaceCodeBuilder(b);
    e5.acceptInterfaceCodeBuilder(b);
    e6.acceptInterfaceCodeBuilder(b);
    e7.acceptInterfaceCodeBuilder(b);
    e8.acceptInterfaceCodeBuilder(b);
    return b.build();
  }

  public static InterfaceCode _interface(
      InterfaceCodeElement e1,
      InterfaceCodeElement e2,
      InterfaceCodeElement e3,
      InterfaceCodeElement e4,
      InterfaceCodeElement e5,
      InterfaceCodeElement e6,
      InterfaceCodeElement e7,
      InterfaceCodeElement e8,
      InterfaceCodeElement e9) {
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
    e1.acceptInterfaceCodeBuilder(b);
    e2.acceptInterfaceCodeBuilder(b);
    e3.acceptInterfaceCodeBuilder(b);
    e4.acceptInterfaceCodeBuilder(b);
    e5.acceptInterfaceCodeBuilder(b);
    e6.acceptInterfaceCodeBuilder(b);
    e7.acceptInterfaceCodeBuilder(b);
    e8.acceptInterfaceCodeBuilder(b);
    e9.acceptInterfaceCodeBuilder(b);
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

  public static final class Builder {

    private final InterfaceExtends.Builder _extends = InterfaceExtends.builder();
    private final GrowableList<AnnotationCode> annotations = new GrowableList<>();
    private final GrowableList<InterfaceBodyElement> bodyElements = new GrowableList<>();
    private BodyFormatter formatter = BodyFormatter.defaultFormatter();
    private final InterfaceModifierSet.Builder modifiers = InterfaceModifierSet.builder();

    private String simpleName = "Unnamed";

    private Builder() {}

    public final Builder addAnnotation(AnnotationCode annotation) {
      annotations.addWithNullMessage(annotation, "annotation == null");
      return this;
    }

    public final Builder addAnnotation(Class<? extends Annotation> annotationType) {
      return addAnnotation(AnnotationCode.annotation(annotationType));
    }

    public final Builder addAnnotations(Iterable<AnnotationCode> annotations) {
      this.annotations.addAllIterable(annotations);
      return this;
    }

    public final Builder addExtends(NamedClassOrParameterized type) {
      _extends.addInterface(type);
      return this;
    }

    public final Builder addField(FieldCode field) {
      bodyElements.addWithNullMessage(field, "field == null");
      return this;
    }

    public final Builder addFields(Iterable<FieldCode> fields) {
      bodyElements.addAllIterable(fields);
      return this;
    }

    public final Builder addMethod(MethodCode method) {
      bodyElements.addWithNullMessage(method, "method == null");
      return this;
    }

    public final Builder addMethods(Iterable<MethodCode> methods) {
      bodyElements.addAllIterable(methods);
      return this;
    }

    public final Builder addModifier(InterfaceModifier modifier) {
      modifiers.addWithNullMessage(modifier, "modifier == null");
      return this;
    }

    public final Builder addModifier(InterfaceModifier... modifiers) {
      this.modifiers.add(modifiers);
      return this;
    }

    public final Builder addModifier(InterfaceModifier mod1, InterfaceModifier mod2) {
      modifiers.addWithNullMessage(mod1, "mod1 == null");
      modifiers.addWithNullMessage(mod2, "mod2 == null");
      return this;
    }

    public final Builder addModifiers(InterfaceModifierSet modifiers) {
      this.modifiers.withModifier(modifiers);
      return this;
    }

    public final Builder addModifiers(Iterable<? extends InterfaceModifier> modifiers) {
      this.modifiers.addAll(modifiers);
      return this;
    }

    public final Builder addNewLine(NewLine newLine) {
      bodyElements.addWithNullMessage(newLine, "newLine == null");
      return this;
    }

    public final InterfaceCode build() {
      return new InterfaceCode(
          simpleName,
          newLineSeparated(annotations),
          annotations.isEmpty() ? noop() : nextLine(),
          modifier(), space(),
          Keywords._interface(), space(),
          new SimpleNameCodeElement(simpleName),
          _extends.build(),
          space(),
          TypeCodeBody.of(bodyElements()),
          popSimpleName()
      );
    }

    public final InterfaceCode buildWith(BodyFormatter formatter) {
      this.formatter = Check.notNull(formatter, "formatter == null");
      return build();
    }

    public final Builder simpleName(NamedClass className) {
      Check.notNull(className, "className == null");
      simpleName = className.getSimpleName();
      return this;
    }

    public final Builder simpleName(String name) {
      simpleName = Check.notNull(name, "name == null");
      return this;
    }

    final UnmodifiableList<InterfaceBodyElement> bodyElements() {
      return formatter.format(bodyElements, InterfaceBodyElement.class);
    }

    final InterfaceModifierSet modifier() {
      return modifiers.build();
    }

  }

}