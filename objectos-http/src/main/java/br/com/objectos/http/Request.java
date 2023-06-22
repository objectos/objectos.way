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
package br.com.objectos.http;

import objectos.lang.Check;
import objectos.util.UnmodifiableList;

public final class Request {

  private final Body body;

  private final UnmodifiableList<RequestHeader> headers;

  private final Method method;

  private final String target;

  private final Version version;

  Request(Body body,
          UnmodifiableList<RequestHeader> headers,
          Method method,
          String target,
          Version version) {
    this.body = body;
    this.headers = headers;
    this.method = method;
    this.target = target;
    this.version = version;
  }

  public final void acceptRequestVisitor(RequestVisitor visitor) {
    Check.notNull(visitor, "visitor == null");

    visitor.visitRequestLine(method, target, version);

    for (int i = 0; i < headers.size(); i++) {
      RequestHeader h;
      h = headers.get(i);

      h.acceptRequestVisitor(visitor);
    }

    body.acceptRequestVisitor(visitor);
  }

}
