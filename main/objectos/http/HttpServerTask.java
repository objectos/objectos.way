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

  private final RequestBodyOptions bodyOptions;

  private final byte[] buffer;

  private final Clock clock;

  private final HttpErrorResponses errorResponses;

  private final HttpHosts hosts;

  private final long id;

  private final Note.Sink noteSink;

  private final Socket socket;

  HttpServerTask(
      RequestBodyOptions bodyOptions,
      byte[] buffer,
      Clock clock,
      HttpErrorResponses errorResponses,
      HttpHosts hosts,
      long id,
      Note.Sink noteSink,
      Socket socket) {
    this.bodyOptions = bodyOptions;

    this.buffer = buffer;

    this.clock = clock;

    this.errorResponses = errorResponses;

    this.hosts = hosts;

    this.id = id;

    this.noteSink = noteSink;

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

      try (RequestBodySupport bodySupport = bodyOptions.supportOf(id)) {
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

  private void run0(InputStream inputStream, OutputStream outputStream, RequestBodySupport bodySupport) throws IOException {
    // input
    final RequestParser0Input input;
    input = new RequestParser0Input(buffer, inputStream);

    // method
    head = false;

    final RequestParser2Method methodParser;
    methodParser = new RequestParser2Method(input);

    final HttpMethod method;
    method = methodParser.parse();

    if (method == null) {
      keepAlive = false;

      return;
    }

    validate(method);

    head = method == HttpMethod.HEAD;

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

    final HttpVersion0 version;
    version = versionParser.parse();

    validate(version);

    // headers
    final RequestParser6Headers headersParser;
    headersParser = new RequestParser6Headers(input);

    final Map<HttpHeaderName, Object> headersMap;
    headersMap = headersParser.parse();

    final RequestHeaders headers;
    headers = new RequestHeaders(headersMap);

    final HttpHost5Pojo host;
    host = validate(headers);

    // body meta
    final RequestParser7BodyMeta bodyMetaParser;
    bodyMetaParser = new RequestParser7BodyMeta(headers);

    final RequestBodyMeta bodyMeta;
    bodyMeta = bodyMetaParser.parse();

    // body data
    final RequestParser8BodyData bodyDataParser;
    bodyDataParser = new RequestParser8BodyData(
        bodySupport,

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

    final Request0 request;
    request = new Request0(
        method,

        path,

        queryParams,

        version,

        headers,

        bodyData,

        bodyForm
    );

    // response
    final HttpResponse0 response;
    response = new HttpResponse0(buffer, clock, errorResponses, head, id, noteSink, outputStream, false);

    // session
    final HttpSession session;
    session = host.loadSession(request, response);

    // static files
    final HttpStaticFilesWriter staticFilesWriter;
    staticFilesWriter = host.staticFilesWriter();

    // exchange
    final HttpExchange0 exchange;
    exchange = new HttpExchange0(request, response, session, staticFilesWriter);

    try {
      try {
        host.handle(exchange);
      } catch (Http.AbstractHandlerException e) {
        e.handle(exchange);
      }
    } catch (Throwable e) {
      throw new HttpServerException(e, HttpServerException.Kind.INTERNAL_SERVER_ERROR);
    }

    final RequestHeaders requestHeaders;
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

  private HttpHost5Pojo validate(RequestHeaders headers) throws IOException {
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

    final HttpHost5Pojo host;
    host = hosts.get(hostValue);

    if (host == null) {
      final String msg;
      msg = "Invalid host: %s".formatted(hostValue);

      throw new HttpClientException(msg, HttpClientException.Kind.HOST_NOT_FOUND);
    }

    return host;
  }

  private void handle(OutputStream outputStream, Message exception) {
    final HttpResponse0 response;
    response = new HttpResponse0(buffer, clock, errorResponses, head, id, noteSink, outputStream, false);

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
