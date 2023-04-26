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

enum Phrasing {

  START,

  STOP,

  BLOB,

  TEXT,

  EOL,

  INLINE_MACRO,
  INLINE_MACRO_END,

  CUSTOM_INLINE_MACRO,

  URI_MACRO,
  URI_MACRO_ATTRLIST,
  URI_MACRO_ROLLBACK,
  URI_MACRO_TARGET,
  URI_MACRO_TARGET_LOOP,

  AUTOLINK;

}