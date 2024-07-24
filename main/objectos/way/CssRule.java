/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

sealed abstract class CssRule implements Comparable<CssRule> permits CssNoop, CssUtility {

  public static final CssRule NOOP = CssNoop.INSTANCE;

  CssRule() {}

  public abstract void accept(CssGeneratorRound gen);

  @Override
  public final int compareTo(CssRule o) {
    int thisKind;
    thisKind = kind();

    int thatKind;
    thatKind = o.kind();

    if (thisKind == thatKind) {
      return compareSameKind(o);
    } else if (thisKind < thatKind) {
      return -1;
    } else {
      return 1;
    }
  }

  abstract int compareSameKind(CssRule o);

  public abstract int kind();

  public abstract void writeTo(StringBuilder out, CssIndentation indentation);

}