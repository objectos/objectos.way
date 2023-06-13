/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css;

import java.io.IOException;
import java.util.Objects;
import objectos.css.internalold.CssPlayer;
import objectos.css.internalold.PrettyPrintWriter;
import objectos.css.internalold.Writer;

/**
 * @since 0.7.0
 */
public final class CssSink extends CssPlayer {

  private PrettyPrintWriter prettyPrintWriter;

  private PrettyPrintWriter writer;

  public final void toStringBuilder(CssTemplate template, StringBuilder out) {
    Objects.requireNonNull(template, "template == null");
    Objects.requireNonNull(out, "out == null");

    executeRecorder(template);

    try {
      writeImpl(out);
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw IOException", e);
    }
  }

  private void writeImpl(Appendable out) throws IOException {
    var writer = thisWriter();

    writer.out = out;

    executePlayer(writer);

    writer.throwIfNecessary();
  }

  private PrettyPrintWriter prettyPrintWriter() {
    if (prettyPrintWriter == null) {
      prettyPrintWriter = new PrettyPrintWriter();
    }

    return prettyPrintWriter;
  }

  private Writer thisWriter() {
    if (writer == null) {
      writer = prettyPrintWriter();
    }

    return writer;
  }

}