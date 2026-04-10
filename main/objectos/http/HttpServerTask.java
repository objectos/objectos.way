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

  private HttpRequest0 parseRequest(InputStream inputStream, OutputStream outputStream, HttpRequestBodySupport bodySupport) {
    // input
    final HttpRequestParser0Input input;
    input = new HttpRequestParser0Input(buffer, inputStream);

    // method
    final HttpRequestParser2Method methodParser;
    methodParser = new HttpRequestParser2Method(input);

    final HttpMethod method;
    method = methodParser.parse();

    String path = "";

    Map<String, Object> queryParams;

    HttpVersion0 version;

    try {
      // path
      final HttpRequestParser3Path pathParser;
      pathParser = new HttpRequestParser3Path(input);

      path = pathParser.parse();

      // query
      final HttpRequestParser4Query queryParser;
      queryParser = new HttpRequestParser4Query(input);

      queryParams = queryParser.parse();

      // version
      final HttpRequestParser5Version versionParser;
      versionParser = new HttpRequestParser5Version(input);

      version = versionParser.parse();
    } catch (HttpRequestParser3Path.Http09Exception e) {
      path = e.path;

      queryParams = Map.of();

      version = HttpVersion0.HTTP_0_9;
    } catch (HttpRequestParser4Query.Http09Exception e) {
      queryParams = e.params;

      version = HttpVersion0.HTTP_0_9;
    }

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

  private boolean keepAlive(HttpRequest0 request, HttpResponse0 response) {
    final HttpRequestHeaders0 requestHeaders;
    requestHeaders = request.headers();

    return requestHeaders.closeConnection()
        ? false
        : !response.closeConnection();
  }

}
