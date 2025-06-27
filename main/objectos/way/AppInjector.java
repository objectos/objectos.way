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

import java.util.Map;

final class AppInjector implements App.Injector {

  private final Map<Object, Object> map;

  AppInjector(Map<Object, Object> map) {
    this.map = map;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T getInstance(Class<T> type) {
    final Object maybeNull;
    maybeNull = map.get(type);

    if (maybeNull == null) {
      throw new IllegalArgumentException("No mappings were found for " + type);
    } else {
      return (T) maybeNull;
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T getInstance(Lang.Key<T> key) {
    final Object maybeNull;
    maybeNull = map.get(key);

    if (maybeNull == null) {
      throw new IllegalArgumentException("No mappings were found for " + key);
    } else {
      return (T) maybeNull;
    }
  }

}