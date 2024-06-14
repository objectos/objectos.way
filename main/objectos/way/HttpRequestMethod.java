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
import objectos.way.Http.Request.Method;

final class HttpRequestMethod implements Http.Request.Method {

  private static class Builder {

    static Builder INSTANCE = new Builder();
    
    private final List<HttpRequestMethod> standardValues = new ArrayList<>();

    private int index;

    public final HttpRequestMethod create(String text) {
      HttpRequestMethod result;
      result = new HttpRequestMethod(index++, text);

      standardValues.add(result);

      return result;
    }

    public final HttpRequestMethod[] buildValues() {
      return standardValues.toArray(HttpRequestMethod[]::new);
    }

  }

  private final int index;

  private final String text;

  HttpRequestMethod(int index, String text) {
    this.index = index;

    this.text = text;
  }
  
  static HttpRequestMethod create(String text) {
    return Builder.INSTANCE.create(text);
  }

  static HttpRequestMethod createLast(String text) {
    HttpRequestMethod method;
    method = Builder.INSTANCE.create(text);
    
    VALUES = Builder.INSTANCE.buildValues();
    
    Builder.INSTANCE = null;
    
    return method;
  }

  private static HttpRequestMethod[] VALUES;

  @SuppressWarnings("exports")
  public static void set(Builder builder) {
    VALUES = builder.buildValues();
  }

  public static HttpRequestMethod get(int index) {
    return VALUES[index];
  }

  public static int size() {
    return VALUES.length;
  }

  public final boolean is(Method method) {
    int index;
    index = index();

    if (index >= 0) {
      return index == method.index();
    } else {
      return equals(method);
    }
  }

  public final boolean is(Method method1, Method method2) {
    return is(method1) || is(method2);
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
    return obj == this || obj instanceof HttpRequestMethod that
        && text.equals(that.text);
  }

}