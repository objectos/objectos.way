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
package br.com.objectos.css;

interface Selector {

  static Selector parse(String selector) {
    if (selector == null) {
      selector = "";
    }

    selector = selector.trim();

    String[] parts = selector.split(",");

    Selector result = new SimpleSelector(parts[0]);
    if (parts.length == 1) {
      return result;
    }

    for (int i = 1; i < parts.length; i++) {
      SimpleSelector next = new SimpleSelector(parts[i]);
      result = result.add(next);
    }

    return result;
  }

  default Selector add(SimpleSelector selector) {
    return new ListSelector(this, selector);
  }

  Selector nest(Nestable child);

  SelectorBuilder selectorBuilder();

}