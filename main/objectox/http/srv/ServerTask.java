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
package objectox.http.srv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.Clock;
import objectos.http.Content;
import objectos.http.HeaderName;
import objectos.http.Status;
import objectos.http.MediaType;
import objectos.http.RequestMethod;
import objectos.way.Note;
import objectox.http.HttpClientException;
import objectox.http.HttpServerException;
import objectox.http.RequestMethodEnum;
import objectox.http.host.Host;
import objectox.http.host.HostMap;
import objectox.http.req.RequestBodySupport;
import objectox.http.req.RequestBodySupportFactory;
import objectox.http.req.RequestParser;
import objectox.http.req.RequestPojo;
import objectox.http.resp.ResponseDate;
import objectox.http.resp.ResponsePojo;
import objectox.http.resp.ResponseSender;

final class ServerTask implements Runnable {

  static final Note.Long1Ref1<Throwable> THROW = Note.Long1Ref1.create(ServerTask.class, "THR", Note.ERROR);

  private final byte[] buffer;

  private final Clock clock;

  private final HostMap hostMap;

  private final Note.Sink noteSink;

  private final RequestBodySupportFactory requestBodySupportFactory;

  private final Socket socket;

  ServerTask(byte[] buffer, Clock clock, HostMap hostMap, Note.Sink noteSink, RequestBodySupportFactory requestBodySupportFactory, Socket socket) {
    this.buffer = buffer;

    this.clock = clock;

    this.hostMap = hostMap;

    this.noteSink = noteSink;

    this.requestBodySupportFactory = requestBodySupportFactory;

    this.socket = socket;
  }

  @Override
  public final void run() {
    final Thread currentThread;
    currentThread = Thread.currentThread();

    final long id;
    id = currentThread.threadId();

    try (socket) {
      final InputStream inputStream;
      inputStream = socket.getInputStream();

      final OutputStream outputStream;
      outputStream = socket.getOutputStream();

      final ResponseDate date;
      date = new ResponseDate(clock);

      final ResponseSender sender;
      sender = new ResponseSender(buffer, date, outputStream);

      try (RequestBodySupport requestBodySupport = requestBodySupportFactory.create(id)) {
        final RequestParser requestParser;
        requestParser = new RequestParser(buffer, inputStream, requestBodySupport);

        while (!currentThread.isInterrupted()) {
          final RequestPojo request;
          request = requestParser.parse();

          if (request == null) {
            break;
          }

          final String hostValue;
          hostValue = request.header(HeaderName.HOST);

          final Host host;
          host = hostMap.get(hostValue);

          final ResponsePojo response;

          if (host == null) {
            final String msg;
            msg = "Invalid host: %s".formatted(hostValue);

            response = error(Status.BAD_REQUEST, msg);
          }

          else {
            try {

              response = host.handle(request);

            } catch (Throwable e) {

              throw new UnsupportedOperationException("Implement me", e);

            }
          }

          final RequestMethod method;
          method = request.method();

          if (method == RequestMethodEnum.HEAD) {
            sender.head(response);
          } else {
            sender.send(response);
          }

          if (request.closeConnection() || response.closeConnection()) {
            break;
          }
        }
      } catch (HttpClientException e) {
        noteSink.send(THROW, id, e);

        final ResponsePojo response;
        response = error(e.status(), e.message());

        sender.send(response);
      } catch (HttpServerException e) {
        noteSink.send(THROW, id, e);

        final ResponsePojo response;
        response = error(e.status(), e.message());

        sender.send(response);
      }
    } catch (IOException e) {
      noteSink.send(THROW, id, e);
    }
  }

  private ResponsePojo error(Status status, String message) {
    return ResponsePojo.create0(opts -> {
      opts.status(status);

      opts.date();

      opts.header(HeaderName.CONNECTION, "close");

      opts.send(Content.of(MediaType.TEXT_PLAIN, message));
    });
  }

}
