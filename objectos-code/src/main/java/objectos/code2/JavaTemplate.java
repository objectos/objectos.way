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

import objectos.code2.JavaModel.At;
import objectos.code2.JavaModel.AutoImports;
import objectos.code2.JavaModel.Block;
import objectos.code2.JavaModel.Body;
import objectos.code2.JavaModel.BodyElement;
import objectos.code2.JavaModel.ClassKeyword;
import objectos.code2.JavaModel.ClassType;
import objectos.code2.JavaModel.ExtendsKeyword;
import objectos.code2.JavaModel.FinalModifier;
import objectos.code2.JavaModel.Identifier;
import objectos.code2.JavaModel.PackageKeyword;
import objectos.code2.JavaModel.VoidKeyword;
import objectos.lang.Check;

public abstract class JavaTemplate {

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

  protected final ClassKeyword _class(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check

    return api().item(ByteProto.CLASS0, api.object(name));
  }

  protected final ExtendsKeyword _extends(ClassType type) {
    return api().elem(ByteProto.EXTENDS, 1);
  }

  protected final FinalModifier _final() {
    return api().modifier(Keyword.FINAL);
  }

  protected final PackageKeyword _package(String name) {
    JavaModel.checkPackageName(name.toString()); // implicit null check

    var api = api();

    api.autoImports.packageName(name);

    return api.item(ByteProto.PACKAGE, api.object(name));
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

  protected final Body body(BodyElement e1, BodyElement e2, BodyElement e3) {
    return api().elem(ByteProto.BODY, 3);
  }

  protected abstract void definition();

  protected final Identifier id(String name) {
    JavaModel.checkIdentifier(name);

    var api = api();

    return api.item(ByteProto.IDENTIFIER, api.object(name));
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