/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package objectos.code;

public enum PseudoElement {

  AFTER_CLASS_ANNOTATION,

  BEFORE_NEXT_TOP_LEVEL_ITEM,

  BEFORE_NEXT_STATEMENT,

  BEFORE_NEXT_COMMA_SEPARATED_ITEM,

  INDENTATION,

  CONTINUATION_INDENTATION;

  private static final PseudoElement[] VALUES = values();

  static PseudoElement get(int index) {
    return VALUES[index];
  }

}