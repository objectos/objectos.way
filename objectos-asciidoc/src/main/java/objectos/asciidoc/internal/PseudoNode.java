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
import java.util.NoSuchElementException;
import objectos.asciidoc.pseudom.Node;

abstract class PseudoNode {

  /*

  document = 100
  header = 200
  title = 300
  inline macro = 400
  monospaced = 500
  paragraph = 600
  section = 700
  unordered list = 800
  listing block = 900
  list item = 1000
  emphasis = 1100;
  
  */

  final InternalSink sink;

  PseudoNode(InternalSink sink) {
    this.sink = sink;
  }

  public abstract boolean hasNext();

  final void appendTo(Appendable out, int start, int end) throws IOException {
    sink.appendTo(out, start, end);
  }

  final void closeImpl() throws IOException {
    sink.close();
  }

  final Node nextNodeDefault() {
    if (hasNext()) {
      return sink.nextNode();
    } else {
      throw new NoSuchElementException();
    }
  }

  final Node nextNodeSink() {
    return sink.nextNode();
  }

}