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
      Note.Long1Ref2<String, IOException> socketError
  ) {

    static Notes get() {
      final Class<?> s;
      s = HttpServerTask.class;

      return new Notes(
          Note.Long1Ref2.create(s, "SOC", Note.ERROR)
      );
    }

  }

  private static final Notes NOTES = Notes.get();

  private final HttpRequestBodyOptions bodyOptions;

  private final byte[] buffer;

  private final HttpHandler handler;

  private final long id;

  private final Note.Sink noteSink;

  private final HttpSessionStoreImpl sessionStore;

  private final Socket socket;

  HttpServerTask(
      HttpRequestBodyOptions bodyOptions,
      byte[] buffer,
      HttpHandler handler,
      long id,
      Note.Sink noteSink,
      HttpSessionStoreImpl sessionStore,
      Socket socket) {
    this.bodyOptions = bodyOptions;

    this.buffer = buffer;

    this.handler = handler;

    this.id = id;

    this.noteSink = noteSink;

    this.sessionStore = sessionStore;

    this.socket = socket;
  }

  @Override
  public final void run() {
    final InputStream inputStream;

    try {
      inputStream = socket.getInputStream();
    } catch (IOException e) {
      noteSink.send(NOTES.socketError, id, "getInputStream", e);

      return;
    }

    final OutputStream outputStream;

    try {
      outputStream = socket.getOutputStream();
    } catch (IOException e) {
      noteSink.send(NOTES.socketError, id, "getOutputStream", e);

      return;
    }

    boolean shouldHandle;
    shouldHandle = true;

    try (socket) {
      while (!Thread.currentThread().isInterrupted() && shouldHandle) {
        shouldHandle = run0(inputStream, outputStream);
      }
    } catch (IOException e) {
      noteSink.send(NOTES.socketError, id, "close", e);

      return;
    }
  }

  private boolean run0(InputStream inputStream, OutputStream outputStream) {
    final HttpRequestParser requestParser;
    requestParser = new HttpRequestParser(bodyOptions, buffer, id, inputStream);

    final HttpRequest request;

    try {
      request = requestParser.parse();
    } catch (HttpClientException e) {
      throw new UnsupportedOperationException("Implement me");
    } catch (IOException e) {
      throw new UnsupportedOperationException("Implement me");
    }

    final HttpVersion version;
    version = request.version();

    final HttpResponse0 response;
    response = new HttpResponse0(buffer, outputStream, version);

    final HttpSession session;
    session = sessionStore.load(request);

    final HttpExchangeImpl2 exchange;
    exchange = new HttpExchangeImpl2(request, response, session);

    try {
      handler.handle(exchange);
    } catch (Throwable e) {
      throw new UnsupportedOperationException("Implement me");
    }

    throw new UnsupportedOperationException("Implement me");
  }

}
