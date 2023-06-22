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
package objectos.http;

import objectos.lang.Check;
import objectos.util.UnmodifiableList;

public final class Response {

  private final Body body;

  private final UnmodifiableList<ResponseHeader> headers;

  private final String reasonPhrase;

  private final Status status;

  private final Version version;

  Response(Body body,
           UnmodifiableList<ResponseHeader> headers,
           String reasonPhrase,
           Status status,
           Version version) {
    this.body = body;
    this.headers = headers;
    this.reasonPhrase = reasonPhrase;
    this.status = status;
    this.version = version;
  }

  public final void acceptResponseVisitor(ResponseVisitor visitor) {
    Check.notNull(visitor, "visitor == null");

    visitor.visitResponseStatusLine(version, status, reasonPhrase);

    for (int i = 0; i < headers.size(); i++) {
      ResponseHeader h;
      h = headers.get(i);

      h.acceptResponseVisitor(visitor);
    }

    body.acceptResponseVisitor(visitor);
  }

}
