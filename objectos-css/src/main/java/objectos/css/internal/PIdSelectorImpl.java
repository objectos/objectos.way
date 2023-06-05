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

import objectos.css.pseudom.PSelectorElement.PIdSelector;

public final class PIdSelectorImpl implements PIdSelector {

  private final CssPlayer player;

  int objectIndex;

  PIdSelectorImpl(CssPlayer player) {
    this.player = player;
  }

  @Override
  public final String id() {
    return (String) player.objectGet(objectIndex);
  }

}