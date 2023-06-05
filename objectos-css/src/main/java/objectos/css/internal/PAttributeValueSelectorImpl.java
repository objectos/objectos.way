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

import objectos.css.pseudom.PSelectorElement.PAttributeValueSelector;

public final class PAttributeValueSelectorImpl implements PAttributeValueSelector {

  private final CssPlayer player;

  int nameIndex;

  AttributeValueOperator operator;

  int valueIndex;

  PAttributeValueSelectorImpl(CssPlayer player) {
    this.player = player;
  }

  @Override
  public final String attributeName() {
    return (String) player.objectGet(nameIndex);
  }

  @Override
  public final AttributeValueOperator operator() {
    return operator;
  }

  @Override
  public final String value() {
    return (String) player.objectGet(valueIndex);
  }

}
