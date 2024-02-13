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

final class ObjectoxStatus extends Status {

  static class Builder {

    private final List<ObjectoxStatus> standardValues = new ArrayList<>();

    private int index;

    public final ObjectoxStatus create(int code, String reasonPhrase) {
      ObjectoxStatus result;
      result = new ObjectoxStatus(index++, code, reasonPhrase);

      standardValues.add(result);

      return result;
    }

    public final ObjectoxStatus[] buildValues() {
      return standardValues.toArray(ObjectoxStatus[]::new);
    }

  }

  public final int index;

  public final int code;

  public final String reasonPhrase;

  public ObjectoxStatus(int index, int code, String reasonPhrase) {
    this.index = index;

    this.code = code;

    this.reasonPhrase = reasonPhrase;
  }

  private static ObjectoxStatus[] VALUES;

  public static void set(Builder builder) {
    VALUES = builder.buildValues();
  }

  public static ObjectoxStatus get(int index) {
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
    return obj == this || obj instanceof ObjectoxStatus that
        && code == that.code;
  }

}