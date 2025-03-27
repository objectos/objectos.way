/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import java.nio.file.Path;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;
import objectos.way.Http.ResponseMessage;

sealed abstract class HttpSupport implements Http.Exchange, Http.ResponseHeaders permits HttpExchange, HttpTestingExchange {

  Clock clock;

  int matcherIndex;

  Map<String, String> pathParams;

  @Override
  public abstract String path();

  @Override
  public final String pathParam(String name) {
    Check.notNull(name, "name == null");

    String result;
    result = null;

    if (pathParams != null) {
      result = pathParams.get(name);
    }

    return result;
  }

  final void matcherReset() {
    matcherIndex = 0;

    if (pathParams != null) {
      pathParams.clear();
    }
  }

  final boolean atEnd() {
    String value;
    value = path();

    return matcherIndex == value.length();
  }

  final boolean exact(String other) {
    String value;
    value = path();

    boolean result;
    result = value.equals(other);

    matcherIndex += value.length();

    return result;
  }

  final boolean namedVariable(String name) {
    String value;
    value = path();

    int solidus;
    solidus = value.indexOf('/', matcherIndex);

    String varValue;

    if (solidus < 0) {
      varValue = value.substring(matcherIndex);
    } else {
      varValue = value.substring(matcherIndex, solidus);
    }

    matcherIndex += varValue.length();

    variable(name, varValue);

    return true;
  }

  final boolean region(String region) {
    String value;
    value = path();

    boolean result;
    result = value.regionMatches(matcherIndex, region, 0, region.length());

    matcherIndex += region.length();

    return result;
  }

  final boolean startsWithMatcher(String prefix) {
    String value;
    value = path();

    boolean result;
    result = value.startsWith(prefix);

    matcherIndex += prefix.length();

    return result;
  }

  private void variable(String name, String value) {
    if (pathParams == null) {
      pathParams = Util.createMap();
    }

    pathParams.put(name, value);
  }

  // response methods

  @Override
  public final void respond(ResponseMessage message) {
    HttpResponseMessage impl;
    impl = (HttpResponseMessage) message;

    impl.accept(this);
  }

  @Override
  public final void respond(Lang.MediaObject object) {
    respond(Http.Status.OK, object);
  }

  @Override
  public final void respond(Lang.MediaWriter writer) {
    respond(Http.Status.OK, writer);
  }

  @Override
  public final void header(Http.HeaderName name, long value) {
    Objects.requireNonNull(name, "name == null");

    header0(name, value);
  }

  @Override
  public final void header(Http.HeaderName name, String value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    header0(name, value);
  }

  @Override
  public final void dateNow() {
    Clock theClock;
    theClock = clock;

    if (theClock == null) {
      theClock = Clock.systemUTC();
    }

    ZonedDateTime now;
    now = ZonedDateTime.now(theClock);

    String value;
    value = Http.formatDate(now);

    header0(Http.HeaderName.DATE, value);
  }

  abstract void endResponse();

  abstract void status0(Http.Status status);

  final void header0(Http.HeaderName name, long value) {
    final String s;
    s = Long.toString(value);

    header0(name, s);
  }

  abstract void header0(Http.HeaderName name, String value);

  abstract void send0();

  abstract void send0(byte[] bytes);

  abstract void send0(Path file);

}