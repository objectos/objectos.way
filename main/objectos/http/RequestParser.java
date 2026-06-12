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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import objectox.http.RequestMethodEnum;

final class RequestParser {

  private final byte[] buffer;

  private final InputStream inputStream;

  private final RequestBodySupport requestBodySupport;

  RequestParser(byte[] buffer, InputStream inputStream, RequestBodySupport requestBodySupport) {
    this.buffer = buffer;

    this.inputStream = inputStream;

    this.requestBodySupport = requestBodySupport;
  }

  public final RequestPojo parse() throws IOException {
    // input
    final RequestParser0Input input;
    input = new RequestParser0Input(buffer, inputStream);

    final int bytesRead;
    bytesRead = input.readToBuffer();

    if (bytesRead < 0) {
      return null;
    }

    // method
    final RequestParser2Method methodParser;
    methodParser = new RequestParser2Method(input);

    final RequestMethodEnum method;
    method = methodParser.parse();

    validate(method);

    // path
    final RequestParser3Path pathParser;
    pathParser = new RequestParser3Path(input);

    final String path;
    path = pathParser.parse();

    // query
    final RequestParser4Query queryParser;
    queryParser = new RequestParser4Query(input);

    final Map<String, Object> queryParams;
    queryParams = queryParser.parse();

    // version
    final RequestParser5Version versionParser;
    versionParser = new RequestParser5Version(input);

    final Version0 version;
    version = versionParser.parse();

    validate(version);

    // headers
    final RequestParser6Headers headersParser;
    headersParser = new RequestParser6Headers(input);

    final Map<HttpHeaderName, Object> headersMap;
    headersMap = headersParser.parse();

    final RequestHeaders headers;
    headers = new RequestHeaders(headersMap);

    validate(headers);

    // body meta
    final RequestParser7BodyMeta bodyMetaParser;
    bodyMetaParser = new RequestParser7BodyMeta(headers);

    final RequestBodyMeta bodyMeta;
    bodyMeta = bodyMetaParser.parse();

    // body data
    final RequestParser8BodyData bodyDataParser;
    bodyDataParser = new RequestParser8BodyData(
        requestBodySupport,

        input,

        bodyMeta.data()
    );

    final RequestBodyData bodyData;
    bodyData = bodyDataParser.parse();

    // body form
    final Map<String, Object> formParams;
    formParams = switch (bodyMeta.type()) {
      case RequestBodyMeta.TypeKind.APPLICATION_FORM_URLENCODED -> {
        try (InputStream in = bodyData.open()) {
          final RequestParser9BodyType0Form parser;
          parser = new RequestParser9BodyType0Form(in);

          yield parser.parse();
        }
      }

      default -> Map.of();
    };

    final RequestBodyForm bodyForm;
    bodyForm = new RequestBodyForm(formParams);

    // body final
    return new RequestPojo(
        new RequestAttributes(),

        method,

        path,

        queryParams,

        version,

        headers,

        bodyData,

        bodyForm
    );
  }

  private void validate(RequestMethodEnum method) throws HttpServerException {
    if (!method.implemented) {
      throw new HttpServerException(HttpServerException.Kind.METHOD_NOT_IMPLEMENTED);
    }
  }

  private void validate(Version0 version) throws HttpServerException {
    if (!version.supported()) {
      throw new HttpServerException(HttpServerException.Kind.HTTP_VERSION_NOT_SUPPORTED);
    }
  }

  private void validate(RequestHeaders headers) throws IOException {
    final List<String> hostHeader;
    hostHeader = headers.headerAll(HttpHeaderName.HOST);

    if (hostHeader.size() != 1) {
      final String msg;
      msg = "Host header field is required";

      throw new HttpClientException(msg, HttpClientException.Kind.HOST_HEADER);
    }

    final String hostValue;
    hostValue = hostHeader.get(0);

    if (hostValue.isEmpty()) {
      final String msg;
      msg = "Host header field is required";

      throw new HttpClientException(msg, HttpClientException.Kind.HOST_HEADER);
    }

    final String transferEncoding;
    transferEncoding = headers.header(HttpHeaderName.TRANSFER_ENCODING);

    if (transferEncoding != null) {
      throw new HttpServerException(HttpServerException.Kind.TRANSFER_ENCODING);
    }
  }

}
