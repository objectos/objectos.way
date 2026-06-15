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
package objectox.http;

/// Enum representing possible values for the SameSite attribute.
public enum SameSite {
  /** Prevents the cookie from being sent in cross-site requests. */
  STRICT("Strict"),

  /** Allows the cookie in top-level navigation cross-site requests. */
  LAX("Lax"),

  /** Allows the cookie in all cross-site requests (not recommended). */
  NONE("None");

  public final String text;

  private SameSite(String text) { this.text = text; }

}