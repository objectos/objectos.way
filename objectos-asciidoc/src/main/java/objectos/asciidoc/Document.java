/*
 * Copyright (C) 2021-2026 Objectos Software LTDA.
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
package objectos.asciidoc;

import objectos.asciidoc.AsciiDoc.Processor;
import objectos.lang.Check;

public final class Document extends Interpreter {

  private class ThisPass2Source implements Pass2.Source {
    @Override
    public int token(int index) { return tokens[index]; }
  }

  private final String source;

  private final DocumentAttributes attributes;

  private final int[] tokens;

  private final int[] codes;

  private int codeCursor;

  Document(String source, DocumentAttributes attributes, int[] tokens, int[] codes) {
    this.source = source;

    this.attributes = attributes;

    this.tokens = tokens;

    this.codes = codes;
  }

  public final String getAttribute(String key, String defaultValue) {
    return attributes.getOrDefault(key, defaultValue);
  }

  public final void process(Processor processor) {
    Check.state(this.processor == null, "Concurrent processing not allowed");

    this.processor = Check.notNull(processor, "processor == null");

    codeCursor = 0;

    super.attributes = this.attributes;

    super.source = this.source;

    pass2Source = new ThisPass2Source();

    process0();
  }

  @Override
  final int codeAt(int index) { return codes[index]; }

  @Override
  final int codeCursor() { return codeCursor; }

  @Override
  final boolean hasCode() { return codeCursor < codes.length; }

  @Override
  final int nextCode() { return codes[codeCursor++]; }

  @Override
  final int tokenAt(int index) { return tokens[index]; }

}