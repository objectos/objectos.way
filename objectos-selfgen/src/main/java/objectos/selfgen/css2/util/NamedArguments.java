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
package objectos.selfgen.css2.util;

import objectos.util.GrowableList;
import objectos.util.UnmodifiableList;

public final class NamedArguments implements NamesValue {

  public final String constantName;

  public final UnmodifiableList<Value> values;

  NamedArguments(String constantName, UnmodifiableList<Value> values) {
    this.constantName = constantName;
    this.values = values;
  }

  @Override
  public void acceptList(GrowableList<NamedArguments> list) {
    list.add(this);
  }

}