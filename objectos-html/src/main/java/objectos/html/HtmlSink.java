/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html;

import java.io.IOException;
import objectos.html.internal.HtmlApi;
import objectos.html.internal.InternalIOException;
import objectos.html.internal.MinifiedWriter;

/**
 * @since 0.5.0
 */
public final class HtmlSink extends HtmlApi {

  private MinifiedWriter minifiedWriter;

  public final void appendTo(HtmlTemplate template, Appendable out) throws IOException {
    try {
      var writer = minifiedWriter();

      writer.out = out;

      visitor = writer;

      template.acceptTemplateDsl(this);
    } catch (InternalIOException e) {
      throw e.unwrap();
    } finally {
      visitor = null;
    }
  }

  private MinifiedWriter minifiedWriter() {
    if (minifiedWriter == null) {
      minifiedWriter = new MinifiedWriter();
    }

    return minifiedWriter;
  }

}