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
package objectox.http.handler;

import java.util.Map;
import java.util.function.Predicate;

record SegmentParam(String paramName, char delimiter, Predicate<String> condition) implements Segment {

  public SegmentParam(String paramName, char delimiter) {
    this(paramName, delimiter, PathParamPredicates.TRUE);
  }

  @Override
  public final boolean matches(RequestPath path) {
    final int delimiterIndex;
    delimiterIndex = path.indexOf(delimiter);

    if (delimiterIndex < 0) {
      return false;
    }

    final String value;
    value = path.substring(delimiterIndex);

    if (!condition.test(value)) {
      return false;
    } else {
      return path.param(paramName, value);
    }
  }

  public final SegmentParam with(Map<String, Predicate<String>> predicates) {
    final String key;
    key = paramName;

    final Predicate<String> defaultValue;
    defaultValue = condition;

    final Predicate<String> predicate;
    predicate = predicates.getOrDefault(key, defaultValue);

    return new SegmentParam(paramName, delimiter, predicate);
  }

}
