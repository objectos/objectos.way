/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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

import module java.base;

final class HttpRequestParser {

  private final HttpExchangeBodyFiles bodyFiles;

  private final int bodyMemoryMax;

  private final long bodySizeMax;

  private final long id;

  private final HttpRequestParser0Input input;

  HttpRequestParser(HttpExchangeBodyFiles bodyFiles, int bodyMemoryMax, long bodySizeMax, long id, HttpRequestParser0Input input) {
    this.bodyFiles = bodyFiles;

    this.bodyMemoryMax = bodyMemoryMax;

    this.bodySizeMax = bodySizeMax;

    this.id = id;

    this.input = input;
  }

  @SuppressWarnings("unused")
  public final HttpRequest parse() throws IOException {
    // method
    final HttpRequestParser2Method methodParser;
    methodParser = new HttpRequestParser2Method(input);

    final HttpMethod method;
    method = methodParser.parse();

    // path
    final HttpRequestParser3Path pathParser;
    pathParser = new HttpRequestParser3Path(input);

    final String path;
    path = pathParser.parse();

    // query
    final HttpRequestParser4Query queryParser;
    queryParser = new HttpRequestParser4Query(input);

    final Map<String, Object> queryParams;
    queryParams = queryParser.parse();

    // version
    final HttpRequestParser5Version versionParser;
    versionParser = new HttpRequestParser5Version(input);

    final HttpVersion version;
    version = versionParser.parse();

    // headers
    final HttpRequestParser6Headers headersParser;
    headersParser = new HttpRequestParser6Headers(input);

    final Map<HttpHeaderName, Object> headers;
    headers = headersParser.parse();

    // body
    final HttpRequestParser7Body bodyParser;
    bodyParser = new HttpRequestParser7Body(bodyFiles, bodyMemoryMax, bodySizeMax, headers, id, input);

    final HttpRequestBody body;
    body = bodyParser.parse();

    return new HttpRequestImpl(
        method,

        path,

        queryParams,

        version,

        headers,

        body
    );
  }

}
