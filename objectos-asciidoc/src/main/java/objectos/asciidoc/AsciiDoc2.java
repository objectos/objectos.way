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

import java.io.IOException;
import java.util.Objects;
import objectos.asciidoc.internal.InternalSink;
import objectos.asciidoc.pseudom.Document;

class AsciiDoc2 extends InternalSink {

  public AsciiDoc2() {}

  public final void toProcessor(String source, Document.Processor processor) throws IOException {
    CharSequence cs = source.toString(); // implicit null-check
    Objects.requireNonNull(processor, "processor == null");

    toProcessorImpl(cs, processor);
  }

}