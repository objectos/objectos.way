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

  private final HttpRequestBodyOptions bodyOptions;

  private final long id;

  private final HttpRequestParser0Input input;

  HttpRequestParser(HttpRequestBodyOptions bodyOptions, long id, HttpRequestParser0Input input) {
    this.bodyOptions = bodyOptions;

    this.id = id;

    this.input = input;
  }

  public static HttpRequestParser of(HttpRequestBodyOptions bodyOptions, long id, byte[] buffer, InputStream inputStream) {
    final HttpRequestParser0Input input;
    input = new HttpRequestParser0Input(buffer, inputStream);

    return new HttpRequestParser(bodyOptions, id, input);
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

    final Map<HttpHeaderName, Object> headersMap;
    headersMap = headersParser.parse();

    final HttpRequestHeadersImpl headers;
    headers = new HttpRequestHeadersImpl(headersMap);

    // body meta
    final HttpRequestParser7BodyMeta bodyMetaParser;
    bodyMetaParser = new HttpRequestParser7BodyMeta(headers);

    final HttpRequestBodyMeta bodyMeta;
    bodyMeta = bodyMetaParser.parse();

    // body data
    final HttpRequestParser8BodyData bodyDataParser;
    bodyDataParser = new HttpRequestParser8BodyData(
        bodyOptions,

        id,

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
    final HttpRequestBodyImpl body;
    body = new HttpRequestBodyImpl(bodyData, formParams);

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
