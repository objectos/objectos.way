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

  private record Notes(
      Note.Long1Ref2<String, Exception> ioe
  ) {

    static Notes get() {
      final Class<?> s;
      s = HttpServerTask.class;

      return new Notes(
          Note.Long1Ref2.create(s, "IOE", Note.ERROR)
      );
    }

  }

  private static final Notes NOTES = Notes.get();

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

  @Override
  public final void run() {
    final InputStream inputStream;

    try {
      inputStream = socket.getInputStream();
    } catch (IOException e) {
      noteSink.send(NOTES.ioe, id, "socket.getInputStream", e);

      return;
    }

    final OutputStream outputStream;

    try {
      outputStream = socket.getOutputStream();
    } catch (IOException e) {
      noteSink.send(NOTES.ioe, id, "socket.getOutputStream", e);

      return;
    }

    boolean keepAlive;
    keepAlive = true;

    try (socket) {
      while (!Thread.currentThread().isInterrupted() && keepAlive) {
        keepAlive = run0(inputStream, outputStream);
      }
    } catch (IOException e) {
      noteSink.send(NOTES.ioe, id, "socket.close", e);

      return;
    }
  }

  private boolean run0(InputStream inputStream, OutputStream outputStream) {
    try (HttpRequestBodySupport bodySupport = bodyOptions.supportOf(id)) {
      return run1(inputStream, outputStream, bodySupport);
    } catch (IOException e) {
      noteSink.send(NOTES.ioe, id, "HttpRequestBodySupport.close", e);

      return false;
    }
  }

  private boolean run1(InputStream inputStream, OutputStream outputStream, HttpRequestBodySupport bodySupport) {
    final HttpResponse0 response;
    response = new HttpResponse0(buffer, clock, outputStream);

    // parse request
    final HttpRequestParser requestParser;
    requestParser = new HttpRequestParser(bodySupport, buffer, inputStream);

    final HttpRequest0 request;

    try {
      request = requestParser.parse();
    } catch (HttpClientException e) {
      e.respond(response);

      return false;
    } catch (IOException e) {
      noteSink.send(NOTES.ioe, id, "socket.read", e);

      return false;
    }

    try {
      request.validate();
    } catch (HttpRequest0InvalidException e) {
      e.respond(response);

      return keepAlive(request, response);
    }

    final HttpSession session;
    session = sessionLoader.loadSession(request);

    final HttpExchange0 exchange;
    exchange = new HttpExchange0(request, response, session);

    try {
      handler.handle(exchange);
    } catch (Throwable e) {
      throw new UnsupportedOperationException("Implement me", e);
    }

    return keepAlive(request, response);
  }

  private boolean keepAlive(HttpRequest0 request, HttpResponse0 response) {
    final HttpRequestHeaders0 requestHeaders;
    requestHeaders = request.headers();

    return requestHeaders.closeConnection()
        ? false
        : !response.closeConnection();
  }

}
