/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
 *
 * This file is part of the Objectos UI project.
 *
 * Objectos UI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Objectos UI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Objectos UI.  If not, see <https://www.gnu.org/licenses/>.
 */
package testing.zite;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.function.Consumer;
import objectos.lang.WayShutdownHook;
import objectos.notes.Level;
import objectos.notes.NoteSink;
import objectos.notes.impl.ConsoleNoteSink;
import objectos.way.Http.Exchange;
import objectos.way.WayTestingServerExchange;
import objectos.way.Web;

public final class TestingTestingSite {

  public static final Clock CLOCK;

  public static final NoteSink NOTE_SINK;

  public static final TestingSiteInjector INJECTOR;

  static {
    // Clock

    LocalDateTime dateTime;
    dateTime = LocalDateTime.of(2023, 6, 28, 12, 8, 43);

    ZoneId zone;
    zone = ZoneId.of("GMT");

    ZonedDateTime zoned;
    zoned = dateTime.atZone(zone);

    Instant fixedInstant;
    fixedInstant = zoned.toInstant();

    CLOCK = Clock.fixed(fixedInstant, zone);

    // NoteSink

    ConsoleNoteSink noteSink;
    noteSink = new ConsoleNoteSink(Level.TRACE);

    NOTE_SINK = noteSink;

    // ShutdownHook
    WayShutdownHook shutdownHook;
    shutdownHook = new WayShutdownHook();

    shutdownHook.noteSink(noteSink);

    // SessionStore
    Random random;
    random = new Random(1234L);

    Web.Store sessionStore;
    sessionStore = Web.createStore(
        Web.random(random)
    );

    // WebResources
    Web.Resources webResources;

    try {
      webResources = Web.createResources();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    shutdownHook.addAutoCloseable(webResources);

    INJECTOR = new TestingSiteInjector(noteSink, sessionStore, webResources);
  }

  private TestingTestingSite() {}

  public static String serverExchange(String request, Consumer<Exchange> handler) {
    WayTestingServerExchange serverExchange;
    serverExchange = new WayTestingServerExchange();

    serverExchange.clock(CLOCK);

    serverExchange.noteSink(NOTE_SINK);

    return serverExchange.handle(request, handler);
  }

}