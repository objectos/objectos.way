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

import br.com.objectos.http.Header.ContentLength;
import br.com.objectos.http.Header.SetCookie;

public class SimpleResponseVisitor implements ResponseVisitor {

  @Override
  public void visitResponseBody(Body.Ignored ignored) {
    defaultResponseBodyAction(ignored);
  }

  @Override
  public void visitResponseBody(Body.Text text) {
    defaultResponseBodyAction(text);
  }

  @Override
  public void visitResponseHeader(ContentLength contentLength) {
    defaultResponseHeaderAction(contentLength);
  }

  @Override
  public void visitResponseHeader(Header.ContentType contentType) {
    defaultResponseHeaderAction(contentType);
  }

  @Override
  public void visitResponseHeader(Header.Location location) {
    defaultResponseHeaderAction(location);
  }

  @Override
  public void visitResponseHeader(Header.Server server) {
    defaultResponseHeaderAction(server);
  }

  @Override
  public void visitResponseHeader(Header.Unknown unknown) {
    defaultResponseHeaderAction(unknown);
  }

  @Override
  public void visitResponseHeader(SetCookie setCookie) {
    defaultResponseHeaderAction(setCookie);
  }

  @Override
  public void visitResponseStatusLine(Version version, Status status, String reasonPhrase) {}

  protected void defaultResponseBodyAction(Body body) {}

  protected void defaultResponseHeaderAction(ResponseHeader headerField) {}

}
