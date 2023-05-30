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
package objectos.css.internal;

import java.util.Iterator;
import objectos.css.pseudom.IterableOnce;
import objectos.css.pseudom.StyleSheet;
import objectos.css.pseudom.StyleSheet.Rule;

public final class PseudoStyleSheet
    implements StyleSheet, IterableOnce<Rule>, Iterator<Rule> {

  @Override
  public final IterableOnce<Rule> rules() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final Iterator<Rule> iterator() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final boolean hasNext() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final Rule next() {
    throw new UnsupportedOperationException("Implement me");
  }

}