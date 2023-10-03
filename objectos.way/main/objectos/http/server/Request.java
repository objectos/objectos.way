/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.http.server;

import objectos.http.Http.Header.Name;
import objectos.http.Http.Header.Value;
import objectox.http.HttpRequestBody;
import objectos.http.Http.Method;

public interface Request {

  sealed interface Body permits HttpRequestBody {}

  Body body();

  Value header(Name name);

  Method method();

  String path();

}