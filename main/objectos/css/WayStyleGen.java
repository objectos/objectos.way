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
import objectos.lang.object.Check;
import objectos.notes.NoteSink;

final class WayStyleGen implements StyleGen {

  private final NoteSink noteSink;

  private final Map<String, RuleFactory> factories;

  private final Map<String, Variant> variants;

  public WayStyleGen(NoteSink noteSink, Map<String, RuleFactory> factories, Map<String, Variant> variants) {
    this.noteSink = noteSink;

    this.factories = factories;

    this.variants = variants;
  }

  @Override
  public final String generate(Iterable<Class<?>> classes) {
    Check.notNull(classes, "classes == null");

    WayStyleGenRound round;
    round = new WayStyleGenRound(noteSink, factories, variants);

    for (var clazz : classes) {
      round.scan(clazz);
    }

    return round.generate();
  }

}