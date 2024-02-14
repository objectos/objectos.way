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

public sealed abstract class Status permits WayStatus {

  private static WayStatus.Builder BUILDER = new WayStatus.Builder();

  // 2.x.x
  public static final Status OK = BUILDER.create(200, "OK");

  // 3.x.x
  public static final Status MOVED_PERMANENTLY = BUILDER.create(301, "MOVED PERMANENTLY");
  public static final Status FOUND = BUILDER.create(302, "FOUND");
  public static final Status SEE_OTHER = BUILDER.create(303, "SEE OTHER");
  public static final Status NOT_MODIFIED = BUILDER.create(304, "NOT MODIFIED");

  // 4.x.x
  public static final Status BAD_REQUEST = BUILDER.create(400, "BAD REQUEST");
  public static final Status NOT_FOUND = BUILDER.create(404, "NOT FOUND");
  public static final Status METHOD_NOT_ALLOWED = BUILDER.create(405, "METHOD NOT ALLOWED");
  public static final Status URI_TOO_LONG = BUILDER.create(414, "URI TOO LONG");
  public static final Status UNSUPPORTED_MEDIA_TYPE = BUILDER.create(415, "UNSUPPORTED MEDIA TYPE");
  public static final Status UNPROCESSABLE_CONTENT = BUILDER.create(422, "UNPROCESSABLE CONTENT");

  // 5.x.x
  public static final Status INTERNAL_SERVER_ERROR = BUILDER.create(500, "INTERNAL SERVER ERROR");
  public static final Status NOT_IMPLEMENTED = BUILDER.create(501, "NOT IMPLEMENTED");
  public static final Status HTTP_VERSION_NOT_SUPPORTED = BUILDER.create(505, "HTTP_VERSION_NOT_SUPPORTED");

  static {
    WayStatus.set(BUILDER);

    BUILDER = null;
  }

  protected Status() {}

  public abstract int code();

  public abstract String reasonPhrase();

}