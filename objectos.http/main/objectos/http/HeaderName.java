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

import java.util.Objects;
import objectox.http.HeaderType;
import objectox.http.ObjectoxHeaderName;

/**
 * Represents an HTTP header name.
 */
public sealed abstract class HeaderName permits ObjectoxHeaderName {

  private static ObjectoxHeaderName.Builder BUILDER = new ObjectoxHeaderName.Builder();

  public static final HeaderName ACCEPT_ENCODING = BUILDER.create("Accept-Encoding", HeaderType.REQUEST);

  public static final HeaderName CONNECTION = BUILDER.create("Connection", HeaderType.BOTH);

  public static final HeaderName CONTENT_LENGTH = BUILDER.create("Content-Length", HeaderType.BOTH);

  public static final HeaderName CONTENT_TYPE = BUILDER.create("Content-Type", HeaderType.BOTH);

  public static final HeaderName COOKIE = BUILDER.create("Cookie", HeaderType.REQUEST);

  public static final HeaderName DATE = BUILDER.create("Date", HeaderType.BOTH);

  public static final HeaderName ETAG = BUILDER.create("ETag", HeaderType.RESPONSE);

  public static final HeaderName HOST = BUILDER.create("Host", HeaderType.REQUEST);

  public static final HeaderName IF_NONE_MATCH = BUILDER.create("If-None-Match", HeaderType.REQUEST);

  public static final HeaderName LOCATION = BUILDER.create("Location", HeaderType.RESPONSE);

  public static final HeaderName SET_COOKIE = BUILDER.create("Set-Cookie", HeaderType.RESPONSE);

  public static final HeaderName TRANSFER_ENCODING = BUILDER.create("Transfer-Encoding", HeaderType.BOTH);

  public static final HeaderName USER_AGENT = BUILDER.create("User-Agent", HeaderType.REQUEST);

  static {
    ObjectoxHeaderName.set(BUILDER);

    BUILDER = null;
  }

  public static HeaderName create(String name) {
    Objects.requireNonNull(name, "name == null");

    HeaderName headerName;
    headerName = ObjectoxHeaderName.findByName(name);

    if (headerName == null) {
      headerName = new ObjectoxHeaderName(name);
    }

    return headerName;
  }

  protected HeaderName() {}

  public abstract int index();

  public abstract String capitalized();

}