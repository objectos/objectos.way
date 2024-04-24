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
package objectos.http;

import java.util.Set;

/**
 * Represents the query component of the request target.
 */
public interface UriQuery {

  Set<String> names();

  String get(String name);

  default int getAsInt(String name, int defaultValue) {
    String maybe;
    maybe = get(name);

    if (maybe == null) {
      return defaultValue;
    }

    try {
      return Integer.parseInt(maybe);
    } catch (NumberFormatException expected) {
      return defaultValue;
    }
  }
  
  UriQuery set(String name, String value);

  String value();

  boolean isEmpty();

}