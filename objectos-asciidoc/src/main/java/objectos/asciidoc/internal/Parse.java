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

enum Parse {
  STOP,

  /* document only */
  DOCUMENT_START,
  MAYBE_HEADER,
  MAYBE_HEADER_TRIM,
  HEADER,
  NOT_HEADER,

  /* document, section or phrase */

  BODY,
  BODY_TRIM,

  MAYBE_ATTRLIST,
  NAME_OR_VALUE,
  MAYBE_ATTRLIST_END,
  MAYBE_ATTRLIST_END_TRIM,
  ATTRLIST,
  NOT_ATTRLIST,
  INLINE_ATTRLIST,

  MAYBE_BOLD_OR_ULIST,
  NOT_BOLD_OR_ULIST,

  MAYBE_LISTING_OR_ULIST,
  NOT_LISTING_OR_ULIST,
  MAYBE_ULIST,
  ULIST,
  NOT_ULIST,

  MAYBE_SECTION,
  MAYBE_SECTION_TRIM,
  SECTION,
  NOT_SECTION,

  PARAGRAPH,

  /* section */

  EXHAUSTED;
}