/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import java.util.Objects;

record NoteImpl(String source, String key, Marker marker)
    implements
    Note.Int1,
    Note.Int2,
    Note.Int3,

    Note.Long1,
    Note.Long1Ref1<Object>,
    Note.Long1Ref2<Object, Object>,
    Note.Long2,

    Note.Ref0,
    Note.Ref1<Object>,
    Note.Ref2<Object, Object>,
    Note.Ref3<Object, Object, Object> {

  NoteImpl(Class<?> source, String key, Marker marker) {
    this(
        source.getCanonicalName(),

        key,

        marker
    );
  }

  NoteImpl {
    Objects.requireNonNull(source, "source == null");

    Objects.requireNonNull(key, "key == null");

    Objects.requireNonNull(marker, "marker == null");
  }

}