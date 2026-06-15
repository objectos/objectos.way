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
package objectox.http.session;

import java.util.Objects;
import objectos.lang.Key;

public enum SessionAbsent implements Session {

  INSTANCE;

  @Override
  public final <T> T attr(Class<T> key) {
    Objects.requireNonNull(key, "key == null");

    return null;
  }

  @Override
  public final <T> T attr(Key<T> key) {
    Objects.requireNonNull(key, "key == null");

    return null;
  }

  @Override
  public final <T> T attr(Class<T> key, T value) {
    Objects.requireNonNull(key, "key == null");

    return null;
  }

  @Override
  public final <T> T attr(Key<T> key, T value) {
    Objects.requireNonNull(key, "key == null");

    return null;
  }

  @Override
  public final void invalidate() {

  }

  @Override
  public final boolean isPresent() {
    return false;
  }

}
