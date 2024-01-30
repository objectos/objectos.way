/*
 * Copyright (C) 2023 Objectos Software LTDA.
 *
 * This file is part of the objectos.www project.
 *
 * objectos.www is NOT free software and is the intellectual property of Objectos Software LTDA.
 *
 * Source is available for educational purposes only.
 */
package objectos.http.server;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Supplier;
import objectos.html.HtmlTemplate;
import objectos.http.HeaderName;
import objectos.http.Http;
import objectos.http.Method;
import objectos.http.Status;
import objectos.http.server.UriPath.Segment;

public abstract class AbstractHttpModule implements Handler {

  protected final Clock clock;

  protected ServerExchange http;

  protected AbstractHttpModule(Clock clock) {
    this.clock = clock;
  }

  @Override
  public final void handle(ServerExchange http) {
    this.http = http;

    try {
      definition();
    } finally {
      this.http = null;
    }
  }

  protected abstract void definition();

  protected final Segment segment(int index) {
    List<Segment> segments;
    segments = segments();

    return segments.get(index);
  }

  protected final List<Segment> segments() {
    UriPath path;
    path = http.path();

    return path.segments();
  }

  // 200 OK

  protected final void okTextHtml(Supplier<HtmlTemplate> factory) {
    Method method;
    method = http.method();

    if (method.is(Method.GET)) {
      HtmlTemplate tmpl;
      tmpl = factory.get();

      String s;
      s = tmpl.toString();

      byte[] bytes;
      bytes = s.getBytes(StandardCharsets.UTF_8);

      http.status(Status.OK);

      http.header(HeaderName.CONTENT_LENGTH, bytes.length);

      http.header(HeaderName.CONTENT_TYPE, "text/html; charset=utf-8");

      dateNow();

      http.send(bytes);
    }

    else if (method.is(Method.HEAD)) {
      http.status(Status.OK);

      http.header(HeaderName.CONTENT_TYPE, "text/html; charset=utf-8");

      dateNow();

      http.send();
    }

    else {
      methodNotAllowed();
    }
  }

  // 301 MOVED PERMANENTLY

  protected final void movedPermanently(String location) {
    Method method;
    method = http.method();

    if (method.is(Method.GET, Method.HEAD)) {
      http.status(Status.MOVED_PERMANENTLY);

      http.header(HeaderName.LOCATION, location);

      dateNow();

      http.send();
    }

    else {
      methodNotAllowed();
    }
  }

  // 404 NOT FOUND

  protected final void notFound() {
    http.status(Status.NOT_FOUND);

    http.header(HeaderName.CONNECTION, "close");

    dateNow();

    http.send();
  }

  // 405 METHOD NOT ALLOWED

  protected final void methodNotAllowed() {
    http.status(Status.METHOD_NOT_ALLOWED);

    http.header(HeaderName.CONNECTION, "close");

    dateNow();

    http.send();
  }

  private void dateNow() {
    ZonedDateTime now;
    now = ZonedDateTime.now(clock);

    String date;
    date = Http.formatDate(now);

    http.header(HeaderName.DATE, date);
  }

}