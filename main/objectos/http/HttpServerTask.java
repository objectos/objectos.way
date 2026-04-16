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
import module objectos.way;

final class HttpServerTask implements Runnable {

  sealed interface Message
      permits
      HttpClientException,
      HttpServerException {

    HttpStatus status();

    String message();

  }

  static final Note.Long1Ref1<Throwable> THROW = Note.Long1Ref1.create(HttpServerTask.class, "THR", Note.ERROR);

  private final HttpRequestBodyOptions bodyOptions;

  private final byte[] buffer;

  private final Clock clock;

  private final HttpHandler handler;

  private final long id;

  private final Note.Sink noteSink;

  private final HttpSessionLoader sessionLoader;

  private final Socket socket;

  HttpServerTask(
      HttpRequestBodyOptions bodyOptions,
      byte[] buffer,
      Clock clock,
      HttpHandler handler,
      long id,
      Note.Sink noteSink,
      HttpSessionLoader sessionLoader,
      Socket socket) {
    this.bodyOptions = bodyOptions;

    this.buffer = buffer;

    this.clock = clock;

    this.handler = handler;

    this.id = id;

    this.noteSink = noteSink;

    this.sessionLoader = sessionLoader;

    this.socket = socket;
  }

  private boolean head = false;

  private boolean keepAlive = true;

  @Override
  public final void run() {
    try (socket) {
      final InputStream inputStream;
      inputStream = socket.getInputStream();

      final OutputStream outputStream;
      outputStream = socket.getOutputStream();

      try (HttpRequestBodySupport bodySupport = bodyOptions.supportOf(id)) {
        while (keepAlive) {
          run0(inputStream, outputStream, bodySupport);
        }
      } catch (HttpClientException e) {
        noteSink.send(THROW, id, e);

        handle(outputStream, e);
      } catch (HttpServerException e) {
        noteSink.send(THROW, id, e);

        handle(outputStream, e);
      }
    } catch (IOException e) {
      noteSink.send(THROW, id, e);
    }
  }

  private void run0(InputStream inputStream, OutputStream outputStream, HttpRequestBodySupport bodySupport) throws IOException {
    // input
    final HttpRequestParser0Input input;
    input = new HttpRequestParser0Input(buffer, inputStream);

    // method
    head = false;

    final HttpRequestParser2Method methodParser;
    methodParser = new HttpRequestParser2Method(input);

    final HttpMethod method;
    method = methodParser.parse();

    if (method == null) {
      keepAlive = false;

      return;
    }

    validate(method);

    head = method == HttpMethod.HEAD;

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

    validate(version);

    // headers
    final HttpRequestParser6Headers headersParser;
    headersParser = new HttpRequestParser6Headers(input);

    final Map<HttpHeaderName, Object> headersMap;
    headersMap = headersParser.parse();

    final HttpRequestHeaders0 headers;
    headers = new HttpRequestHeaders0(headersMap);

    validate(headers);

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

    final HttpRequest0 request;
    request = new HttpRequest0(
        method,

        path,

        queryParams,

        version,

        headers,

        body
    );

    // response
    final HttpResponse0 response;
    response = new HttpResponse0(buffer, clock, head, id, noteSink, outputStream);

    // session
    final HttpSession session;
    session = sessionLoader.loadSession(request, response);

    // exchange
    final HttpExchange0 exchange;
    exchange = new HttpExchange0(request, response, session);

    try {
      try {
        handler.handle(exchange);
      } catch (Http.AbstractHandlerException e) {
        e.handle(exchange);
      }
    } catch (Throwable e) {
      throw new HttpServerException(e, HttpServerException.Kind.INTERNAL_SERVER_ERROR);
    }

    final HttpRequestHeaders0 requestHeaders;
    requestHeaders = request.headers();

    keepAlive = requestHeaders.closeConnection()
        ? false
        : !response.closeConnection();
  }

  private void validate(HttpMethod method) throws HttpServerException {
    if (!method.implemented) {
      throw new HttpServerException(HttpServerException.Kind.METHOD_NOT_IMPLEMENTED);
    }
  }

  private void validate(HttpVersion0 version) throws HttpServerException {
    if (!version.supported()) {
      throw new HttpServerException(HttpServerException.Kind.HTTP_VERSION_NOT_SUPPORTED);
    }
  }

  private void validate(HttpRequestHeaders0 headers) throws IOException {
    final List<String> host;
    host = headers.headerAll(HttpHeaderName.HOST);

    if (host.size() != 1) {
      final String msg;
      msg = "Host header field is required";

      throw new HttpClientException(msg, HttpClientException.Kind.HOST_HEADER);
    }

    final String hostValue;
    hostValue = host.get(0);

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

  private void handle(OutputStream outputStream, Message exception) {
    final HttpResponse0 response;
    response = new HttpResponse0(buffer, clock, head, id, noteSink, outputStream);

    response.status(exception.status());

    response.header(HttpHeaderName.DATE, response.now());

    response.header(HttpHeaderName.CONNECTION, "close");

    final String msg;
    msg = exception.message();

    final Media.Bytes body;
    body = Media.Bytes.textPlain(msg);

    response.send(body);
  }

}
