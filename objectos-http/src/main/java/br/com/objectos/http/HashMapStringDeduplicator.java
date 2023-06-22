/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package br.com.objectos.http;

import java.util.HashMap;
import java.util.Map;

final class HashMapStringDeduplicator implements StringDeduplicator {

  private final Map<String, String> map = new HashMap<>();

  @Override
  public final String dedup(String name) {
    String s;
    s = map.get(name);

    if (s == null) {
      map.put(name, name);

      s = name;
    }

    return s;
  }

}