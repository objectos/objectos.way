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

import objectos.http.Header.ContentLength;
import objectos.http.Header.ContentType;
import objectos.http.Header.Cookie;

public class SimpleRequestVisitor implements RequestVisitor {

  @Override
  public void visitRequestBody(Body.Ignored ignored) {
    defaultRequestBodyAction(ignored);
  }

  @Override
  public void visitRequestBody(Body.Text text) {
    defaultRequestBodyAction(text);
  }

  @Override
  public void visitRequestHeader(ContentLength contentLength) {
    defaultRequestHeaderAction(contentLength);
  }

  @Override
  public void visitRequestHeader(ContentType contentType) {
    defaultRequestHeaderAction(contentType);
  }

  @Override
  public void visitRequestHeader(Cookie cookie) {
    defaultRequestHeaderAction(cookie);
  }

  @Override
  public void visitRequestHeader(Header.Accept accept) {
    defaultRequestHeaderAction(accept);
  }

  @Override
  public void visitRequestHeader(Header.Host host) {
    defaultRequestHeaderAction(host);
  }

  @Override
  public void visitRequestHeader(Header.Unknown unknown) {
    defaultRequestHeaderAction(unknown);
  }

  @Override
  public void visitRequestLine(Method method, String target, Version version) {}

  protected void defaultRequestBodyAction(Body body) {}

  protected void defaultRequestHeaderAction(RequestHeader header) {}

}