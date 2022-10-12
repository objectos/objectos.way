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

import br.com.objectos.code.java.io.JavaFileImportSet;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedType;
import br.com.objectos.code.java.type.NamedTypeVisitor;
import br.com.objectos.code.model.AnnotatedElementOrType;
import br.com.objectos.code.processing.type.PDeclaredType;
import br.com.objectos.code.processing.type.PTypeMirror;
import br.com.objectos.code.util.SimpleAnnotationValueVisitor;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;
import objectos.util.UnmodifiableMap;
import objectos.util.UnmodifiableSet;
import objectos.util.GrowableMap;

public class ProcessingAnnotation
    extends AnnotatedElementOrType
    implements CanGenerateCompilationError {

  private final Element annotatedElement;
  private final AnnotationMirror annotation;

  private UnmodifiableMap<String, DeclaredOrDefault> declaredOrDefaultMap;

  private PDeclaredType type;

  ProcessingAnnotation(ProcessingEnvironment processingEnv,
                       AnnotationMirror annotation) {
    this(processingEnv, null, annotation);
  }

  ProcessingAnnotation(ProcessingEnvironment processingEnv,
                       Element annotatedElement,
                       AnnotationMirror annotation) {
    super(processingEnv);
    this.annotatedElement = annotatedElement;
    this.annotation = annotation;
  }

  public static ProcessingAnnotation adapt(
      ProcessingEnvironment processingEnv,
      Element annotatedElement,
      AnnotationMirror mirror) {
    Check.notNull(processingEnv, "processingEnv == null");
    Check.notNull(annotatedElement, "annotatedElement == null");
    Check.notNull(mirror, "mirror == null");
    return new ProcessingAnnotation(processingEnv, annotatedElement, mirror);
  }

  public final NamedClass className() {
    return NamedClass.of((TypeElement) element());
  }

  @Override
  public final void compilationError(String message) {
    if (annotatedElement != null) {
      Messager messager = processingEnv.getMessager();
      messager.printMessage(Kind.ERROR, message, annotatedElement, annotation);
    }
  }

  @Override
  public final void compilationError(String template, Object... arguments) {
    String message;
    message = String.format(template, arguments);

    compilationError(message);
  }

  public final ProcessingAnnotationValue getDeclaredOrDefaultValue(String name) {
    DeclaredOrDefault value;
    value = getDeclaredOrDefault(name);

    return value.getDeclaredOrDefaultValue();
  }

  public final ProcessingAnnotationValue getDeclaredValue(String name) {
    DeclaredOrDefault value;
    value = getDeclaredOrDefault(name);

    return value.getDeclaredValue();
  }

  public final ProcessingAnnotationValue getDefaultValue(String name) {
    DeclaredOrDefault value;
    value = getDeclaredOrDefault(name);

    return value.getDefaultValue();
  }

  public final ProcessingAnnotation getDirectlyPresentOrInheritedAnnotation(
      Class<? extends Annotation> annotationType) {
    return getDirectlyPresentOrInheritedAnnotationImpl(element(), annotationType);
  }

  public final ProcessingAnnotation getDirectlyPresentOrInheritedAnnotation(
      String canonicalName)
      throws NoSuchElementException {
    return getDirectlyPresentOrInheritedAnnotationImpl(element(), canonicalName);
  }

  public final UnmodifiableList<ProcessingAnnotation> getDirectlyPresentOrInheritedAnnotations() {
    return getDirectlyPresentOrInheritedAnnotationsImpl(element());
  }

  public final PDeclaredType getType() {
    if (type == null) {
      type = getType0();
    }

    return type;
  }

  @Override
  public final int hashCode() {
    return annotation.hashCode();
  }

  public final boolean hasQualifiedName(String qualifiedName) {
    Name thisQualifiedName;
    thisQualifiedName = ((TypeElement) element()).getQualifiedName();

    return thisQualifiedName.contentEquals(qualifiedName);
  }

  public final String simpleName() {
    return element().getSimpleName().toString();
  }

  @Override
  public final String toString() {
    return annotation.toString();
  }

  @Override
  protected final boolean equalsImpl(AnnotatedElementOrType obj) {
    ProcessingAnnotation that;
    that = (ProcessingAnnotation) obj;

    return annotation.equals(that.annotation);
  }

  @Override
  protected final List<? extends AnnotationMirror> getAnnotationMirrors() {
    Element element;
    element = element();

    return element.getAnnotationMirrors();
  }

  @Override
  protected final ProcessingAnnotation toProcessingAnnotation(AnnotationMirror mirror) {
    return new ProcessingAnnotation(processingEnv, mirror);
  }

  final void compilationError(AnnotationValue value, String message) {
    if (annotatedElement != null) {
      Messager messager = processingEnv.getMessager();
      messager.printMessage(Kind.ERROR, message, annotatedElement, annotation, value);
    }
  }

  final AnnotationValue getDeclaredValue(ExecutableElement element) {
    return annotation.getElementValues().get(element);
  }

  final boolean isSameType0(TypeMirror componentType, String qualifiedName) {
    return isSameType(componentType, qualifiedName);
  }

  final ProcessingEnumConstant toEnumConstant(VariableElement enumConstant) {
    return new ProcessingEnumConstant(processingEnv, enumConstant);
  }

  final PTypeMirror toModelTypeIfPossible(AnnotationValue value) {
    return value.accept(ToModelType.INSTANCE, processingEnv);
  }

  private Element element() {
    DeclaredType annotationType;
    annotationType = annotation.getAnnotationType();

    return annotationType.asElement();
  }

  private DeclaredOrDefault getDeclaredOrDefault(String name) {
    UnmodifiableMap<String, DeclaredOrDefault> map;
    map = getDeclaredOrDefaultMap();

    DeclaredOrDefault value = map.get(name);

    if (value == null) {
      throw new NoSuchElementException(name);
    }

    return value;
  }

  private UnmodifiableMap<String, DeclaredOrDefault> getDeclaredOrDefaultMap() {
    if (declaredOrDefaultMap == null) {
      declaredOrDefaultMap = getDeclaredOrDefaultMap0();
    }

    return declaredOrDefaultMap;
  }

  private UnmodifiableMap<String, DeclaredOrDefault> getDeclaredOrDefaultMap0() {
    GrowableMap<String, DeclaredOrDefault> map;
    map = new GrowableMap<>();

    Map<? extends ExecutableElement, ? extends AnnotationValue> declaredValues;
    declaredValues = annotation.getElementValues();

    Elements elements;
    elements = elementUtils();

    Map<? extends ExecutableElement, ? extends AnnotationValue> declaredOrDefaultValues;
    declaredOrDefaultValues = elements.getElementValuesWithDefaults(annotation);

    for (ExecutableElement element : declaredOrDefaultValues.keySet()) {
      AnnotationValue declaredValue;
      declaredValue = declaredValues.get(element);

      DeclaredOrDefault value;
      value = new DeclaredOrDefault(element, declaredValue);

      value.addTo(map);
    }

    return map.toUnmodifiableMap();
  }

  private PDeclaredType getType0() {
    DeclaredType annotationType;
    annotationType = annotation.getAnnotationType();

    return PDeclaredType.adapt(processingEnv, annotationType);
  }

  private abstract static class AbstractAnnotatedConstruct extends NamedType {

    public final <A extends Annotation> A getAnnotation(Class<A> annotationType) {
      return null;
    }

    public final List<? extends AnnotationMirror> getAnnotationMirrors() {
      return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    public final <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
      return (A[]) new Object[] {};
    }

  }

  private class DeclaredOrDefault
      implements
      AnnotationValueVisitor<ProcessingAnnotationValue, AnnotationValue> {

    private ProcessingAnnotationValue declaredResult;
    private final AnnotationValue declaredValue;
    private ProcessingAnnotationValue defaultResult;
    private final AnnotationValue defaultValue;

    private final ExecutableElement element;
    private final ProcessingAnnotation outer = ProcessingAnnotation.this;

    DeclaredOrDefault(ExecutableElement element,
                      AnnotationValue declaredValue) {
      this.element = element;
      this.declaredValue = declaredValue;

      defaultValue = element.getDefaultValue();
    }

    @Override
    public final ProcessingAnnotationValue visit(AnnotationValue av) {
      throw new UnsupportedOperationException();
    }

    @Override
    public final ProcessingAnnotationValue visit(
        AnnotationValue av, AnnotationValue annotationValue) {
      throw new UnsupportedOperationException();
    }

    @Override
    public ProcessingAnnotationValue visitAnnotation(
        AnnotationMirror a, AnnotationValue annotationValue) {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public final ProcessingAnnotationValue visitArray(
        List<? extends AnnotationValue> values, AnnotationValue annotationValue) {
      TypeMirror returnType;
      returnType = element.getReturnType();

      ArrayType arrayType;
      arrayType = (ArrayType) returnType;

      TypeMirror componentType;
      componentType = arrayType.getComponentType();

      TypeKind componentKind;
      componentKind = componentType.getKind();

      switch (componentKind) {
        case DECLARED:
          DeclaredType declaredType = (DeclaredType) componentType;
          return visitArray0Declared(values, annotationValue, declaredType);
        default:
          throw new AssertionError(
              "Unexpected TypeKind for annotation array value component: " + componentKind);
      }
    }

    @Override
    public ProcessingAnnotationValue visitBoolean(boolean b, AnnotationValue annotationValue) {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public ProcessingAnnotationValue visitByte(byte b, AnnotationValue annotationValue) {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public final ProcessingAnnotationValue visitChar(char c, AnnotationValue annotationValue) {
      return new ProcessingAnnotationValueChar(outer, element, annotationValue, c);
    }

    @Override
    public ProcessingAnnotationValue visitDouble(double d, AnnotationValue annotationValue) {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public final ProcessingAnnotationValue visitEnumConstant(
        VariableElement c, AnnotationValue annotationValue) {
      return new ProcessingAnnotationValueEnumConstant(
          outer, element, annotationValue, toEnumConstant(c)
      );
    }

    @Override
    public ProcessingAnnotationValue visitFloat(float f, AnnotationValue annotationValue) {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public final ProcessingAnnotationValue visitInt(int i, AnnotationValue annotationValue) {
      return new ProcessingAnnotationValueInt(outer, element, annotationValue, i);
    }

    @Override
    public ProcessingAnnotationValue visitLong(long i, AnnotationValue annotationValue) {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public ProcessingAnnotationValue visitShort(short s, AnnotationValue annotationValue) {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public final ProcessingAnnotationValue visitString(String s, AnnotationValue annotationValue) {
      TypeMirror returnType;
      returnType = element.getReturnType();

      if (isSameType0(returnType, "java.lang.Class")) {
        ThisErrorType errorType;
        errorType = new ThisErrorType(s);

        return visitType(errorType, annotationValue);
      }

      return new ProcessingAnnotationValueString(outer, element, annotationValue, s);
    }

    @Override
    public final ProcessingAnnotationValue visitType(
        TypeMirror t, AnnotationValue annotationValue) {
      return new ProcessingAnnotationValueType(
          outer, element, annotationValue, PTypeMirror.adapt(processingEnv, t)
      );
    }

    @Override
    public ProcessingAnnotationValue visitUnknown(
        AnnotationValue av, AnnotationValue annotationValue) {
      throw new UnsupportedOperationException("Implement me");
    }

    final void addTo(GrowableMap<String, DeclaredOrDefault> map) {
      String key;
      key = getSimpleName();

      map.put(key, this);
    }

    final ProcessingAnnotationValue getDeclaredOrDefaultValue() {
      ProcessingAnnotationValue declared;
      declared = getDeclaredResult();

      if (declared != null) {
        return declared;
      }

      return getDefaultResult();
    }

    final ProcessingAnnotationValue getDeclaredValue() {
      ProcessingAnnotationValue result;
      result = getDeclaredResult();

      if (result == null) {
        throw new NoDeclaredValueException(getSimpleName());
      }

      return result;
    }

    final ProcessingAnnotationValue getDefaultValue() {
      ProcessingAnnotationValue result;
      result = getDefaultResult();

      if (result == null) {
        throw new NoDefaultValueException(getSimpleName());
      }

      return result;
    }

    private ProcessingAnnotationValue getDeclaredResult() {
      if (declaredResult == null) {
        declaredResult = getDeclaredResult0();
      }

      return declaredResult;
    }

    private ProcessingAnnotationValue getDeclaredResult0() {
      ProcessingAnnotationValue result = null;

      if (declaredValue != null) {
        result = declaredValue.accept(this, declaredValue);
      }

      return result;
    }

    private ProcessingAnnotationValue getDefaultResult() {
      if (defaultResult == null) {
        defaultResult = getDefaultResult0();
      }

      return defaultResult;
    }

    private ProcessingAnnotationValue getDefaultResult0() {
      ProcessingAnnotationValue result = null;

      if (defaultValue != null) {
        result = defaultValue.accept(this, defaultValue);
      }

      return result;
    }

    private String getSimpleName() {
      Name simpleName;
      simpleName = element.getSimpleName();

      return simpleName.toString();
    }

    private ProcessingAnnotationValue visitArray0Declared(
        List<? extends AnnotationValue> values,
        AnnotationValue annotationValue,
        DeclaredType componentType) {
      if (isSameType0(componentType, "java.lang.Class")) {
        return ProcessingAnnotationValueTypeArray.build(
            outer, element, annotationValue, values
        );
      }

      if (isSameType0(componentType, "java.lang.String")) {
        return ProcessingAnnotationValueStringArray.build(
            outer, element, annotationValue, values
        );
      }

      Element componentElement;
      componentElement = componentType.asElement();

      ElementKind elementKind;
      elementKind = componentElement.getKind();

      if (elementKind == ElementKind.ANNOTATION_TYPE) {
        return ProcessingAnnotationValueAnnotationArray.build(
            outer, element, annotationValue, values
        );
      }

      if (elementKind == ElementKind.ENUM) {
        return ProcessingAnnotationValueEnumConstantArray.build(
            outer, element, annotationValue, values
        );
      }

      throw new AssertionError(
          "Unexpected DeclaredType as the component for an annotation array value: "
              + componentType);
    }

  }

  private static class ThisErrorType extends AbstractAnnotatedConstruct implements ErrorType {

    private final String name;

    ThisErrorType(String name) {
      this.name = name;
    }

    @Override
    public final <R, P> R accept(TypeVisitor<R, P> v, P p) {
      return v.visitError(this, p);
    }

    @Override
    public final String acceptJavaFileImportSet(JavaFileImportSet set) {
      return name;
    }

    @Override
    public final <R, P> R acceptTypeNameVisitor(NamedTypeVisitor<R, P> visitor, P p) {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public final Element asElement() {
      throw new UnsupportedOperationException();
    }

    @Override
    public final TypeMirror getEnclosingType() {
      return null;
    }

    @Override
    public final TypeKind getKind() {
      return TypeKind.ERROR;
    }

    @Override
    public final List<? extends TypeMirror> getTypeArguments() {
      return Collections.emptyList();
    }

  }

  private static class ToModelType
      extends SimpleAnnotationValueVisitor<PTypeMirror, ProcessingEnvironment> {

    static final UnmodifiableSet<String> ERROR_MESSAGES = UnmodifiableSet.of("<any>", "<error>");
    static final ToModelType INSTANCE = new ToModelType();

    private ToModelType() {}

    @Override
    public final PTypeMirror visitString(String s, ProcessingEnvironment p) {
      if (ERROR_MESSAGES.contains(s)) {
        ThisErrorType errorType = new ThisErrorType(s);
        return PTypeMirror.adapt(p, errorType);
      } else {
        return defaultAction(s, p);
      }
    }

    @Override
    public final PTypeMirror visitType(TypeMirror t, ProcessingEnvironment p) {
      return PTypeMirror.adapt(p, t);
    }

    @Override
    protected final PTypeMirror defaultAction(Object o, ProcessingEnvironment p) {
      throw new AssertionError("Unexpected value: " + o);
    }

  }

}