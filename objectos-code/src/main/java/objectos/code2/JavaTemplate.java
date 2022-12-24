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

import objectos.code2.JavaModel.AutoImports;
import objectos.code2.JavaModel.Body;
import objectos.code2.JavaModel.ClassKeyword;
import objectos.code2.JavaModel.ClassType;
import objectos.code2.JavaModel.ExtendsKeyword;
import objectos.code2.JavaModel.FinalModifier;
import objectos.code2.JavaModel.PackageKeyword;
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
    return api()._class(name);
  }

  protected final ExtendsKeyword _extends(ClassType type) {
    return api()._extends(type);
  }

  protected final FinalModifier _final() {
    return api()._final();
  }

  protected final PackageKeyword _package(String name) {
    return api()._package(name);
  }

  protected final AutoImports autoImports() {
    return api().autoImports();
  }

  protected final Body body() {
    return api().body();
  }

  protected abstract void definition();

  protected final ClassType t(Class<?> value) {
    return api().t(value);
  }

  protected final ClassType t(String packageName, String simpleName) {
    return api().t(packageName, simpleName);
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