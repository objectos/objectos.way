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
import br.com.objectos.code.java.type.NamedTypeParameter;
import java.lang.annotation.Annotation;
import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public class ClassCode extends AbstractTypeCode {

  private final String simpleName;

  private ClassCode(String simpleName, CodeElement... elements) {
    super(elements);
    this.simpleName = simpleName;
  }

  public static ClassCode _class(ClassCodeElement e1) {
    Check.notNull(e1, "e1 == null");
    Builder b = builder();
    e1.acceptClassCodeBuilder(b);
    return b.build();
  }

  public static ClassCode _class(ClassCodeElement... elements) {
    Check.notNull(elements, "elements == null");
    Builder b = builder();

    for (int i = 0; i < elements.length; i++) {
      ClassCodeElement element;
      element = elements[i];

      if (element == null) {
        throw new NullPointerException("elements[" + i + "] == null");
      }

      element.acceptClassCodeBuilder(b);
    }

    return b.build();
  }

  public static ClassCode _class(
      ClassCodeElement e1,
      ClassCodeElement e2) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Builder b = builder();
    e1.acceptClassCodeBuilder(b);
    e2.acceptClassCodeBuilder(b);
    return b.build();
  }

  public static ClassCode _class(
      ClassCodeElement e1,
      ClassCodeElement e2,
      ClassCodeElement e3) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Builder b = builder();
    e1.acceptClassCodeBuilder(b);
    e2.acceptClassCodeBuilder(b);
    e3.acceptClassCodeBuilder(b);
    return b.build();
  }

  public static ClassCode _class(
      ClassCodeElement e1,
      ClassCodeElement e2,
      ClassCodeElement e3,
      ClassCodeElement e4) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Builder b = builder();
    e1.acceptClassCodeBuilder(b);
    e2.acceptClassCodeBuilder(b);
    e3.acceptClassCodeBuilder(b);
    e4.acceptClassCodeBuilder(b);
    return b.build();
  }

  public static ClassCode _class(
      ClassCodeElement e1,
      ClassCodeElement e2,
      ClassCodeElement e3,
      ClassCodeElement e4,
      ClassCodeElement e5) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Builder b = builder();
    e1.acceptClassCodeBuilder(b);
    e2.acceptClassCodeBuilder(b);
    e3.acceptClassCodeBuilder(b);
    e4.acceptClassCodeBuilder(b);
    e5.acceptClassCodeBuilder(b);
    return b.build();
  }

  public static ClassCode _class(
      ClassCodeElement e1,
      ClassCodeElement e2,
      ClassCodeElement e3,
      ClassCodeElement e4,
      ClassCodeElement e5,
      ClassCodeElement e6) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Builder b = builder();
    e1.acceptClassCodeBuilder(b);
    e2.acceptClassCodeBuilder(b);
    e3.acceptClassCodeBuilder(b);
    e4.acceptClassCodeBuilder(b);
    e5.acceptClassCodeBuilder(b);
    e6.acceptClassCodeBuilder(b);
    return b.build();
  }

  public static ClassCode _class(
      ClassCodeElement e1,
      ClassCodeElement e2,
      ClassCodeElement e3,
      ClassCodeElement e4,
      ClassCodeElement e5,
      ClassCodeElement e6,
      ClassCodeElement e7) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Builder b = builder();
    e1.acceptClassCodeBuilder(b);
    e2.acceptClassCodeBuilder(b);
    e3.acceptClassCodeBuilder(b);
    e4.acceptClassCodeBuilder(b);
    e5.acceptClassCodeBuilder(b);
    e6.acceptClassCodeBuilder(b);
    e7.acceptClassCodeBuilder(b);
    return b.build();
  }

  public static ClassCode _class(
      ClassCodeElement e1,
      ClassCodeElement e2,
      ClassCodeElement e3,
      ClassCodeElement e4,
      ClassCodeElement e5,
      ClassCodeElement e6,
      ClassCodeElement e7,
      ClassCodeElement e8) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Check.notNull(e8, "e8 == null");
    Builder b = builder();
    e1.acceptClassCodeBuilder(b);
    e2.acceptClassCodeBuilder(b);
    e3.acceptClassCodeBuilder(b);
    e4.acceptClassCodeBuilder(b);
    e5.acceptClassCodeBuilder(b);
    e6.acceptClassCodeBuilder(b);
    e7.acceptClassCodeBuilder(b);
    e8.acceptClassCodeBuilder(b);
    return b.build();
  }

  public static ClassCode _class(
      ClassCodeElement e1,
      ClassCodeElement e2,
      ClassCodeElement e3,
      ClassCodeElement e4,
      ClassCodeElement e5,
      ClassCodeElement e6,
      ClassCodeElement e7,
      ClassCodeElement e8,
      ClassCodeElement e9) {
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
    e1.acceptClassCodeBuilder(b);
    e2.acceptClassCodeBuilder(b);
    e3.acceptClassCodeBuilder(b);
    e4.acceptClassCodeBuilder(b);
    e5.acceptClassCodeBuilder(b);
    e6.acceptClassCodeBuilder(b);
    e7.acceptClassCodeBuilder(b);
    e8.acceptClassCodeBuilder(b);
    e9.acceptClassCodeBuilder(b);
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

    private final ClassExtends.Builder _extends = ClassExtends.builder();
    private Implements _implements;
    private final Implements.Builder _implementsBuilder = Implements.builder();
    private final GrowableList<AnnotationCode> annotations = new GrowableList<>();
    private final GrowableList<ClassBodyElement> bodyElements = new GrowableList<>();
    private BodyFormatter formatter = BodyFormatter.defaultFormatter();
    private final ClassModifierSet.Builder modifiers = ClassModifierSet.builder();
    private String simpleName = "Unnamed";

    private final GrowableList<NamedTypeParameter> typeParameters = new GrowableList<>();

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

    public final Builder addConstructor(ConstructorCode constructor) {
      bodyElements.addWithNullMessage(constructor, "constructor == null");
      return this;
    }

    public final Builder addConstructors(Iterable<ConstructorCode> constructors) {
      bodyElements.addAllIterable(constructors);
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

    public final Builder addMethods(Iterable<MethodCode> methods) {
      bodyElements.addAllIterable(methods);
      return this;
    }

    public final Builder addModifier(ClassModifier... modifiers) {
      this.modifiers.add(modifiers);
      return this;
    }

    public final Builder addModifiers(ClassModifierSet modifiers) {
      this.modifiers.withModifier(modifiers);
      return this;
    }

    public final Builder addModifiers(Iterable<? extends ClassModifier> modifiers) {
      this.modifiers.addAll(modifiers);
      return this;
    }

    public final Builder addNewLine(NewLine newLine) {
      bodyElements.addWithNullMessage(newLine, "newLine == null");
      return this;
    }

    public final Builder addType(TypeCode type) {
      bodyElements.addWithNullMessage(type, "type == null");
      return this;
    }

    public final Builder addTypeParameter(NamedTypeParameter parameter) {
      Check.notNull(parameter, "parameter == null");
      return addTypeParameter0(parameter);
    }

    public final Builder addTypeParameter(String name) {
      return addTypeParameter0(NamedTypeParameter.named(name));
    }

    public final Builder addTypeParameters(Iterable<NamedTypeParameter> parameters) {
      Check.notNull(parameters, "parameters == null");
      return addTypeParameters0(parameters);
    }

    public final Builder addTypeParametersFrom(TypeElement type) {
      Check.notNull(type, "type == null");

      List<? extends TypeParameterElement> elements;
      elements = type.getTypeParameters();

      for (int i = 0; i < elements.size(); i++) {
        TypeParameterElement element;
        element = elements.get(i);

        NamedTypeParameter named;
        named = NamedTypeParameter.of(element);

        typeParameters.add(named);
      }

      return this;
    }

    public final Builder addTypes(Iterable<? extends TypeCode> types) {
      bodyElements.addAllIterable(types);
      return this;
    }

    public final ClassCode build() {
      return new ClassCode(
          simpleName,
          newLineSeparated(annotations),
          annotations.isEmpty() ? noop() : nextLine(),
          modifier(), space(),
          Keywords._class(), space(),
          new SimpleNameCodeElement(simpleName),
          typeParameters.isEmpty() ? noop() : angled(commaSeparated(typeParameters)),
          _extends.build(),
          _implements != null ? _implements : _implementsBuilder.build(),
          space(),
          TypeCodeBody.of(bodyElements()),
          popSimpleName()
      );
    }

    public final ClassCode buildWith(BodyFormatter formatter) {
      this.formatter = Check.notNull(formatter, "formatter == null");
      return build();
    }

    public final String simpleName() {
      return simpleName;
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

    public final Builder superclass(Class<?> superclass) {
      _extends.setSuperclass(superclass);
      return this;
    }

    public final Builder superclass(NamedClassOrParameterized superclass) {
      _extends.setSuperclass(superclass);
      return this;
    }

    final Builder addBodyElement0(ClassBodyElement element) {
      bodyElements.add(element);
      return this;
    }

    final UnmodifiableList<AnnotationCode> annotationList() {
      return annotations.toUnmodifiableList();
    }

    final UnmodifiableList<ClassBodyElement> bodyElements() {
      return formatter.format(bodyElements, ClassBodyElement.class);
    }

    final ClassModifierSet modifier() {
      return modifiers.build();
    }

    final Builder setImplementsUnchecked(Implements _implements) {
      this._implements = _implements;
      return this;
    }

    final UnmodifiableList<NamedTypeParameter> typeParameters() {
      return typeParameters.toUnmodifiableList();
    }

    private Builder addTypeParameter0(NamedTypeParameter name) {
      typeParameters.add(name);
      return this;
    }

    private Builder addTypeParameters0(Iterable<NamedTypeParameter> parameters) {
      typeParameters.addAllIterable(parameters);
      return this;
    }

  }

}