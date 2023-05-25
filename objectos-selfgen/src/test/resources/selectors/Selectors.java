/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package br.com.objectos.css.select;

public final class Selectors {
  public static TypeSelectorAlt a = null;

  public static TypeSelectorAlt div = null;

  private Selectors() {
  }

  public static PseudoElementSelectorAlt after() {
    throw new UnsupportedOperationException("Implement me");
  }

  public static PseudoElementSelectorAlt firstLetter() {
    throw new UnsupportedOperationException("Implement me");
  }
}
