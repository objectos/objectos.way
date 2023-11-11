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
package objectos.http;

import java.io.IOException;
import java.net.Socket;
import java.time.Clock;
import objectos.lang.Note1;
import objectos.lang.NoteSink;

final class MarketingSiteTask extends AbstractHttpModule implements Runnable {

	static final Note1<IOException> IO_ERROR;

	static {
		Class<?> source;
		source = MarketingSiteTask.class;

		IO_ERROR = Note1.error(source, "I/O error");
	}

	private final NoteSink noteSink;

	private final Socket socket;

	public MarketingSiteTask(Clock clock, NoteSink noteSink, Socket socket) {
		super(clock);

		this.noteSink = noteSink;

		this.socket = socket;
	}

	@Override
	public final void run() {
		HttpExchange exchange;
		exchange = HttpExchange.of(socket, HttpExchange.Option.noteSink(noteSink));

		try (exchange) {
			while (exchange.active()) {
				//				MaybeRequest maybeReq;
				//				maybeReq = exchange.parseRequest();
				//
				//				try (maybeReq) {
				//					handle(maybeReq);
				//				}
				handle(exchange);
			}
		} catch (IOException e) {
			noteSink.send(IO_ERROR, e);
		}
	}

	private static final Segment FILENAME = Segment.ofAny();

	@Override
	protected final void definition() {
		if (matches(FILENAME)) {
			MarketingSiteRoot root;
			root = new MarketingSiteRoot(clock);

			root.handle(http);
		}

		else {
			notFound();
		}
	}

}

final class MarketingSiteRoot extends AbstractHttpModule {
	protected MarketingSiteRoot(Clock clock) {
		super(clock);
	}

	@Override
	protected final void definition() {
		String fileName;
		fileName = segment(0);

		switch (fileName) {
			case "" -> movedPermanently("/index.html");

			default -> notFound();
		}
	}
}