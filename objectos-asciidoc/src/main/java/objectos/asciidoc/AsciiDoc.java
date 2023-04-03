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
package objectos.asciidoc;

import objectos.lang.Check;

public class AsciiDoc extends Interpreter {

  public interface Processor {

    void boldEnd();

    void boldStart();

    void documentEnd();

    void documentStart(DocumentAttributes attributes);

    void headingEnd(int level);

    void headingStart(int level);

    void inlineMacro(String name, String target, InlineMacroAttributes attributes);

    void italicEnd();

    void italicStart();

    void lineFeed();

    void link(String href, LinkText text);

    void listingBlockEnd();

    void listingBlockStart();

    void listItemEnd();

    void listItemStart();

    void monospaceEnd();

    void monospaceStart();

    void paragraphEnd();

    void paragraphStart();

    void preambleEnd();

    void preambleStart();

    void sectionEnd();

    void sectionStart(int level);

    void sectionStart(int level, String style);

    void sourceCodeBlockEnd();

    void sourceCodeBlockStart(String language);

    void text(String s);

    void unorderedListEnd();

    void unorderedListStart();

  }

  private final Pass0 pass0 = new Pass0();

  private final Pass1 pass1 = new Pass1();

  private AsciiDoc() {
  }

  public static AsciiDoc create() {
    return new AsciiDoc();
  }

  public final Document parse(String source) {
    this.source = Check.notNull(source, "source == null");

    pass0.execute(source);

    pass1.execute(pass0);

    int[] tokens = pass0.toToken();

    int[] codes = pass1.toCode();

    var attributes = pass1.toDocumentAttributes();

    pass1.running = false;

    return new Document(
      source, attributes, tokens, codes
    );
  }

  public final void process(String source, Processor processor) {
    this.source = Check.notNull(source, "source == null");
    this.processor = Check.notNull(processor, "processor == null");

    pass0.execute(source);

    pass2Source = pass0;

    pass1.execute(pass0);

    attributes = pass1.toDocumentAttributes();

    process0();

    pass1.running = false;
  }

  @Override
  final int codeAt(int index) { return pass1.codeAt(index); }

  @Override
  final int codeCursor() { return pass1.codeCursor(); }

  @Override
  final boolean hasCode() { return pass1.hasCode(); }

  @Override
  final int nextCode() { return pass1.nextCode(); }

  @Override
  final int tokenAt(int index) { return pass0.tokenAt(index); }

}