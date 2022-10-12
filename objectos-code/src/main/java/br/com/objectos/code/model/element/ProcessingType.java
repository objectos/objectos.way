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

import br.com.objectos.code.java.declaration.Modifiers;
import br.com.objectos.code.java.declaration.TypeModifier;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedTypeParameter;
import br.com.objectos.code.processing.Reprocessor;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.util.Elements;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;
import objectos.util.UnmodifiableSet;

public class ProcessingType extends ProcessingElement<TypeElement>
    implements
    TypeElementKindQuery,
    TypeElementModifiersQuery {

  private ProcessingTypeElements declaredElements;
  private ProcessingTypeElements declaredOrInheritedElements;
  private NamedClass name;
  private UnmodifiableList<NamedTypeParameter> typeParameters;

  ProcessingType(ProcessingEnvironment processingEnv, TypeElement element) {
    super(processingEnv, element);
  }

  public static ProcessingType adapt(ProcessingEnvironment processingEnv, TypeElement typeElement) {
    Check.notNull(processingEnv, "processingEnv == null");
    Check.notNull(typeElement, "typeElement == null");

    Class<? extends ProcessingEnvironment> envType;
    envType = processingEnv.getClass();

    Package envPackage;
    envPackage = envType.getPackage();

    String envPackageName;
    envPackageName = envPackage.getName();

    return envPackageName.startsWith("org.eclipse")
        || envPackageName.startsWith("br.com.objectos.code.jdt")
            ? new JdtProcessingType(processingEnv, typeElement)
            : new ProcessingType(processingEnv, typeElement);
  }

  public final void acceptReprocessor(Reprocessor reprocessor) {
    Check.notNull(reprocessor, "reprocessor == null");

    reprocessor.reprocessType(element);
  }

  public final String getBinaryName() {
    Elements elements;
    elements = elementUtils();

    Name binaryName;
    binaryName = elements.getBinaryName(element);

    return binaryName.toString();
  }

  public final String getCanonicalName() {
    Name qualifiedName;
    qualifiedName = element.getQualifiedName();

    return qualifiedName.toString();
  }

  public final UnmodifiableList<ProcessingConstructor> getDeclaredConstructors() {
    return getDeclaredElements().constructors;
  }

  public final UnmodifiableList<ProcessingField> getDeclaredFields() {
    return getDeclaredElements().fields;
  }

  public final UnmodifiableList<ProcessingMethod> getDeclaredMethods() {
    return getDeclaredElements().methods;
  }

  public final UnmodifiableList<ProcessingField> getDeclaredOrInheritedFields() {
    return getDeclaredOrInheritedElements().fields;
  }

  public final UnmodifiableList<ProcessingMethod> getDeclaredOrInheritedMethods() {
    return getDeclaredOrInheritedElements().methods;
  }

  public final UnmodifiableList<ProcessingType> getDeclaredOrInheritedTypes() {
    return getDeclaredOrInheritedElements().types;
  }

  public final UnmodifiableList<ProcessingType> getDeclaredTypes() {
    return getDeclaredElements().types;
  }

  public final ProcessingAnnotation getDirectlyPresentOrInheritedAnnotation(
      Class<? extends Annotation> annotationType) {
    return getDirectlyPresentOrInheritedAnnotationImpl(element, annotationType);
  }

  public final ProcessingAnnotation getDirectlyPresentOrInheritedAnnotation(
      String canonicalName)
      throws NoSuchElementException {
    return getDirectlyPresentOrInheritedAnnotationImpl(element, canonicalName);
  }

  public final UnmodifiableList<ProcessingAnnotation> getDirectlyPresentOrInheritedAnnotations() {
    return getDirectlyPresentOrInheritedAnnotationsImpl(element);
  }

  public final String getDocComment() {
    return getDocComment0();
  }

  @Override
  public final UnmodifiableSet<TypeModifier> getModifiers() {
    return getModifiersImpl();
  }

  public final NamedClass getName() {
    if (name == null) {
      name = getName0();
    }

    return name;
  }

  public final String getSimpleName() {
    Name simpleName;
    simpleName = element.getSimpleName();

    return simpleName.toString();
  }

  public final UnmodifiableList<NamedTypeParameter> getTypeParameters() {
    if (typeParameters == null) {
      typeParameters = getTypeParameters0();
    }

    return typeParameters;
  }

  public final boolean instanceOf(Class<?> type) {
    Check.notNull(type, "type == null");
    TypeElement typeElement = elementUtils().getTypeElement(type.getCanonicalName());
    return typeUtils().isAssignable(element.asType(), typeElement.asType());
  }

  @Override
  public final boolean isAbstract() {
    return hasModifier(Modifiers.ABSTRACT);
  }

  @Override
  public final boolean isAnnotation() {
    return hasElementKind(ElementKind.ANNOTATION_TYPE);
  }

  @Override
  public final boolean isClass() {
    return hasElementKind(ElementKind.CLASS);
  }

  @Override
  public final boolean isEnum() {
    return hasElementKind(ElementKind.ENUM);
  }

  @Override
  public final boolean isFinal() {
    return hasModifier(Modifiers.FINAL);
  }

  @Override
  public final boolean isInterface() {
    return hasElementKind(ElementKind.INTERFACE);
  }

  @Override
  public final boolean isPrivate() {
    return hasModifier(Modifiers.PRIVATE);
  }

  @Override
  public final boolean isProtected() {
    return hasModifier(Modifiers.PROTECTED);
  }

  @Override
  public final boolean isPublic() {
    return hasModifier(Modifiers.PUBLIC);
  }

  @Override
  public final boolean isStatic() {
    return hasModifier(Modifiers.STATIC);
  }

  @Override
  public final boolean isStrictfp() {
    return hasModifier(Modifiers.STRICTFP);
  }

  List<? extends Element> getAllMembers() {
    Elements elements;
    elements = processingEnv.getElementUtils();

    return elements.getAllMembers(element);
  }

  List<? extends Element> getEnclosedElements() {
    return element.getEnclosedElements();
  }

  ProcessingType newTypeElementQuery(TypeElement e) {
    return new ProcessingType(processingEnv, e);
  }

  final ProcessingEnvironment processingEnv() {
    return processingEnv;
  }

  private ProcessingTypeElements getDeclaredElements() {
    if (declaredElements == null) {
      declaredElements = ProcessingTypeElements.getDeclared(this);
    }

    return declaredElements;
  }

  private ProcessingTypeElements getDeclaredOrInheritedElements() {
    if (declaredOrInheritedElements == null) {
      declaredOrInheritedElements = ProcessingTypeElements.getDeclaredOrInherited(this);
    }

    return declaredOrInheritedElements;
  }

  private NamedClass getName0() {
    return NamedClass.of(element);
  }

  private UnmodifiableList<NamedTypeParameter> getTypeParameters0() {
    List<? extends TypeParameterElement> parameters;
    parameters = element.getTypeParameters();

    return toNamedTypeParameter(parameters);
  }

}