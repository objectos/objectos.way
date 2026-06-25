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
package objectox.http.req;

import java.nio.file.Files;
import java.util.Objects;
import objectos.http.RequestBodyFiles;
import objectos.http.RequestBodyOptions;

public final class RequestBodyConfigBuilder implements RequestBodyOptions {

  private RequestBodyFiles files = () -> Files.createTempFile("", "");

  private int memoryMax = 32 * 1024;

  private long sizeMax = 10 * 1024 * 1024;

  public final RequestBodyConfig build() {
    return new RequestBodyConfig(files, memoryMax, sizeMax);
  }

  @Override
  public final void files(RequestBodyFiles value) {
    files = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void memoryMax(int value) {
    if (value < 0) {
      throw new IllegalArgumentException("memoryMax value must not be negative");
    }

    memoryMax = value;
  }

  @Override
  public final void sizeMax(long value) {
    if (value < 0) {
      throw new IllegalArgumentException("sizeMax value must not be negative");
    }

    sizeMax = value;
  }

}
