/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.http;

import java.util.ArrayList;
import java.util.List;

final class WayMethod extends Method {

  static class Builder {

    private final List<WayMethod> standardValues = new ArrayList<>();

    private int index;

    public final WayMethod create(String text) {
      WayMethod result;
      result = new WayMethod(index++, text);

      standardValues.add(result);

      return result;
    }

    public final WayMethod[] buildValues() {
      return standardValues.toArray(WayMethod[]::new);
    }

  }

  private final int index;

  private final String text;

  WayMethod(int index, String text) {
    this.index = index;

    this.text = text;
  }

  private static WayMethod[] VALUES;

  public static void set(Builder builder) {
    VALUES = builder.buildValues();
  }

  public static WayMethod get(int index) {
    return VALUES[index];
  }

  public static int size() {
    return VALUES.length;
  }

  @Override
  public final int index() {
    return index;
  }

  @Override
  public final String text() {
    return text;
  }

  @Override
  public final String toString() {
    return text;
  }

  @Override
  public final int hashCode() {
    return text.hashCode();
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof WayMethod that
        && text.equals(that.text);
  }

}