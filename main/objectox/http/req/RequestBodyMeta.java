/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.http.req;

record RequestBodyMeta(Data data, Type type) {

  sealed interface Data {}

  enum DataKind implements Data {
    EMPTY;
  }

  record Fixed(long length) implements Data {}

  sealed interface Type {}

  enum TypeKind implements Type {
    APPLICATION_FORM_URLENCODED,

    NONE;
  }

  private static final RequestBodyMeta EMPTY = new RequestBodyMeta(DataKind.EMPTY, TypeKind.NONE);

  public static RequestBodyMeta of(long length, Type type) {
    final Fixed fixed;
    fixed = new Fixed(length);

    return new RequestBodyMeta(fixed, type);
  }

  public static RequestBodyMeta ofEmpty() {
    return EMPTY;
  }

}
