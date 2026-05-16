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
    final RequestParser requestParser;
    requestParser = new RequestParser(buffer, inputStream, bodySupport);

    final Request0 request;
    request = requestParser.parse();

    if (request == null) {
      keepAlive = false;

      return;
    }

    final HttpMethod method;
    method = request.method();

    head = method == HttpMethod.HEAD;

    final String hostValue;
    hostValue = request.header(HttpHeaderName.HOST);

    final HttpHost5Pojo host;
    host = hosts.get(hostValue);

    if (host == null) {
      final String msg;
      msg = "Invalid host: %s".formatted(hostValue);

      throw new HttpClientException(msg, HttpClientException.Kind.HOST_NOT_FOUND);
    }

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
