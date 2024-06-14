/*
 * Copyright (C) 2023 Objectos Software LTDA.
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

import java.util.Set;

public enum HttpUriQueryEmpty implements Http.Request.Target.Query {

  INSTANCE;

  @Override
  public final Set<String> names() {
    return Set.of();
  }

  @Override
  public final String get(String name) {
    return null;
  }

  @Override
  public final Http.Request.Target.Query set(String name, String value) {
    HttpUriQuery q;
    q = new HttpUriQuery();

    return q.set(name, value);
  }

  @Override
  public final String value() {
    return "";
  }

  @Override
  public final boolean isEmpty() {
    return true;
  }

  @Override
  public final String encodedValue() {
    return "";
  }

}