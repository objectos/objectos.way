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
package objectos.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

final class HttpRouting0Builder implements HttpRouting2 {

  private final List<HttpHandler> handlers = new ArrayList<>();

  @Override
  public final void handler(HttpHandler value) {
    Objects.requireNonNull(value, "value == null");

    handlers.add(value);
  }

  @Override
  public final void path(String path, Consumer<? super OfPath> opts) {

  }

}
