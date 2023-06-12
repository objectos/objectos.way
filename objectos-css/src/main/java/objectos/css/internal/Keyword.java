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
package objectos.css.internal;

import java.util.Locale;
import objectos.css.pseudom.PPropertyValue.PKeyword;
import objectos.css.tmpl.Keywords;

// generate me
public enum Keyword implements PKeyword, Keywords.Block, Keywords.ColorKeyword {

  BLOCK,

  TRANSPARENT;

  private static final Keyword[] VALUES = Keyword.values();

  private final String keywordName;

  private Keyword() {
    this.keywordName = name().toLowerCase(Locale.US);
  }

  public static Keyword ofOrdinal(int value) {
    return VALUES[value];
  }

  @Override
  public final String keywordName() {
    return keywordName;
  }

}