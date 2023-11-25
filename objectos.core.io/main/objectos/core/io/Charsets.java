/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.core.io;

import java.nio.charset.Charset;

/**
 * Convenience class for accessing {@link Charset charsets}.
 *
 * @since 2
 */
public final class Charsets {

  private Charsets() {}

  /**
   * Returns the ISO-8859-1 {@link Charset charset}.
   *
   * @return the ISO-8859-1 charset
   */
  public static Charset isoLatin1() {
    return CharsetIsoLatin1.INSTANCE;
  }

  /**
   * Returns the US-ASCII {@link Charset charset}.
   *
   * @return the US-ASCII charset
   */
  public static Charset usAscii() {
    return CharsetUsAscii.INSTANCE;
  }

  /**
   * Returns the UTF-8 {@link Charset charset}.
   *
   * @return the UTF-8 charset
   */
  public static Charset utf8() {
    return CharsetUtf8.INSTANCE;
  }

}