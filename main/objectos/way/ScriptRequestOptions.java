/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import module java.base;
import objectos.way.Script.Callback;

final class ScriptRequestOptions implements Script.RequestOptions {

  Script.Method method = Script.GET;

  Object url;

  Callback onSuccess = () -> {};

  @Override
  public final void method(Script.Method method) {
    this.method = Objects.requireNonNull(method, "method == null");
  }

  @Override
  public final void url(String value) {
    url = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void url(Script.StringQuery value) {
    url = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void onSuccess(Callback callback) {
    onSuccess = Objects.requireNonNull(callback, "callback == null");
  }

}