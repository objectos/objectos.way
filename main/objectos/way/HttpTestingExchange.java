/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Map;
import objectos.lang.object.Check;

final class HttpTestingExchange implements Http.TestingExchange {

  Map<Object, Object> attributes;

  Http.Request.Target requestTarget;

  @Override
  public final Http.Request.Body body() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final Http.Request.Headers headers() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final byte method() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final Http.Request.Target target() {
    Check.state(requestTarget != null, "The request target was not set");

    return requestTarget;
  }

  @Override
  public final <T> void set(Class<T> key, T value) {
    Check.notNull(key, "key == null");
    Check.notNull(value, "value == null");

    if (attributes == null) {
      attributes = Util.createMap();
    }

    attributes.put(key, value);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T get(Class<T> key) {
    if (attributes == null) {
      return null;
    } else {
      return (T) attributes.get(key);
    }
  }

  @Override
  public void header(Http.HeaderName name, long value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void header(Http.HeaderName name, String value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void dateNow() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void send() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void send(byte[] body) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void send(Lang.CharWritable body, Charset charset) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void send(Path file) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void notFound() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void methodNotAllowed() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void internalServerError(Throwable t) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean processed() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void status(Http.Response.Status value) {
    throw new UnsupportedOperationException();
  }

}