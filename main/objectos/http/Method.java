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

public sealed abstract class Method permits WayMethod {

  private static WayMethod.Builder BUILDER = new WayMethod.Builder();

  /**
   * The CONNECT method.
   */
  public static final Method CONNECT = BUILDER.create("CONNECT");

  /**
   * The DELETE method.
   */
  public static final Method DELETE = BUILDER.create("DELETE");

  /**
   * The GET method.
   */
  public static final Method GET = BUILDER.create("GET");

  /**
   * The HEAD method.
   */
  public static final Method HEAD = BUILDER.create("HEAD");

  /**
   * The OPTIONS method.
   */
  public static final Method OPTIONS = BUILDER.create("OPTIONS");

  /**
   * The PATCH method.
   */
  public static final Method PATCH = BUILDER.create("PATCH");

  /**
   * The POST method.
   */
  public static final Method POST = BUILDER.create("POST");

  /**
   * The PUT method.
   */
  public static final Method PUT = BUILDER.create("PUT");

  /**
   * The TRACE method.
   */
  public static final Method TRACE = BUILDER.create("TRACE");

  static {
    WayMethod.set(BUILDER);

    BUILDER = null;
  }

  protected Method() {}

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

  public abstract int index();

  public abstract String text();

}