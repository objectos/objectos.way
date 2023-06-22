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

public interface ResponseVisitor {

  void visitResponseBody(Body.Ignored ignored);

  void visitResponseBody(Body.Text text);

  void visitResponseHeader(Header.ContentLength contentLength);

  void visitResponseHeader(Header.ContentType contentType);

  void visitResponseHeader(Header.Location location);

  void visitResponseHeader(Header.Server server);

  void visitResponseHeader(Header.SetCookie setCookie);

  void visitResponseHeader(Header.Unknown unknown);

  void visitResponseStatusLine(Version version, Status status, String reasonPhrase);

}
