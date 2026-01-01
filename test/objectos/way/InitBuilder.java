/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectos.way;

import java.util.ArrayList;
import java.util.List;

final class InitBuilder {

  static InitBuilder BUILDER = new InitBuilder();

  private final List<InitImpl> standardValues = new ArrayList<>();

  private int index;

  public final InitImpl create(String name) {
    InitImpl result;
    result = new InitImpl(index++, name);

    standardValues.add(result);

    return result;
  }

  public final InitImpl[] buildValues() {
    return standardValues.toArray(InitImpl[]::new);
  }

}