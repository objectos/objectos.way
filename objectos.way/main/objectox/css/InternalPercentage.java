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
package objectox.css;

import objectos.css.util.Percentage;

public final class InternalPercentage extends Percentage {

  final String raw;

  InternalPercentage(String raw) {
    this.raw = raw;
  }

  public static InternalPercentage of(double value) {
    return new InternalPercentage(
      Double.toString(value) + "%"
    );
  }

  public static InternalPercentage of(int value) {
    return new InternalPercentage(
      Integer.toString(value) + "%"
    );
  }

  @Override
  public final String toString() {
    return raw;
  }

}