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

import objectos.code.JavaModel.Body;
import objectos.code.JavaModel.ClassKeyword;
import objectos.code.JavaModel.PackageKeyword;

abstract class JavaTemplate2 extends JavaTemplate {

  protected final ClassKeyword _class(String name) {
    JavaModel.checkSimpleName(name.toString()); // implicit null check

    var api = api();

    return api.item(ByteProto.CLASS, api.object(name));
  }

  @Override
  protected final PackageKeyword _package(String packageName) {
    JavaModel.checkPackageName(packageName.toString()); // implicit null check

    var api = api();

    return api.item(ByteProto.PACKAGE, api.object(packageName));
  }

  protected final Body body() {
    return api().elem(ByteProto.BODY, 0);
  }

  @Override
  final void onEval() {
    v2 = true;
  }

}