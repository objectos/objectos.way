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
package css.test;

import br.com.objectos.css.Keyword;
import br.com.objectos.css.ValueVisitor;
import javax.annotation.Generated;

@Generated("br.com.objectos.css.selfgen.SpecProcessor")
final class KeywordImpl implements BackgroundColorValue, FixedKeyword {
  private final String name;

  KeywordImpl(String name) {
    this.name = name;
  }

  @Override
  public final void acceptValueVisitor(ValueVisitor visitor) {
    visitor.visitKeyword(name);
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof Keyword)) {
      return false;
    }
    Keyword that = (Keyword) obj;
    return toString().equals(that.toString());
  }

  @Override
  public final int hashCode() {
    return name.hashCode();
  }

  @Override
  public final String name() {
    return name;
  }

  @Override
  public final String toString() {
    return name;
  }
}
