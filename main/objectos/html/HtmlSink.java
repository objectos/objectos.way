/*
 * Copyright (C) 2023 Objectos Software LTDA.
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

import java.util.Objects;
import objectos.html.internal.HtmlCompiler02;
import objectos.html.pseudom.DocumentProcessor;

/**
 * TODO
 */
@SuppressWarnings("exports")
public final class HtmlSink extends HtmlCompiler02 {

  private HtmlSink() {}

  /**
   * Returns a new {@code HtmlSink} instance.
   *
   * @return a new {@code HtmlSink} instance.
   */
  public static HtmlSink of() {
    return new HtmlSink();
  }

  /**
   * Process the specified template with the specified processor.
   *
   * @param template
   *        the template to be processed
   *
   * @param processor
   *        the processor that will consume the template
   */
  public final void toProcessor(HtmlTemplate template, DocumentProcessor processor) {
    Objects.requireNonNull(template, "template == null");
    Objects.requireNonNull(processor, "processor == null");

    template.process(this, processor);
  }

}