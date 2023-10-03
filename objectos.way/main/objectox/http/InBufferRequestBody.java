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
package objectox.http;

import java.nio.charset.StandardCharsets;

public final class InBufferRequestBody extends HttpRequestBody {

  private final byte[] buffer;

  public final int start;

  public final int end;

  public InBufferRequestBody(byte[] buffer, int start, int end) {
    this.buffer = buffer;
    this.start = start;
    this.end = end;
  }

  public final byte get(int index) {
    return buffer[index];
  }

  @Override
  public final String toString() {
    return new String(buffer, start, end - start, StandardCharsets.UTF_8);
  }

}