/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.carbonated;

import objectos.css.util.Length;

public final class Breakpoint {

  public static final Length SMALL = Length.px(320);

  public static final Length MEDIUM = Length.px(672);

  public static final Length LARGE = Length.px(1056);

  public static final Length X_LARGE = Length.px(1312);

  public static final Length MAX = Length.px(1584);

  private Breakpoint() {}

}