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
package objectos.code;

import java.util.Objects;
import objectos.code.JavaModel.AbstractModifier;
import objectos.code.JavaModel.At;
import objectos.code.JavaModel.AutoImports;
import objectos.code.JavaModel.Block;
import objectos.code.JavaModel.BlockElement;
import objectos.code.JavaModel.Body;
import objectos.code.JavaModel.BodyElement;
import objectos.code.JavaModel.ClassKeyword;
import objectos.code.JavaModel.ClassType;
import objectos.code.JavaModel.ExpressionName;
import objectos.code.JavaModel.ExtendsKeyword;
import objectos.code.JavaModel.FinalModifier;
import objectos.code.JavaModel.Identifier;
import objectos.code.JavaModel.ImplementsKeyword;
import objectos.code.JavaModel.Include;
import objectos.code.JavaModel.PackageKeyword;
import objectos.code.JavaModel.PrimitiveType;
import objectos.code.JavaModel.PrivateModifier;
import objectos.code.JavaModel.ProtectedModifier;
import objectos.code.JavaModel.PublicModifier;
import objectos.code.JavaModel.ReturnKeyword;
import objectos.code.JavaModel.StaticModifier;
import objectos.code.JavaModel.StringLiteral;
import objectos.code.JavaModel.VoidKeyword;

abstract class JavaTemplate2 extends JavaTemplate {

  @Override
  protected final AbstractModifier _abstract() {
    return modifier(Keyword.ABSTRACT);
  }

  protected final ClassKeyword _class(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check

    var api = api();

    return api.item(ByteProto.CLASS, api.object(name));
  }

  @Override
  protected final ExtendsKeyword _extends() {
    return api().item(ByteProto.EXTENDS);
  }

  @Override
  protected final FinalModifier _final() {
    return api().item(ByteProto.MODIFIER, Keyword.FINAL.ordinal());
  }

  @Override
  protected final ImplementsKeyword _implements() {
    return api().item(ByteProto.IMPLEMENTS);
  }

  @Override
  protected final PrimitiveType _int() {
    return api().item(ByteProto.PRIMITIVE_TYPE, Keyword.INT.ordinal());
  }

  @Override
  protected final PackageKeyword _package(String packageName) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check

    var api = api();

    api.autoImports.packageName(packageName);

    return api.item(ByteProto.PACKAGE, api.object(packageName));
  }

  @Override
  protected final PrivateModifier _private() {
    return modifier(Keyword.PRIVATE);
  }

  @Override
  protected final ProtectedModifier _protected() {
    return modifier(Keyword.PROTECTED);
  }

  @Override
  protected final PublicModifier _public() {
    return modifier(Keyword.PUBLIC);
  }

  protected final ReturnKeyword _return() {
    return api().item(ByteProto.RETURN);
  }

  @Override
  protected final StaticModifier _static() {
    return modifier(Keyword.STATIC);
  }

  @Override
  protected final VoidKeyword _void() {
    return api().item(ByteProto.VOID);
  }

  protected final At at(ClassType annotationType) {
    return api().elem(ByteProto.ANNOTATION, annotationType.self());
  }

  @Override
  protected final AutoImports autoImports() {
    var api = api();

    api.autoImports.enable();

    return api.item(ByteProto.AUTO_IMPORTS);
  }

  protected final Block block() {
    return api().elem(ByteProto.BLOCK);
  }

  protected final Block block(BlockElement e1) {
    return api().elem(ByteProto.BLOCK, e1.self());
  }

  protected final Block block(BlockElement e1, BlockElement e2) {
    return api().elem(ByteProto.BLOCK, e1.self(), e2.self());
  }

  protected final Body body() {
    return api().elem(ByteProto.BODY);
  }

  protected final Body body(BodyElement e1) {
    return api().elem(ByteProto.BODY, e1.self());
  }

  protected final Body body(BodyElement e1, BodyElement e2) {
    return api().elem(ByteProto.BODY, e1.self(), e2.self());
  }

  protected final Body body(BodyElement e1, BodyElement e2, BodyElement e3) {
    return api().elem(ByteProto.BODY, e1.self(), e2.self(), e3.self());
  }

  protected final Body body(BodyElement e1, BodyElement e2, BodyElement e3, BodyElement e4) {
    return api().elem(ByteProto.BODY, e1.self(), e2.self(), e3.self(), e4.self());
  }

  protected final Body body(BodyElement e1, BodyElement e2, BodyElement e3, BodyElement e4,
      BodyElement e5) {
    return api().elem(ByteProto.BODY, e1.self(), e2.self(), e3.self(), e4.self(),
      e5.self());
  }

  @Override
  protected void definition() {}

  @Override
  protected final Identifier id(String name) {
    JavaModel.checkIdentifier(name);
    var api = api();
    return api.item(ByteProto.IDENTIFIER, api.object(name));
  }

  @Override
  protected final Include include(IncludeTarget target) {
    var api = api();
    api.includestart();
    target.execute(); // implicit null-check
    api.includeend();
    return JavaModel.INCLUDE;
  }

  protected final Include include(JavaTemplate2 template) {
    var api = api();
    api.includestart();
    template.execute(api);
    api.includeend();
    return JavaModel.INCLUDE;
  }

  protected final ExpressionName n(String id1) {
    JavaModel.checkIdentifier(id1.toString()); // implicit null check

    var api = api();
    api.identifierext(id1);
    return api.elem(ByteProto.EXPRESSION_NAME, JavaModel.EXT);
  }

  @Override
  protected final StringLiteral s(String string) {
    Objects.requireNonNull(string, "string == null");

    var api = api();

    return api.item(ByteProto.STRING_LITERAL, api.object(string));
  }

  @Override
  protected final ClassType t(Class<?> type) {
    return api().classType(type);
  }

  @Override
  protected final ClassType t(String packageName, String simpleName) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check
    JavaModel.checkSimpleName(simpleName.toString()); // implicit null check

    var api = api();

    return api.item(
      ByteProto.CLASS_TYPE, api.object(packageName),
      1, api.object(simpleName)
    );
  }

  @Override
  final void onEval() {
    v2 = true;
  }

  private JavaModel._Item modifier(Keyword value) {
    return api().item(ByteProto.MODIFIER, value.ordinal());
  }

}