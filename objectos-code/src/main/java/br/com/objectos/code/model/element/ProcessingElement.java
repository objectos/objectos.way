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
package br.com.objectos.code.model.element;

import br.com.objectos.code.java.declaration.AccessLevel;
import br.com.objectos.code.java.declaration.Modifier;
import br.com.objectos.code.java.declaration.Modifiers;
import br.com.objectos.code.java.declaration.PackageName;
import br.com.objectos.code.java.type.NamedTypeParameter;
import br.com.objectos.code.model.AnnotatedElementOrType;
import java.io.IOException;
import java.util.List;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;
import objectos.util.UnmodifiableSet;
import objectos.util.GrowableList;
import objectos.util.GrowableSet;

public abstract class ProcessingElement<E extends Element>
    extends AnnotatedElementOrType
    implements
    ElementKindQuery,
    ElementModifiersQuery {

  final E element;

  private AccessLevel accessLevel;

  private UnmodifiableSet<Modifier> modifiers;

  ProcessingElement(ProcessingEnvironment processingEnv, E element) {
    super(processingEnv);
    this.element = element;
  }

  public final void compilationError(String message) {
    Messager messager = processingEnv.getMessager();
    messager.printMessage(Kind.ERROR, message, element);
  }

  public final void compilationError(String template, Object... arguments) {
    String message = String.format(template, arguments);
    compilationError(message);
  }

  @Override
  public final boolean hasElementKind(ElementKind kind) {
    Check.notNull(kind, "kind == null");
    return element.getKind().equals(kind);
  }

  @Override
  public final int hashCode() {
    return element.hashCode();
  }

  @Override
  public final boolean hasModifier(Modifier modifier) {
    return getModifiersImpl().contains(modifier);
  }

  public ProcessingElement<?> originatingProcessingType() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String toString() {
    return element.toString();
  }

  @Override
  protected final boolean equalsImpl(AnnotatedElementOrType obj) {
    ProcessingElement<?> that = (ProcessingElement<?>) obj;
    return element.equals(that.element);
  }

  @Override
  protected final List<? extends AnnotationMirror> getAnnotationMirrors() {
    return element.getAnnotationMirrors();
  }

  @Override
  protected final ProcessingAnnotation toProcessingAnnotation(AnnotationMirror mirror) {
    return ProcessingAnnotation.adapt(processingEnv, element, mirror);
  }

  final AccessLevel getAccessLevelImpl() {
    if (accessLevel == null) {
      accessLevel = getAccessLevel0();
    }

    return accessLevel;
  }

  final ProcessingType getDeclaringType0() {
    Element enclosingElement;
    enclosingElement = element.getEnclosingElement();

    TypeElement enclosingTypeElement;
    enclosingTypeElement = TypeElement.class.cast(enclosingElement);

    return ProcessingType.adapt(processingEnv, enclosingTypeElement);
  }

  final String getDocComment0() {
    Elements elements;
    elements = processingEnv.getElementUtils();

    String maybeComment;
    maybeComment = elements.getDocComment(element);

    if (maybeComment == null) {
      maybeComment = "";
    }

    return maybeComment;
  }

  @SuppressWarnings("unchecked")
  final <M extends Modifier> UnmodifiableSet<M> getModifiersImpl() {
    if (modifiers == null) {
      modifiers = getModifiers0();
    }

    return (UnmodifiableSet<M>) modifiers;
  }

  final ProcessingResource getResource0(
      StandardLocation location, PackageName packageName, String resourceName)
      throws IOException {
    Filer filer;
    filer = processingEnv.getFiler();

    FileObject object;
    object = filer.getResource(location, packageName.getCanonicalName(), resourceName);

    return new FileObjectProcessingResource(object);
  }

  final boolean hasAccessLevelImpl(AccessLevel level) {
    return getAccessLevelImpl() == level;
  }

  final String modifiersToString() {
    return getModifiers().join(" ");
  }

  final UnmodifiableList<NamedTypeParameter>
      toNamedTypeParameter(List<? extends TypeParameterElement> elements) {
    GrowableList<NamedTypeParameter> result;
    result = new GrowableList<>();

    for (int i = 0; i < elements.size(); i++) {
      TypeParameterElement element;
      element = elements.get(i);

      NamedTypeParameter named;
      named = NamedTypeParameter.of(element);

      result.add(named);
    }

    return result.toUnmodifiableList();
  }

  private AccessLevel getAccessLevel0() {
    return AccessLevel.of(element);
  }

  private UnmodifiableSet<Modifier> getModifiers0() {
    GrowableSet<Modifier> set = new GrowableSet<>();

    for (javax.lang.model.element.Modifier modifier : element.getModifiers()) {
      set.add(Modifiers.of(modifier));
    }

    return set.toUnmodifiableSet();
  }

}