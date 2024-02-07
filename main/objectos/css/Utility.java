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

class Utility implements Comparable<Utility> {

  public static final Utility UNKNOWN = new Utility(UtilityKind.UNKNOWN);

  final UtilityKind kind;

  Utility(UtilityKind kind) {
    this.kind = kind;
  }

  @Override
  public final int compareTo(Utility o) {
    return kind.compareTo(o.kind);
  }

  int compareSameKind(Utility o) {
    return 0;
  }

  @Override
  public String toString() {
    return kind.toString();
  }

}