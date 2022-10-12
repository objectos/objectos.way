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

import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PROTECTED;
import static javax.lang.model.element.Modifier.PUBLIC;

import br.com.objectos.code.annotations.Ignore;
import br.com.objectos.code.java.element.AbstractCodeElement;
import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.io.NewLineFormatting.NewLineFormattingAction;
import br.com.objectos.code.java.statement.Block;
import br.com.objectos.code.java.statement.BlockStatement;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedType;
import br.com.objectos.code.java.type.NamedTypeParameter;
import br.com.objectos.code.java.type.NamedTypeVariable;
import br.com.objectos.code.java.type.NamedVoid;
import br.com.objectos.code.model.element.ProcessingMethod;
import br.com.objectos.code.model.element.ProcessingParameter;
import java.lang.annotation.Annotation;
import java.util.Set;
import javax.lang.model.element.ExecutableElement;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public final class MethodCode
    extends AbstractCodeElement
    implements
    ClassBodyElement,
    EnumBodyElement,
    InterfaceBodyElement {

  private final UnmodifiableList<AnnotationCode> annotations;
  private final Block block;
  private final MethodModifierSet modifiers;
  private final String name;
  private final UnmodifiableList<ParameterCode> parameters;
  private final NamedType returnType;
  private final UnmodifiableList<NamedType> thrownTypes;
  private final UnmodifiableList<NamedTypeParameter> typeParameters;

  private MethodCode(Builder builder) {
    annotations = builder.getAnnotations();
    modifiers = builder.getModifiers();
    typeParameters = builder.getTypeParameters();
    returnType = builder.getReturnType();
    name = builder.name();
    parameters = builder.getParameters();
    thrownTypes = builder.getThrownTypes();
    block = builder.block();
  }

  @Ignore("AggregatorGenProcessor")
  public static Builder builder() {
    return new Builder();
  }

  public static MethodCode method(
      MethodCodeElement e1) {
    Check.notNull(e1, "e1 == null");
    Builder b = MethodCode.builder();
    e1.acceptMethodCodeBuilder(b);
    return b.build();
  }

  public static MethodCode method(MethodCodeElement... elements) {
    Check.notNull(elements, "elements == null");
    Builder b = MethodCode.builder();
    for (int i = 0; i < elements.length; i++) {
      MethodCodeElement element = elements[i];
      if (element == null) {
        throw new NullPointerException("elements[" + i + "] == null");
      }
      element.acceptMethodCodeBuilder(b);
    }
    return b.build();
  }

  public static MethodCode method(
      MethodCodeElement e1, MethodCodeElement e2) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Builder b = MethodCode.builder();
    e1.acceptMethodCodeBuilder(b);
    e2.acceptMethodCodeBuilder(b);
    return b.build();
  }

  public static MethodCode method(
      MethodCodeElement e1, MethodCodeElement e2, MethodCodeElement e3) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Builder b = MethodCode.builder();
    e1.acceptMethodCodeBuilder(b);
    e2.acceptMethodCodeBuilder(b);
    e3.acceptMethodCodeBuilder(b);
    return b.build();
  }

  public static MethodCode method(
      MethodCodeElement e1, MethodCodeElement e2, MethodCodeElement e3, MethodCodeElement e4) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Builder b = MethodCode.builder();
    e1.acceptMethodCodeBuilder(b);
    e2.acceptMethodCodeBuilder(b);
    e3.acceptMethodCodeBuilder(b);
    e4.acceptMethodCodeBuilder(b);
    return b.build();
  }

  public static MethodCode method(
      MethodCodeElement e1,
      MethodCodeElement e2,
      MethodCodeElement e3,
      MethodCodeElement e4,
      MethodCodeElement e5) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Builder b = MethodCode.builder();
    e1.acceptMethodCodeBuilder(b);
    e2.acceptMethodCodeBuilder(b);
    e3.acceptMethodCodeBuilder(b);
    e4.acceptMethodCodeBuilder(b);
    e5.acceptMethodCodeBuilder(b);
    return b.build();
  }

  public static MethodCode method(
      MethodCodeElement e1,
      MethodCodeElement e2,
      MethodCodeElement e3,
      MethodCodeElement e4,
      MethodCodeElement e5,
      MethodCodeElement e6) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Builder b = MethodCode.builder();
    e1.acceptMethodCodeBuilder(b);
    e2.acceptMethodCodeBuilder(b);
    e3.acceptMethodCodeBuilder(b);
    e4.acceptMethodCodeBuilder(b);
    e5.acceptMethodCodeBuilder(b);
    e6.acceptMethodCodeBuilder(b);
    return b.build();
  }

  public static MethodCode method(
      MethodCodeElement e1,
      MethodCodeElement e2,
      MethodCodeElement e3,
      MethodCodeElement e4,
      MethodCodeElement e5,
      MethodCodeElement e6,
      MethodCodeElement e7) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Builder b = MethodCode.builder();
    e1.acceptMethodCodeBuilder(b);
    e2.acceptMethodCodeBuilder(b);
    e3.acceptMethodCodeBuilder(b);
    e4.acceptMethodCodeBuilder(b);
    e5.acceptMethodCodeBuilder(b);
    e6.acceptMethodCodeBuilder(b);
    e7.acceptMethodCodeBuilder(b);
    return b.build();
  }

  public static MethodCode method(
      MethodCodeElement e1,
      MethodCodeElement e2,
      MethodCodeElement e3,
      MethodCodeElement e4,
      MethodCodeElement e5,
      MethodCodeElement e6,
      MethodCodeElement e7,
      MethodCodeElement e8) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Check.notNull(e8, "e8 == null");
    Builder b = MethodCode.builder();
    e1.acceptMethodCodeBuilder(b);
    e2.acceptMethodCodeBuilder(b);
    e3.acceptMethodCodeBuilder(b);
    e4.acceptMethodCodeBuilder(b);
    e5.acceptMethodCodeBuilder(b);
    e6.acceptMethodCodeBuilder(b);
    e7.acceptMethodCodeBuilder(b);
    e8.acceptMethodCodeBuilder(b);
    return b.build();
  }

  public static MethodCode method(
      MethodCodeElement e1,
      MethodCodeElement e2,
      MethodCodeElement e3,
      MethodCodeElement e4,
      MethodCodeElement e5,
      MethodCodeElement e6,
      MethodCodeElement e7,
      MethodCodeElement e8,
      MethodCodeElement e9) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Check.notNull(e8, "e8 == null");
    Check.notNull(e9, "e9 == null");
    Builder b = MethodCode.builder();
    e1.acceptMethodCodeBuilder(b);
    e2.acceptMethodCodeBuilder(b);
    e3.acceptMethodCodeBuilder(b);
    e4.acceptMethodCodeBuilder(b);
    e5.acceptMethodCodeBuilder(b);
    e6.acceptMethodCodeBuilder(b);
    e7.acceptMethodCodeBuilder(b);
    e8.acceptMethodCodeBuilder(b);
    e9.acceptMethodCodeBuilder(b);
    return b.build();
  }

  public static OverridingProcessingMethod overriding(ProcessingMethod method) {
    Check.notNull(method, "method == null");
    Check.argument(!method.isFinal(), "Cannot override a final method");
    Check.argument(!method.isStatic(), "Cannot override a static method");
    Check.argument(!method.isPrivate(), "Cannot override a private method");
    return new OverridingProcessingMethod(method);
  }

  public static SignatureOfProcessingMethod signatureOf(ProcessingMethod method) {
    Check.notNull(method, "method == null");
    return new SignatureOfProcessingMethod(method);
  }

  @Override
  public final void acceptClassCodeBuilder(ClassCode.Builder builder) {
    builder.addMethod(this);
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    for (int i = 0; i < annotations.size(); i++) {
      AnnotationCode annotation;
      annotation = annotations.get(i);

      w.writeCodeElement(annotation);

      w.nextLine();
    }

    w.writeCodeElement(modifiers);

    switch (typeParameters.size()) {
      case 0:
        w.writeCodeElement(space());
        break;
      default:
        w.writeCodeElement(space());
        w.writeCodeElement(angled(commaSeparated(typeParameters)));
        w.writeCodeElement(space());
        break;
    }

    w.writeCodeElement(returnType);

    w.writeCodeElement(space());

    w.writeCodeElement(identifier(name));

    w.writeCodeElement(
        parenthesized(
            commaSeparated(parameters)
        )
    );

    if (!thrownTypes.isEmpty()) {
      w.writeCodeElement(space());

      w.writeCodeElement(Keywords._throws());

      w.writeCodeElement(space());

      w.writeCodeElement(commaSeparated(thrownTypes));
    }

    if (block != null) {
      w.writeCodeElement(space());

      w.writeCodeElement(block);
    } else {
      w.writeCodeElement(semicolon());
    }

    return w;
  }

  @Override
  public final void acceptEnumCodeBuilder(EnumCode.Builder builder) {
    builder.addMethod(this);
  }

  @Override
  public final void acceptInterfaceCodeBuilder(InterfaceCode.Builder builder) {
    builder.addMethod(this);
  }

  @Override
  public final void acceptNewLineFormattingAction(NewLineFormattingAction action) {
    action.propagateElement(this);
  }

  @Override
  public final Kind kind() {
    return Kind.METHOD;
  }

  public static class Builder implements ThrowsElement.Consumer {

    private final GrowableList<AnnotationCode> annotations = new GrowableList<>();
    private Block block;
    private final MethodModifierSet.Builder modifiers = MethodModifierSet.builder();
    private String name = "unnamed";
    private final GrowableList<ParameterCode> parameters = new GrowableList<>();
    private NamedType returnType = NamedVoid._void();
    private final GrowableList<BlockStatement> statements = new GrowableList<>();
    private final GrowableList<NamedType> thrownTypes = new GrowableList<>();
    private final GrowableList<NamedTypeParameter> typeParameters = new GrowableList<>();

    private Builder() {}

    public final Builder accessLevel(AccessLevel accessLevel) {
      Check.notNull(accessLevel, "accessLevel == null");
      switch (accessLevel) {
        case PUBLIC:
          return uncheckedAddModifier(Modifiers.PUBLIC);
        case PROTECTED:
          return uncheckedAddModifier(Modifiers.PROTECTED);
        case PRIVATE:
          return uncheckedAddModifier(Modifiers.PRIVATE);
        default:
          return this;
      }
    }

    public final Builder accessLevelOf(ExecutableElement element) {
      Check.notNull(element, "element == null");
      Set<javax.lang.model.element.Modifier> modifierSet = element.getModifiers();

      if (modifierSet.contains(PUBLIC)) {
        return uncheckedAddModifier(Modifiers.PUBLIC);
      } else if (modifierSet.contains(PROTECTED)) {
        return uncheckedAddModifier(Modifiers.PROTECTED);
      } else if (modifierSet.contains(PRIVATE)) {
        return uncheckedAddModifier(Modifiers.PRIVATE);
      } else {
        return this;
      }
    }

    public final Builder addAnnotation(AnnotationCode annotation) {
      annotations.add(annotation);
      return this;
    }

    public final Builder addAnnotation(Class<? extends Annotation> annotationType) {
      return addAnnotation(AnnotationCode.annotation(annotationType));
    }

    public final Builder addAnnotations(Iterable<AnnotationCode> annotations) {
      this.annotations.addAllIterable(annotations);
      return this;
    }

    public final Builder addModifier(MethodModifier... modifiers) {
      this.modifiers.addModifier(modifiers);
      return this;
    }

    public final Builder addModifiers(Iterable<? extends MethodModifier> modifiers) {
      this.modifiers.addModifiers(modifiers);
      return this;
    }

    public final Builder addModifiers(MethodModifierSet modifiers) {
      this.modifiers.withModifier(modifiers);
      return this;
    }

    public final Builder addParameter(Class<?> type, String name) {
      NamedClass typeName;
      typeName = NamedClass.of(type);

      Check.notNull(name, "name == null");

      ParameterCode parameter;
      parameter = ParameterCode.ofUnchecked(typeName, name);

      return withParameterUnchecked(parameter);
    }

    public final Builder addParameter(ParameterCode parameter) {
      Check.notNull(parameter, "parameter == null");
      return withParameterUnchecked(parameter);
    }

    public final Builder addParameter(ParameterTypeName typeName, String name) {
      Check.notNull(typeName, "typeName == null");
      Check.notNull(name, "name == null");
      ParameterCode parameter = ParameterCode.ofUnchecked(typeName, name);
      return withParameterUnchecked(parameter);
    }

    public final Builder addParameters(Iterable<ParameterCode> parameters) {
      Check.notNull(parameters, "parameters == null");
      for (ParameterCode parameter : parameters) {
        addParameter(parameter);
      }
      return this;
    }

    public final Builder addStatement(BlockStatement statement) {
      statements.addWithNullMessage(statement, "statement == null");
      return emptyBody();
    }

    public final Builder addStatements(Iterable<? extends BlockStatement> statements) {
      this.statements.addAllIterable(statements);
      return emptyBody();
    }

    public final Builder addThrownType(Class<? extends Throwable> type) {
      NamedClass thrown;
      thrown = NamedClass.of(type);

      thrownTypes.add(thrown);

      return this;
    }

    @Override
    public final Builder addThrownType(NamedClass type) {
      thrownTypes.addWithNullMessage(type, "type == null");

      return this;
    }

    @Override
    public final Builder addThrownType(NamedTypeVariable type) {
      thrownTypes.addWithNullMessage(type, "type == null");

      return this;
    }

    public final Builder addThrownType(String name) {
      NamedTypeVariable type;
      type = NamedTypeVariable.of(name);

      thrownTypes.add(type);

      return this;
    }

    public final Builder addTypeParameter(NamedTypeParameter parameter) {
      Check.notNull(parameter, "parameter == null");
      typeParameters.add(parameter);
      return this;
    }

    public final MethodCode build() {
      return new MethodCode(this);
    }

    public final Builder emptyBody() {
      block = Block.empty();
      return this;
    }

    public final Builder name(String name) {
      this.name = Check.notNull(name, "name == null");
      return this;
    }

    public final Builder returnType(Class<?> type) {
      NamedClass named;
      named = NamedClass.of(type);

      return setTypeNameUnchecked(named);
    }

    public final Builder returnType(NamedType typeName) {
      Check.notNull(typeName, "typeName == null");
      return setTypeNameUnchecked(typeName);
    }

    final Block block() {
      return statements.isEmpty() ? block : Block.block(statements);
    }

    final UnmodifiableList<AnnotationCode> getAnnotations() {
      return annotations.toUnmodifiableList();
    }

    final MethodModifierSet getModifiers() {
      return modifiers.build();
    }

    final UnmodifiableList<ParameterCode> getParameters() {
      return parameters.toUnmodifiableList();
    }

    final NamedType getReturnType() {
      return returnType;
    }

    final UnmodifiableList<NamedType> getThrownTypes() {
      return thrownTypes.toUnmodifiableList();
    }

    final UnmodifiableList<NamedTypeParameter> getTypeParameters() {
      return typeParameters.toUnmodifiableList();
    }

    final String name() {
      return name;
    }

    final UnmodifiableList<ParameterCode> parameters() {
      return parameters.toUnmodifiableList();
    }

    final Builder uncheckedAddModifier(MethodModifier modifier) {
      this.modifiers.addModifier(modifier);
      return this;
    }

    private Builder setTypeNameUnchecked(NamedType typeName) {
      this.returnType = typeName;
      return this;
    }

    private Builder withParameterUnchecked(ParameterCode parameter) {
      parameters.add(parameter);
      return this;
    }

  }

  public static class OverridingProcessingMethod extends OverridingOrSignatureOf {

    private OverridingProcessingMethod(ProcessingMethod method) {
      super(method);
    }

    @Override
    final void addModifier(Builder builder, MethodModifier modifier) {
      if (!modifier.equals(Modifiers.ABSTRACT)) {
        builder.addModifier(modifier);
      }
    }

  }

  public static class SignatureOfProcessingMethod extends OverridingOrSignatureOf {

    private SignatureOfProcessingMethod(ProcessingMethod method) {
      super(method);
    }

    @Override
    final void addModifier(Builder builder, MethodModifier modifier) {
      builder.addModifier(modifier);
    }

  }

  private abstract static class OverridingOrSignatureOf implements MethodCodeElement {

    private final ProcessingMethod method;

    OverridingOrSignatureOf(ProcessingMethod method) {
      this.method = method;
    }

    @Override
    public final void acceptMethodCodeBuilder(Builder builder) {
      for (MethodModifier modifier : method.getModifiers()) {
        addModifier(builder, modifier);
      }

      for (NamedTypeParameter parameter : method.getTypeParameters()) {
        builder.addTypeParameter(parameter);
      }

      builder.returnType(method.getReturnType().getName());

      builder.name(method.getName());

      for (ProcessingParameter parameter : method.getParameters()) {
        builder.addParameter(parameter.toParameterCode());
      }
    }

    abstract void addModifier(Builder builder, MethodModifier modifier);

  }

}
