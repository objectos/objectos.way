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
package br.com.objectos.code.java.type;

import br.com.objectos.code.java.declaration.AnnotationCode;
import br.com.objectos.code.java.declaration.AnnotationCodeElement;
import br.com.objectos.code.java.declaration.ClassCode;
import br.com.objectos.code.java.declaration.ClassCodeElement;
import br.com.objectos.code.java.declaration.EnumCode;
import br.com.objectos.code.java.declaration.EnumCodeElement;
import br.com.objectos.code.java.declaration.InterfaceCode;
import br.com.objectos.code.java.declaration.InterfaceCodeElement;
import br.com.objectos.code.java.declaration.PackageName;
import br.com.objectos.code.java.declaration.ThrowsElement;
import br.com.objectos.code.java.expression.ArgumentsElement;
import br.com.objectos.code.java.expression.Callee;
import br.com.objectos.code.java.expression.ExpressionName;
import br.com.objectos.code.java.expression.Expressions;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.expression.MethodInvocation;
import br.com.objectos.code.java.expression.MethodReference;
import br.com.objectos.code.java.expression.TypeWitness;
import br.com.objectos.code.java.io.JavaFileImportSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import objectos.lang.Check;
import objectos.lang.HashCode;

public class NamedClass extends NamedClassOrParameterized
    implements
    NamedClassOrPackage,
    Comparable<NamedClass>,
    Callee,
    AnnotationCodeElement,
    ClassCodeElement,
    EnumCodeElement,
    InterfaceCodeElement,
    ThrowsElement {

  private static final NamedClass OBJECT = PackageName.named("java.lang").nestedClass("Object");

  private final String canonicalName;
  private final NamedClassOrPackage enclosingElement;

  private final String simpleName;

  NamedClass(NamedClassOrPackage enclosingElement, String simpleName) {
    this.enclosingElement = enclosingElement;
    this.simpleName = simpleName;

    canonicalName = getCanonicalName0(enclosingElement, simpleName);
  }

  public static NamedClass object() {
    return OBJECT;
  }

  public static NamedClass of(Class<?> type) {
    return ofWithNullMessage(type, "type == null");
  }

  public static NamedClass of(NamedClass className, String simpleName) {
    Check.notNull(className, "className == null");
    Check.notNull(simpleName, "simpleName == null");
    Check.argument(
      SourceVersion.isName(simpleName),
      simpleName, " is not a valid class name"
    );

    return new NamedClass(className, simpleName);
  }

  public static NamedClass of(PackageName packageName, String simpleName) {
    Check.notNull(packageName, "packageName == null");
    Check.notNull(simpleName, "simpleName == null");
    Check.argument(
      SourceVersion.isName(simpleName),
      simpleName, " is not a valid class name"
    );

    return new NamedClass(packageName, simpleName);
  }

  public static NamedClass of(TypeElement element) {
    Check.notNull(element, "element == null");

    return ofUnchecked(element);
  }

  public static NamedClass ofWithNullMessage(Class<?> type, String nullMessage) {
    Check.notNull(type, nullMessage);
    return ofUnchecked(type);
  }

  private static NamedClass ofUnchecked(Class<?> type) {
    List<String> names;
    names = new ArrayList<>();

    names.add(type.getSimpleName());

    Class<?> enclosing;
    enclosing = type.getEnclosingClass();

    while (enclosing != null) {
      String enclosingSimpleName;
      enclosingSimpleName = enclosing.getSimpleName();

      names.add(enclosingSimpleName);

      enclosing = enclosing.getEnclosingClass();
    }

    NamedClass result = null;

    Package typePackage;
    typePackage = type.getPackage();

    NamedClassOrPackage currentName;
    currentName = PackageName.of(typePackage);

    Collections.reverse(names);

    for (String name : names) {
      result = currentName.nestedClass(name);

      currentName = result;
    }

    return result;
  }

  private static NamedClass ofUnchecked(TypeElement element) {
    List<String> names;
    names = new ArrayList<>();

    Name elementSimpleName;
    elementSimpleName = element.getSimpleName();

    names.add(elementSimpleName.toString());

    Element enclosing;
    enclosing = element.getEnclosingElement();

    while (enclosing.getKind() != ElementKind.PACKAGE) {
      Name enclosingSimpleName;
      enclosingSimpleName = enclosing.getSimpleName();

      names.add(enclosingSimpleName.toString());

      enclosing = enclosing.getEnclosingElement();
    }

    NamedClass result = null;

    PackageElement enclosingPackageElement;
    enclosingPackageElement = (PackageElement) enclosing;

    Name packageQualifiedName;
    packageQualifiedName = enclosingPackageElement.getQualifiedName();

    NamedClassOrPackage currentName;
    currentName = PackageName.named(packageQualifiedName.toString());

    Collections.reverse(names);

    for (String name : names) {
      result = currentName.nestedClass(name);

      currentName = result;
    }

    return result;
  }

  public final QualifiedSuperKeyword _super() {
    return QualifiedSuperKeywordImpl.ofUnchecked(this);
  }

  @Override
  public final void acceptAnnotationCodeBuilder(AnnotationCode.Builder builder) {
    builder.setClassName(this);
  }

  @Override
  public final void acceptClassCodeBuilder(ClassCode.Builder builder) {
    builder.simpleName(this);
  }

  @Override
  public final void acceptEnumCodeBuilder(EnumCode.Builder builder) {
    builder.simpleName(this);
  }

  @Override
  public final void acceptInterfaceCodeBuilder(InterfaceCode.Builder builder) {
    builder.simpleName(simpleName);
  }

  @Override
  public final String acceptJavaFileImportSet(JavaFileImportSet set) {
    if (set.contains(this)) {
      return simpleName;
    }

    if (set.canSkipImport(getPackage())) {
      set.addSimpleName(simpleName);
      return simpleName;
    }

    if (set.addSimpleName(simpleName)) {
      set.addQualifiedName(this);
      return simpleName;
    }

    return toString();
  }

  @Override
  public final void acceptThrowsElementConsumer(Consumer consumer) {
    consumer.addThrownType(this);
  }

  @Override
  public final <R, P> R acceptTypeNameVisitor(NamedTypeVisitor<R, P> visitor, P p) {
    return visitor.visitNamedClass(this, p);
  }

  @Override
  public final int compareTo(NamedClass o) {
    return canonicalName.compareTo(o.canonicalName);
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof NamedClass)) {
      return false;
    }
    NamedClass that = (NamedClass) obj;
    return enclosingElement.equals(that.enclosingElement)
        && simpleName.equals(that.simpleName);
  }

  @Override
  public final NamedType getArrayCreationExpressionName() {
    return this;
  }

  public final String getCanonicalName() {
    return canonicalName;
  }

  @Override
  public final PackageName getPackage() {
    return enclosingElement.getPackage();
  }

  public final String getSimpleName() {
    return simpleName;
  }

  public final boolean hasCanonicalName(String canonicalName) {
    return this.canonicalName.equals(canonicalName);
  }

  @Override
  public final int hashCode() {
    return HashCode.of(enclosingElement, simpleName);
  }

  public final ExpressionName id(Identifier id) {
    return Expressions.expressionName(this, id);
  }

  public final ExpressionName id(String id) {
    return Expressions.expressionName(this, id);
  }

  @Override
  public final MethodInvocation invoke(String methodName) {
    return Expressions.invoke(this, methodName);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1) {
    return Expressions.invoke(this, methodName, a1);
  }

  @Override
  public final MethodInvocation invoke(String methodName, ArgumentsElement... args) {
    return Expressions.invoke(this, methodName, args);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2) {
    return Expressions.invoke(this, methodName, a1, a2);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3) {
    return Expressions.invoke(this, methodName, a1, a2, a3);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4) {
    return Expressions.invoke(this, methodName, a1, a2, a3, a4);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5) {
    return Expressions.invoke(this, methodName, a1, a2, a3, a4, a5);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5,
      ArgumentsElement a6) {
    return Expressions.invoke(this, methodName, a1, a2, a3, a4, a5, a6);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5,
      ArgumentsElement a6,
      ArgumentsElement a7) {
    return Expressions.invoke(this, methodName, a1, a2, a3, a4, a5, a6, a7);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5,
      ArgumentsElement a6,
      ArgumentsElement a7,
      ArgumentsElement a8) {
    return Expressions.invoke(this, methodName, a1, a2, a3, a4, a5, a6, a7, a8);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5,
      ArgumentsElement a6,
      ArgumentsElement a7,
      ArgumentsElement a8,
      ArgumentsElement a9) {
    return Expressions.invoke(this, methodName, a1, a2, a3, a4, a5, a6, a7, a8, a9);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      Iterable<? extends ArgumentsElement> args) {
    return Expressions.invoke(this, methodName, args);
  }

  @Override
  public final MethodInvocation invoke(
      TypeWitness witness, String methodName) {
    return Expressions.invoke(this, witness, methodName);
  }

  @Override
  public final MethodInvocation invoke(
      TypeWitness witness, String methodName, ArgumentsElement a1) {
    return Expressions.invoke(this, witness, methodName, a1);
  }

  @Override
  public final MethodInvocation invoke(
      TypeWitness witness, String methodName, ArgumentsElement a1, ArgumentsElement a2) {
    return Expressions.invoke(this, witness, methodName, a1, a2);
  }

  @Override
  public final MethodInvocation invoke(
      TypeWitness witness, String methodName, ArgumentsElement a1, ArgumentsElement a2,
      ArgumentsElement a3) {
    return Expressions.invoke(this, witness, methodName, a1, a2, a3);
  }

  @Override
  public final MethodInvocation invoke(
      TypeWitness witness, String methodName, ArgumentsElement a1, ArgumentsElement a2,
      ArgumentsElement a3, ArgumentsElement a4) {
    return Expressions.invoke(this, witness, methodName, a1, a2, a3, a4);
  }

  @Override
  public final MethodInvocation invoke(
      TypeWitness witness, String methodName, Iterable<? extends ArgumentsElement> args) {
    return Expressions.invoke(this, witness, methodName, args);
  }

  @Override
  public final boolean isJavaLangObject() {
    return canonicalName.equals("java.lang.Object");
  }

  @Override
  public final NamedClass nestedClass(String simpleName) {
    return of(this, simpleName);
  }

  @Override
  public final MethodReference ref(String methodName) {
    return Expressions.ref(this, methodName);
  }

  @Override
  public final MethodReference ref(TypeWitness witness, String methodName) {
    return Expressions.ref(this, witness, methodName);
  }

  @Override
  public final NamedClass toClassNameUnchecked() {
    return this;
  }

  @Override
  public final String toString() {
    return canonicalName;
  }

  public final NamedClass withPrefix(String prefix) {
    Check.notNull(prefix, "prefix == null");
    String withPrefix = prefix + simpleName;
    checkTypeName(withPrefix);
    return new NamedClass(enclosingElement, withPrefix);
  }

  public final NamedClass withSuffix(String suffix) {
    Check.notNull(suffix, "suffix == null");
    String withSuffix = simpleName + suffix;
    checkTypeName(withSuffix);
    return new NamedClass(enclosingElement, withSuffix);
  }

  private void checkTypeName(String simpleName) {
    Check.argument(
      simpleName != null && SourceVersion.isName(simpleName),
      simpleName, "%s is not a valid type name"
    );
  }

  private String getCanonicalName0(NamedClassOrPackage element, String simpleName) {
    String enclosing;
    enclosing = element.toString();

    if (enclosing.isEmpty()) {
      return simpleName;
    } else {
      return enclosing + "." + simpleName;
    }
  }

}