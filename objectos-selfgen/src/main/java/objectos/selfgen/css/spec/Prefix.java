/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.css.spec;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Prefix {

  STANDARD(""),

  WEBKIT("-webkit-"),

  MICROSOFT("-ms-"),

  MOZILLA("-moz-"),

  UNKNOWN("-unknown-");

  private static final Map<String, Prefix> PARSER;

  static {
    Map<String, Prefix> parser = new HashMap<>();

    parser.put("-webkit-", WEBKIT);
    parser.put("-ms-", MICROSOFT);
    parser.put("-mso-", MICROSOFT);
    parser.put("-moz-", MOZILLA);

    PARSER = Collections.unmodifiableMap(parser);
  }

  private final String value;

  Prefix(String value) {
    this.value = value;
  }

  public static Prefix parse(String text) {
    Prefix res = STANDARD;

    if (text.startsWith("-")) {
      res = PARSER.getOrDefault(text, UNKNOWN);
    }

    return res;
  }

  @Override
  public String toString() {
    return value;
  }

}