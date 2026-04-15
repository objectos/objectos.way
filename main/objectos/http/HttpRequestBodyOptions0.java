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

final class HttpRequestBodyOptions0 implements HttpRequestBodyOptions {

  private final int memoryMax;

  private final long sizeMax;

  HttpRequestBodyOptions0(int memoryMax, long sizeMax) {
    this.memoryMax = memoryMax;

    this.sizeMax = sizeMax;
  }

  @Override
  public final HttpRequestBodySupport supportOf(long id) {
    return new HttpRequestBodySupport0(id, memoryMax, sizeMax);
  }

}
