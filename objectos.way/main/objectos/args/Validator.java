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
package objectos.args;

import java.util.function.Predicate;

final class Validator<T> {

  final Predicate<T> predicate;

  final String reasonPhrase;

  public Validator(Predicate<T> predicate, String reasonPhrase) {
    this.predicate = predicate;

    this.reasonPhrase = reasonPhrase;
  }

  public final void accept(ErrorCollector collector, String name, T value) {
    if (!predicate.test(value)) {
      collector.addMessage("Invalid " + name + " value: " + reasonPhrase);
    }
  }

}