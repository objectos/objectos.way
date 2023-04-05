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

import java.io.IOException;
import objectos.asciidoc.pseudom.Heading;
import objectos.asciidoc.pseudom.Node;

public final class PseudoHeading implements Heading {

  private final InternalSink sink;

  int level;

  PseudoHeading(InternalSink sink) {
    this.sink = sink;
  }

  @Override
  public final int level() {
    return level;
  }

  @Override
  public final boolean hasNext() throws IOException {
    return sink.headingHasNext();
  }

  @Override
  public final Node next() throws IOException {
    return sink.headingNext();
  }

}
