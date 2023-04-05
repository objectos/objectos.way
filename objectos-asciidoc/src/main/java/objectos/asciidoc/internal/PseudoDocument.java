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
import objectos.asciidoc.pseudom.Document;
import objectos.asciidoc.pseudom.Node;

public final class PseudoDocument implements Document {

  private final InternalSink sink;

  PseudoDocument(InternalSink sink) {
    this.sink = sink;
  }

  @Override
  public final boolean hasNext() throws IOException {
    return sink.documentHasNext();
  }

  @Override
  public final Node next() throws IOException {
    return sink.documentNext();
  }

}