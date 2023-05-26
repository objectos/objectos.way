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
package objectos.css.select;

import java.util.Objects;
import objectos.html.tmpl.Instruction.ExternalAttribute;

public class IdSelector extends SimpleSelector implements ExternalAttribute.Id {

  private final String id;

  IdSelector(String id) {
    this.id = id;
  }

  @Override
  public final <R, P> R acceptSimpleSelectorVisitor(SimpleSelectorVisitor<R, P> visitor, P p) {
    return visitor.visitIdSelector(this, p);
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof IdSelector)) {
      return false;
    }
    return toString().equals(obj.toString());
  }

  @Override
  public final int hashCode() {
    return id.hashCode();
  }

  public final String id() {
    return id;
  }

  @Override
  public final boolean matches(Selectable element) {
    String attributeValue = element.attributeValue("id");
    return Objects.equals(attributeValue, id);
  }

  @Override
  public final String toString() {
    return "#" + id;
  }

  @Override
  public final String value() {
    return id;
  }

}