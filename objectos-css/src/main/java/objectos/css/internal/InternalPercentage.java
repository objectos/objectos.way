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

import objectos.css.tmpl.Percentage;

public record InternalPercentage(String value) implements Percentage {

  private static final InternalPercentage ZERO = new InternalPercentage("0");

  public static InternalPercentage of(double value) {
    if (value == 0) {
      return ZERO;
    }

    var s = Double.toString(value);

    return new InternalPercentage(s + "%");
  }

  public static InternalPercentage of(int value) {
    if (value == 0) {
      return ZERO;
    }

    var s = Integer.toString(value);

    return new InternalPercentage(s + "%");
  }

  @Override
  public final String toString() {
    return value;
  }

}