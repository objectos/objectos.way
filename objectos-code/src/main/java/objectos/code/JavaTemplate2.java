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

import objectos.code.JavaModel.AutoImports;
import objectos.code.JavaModel.Body;
import objectos.code.JavaModel.ClassKeyword;
import objectos.code.JavaModel.ClassType;
import objectos.code.JavaModel.ExtendsKeyword;
import objectos.code.JavaModel.PackageKeyword;

abstract class JavaTemplate2 extends JavaTemplate {

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
  protected final PackageKeyword _package(String packageName) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check

    var api = api();

    api.autoImports.packageName(packageName);

    return api.item(ByteProto.PACKAGE, api.object(packageName));
  }

  @Override
  protected final AutoImports autoImports() {
    var api = api();

    api.autoImports.enable();

    return api.item(ByteProto.AUTO_IMPORTS);
  }

  protected final Body body() {
    return api().elem(ByteProto.BODY, 0);
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

}