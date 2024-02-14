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

final class WayStatus extends Status {

  static class Builder {

    private final List<WayStatus> standardValues = new ArrayList<>();

    private int index;

    public final WayStatus create(int code, String reasonPhrase) {
      WayStatus result;
      result = new WayStatus(index++, code, reasonPhrase);

      standardValues.add(result);

      return result;
    }

    public final WayStatus[] buildValues() {
      return standardValues.toArray(WayStatus[]::new);
    }

  }

  public final int index;

  public final int code;

  public final String reasonPhrase;

  public WayStatus(int index, int code, String reasonPhrase) {
    this.index = index;

    this.code = code;

    this.reasonPhrase = reasonPhrase;
  }

  private static WayStatus[] VALUES;

  public static void set(Builder builder) {
    VALUES = builder.buildValues();
  }

  public static WayStatus get(int index) {
    return VALUES[index];
  }

  public static int size() {
    return VALUES.length;
  }

  public final int index() {
    return index;
  }

  @Override
  public final int code() {
    return code;
  }

  @Override
  public final String reasonPhrase() {
    return reasonPhrase;
  }

  @Override
  public final int hashCode() {
    return code;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof WayStatus that
        && code == that.code;
  }

}