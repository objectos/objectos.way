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

final class Parse {

  public static final int START = 0;

  public static final int STOP = 1;

  public static final int MAYBE_HEADING = 2;

  public static final int MAYBE_HEADING_TRIM = 3;

  public static final int HEADING = 4;

  public static final int HEADING_CONTENTS = 5;

  public static final int HEADING_CONTENTS_EOF = 6;

  public static final int HEADING_CONTENTS_NL = 7;

  private Parse() {}

}