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

import java.time.Clock;
import java.time.ZonedDateTime;
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

	protected final boolean matches(Segment pat1) {
		return http.matches(pat1);
	}

	protected final String segment(int index) {
		return http.segment(index);
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

	private void dateNow() {
		ZonedDateTime now;
		now = ZonedDateTime.now(clock);

		String formatted;
		formatted = Http.formatDate(now);

		http.header(Http.Header.DATE, formatted);
	}

}