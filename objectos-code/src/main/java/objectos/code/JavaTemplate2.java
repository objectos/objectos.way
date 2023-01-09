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

import objectos.code.JavaModel.AbstractModifier;
import objectos.code.JavaModel.At;
import objectos.code.JavaModel.AutoImports;
import objectos.code.JavaModel.Block;
import objectos.code.JavaModel.Body;
import objectos.code.JavaModel.BodyElement;
import objectos.code.JavaModel.ClassKeyword;
import objectos.code.JavaModel.ClassType;
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
import objectos.code.JavaModel.StaticModifier;
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

  @Override
  protected final StaticModifier _static() {
    return modifier(Keyword.STATIC);
  }

  @Override
  protected final VoidKeyword _void() {
    return api().item(ByteProto.VOID);
  }

  protected final At at(ClassType annotationType) {
    var api = api();
    api.elemstart(ByteProto.ANNOTATION);
    api.elemcnt(annotationType);
    return api.elemret();
  }

  @Override
  protected final AutoImports autoImports() {
    var api = api();

    api.autoImports.enable();

    return api.item(ByteProto.AUTO_IMPORTS);
  }

  protected final Block block() {
    var api = api();
    api.elemstart(ByteProto.BLOCK);
    return api.elemret();
  }

  protected final Body body() {
    var api = api();
    api.elemstart(ByteProto.BODY);
    return api.elemret();
  }

  protected final Body body(
      BodyElement e1) {
    var api = api();
    api.elemstart(ByteProto.BODY);
    api.elemcnt(e1);
    return api.elemret();
  }

  protected final Body body(
      BodyElement e1, BodyElement e2) {
    var api = api();
    api.elemstart(ByteProto.BODY);
    api.elemcnt(e1);
    api.elemcnt(e2);
    return api.elemret();
  }

  protected final Body body(
      BodyElement e1, BodyElement e2, BodyElement e3) {
    var api = api();
    api.elemstart(ByteProto.BODY);
    api.elemcnt(e1);
    api.elemcnt(e2);
    api.elemcnt(e3);
    return api.elemret();
  }

  protected final Body body(
      BodyElement e1, BodyElement e2, BodyElement e3, BodyElement e4) {
    var api = api();
    api.elemstart(ByteProto.BODY);
    api.elemcnt(e1);
    api.elemcnt(e2);
    api.elemcnt(e3);
    api.elemcnt(e4);
    return api.elemret();
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