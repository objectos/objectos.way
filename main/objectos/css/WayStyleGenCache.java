/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.css;

import java.util.Map;
import objectos.util.map.GrowableSequencedMap;

abstract class WayStyleGenCache extends WayStyleGenSplitter {

  final Map<String, Rule> rules = new GrowableSequencedMap<>();

  @Override
  final void onSplit(String s) {
    Rule existing;
    existing = rules.get(s);

    if (existing == null) {
      Rule newRule;
      newRule = onCacheMiss(s);

      rules.put(s, newRule);
    }

    else {
      onCacheHit(existing);
    }
  }

  void onCacheHit(Rule existing) {
    // for testing
  }

  abstract Rule onCacheMiss(String className);

}