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

  private final HttpRequestBodySupport bodySupport;

  private final byte[] buffer;

  private final InputStream inputStream;

  HttpRequestParser(HttpRequestBodySupport bodySupport, byte[] buffer, InputStream inputStream) {
    this.bodySupport = bodySupport;

    this.buffer = buffer;

    this.inputStream = inputStream;
  }

  public final HttpRequest0 parse() throws IOException {
    // input
    final HttpRequestParser0Input input;
    input = new HttpRequestParser0Input(buffer, inputStream);

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

    final HttpVersion0 version;
    version = versionParser.parse();

    // headers
    final HttpRequestParser6Headers headersParser;
    headersParser = new HttpRequestParser6Headers(input);

    final Map<HttpHeaderName, Object> headersMap;
    headersMap = headersParser.parse();

    final HttpRequestHeaders0 headers;
    headers = new HttpRequestHeaders0(headersMap);

    // body meta
    final HttpRequestParser7BodyMeta bodyMetaParser;
    bodyMetaParser = new HttpRequestParser7BodyMeta(headers);

    final HttpRequestBodyMeta bodyMeta;
    bodyMeta = bodyMetaParser.parse();

    // body data
    final HttpRequestParser8BodyData bodyDataParser;
    bodyDataParser = new HttpRequestParser8BodyData(
        bodySupport,

        input,

        bodyMeta.data()
    );

    final HttpRequestBodyData bodyData;
    bodyData = bodyDataParser.parse();

    // body form
    final Map<String, Object> formParams;
    formParams = switch (bodyMeta.type()) {
      case HttpRequestBodyMeta.TypeKind.APPLICATION_FORM_URLENCODED -> {
        try (InputStream in = bodyData.open()) {
          final HttpRequestParser9BodyType0Form parser;
          parser = new HttpRequestParser9BodyType0Form(in);

          yield parser.parse();
        }
      }

      default -> Map.of();
    };

    // body final
    final HttpRequestBody0 body;
    body = new HttpRequestBody0(bodyData, formParams);

    return new HttpRequest0(
        method,

        path,

        queryParams,

        version,

        headers,

        body
    );
  }

}
