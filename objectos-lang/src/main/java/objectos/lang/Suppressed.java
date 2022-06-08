/*
 * Copyright (C) 2022 Objectos Software LTDA.
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
package objectos.lang;

/**
 * Utility for conditionally adding a
 * {@linkplain Throwable#addSuppressed(Throwable) suppressed throwable}.
 *
 * <p>
 * Example:
 *
 * <pre>{@code
 * Throwable rethrow = null;
 *
 * rethrow = Suppressed.addIfPossible(rethrow, new IOException());
 *
 * // rethrow is IOException now
 *
 * rethrow = Suppressed.addIfPossible(rethrow, new NullPointerException());
 *
 * // NPE is a suppressed throwable of IOException
 * }</pre>
 *
 * @since 0.2
 */
public final class Suppressed {

  private Suppressed() {}

  /**
   * Adds to the first throwable the second one as a suppressed
   * throwable and returns the first throwable. If the first throwable is
   * {@code null} then the second throwable is returned unchanged.
   *
   * @param primary
   *        the (maybe null) throwable
   * @param suppressed
   *        the suppresed throwable
   *
   * @return {@code exception} if it not null, {@code suppressed} otherwise
   */
  public static Throwable addIfPossible(Throwable primary, Throwable suppressed) {
    Check.notNull(suppressed, "suppressed == null");

    if (primary != null) {
      primary.addSuppressed(suppressed);

      return primary;
    } else {
      return suppressed;
    }
  }

}
