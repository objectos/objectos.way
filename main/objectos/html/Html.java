/*
 * Copyright (C) 2015-2026 Objectos Software LTDA.
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

import java.io.Reader;
import java.util.stream.Stream;
import objectos.html.format.Formatter;
import objectos.html.stream.Event;
import objectos.lang.BinaryObject;
import objectox.html.HtmlImpl;

/// Represents a compiled HTML document.
public sealed interface Html extends BinaryObject permits HtmlImpl {

  /// Returns the contents of this Objectos HTML document as a stream of events.
  ///
  /// @return the contents of this document as stream of events
  Stream<Event> events();

  /// Formats this HTML document using the specified formatter.
  ///
  /// @param formatter the formatter to use, not null
  ///
  /// @return the formatted string, not null
  String format(Formatter formatter);

  /// Returns a new [Reader] for reading the formatted HTML represented by this
  /// object.
  ///
  /// @param formatter the object to format the stream of events
  ///
  /// @return a newly created [Reader] for reading the formatted HTML
  Reader openStream(Formatter formatter);

  /// Returns the formatted HTML represented by this object.
  ///
  /// This method is equivalent to invoking `format(Formatter.standard())`.
  ///
  /// @return the formatted HTML represented by this object
  @Override
  String toString();

}
