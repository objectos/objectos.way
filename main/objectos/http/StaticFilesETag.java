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

import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Function;

final class StaticFilesETag implements Function<BasicFileAttributes, String> {

  private final long highBits;

  StaticFilesETag(int highBits) {
    this.highBits = highBits;
  }

  @Override
  public final String apply(BasicFileAttributes attributes) {
    final Object fileKey;
    fileKey = attributes.fileKey();

    final int lowBits;
    lowBits = fileKey.hashCode();

    long etag;
    etag = highBits << 32L;

    etag |= lowBits;

    return Long.toHexString(etag);
  }

}
