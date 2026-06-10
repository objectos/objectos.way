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

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

record Host(
    Handler handler,

    String name,

    ResultProcessor resultProcessor,

    Consumer<Request> sessionRequest,

    BiConsumer<Request, ResponsePojo> sessionResponse,

    BiFunction<Request, Result, Result> staticFiles
) {

  public final ResponsePojo handle(Request request) {
    sessionRequest.accept(request);

    final Result initial;
    initial = handler.handle(request);

    final Result result;
    result = staticFiles.apply(request, initial);

    final Response res;
    res = resultProcessor.process(result);

    final ResponsePojo response;
    response = (ResponsePojo) res;

    sessionResponse.accept(request, response);

    return response;
  }

  public final boolean test(String hostValue) {
    return name.equals(hostValue);
  }

}
