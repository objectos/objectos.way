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

import java.util.function.Consumer;
import objectox.http.resp.ResponsePojo;

/// An HTTP response message.
public sealed interface Response
    extends
    Result,
    RoutingOption
    permits ResponsePojo {

  /// Creates a new response with the specified options.
  ///
  /// @param opts allows for setting the options
  ///
  /// @return a newly created response message
  static Response create(Consumer<? super ResponseOptions> opts) {
    return ResponsePojo.create0(opts);
  }

}
