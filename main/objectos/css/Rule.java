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

import java.util.List;
import objectos.util.list.UnmodifiableList;

class Rule implements Comparable<Rule> {

  public static final Rule NOOP = new Rule(Utility.NOOP, UnmodifiableList.of());

  final Utility utility;

  final List<Variant> variants;

  Rule(Utility utility, List<Variant> variants) {
    this.utility = utility;

    this.variants = variants;
  }

  public void accept(WayStyleGenRound gen) {
    int size;
    size = variants.size();

    switch (size) {
      case 0 -> {
        StringBuilder out;
        out = gen.topLevel();

        writeTo(out);
      }

      case 1 -> {
        Variant variant;
        variant = variants.getFirst();

        VariantKind kind;
        kind = variant.kind();

        if (kind.isMediaQuery()) {
          StringBuilder out;
          out = gen.mediaQuery(variant);

          out.append("  ");

          writeTo(out);
        }

        else {
          throw new UnsupportedOperationException("Implement me");
        }
      }

      default -> throw new UnsupportedOperationException("Implement me");
    }
  }

  @Override
  public final int compareTo(Rule o) {
    return utility.compareTo(o.utility);
  }

  @Override
  public String toString() {
    return utility.toString();
  }

  void writeTo(StringBuilder out) {
    // noop
  }

}