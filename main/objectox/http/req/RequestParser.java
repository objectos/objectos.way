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
package objectox.http.req;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import objectos.http.HeaderName;
import objectox.http.HttpClientException;
import objectox.http.HttpServerException;
import objectox.http.RequestMethodEnum;
import objectox.http.Version0;

public final class RequestParser {

  private final RequestBodySupport requestBodySupport;

  private final RequestInputStream requestInputStream;

  public RequestParser(RequestBodySupport requestBodySupport, RequestInputStream requestInputStream) {
    this.requestBodySupport = requestBodySupport;

    this.requestInputStream = requestInputStream;
  }

  public final boolean hasNext() throws IOException {
    return requestInputStream.start();
  }

  public final RequestPojo parse() throws IOException {
    // method
    final RequestMethodParser methodParser;
    methodParser = new RequestMethodParser(requestInputStream);

    final RequestMethodEnum method;
    method = methodParser.parse();

    validate(method);

    // path
    final RequestPathParser pathParser;
    pathParser = new RequestPathParser(requestInputStream);

    final String path;
    path = pathParser.parse();

    // query
    final RequestQueryParser queryParser;
    queryParser = new RequestQueryParser(requestInputStream);

    final Map<String, Object> queryParams;
    queryParams = queryParser.parse();

    // version
    final RequestVersionParser versionParser;
    versionParser = new RequestVersionParser(requestInputStream);

    final Version0 version;
    version = versionParser.parse();

    validate(version);

    // headers
    final RequestHeadersParser headersParser;
    headersParser = new RequestHeadersParser(requestInputStream);

    final Map<HeaderName, Object> headersMap;
    headersMap = headersParser.parse();

    final RequestHeaders headers;
    headers = new RequestHeaders(headersMap);

    validate(headers);

    // body meta
    final RequestBodyMetaParser bodyMetaParser;
    bodyMetaParser = new RequestBodyMetaParser(headers);

    final RequestBodyMeta bodyMeta;
    bodyMeta = bodyMetaParser.parse();

    // body data
    final RequestBodyDataParser bodyDataParser;
    bodyDataParser = new RequestBodyDataParser(
        requestBodySupport,

        requestInputStream,

        bodyMeta.data()
    );

    final RequestBodyData bodyData;
    bodyData = bodyDataParser.parse();

    // body form
    final Map<String, Object> formParams;
    formParams = switch (bodyMeta.type()) {
      case RequestBodyMeta.TypeKind.APPLICATION_FORM_URLENCODED -> {
        try (InputStream in = bodyData.open()) {
          final RequestBodyFormParser parser;
          parser = new RequestBodyFormParser(in);

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
    hostHeader = headers.headerAll(HeaderName.HOST);

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
    transferEncoding = headers.header(HeaderName.TRANSFER_ENCODING);

    if (transferEncoding != null) {
      throw new HttpServerException(HttpServerException.Kind.TRANSFER_ENCODING);
    }
  }

}
