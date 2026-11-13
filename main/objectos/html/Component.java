/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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

import objectos.html.format.Formatter;
import objectos.html.markup.RegularElementValue;
import objectox.html.MarkupImpl;

/// An object that renders HTML on its own or as part of a larger HTML document.
@FunctionalInterface
public non-sealed interface Component
    extends
    RegularElementValue {

  /// Compiles this component into an HTML document.
  ///
  /// @return the compiled HTML document, not null
  default Html compile() {
    final Markup m;
    m = newHtmlMarkup();

    renderHtml(m);

    return m.compile();
  }

  /// Returns the markup instance to be used by this component.
  ///
  /// @return the markup instance to be used by this component
  default Markup newHtmlMarkup() {
    return MarkupImpl.create();
  }

  /// Renders the HTML of this component.
  ///
  /// @param m the markup object to use
  void renderHtml(Markup m);

  /// Returns the formatted string representation of the testable nodes of this
  /// component.
  ///
  /// @return the formatted string representation of the testable nodes of
  ///         this component
  default String toTestableText() {
    final Html html;
    html = compile();

    return html.format(Formatter.testable());
  }

}