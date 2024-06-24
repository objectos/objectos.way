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
package objectos.way;

import java.util.ArrayList;
import java.util.List;

final class HttpResponseStatus implements Http.Response.Status {

  private static class Builder {

    static Builder INSTANCE = new Builder();

    private final List<HttpResponseStatus> standardValues = new ArrayList<>();

    private int index;

    public final HttpResponseStatus create(int code, String reasonPhrase) {
      HttpResponseStatus result;
      result = new HttpResponseStatus(index++, code, reasonPhrase);

      standardValues.add(result);

      return result;
    }

    public final HttpResponseStatus[] buildValues() {
      return standardValues.toArray(HttpResponseStatus[]::new);
    }

  }

  public final int index;

  public final int code;

  public final String reasonPhrase;

  public HttpResponseStatus(int index, int code, String reasonPhrase) {
    this.index = index;

    this.code = code;

    this.reasonPhrase = reasonPhrase;
  }

  static Http.Response.Status create(int code, String reasonPhrase) {
    return Builder.INSTANCE.create(code, reasonPhrase);
  }

  static Http.Response.Status createLast(int code, String reasonPhrase) {
    Builder builder;
    builder = Builder.INSTANCE;

    HttpResponseStatus result;
    result = builder.create(code, reasonPhrase);

    VALUES = builder.buildValues();

    Builder.INSTANCE = null;

    return result;
  }

  private static HttpResponseStatus[] VALUES;

  public static HttpResponseStatus get(int index) {
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
    return obj == this || obj instanceof HttpResponseStatus that
        && code == that.code;
  }

}