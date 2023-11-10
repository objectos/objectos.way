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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import objectos.lang.Note1;
import objectos.lang.NoteSink;

final class MarketingSiteTask implements Runnable {

	static final Note1<IOException> IO_ERROR;

	static {
		Class<?> source;
		source = MarketingSiteTask.class;

		IO_ERROR = Note1.error(source, "I/O error");
	}

	private final NoteSink noteSink;

	private final RequestParser requestParser;

	private final Socket socket;

	private boolean stop;

	public MarketingSiteTask(NoteSink noteSink, RequestParser requestParser, Socket socket) {
		this.noteSink = noteSink;

		this.requestParser = requestParser;

		this.socket = socket;
	}

	@Override
	public final void run() {
		try (socket) {
			while (shouldExecute()) {
				InputStream inputStream;
				inputStream = socket.getInputStream();

				RequestParser.Result result;
				result = requestParser.parse(inputStream);

				Response resp;

				try {
					resp = execute(result);
				} catch (Throwable t) {
					t.printStackTrace();

					return;
				}

				OutputStream outputStream;
				outputStream = socket.getOutputStream();

				resp.writeTo(outputStream);
			}
		} catch (IOException e) {
			noteSink.send(IO_ERROR, e);
		}
	}

	private boolean shouldExecute() {
		return !stop && !Thread.currentThread().isInterrupted();
	}

	private Response execute(RequestParser.Result result) {
		return switch (result) {
			case Request req -> handle(req);
		};
	}

	private Response handle(Request req) {
		HttpAction action;
		action = new MarketingSiteRootRedirect();

		if (action.shouldHandle(req)) {
			return action.handle(req);
		}

		throw new UnsupportedOperationException("Implement me");
	}

}

final class MarketingSiteRootRedirect implements HttpAction {

	@Override
	public final boolean shouldHandle(Request req) {
		return req.pathEquals("/");
	}

	@Override
	public final Response handle(Request req) {
		return switch (req) {
			case GetRequest get -> movedPermanently("/index.html");

			case HeadRequest head -> movedPermanently("/index.html");

			default -> methodNotAllowed();
		};
	}

	private Response methodNotAllowed() {
		throw new UnsupportedOperationException("Implement me");
	}

	private Response movedPermanently(String location) {
		throw new UnsupportedOperationException("Implement me");
	}

}