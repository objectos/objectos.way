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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

record HttpRequestImpl(HttpMethod method, String path) implements HttpRequest {

  @Override
  public HttpVersion version() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public String pathParam(String name) { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String queryParam(String name) { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public List<String> queryParamAll(String name) { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public Set<String> queryParamNames() { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String rawPath() { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String rawQuery() { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String rawQueryWith(String name, String value) { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public String header(HttpHeaderName name) { throw new UnsupportedOperationException("Implement me"); }

  @Override
  public InputStream bodyInputStream() throws IOException { throw new UnsupportedOperationException("Implement me"); }

}
