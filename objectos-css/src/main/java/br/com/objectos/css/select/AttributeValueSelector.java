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
package br.com.objectos.css.select;

import java.util.Objects;

public class AttributeValueSelector extends SimpleSelector {

  private final AttributeSelector previous;
  private final AttributeValueOperator operator;
  private final String value;

  AttributeValueSelector(AttributeSelector previous,
                         AttributeValueElement element) {
    this(previous, element.operator, element.value);
  }

  AttributeValueSelector(AttributeSelector previous,
                         AttributeValueOperator operator,
                         String value) {
    this.previous = previous;
    this.operator = operator;
    this.value = value;
  }

  @Override
  public final <R, P> R acceptSimpleSelectorVisitor(SimpleSelectorVisitor<R, P> visitor, P p) {
    return visitor.visitAttributeValueSelector(this, p);
  }

  public final AttributeSelector attribute() {
    return previous;
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof AttributeValueSelector)) {
      return false;
    }
    return toString().equals(obj.toString());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(previous, operator, value);
  }

  @Override
  public final boolean matches(Selectable element) {
    return previous.matches(element)
        && matchesImpl(element);
  }

  public final String name() {
    return previous.name();
  }

  public final AttributeValueOperator operator() {
    return operator;
  }

  @Override
  public final String toString() {
    return new StringBuilder()
        .append('[')
        .append(previous.name())
        .append(operator.getSymbol())
        .append(value)
        .append(']')
        .toString();
  }

  public final String value() {
    return value;
  }

  private boolean matchesImpl(Selectable element) {
    String actualValue = element.attributeValue(previous.name());
    return operator.matches(value, actualValue);
  }

}