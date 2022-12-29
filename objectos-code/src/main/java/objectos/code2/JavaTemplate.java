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
package objectos.code2;

import java.util.Objects;
import objectos.code2.JavaModel.ArrayDimension;
import objectos.code2.JavaModel.ArrayType;
import objectos.code2.JavaModel.ArrayTypeComponent;
import objectos.code2.JavaModel.ArrayTypeElement;
import objectos.code2.JavaModel.At;
import objectos.code2.JavaModel.AutoImports;
import objectos.code2.JavaModel.Block;
import objectos.code2.JavaModel.Body;
import objectos.code2.JavaModel.BodyElement;
import objectos.code2.JavaModel.ClassKeyword;
import objectos.code2.JavaModel.ClassType;
import objectos.code2.JavaModel.Expression;
import objectos.code2.JavaModel.ExtendsKeyword;
import objectos.code2.JavaModel.FinalModifier;
import objectos.code2.JavaModel.Identifier;
import objectos.code2.JavaModel.ImplementsKeyword;
import objectos.code2.JavaModel.Include;
import objectos.code2.JavaModel.Modifier;
import objectos.code2.JavaModel.PackageKeyword;
import objectos.code2.JavaModel.PrimitiveType;
import objectos.code2.JavaModel.ReturnStatement;
import objectos.code2.JavaModel.StringLiteral;
import objectos.code2.JavaModel.ThisKeyword;
import objectos.code2.JavaModel.VoidKeyword;
import objectos.lang.Check;

public abstract class JavaTemplate {

  @FunctionalInterface
  protected interface IncludeTarget {
    void execute();
  }

  private InternalApi api;

  /**
   * Sole constructor.
   */
  protected JavaTemplate() {}

  @Override
  public String toString() {
    var out = new StringBuilder();

    var sink = JavaSink.ofStringBuilder(out);

    sink.eval(this);

    return out.toString();
  }

  protected final Modifier _abstract() {
    return api().modifier(Keyword.ABSTRACT);
  }

  protected final PrimitiveType _boolean() {
    return api().item(ByteProto.PRIMITIVE_TYPE, Keyword.BOOLEAN.ordinal());
  }

  protected final ClassKeyword _class(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check

    var api = api();

    return api.item(ByteProto.CLASS0, api.object(name));
  }

  protected final PrimitiveType _double() {
    return api().item(ByteProto.PRIMITIVE_TYPE, Keyword.DOUBLE.ordinal());
  }

  protected final ExtendsKeyword _extends(ClassType type) {
    var api = api();

    return api.elem(ByteProto.EXTENDS, api.count(type));
  }

  protected final FinalModifier _final() {
    return api().modifier(Keyword.FINAL);
  }

  protected final ImplementsKeyword _implements() {
    return api.item(ByteProto.IMPLEMENTS);
  }

  protected final PrimitiveType _int() {
    return api().item(ByteProto.PRIMITIVE_TYPE, Keyword.INT.ordinal());
  }

  protected final PackageKeyword _package(String name) {
    JavaModel.checkPackageName(name.toString()); // implicit null check

    var api = api();

    api.autoImports.packageName(name);

    return api.item(ByteProto.PACKAGE, api.object(name));
  }

  protected final Modifier _private() {
    return api().modifier(Keyword.PRIVATE);
  }

  protected final Modifier _protected() {
    return api().modifier(Keyword.PROTECTED);
  }

  protected final Modifier _public() {
    return api().modifier(Keyword.PUBLIC);
  }

  protected final ReturnStatement _return(Expression expression) {
    var api = api();

    var count = api.count(expression);

    return api.elem(ByteProto.RETURN_STATEMENT, count);
  }

  protected final Modifier _static() {
    return api().modifier(Keyword.STATIC);
  }

  protected final ThisKeyword _this() {
    return api().item(ByteProto.THIS);
  }

  protected final VoidKeyword _void() {
    return api().item(ByteProto.VOID);
  }

  protected final At at(ClassType annotationType) {
    return api().elem(ByteProto.ANNOTATION, 1);
  }

  protected final AutoImports autoImports() {
    var api = api();

    api.autoImports.enable();

    return api.item(ByteProto.AUTO_IMPORTS);
  }

  protected final Block block() {
    return api().elem(ByteProto.BLOCK, 0);
  }

  protected final Body body() {
    return api().elem(ByteProto.BODY, 0);
  }

  protected final Body body(BodyElement e1) {
    var api = api();

    var count = api.count(e1);

    return api.elem(ByteProto.BODY, count);
  }

  protected final Body body(BodyElement e1, BodyElement e2) {
    var api = api();

    var count = api.count(e1) + api.count(e2);

    return api.elem(ByteProto.BODY, count);
  }

