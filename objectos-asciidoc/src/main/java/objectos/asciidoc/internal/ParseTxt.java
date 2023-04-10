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
package objectos.asciidoc.internal;

final class ParseTxt {

  public static final boolean SINGLE_LINE = true;

  public static final int START_LIKE = 2;
  public static final int STOP = 3;
  public static final int SPACE_LIKE = 4;
  public static final int BLOB = 5;
  public static final int EOF = 6;
  public static final int EOL = 7;

  private ParseTxt() {}

}