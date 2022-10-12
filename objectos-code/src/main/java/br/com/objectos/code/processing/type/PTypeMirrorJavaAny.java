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
package br.com.objectos.code.processing.type;

import br.com.objectos.code.java.type.NamedArray;
import br.com.objectos.code.java.type.NamedType;
import br.com.objectos.code.model.AnnotatedElementOrType;
import br.com.objectos.code.model.element.ProcessingAnnotation;
import br.com.objectos.code.util.SimpleTypeVisitor;
import java.util.Locale;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import objectos.lang.Check;

abstract class PTypeMirrorJavaAny extends AnnotatedElementOrType {

  private static class PTypeMirrorFactory
      extends
      SimpleTypeVisitor<PTypeMirror, ProcessingEnvironment> {

    @Override
    public final PTypeMirror visitArray(ArrayType t, ProcessingEnvironment p) {
      return new PArrayType(p, t);
    }

    @Override
    public final PTypeMirror visitDeclared(DeclaredType t, ProcessingEnvironment p) {
      return new PDeclaredType(p, t);
    }

    @Override
    public final PTypeMirror visitError(ErrorType t, ProcessingEnvironment p) {
      return new PErrorType(p, t);
    }

    @Override
    public final PTypeMirror visitNoType(NoType t, ProcessingEnvironment p) {
      return new PNoType(p, t);
    }

    @Override
    public final PTypeMirror visitPrimitive(PrimitiveType t, ProcessingEnvironment p) {
      return new PPrimitiveType(p, t);
    }

    @Override
    public final PTypeMirror visitTypeVariable(TypeVariable t, ProcessingEnvironment p) {
      TypeMirror lowerBound;
      lowerBound = t.getLowerBound();

      Types types;
      types = p.getTypeUtils();

      NullType nullType;
      nullType = types.getNullType();

      if (!types.isSameType(lowerBound, nullType)) {
        throw new AssertionError("TypeVariable has a lower bound: " + lowerBound);
      }

      return new PTypeVariable(p, t);
    }

    @Override
    public final PTypeMirror visitWildcard(WildcardType t, ProcessingEnvironment p) {
      return new PWildcardType(p, t);
    }

    @Override
    protected final PTypeMirror defaultAction(TypeMirror e, ProcessingEnvironment p) {
      throw new UnsupportedOperationException("Implement me");
    }

  }

  private static final PTypeMirrorFactory FACTORY = new PTypeMirrorFactory();

  PTypeMirrorJavaAny(ProcessingEnvironment processingEnv) {
    super(processingEnv);
  }

  public static PTypeMirror adapt(ProcessingEnvironment processingEnv, TypeMirror type) {
    Check.notNull(processingEnv, "processingEnv == null");
    Check.notNull(type, "type == null");

    return type.accept(FACTORY, processingEnv);
  }

  public abstract NamedType getName();

  @Override
  public final int hashCode() {
    return getType().hashCode();
  }

  public boolean isArrayType() {
    return false;
  }

  public boolean isDeclaredType() {
    return false;
  }

  public boolean isErrorType() {
    return false;
  }

  public final boolean isInstanceOf(Class<?> type) {
    Check.notNull(type, "type == null");

    Types types;
    types = processingEnv.getTypeUtils();

    TypeMirror thatType;
    thatType = getRawType(type);

    return types.isSubtype(getType(), thatType);
  }

  public boolean isNoType() {
    return false;
  }

  public boolean isPrimitiveType() {
    return false;
  }

  public boolean isTypeVariable() {
    return false;
  }

  public boolean isWildcardType() {
    return false;
  }

  public void throwErrorTypeExceptionIfPossible() throws ErrorTypeException {
    // noop
  }

  public PArrayType toArrayType() {
    throw new AssertionError(
      "Not a PArrayType instance. See isArrayType() method if necessary."
    );
  }

  public PDeclaredType toDeclaredType() {
    throw new AssertionError(
      "Not a PDeclaredType instance. See isDeclaredType() method if necessary."
    );
  }

  public PErrorType toErrorType() {
    throw new AssertionError(
      "Not a PErrorType instance. See isErrorType() method if necessary."
    );
  }

  public PNoType toNoType() {
    throw new AssertionError(
      "Not a PNoType instance. See isNoType() method if necessary."
    );
  }

  public PPrimitiveType toPrimitiveType() {
    throw new AssertionError(
      "Not a PPrimitiveType instance. See isPrimitiveType() method if necessary."
    );
  }

  @Override
  public final String toString() {
    return getType().toString();
  }

  public PTypeVariable toTypeVariable() {
    throw new AssertionError(
      "Not a PTypeVariable instance. See isTypeVariable() method if necessary."
    );
  }

  public PWildcardType toWildcardType() {
    throw new AssertionError(
      "Not a PWildcardType instance. See isWildcardType() method if necessary."
    );
  }

  @Override
  protected final boolean equalsImpl(AnnotatedElementOrType obj) {
    PTypeMirror that;
    that = (PTypeMirror) obj;

    Types types;
    types = processingEnv.getTypeUtils();

    return types.isSameType(getType(), that.getType());
  }

  @Override
  protected final ProcessingAnnotation toProcessingAnnotation(AnnotationMirror mirror) {
    return ProcessingAnnotation.adapt(processingEnv, null, mirror);
  }

  abstract TypeMirror getType();

  NamedArray toNamedArray() {
    throw new UnsupportedOperationException("Implement me");
  }

  private TypeMirror getRawType(Class<?> type) {
    if (type.isArray()) {
      return getRawType0ArrayType(type);
    }

    if (type.isPrimitive()) {
      return getRawType0PrimitiveType(type);
    }

    return getRawType0DeclaredType(type);
  }

  private TypeMirror getRawType0ArrayType(Class<?> type) {
    Types types;
    types = processingEnv.getTypeUtils();

    Class<?> componentType;
    componentType = type.getComponentType();

    TypeMirror componentRawType;
    componentRawType = getRawType(componentType);

    return types.getArrayType(componentRawType);
  }

  private DeclaredType getRawType0DeclaredType(Class<?> type) {
    Elements elements;
    elements = processingEnv.getElementUtils();

    String thatCanonicalName;
    thatCanonicalName = type.getCanonicalName();

    TypeElement thatTypeElement;
    thatTypeElement = elements.getTypeElement(thatCanonicalName);

    Types types;
    types = processingEnv.getTypeUtils();

    return types.getDeclaredType(thatTypeElement);
  }

  private TypeMirror getRawType0PrimitiveType(Class<?> type) {
    String canonicalName;
    canonicalName = type.getCanonicalName();

    String enumName;
    enumName = canonicalName.toUpperCase(Locale.US);

    TypeKind kind;
    kind = TypeKind.valueOf(enumName);

    Types types;
    types = processingEnv.getTypeUtils();

    return types.getPrimitiveType(kind);
  }

}