  protected final Body body(BodyElement e1, BodyElement e2, BodyElement e3) {
    var api = api();

    var count = api.count(e1) + api.count(e2) + api.count(e3);

    return api.elem(ByteProto.BODY, count);
  }

  protected final Body body(BodyElement e1, BodyElement e2, BodyElement e3, BodyElement e4) {
    var api = api();

    var count = api.count(e1) + api.count(e2) + api.count(e3) + api.count(e4);

    return api.elem(ByteProto.BODY, count);
  }

  protected final Body body(
      BodyElement e1, BodyElement e2, BodyElement e3, BodyElement e4, BodyElement e5,
      BodyElement e6) {
    var api = api();

    var count = api.count(e1) + api.count(e2) + api.count(e3) + api.count(e4) + api.count(e5) +
        api.count(e6);

    return api.elem(ByteProto.BODY, count);
  }

  protected final Body body(
      BodyElement e01, BodyElement e02, BodyElement e03, BodyElement e04, BodyElement e05,
      BodyElement e06, BodyElement e07, BodyElement e08, BodyElement e09) {
    var api = api();

    var count = api.count(e01) + api.count(e02) + api.count(e03) + api.count(e04) + api.count(e05) +
        api.count(e06) + api.count(e07) + api.count(e08) + api.count(e09);

    return api.elem(ByteProto.BODY, count);
  }

  protected final Body body(
      BodyElement e01, BodyElement e02, BodyElement e03, BodyElement e04, BodyElement e05,
      BodyElement e06, BodyElement e07, BodyElement e08, BodyElement e09, BodyElement e10,
      BodyElement e11, BodyElement e12) {
    var api = api();

    var count = api.count(e01) + api.count(e02) + api.count(e03) + api.count(e04) + api.count(e05) +
        api.count(e06) + api.count(e07) + api.count(e08) + api.count(e09) + api.count(e10) +
        api.count(e11) + api.count(e12);

    return api.elem(ByteProto.BODY, count);
  }

  protected abstract void definition();

  protected final ArrayDimension dim() {
    return api().item(ByteProto.ARRAY_DIMENSION);
  }

  protected final Identifier id(String name) {
    JavaModel.checkIdentifier(name);

    var api = api();

    return api.item(ByteProto.IDENTIFIER, api.object(name));
  }

  protected final Include include(IncludeTarget target) {
    var api = api();

    api.lambdaStart();

    target.execute(); // implicit null-check

    api.lambdaEnd();

    return JavaModel.INCLUDE;
  }

  protected final StringLiteral s(String string) {
    Objects.requireNonNull(string, "string == null");

    var api = api();

    return api.item(ByteProto.STRING_LITERAL, api.object(string));
  }

  protected final ArrayType t(
      ArrayTypeComponent type,
      ArrayTypeElement e1) {
    var api = api();

    var count = api.count(type) + api.count(e1);

    return api.elem(ByteProto.ARRAY_TYPE, count);
  }

  protected final ArrayType t(
      ArrayTypeComponent type,
      ArrayTypeElement e1, ArrayTypeElement e2) {
    var api = api();

    var count = api.count(type) + api.count(e1) + api.count(e2);

    return api.elem(ByteProto.ARRAY_TYPE, count);
  }

  protected final ArrayType t(
      ArrayTypeComponent type,
      ArrayTypeElement e1, ArrayTypeElement e2, ArrayTypeElement e3) {
    var api = api();

    var count = api.count(type) + api.count(e1) + api.count(e2) + api.count(e3);

    return api.elem(ByteProto.ARRAY_TYPE, count);
  }

  protected final ArrayType t(
      ArrayTypeComponent type,
      ArrayTypeElement e1, ArrayTypeElement e2, ArrayTypeElement e3, ArrayTypeElement e4) {
    var api = api();

    var count = api.count(type) + api.count(e1) + api.count(e2) + api.count(e3) + api.count(e4);

    return api.elem(ByteProto.ARRAY_TYPE, count);
  }

  protected final ClassType t(Class<?> type) {
    return api().t(type);
  }

  protected final ClassType t(String packageName, String simpleName) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check
    JavaModel.checkSimpleName(simpleName.toString()); // implicit null check

    var api = api();

    return api.item(
      ByteProto.CLASS_TYPE, api.object(packageName),
      1, api.object(simpleName)
    );
  }

  final void execute(InternalApi api) {
    Check.state(this.api == null, """
    Another evaluation is already in progress.
    """);

    this.api = api;

    try {
      definition();
    } finally {
      this.api = null;
    }
  }

  private InternalApi api() {
    Check.state(api != null, """
    An InternalApi instance was not set.

    Are you trying to execute the method directly?
    Please not that this method should only be invoked inside a definition() method.
    """);

    return api;
  }

}