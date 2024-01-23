/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.http.server;

import java.io.IOException;
import java.net.Socket;
import java.time.Clock;
import java.util.List;
import objectos.html.HtmlTemplate;
import objectos.http.server.UriPath.Segment;
import objectos.notes.Note1;
import objectos.notes.NoteSink;

final class MarketingSite extends AbstractHttpModule implements Runnable {

  static final Note1<IOException> IO_ERROR;

  static {
    Class<?> source;
    source = MarketingSite.class;

    IO_ERROR = Note1.error(source, "I/O error");
  }

  private final NoteSink noteSink;

  private final Socket socket;

  public MarketingSite(Clock clock, NoteSink noteSink, Socket socket) {
    super(clock);

    this.noteSink = noteSink;

    this.socket = socket;
  }

  @Override
  public final void run() {
    ServerLoop loop;
    loop = ServerLoop.create(socket);

    loop.noteSink(noteSink);

    try (loop) {
      while (!Thread.currentThread().isInterrupted()) {
        loop.parse();

        if (loop.badRequest()) {
          throw new UnsupportedOperationException("Implement me");
        }

        handle(loop);

        loop.commit();

        if (!loop.keepAlive()) {
          break;
        }
      }
    } catch (IOException e) {
      noteSink.send(IO_ERROR, e);
    }
  }

  @Override
  protected final void definition() {
    List<Segment> segments;
    segments = segments();

    if (segments.size() == 1) {
      MarketingSiteRoot root;
      root = new MarketingSiteRoot(clock);

      root.handle(http);
    } else {
      notFound();
    }
  }

}

final class MarketingSiteRoot extends AbstractHttpModule {
  MarketingSiteRoot(Clock clock) {
    super(clock);
  }

  @Override
  protected final void definition() {
    Segment first;
    first = segment(0);

    String fileName;
    fileName = first.value();

    switch (fileName) {
      case "" -> movedPermanently("/index.html");

      case "index.html" -> okTextHtml(MarketingSiteHome::new);

      default -> notFound();
    }
  }
}

final class MarketingSiteHome extends HtmlTemplate {
  @Override
  protected void definition() {
    doctype();
    h1("home");
  }
}