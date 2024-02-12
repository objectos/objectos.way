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
package objectos.css;

record Breakpoint(int index, String value) implements Variant {

  @Override
  public final int compareTo(Variant o) {
    VariantKind thisKind;
    thisKind = kind();

    VariantKind thatKind;
    thatKind = o.kind();

    if (thisKind == thatKind) {
      Breakpoint that;
      that = (Breakpoint) o;

      return Integer.compare(index, that.index);
    } else {
      return thisKind.compareTo(thatKind);
    }
  }

  @Override
  public final VariantKind kind() {
    return VariantKind.BREAKPOINT;
  }

  @Override
  public final void writeMediaQueryStart(StringBuilder out) {
    out.append("@media (min-width: ");
    out.append(value);
    out.append(") {");
    out.append(System.lineSeparator());
  }

}