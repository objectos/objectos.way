/*
 * Copyright (C) 2023 Objectos Software LTDA.
 *
 * This file is part of the objectos.www project.
 *
 * objectos.www is NOT free software and is the intellectual property of Objectos Software LTDA.
 *
 * Source is available for educational purposes only.
 */
package objectos.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.ZonedDateTime;
import objectos.html.HtmlTemplate;
import objectos.http.Http.Method;
import objectos.http.Http.Status;

public abstract class AbstractHttpModule implements HttpModule {

	protected final Clock clock;

	protected HttpExchange http;

	protected AbstractHttpModule(Clock clock) {
		this.clock = clock;
	}

	@Override
	public final void handle(HttpExchange http) {
		this.http = http;

		try {
			definition();
		} finally {
			this.http = null;
		}
	}

	protected abstract void definition();

	protected final void ifGetOrHead() {
		if (!http.methodIn(Method.GET, Method.HEAD)) {
			methodNotAllowed();
		}
	}

	protected final boolean matches(Segment pat1) {
		return http.matches(pat1);
	}

	protected final String segment(int index) {
		return http.segment(index);
	}

	// 200

	protected final void send(String contentType, Factory<?> factory) {
		switch (http.method()) {
			case GET -> sendGet(contentType, factory);

			case HEAD -> sendHead(contentType, factory);

			default -> methodNotAllowed();
		}
	}

	private void sendGet(String contentType, Factory<?> factory) {
		try {
			sendGetUnchecked(contentType, factory);
		} catch (IOException e) {
			internalServerError(e);
		}
	}

	private void sendGetUnchecked(String contentType, Factory<?> factory) throws IOException {
		Object o;
		o = factory.create();

		byte[] data;

		if (o instanceof byte[] bytes) {
			data = bytes;
		} else {
			String s;
			s = o.toString();

			data = s.getBytes(StandardCharsets.UTF_8);
		}

		http.status(Status.OK_200);

		http.header(Http.Header.CONTENT_LENGTH, Integer.toString(data.length));

		http.header(Http.Header.CONTENT_TYPE, contentType);

		dateNow();

		http.body(data);
	}

	private void sendHead(String contentType, Factory<?> factory) {
		try {
			sendGetUnchecked(contentType, factory);

			http.bodyClear();
		} catch (IOException e) {
			internalServerError(e);
		}
	}

	// 200 text/html

	protected final void textHtml(Factory<HtmlTemplate> factory) {
		send("text/html; charset=utf-8", factory);
	}

	// 301

	protected final void movedPermanently(String location) {
		http.status(Status.MOVED_PERMANENTLY_301);

		http.header(Http.Header.LOCATION, location);

		dateNow();
	}

	// 404

	protected final void notFound() {
		http.status(Status.NOT_FOUND_404);

		http.header(Http.Header.CONNECTION, "close");

		dateNow();
	}

	// 405

	protected final void methodNotAllowed() {
		http.status(Status.METHOD_NOT_ALLOWED_405);

		http.header(Http.Header.CONNECTION, "close");

		dateNow();
	}

	// 500

	protected final void internalServerError(Throwable t) {
		StringWriter sw;
		sw = new StringWriter();

		PrintWriter pw;
		pw = new PrintWriter(sw);

		t.printStackTrace(pw);

		String msg;
		msg = sw.toString();

		byte[] bytes;
		bytes = msg.getBytes();

		http.status(Status.INTERNAL_SERVER_ERROR_500);

		http.header(Http.Header.CONTENT_LENGTH, Long.toString(bytes.length));

		http.header(Http.Header.CONTENT_TYPE, "text/plain");

		dateNow();

		http.body(bytes);
	}

	private void dateNow() {
		ZonedDateTime now;
		now = ZonedDateTime.now(clock);

		String formatted;
		formatted = Http.formatDate(now);

		http.header(Http.Header.DATE, formatted);
	}

}