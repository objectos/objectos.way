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

import java.util.Objects;

class SelectorParser {

  private final char[] value;

  private final StringBuilder buffer = new StringBuilder();
  private Mode mode = Mode.START;
  private int index;
  private Selector selector;
  private boolean whitespace;

  SelectorParser(String value) {
    this.value = Objects.requireNonNull(value).toCharArray();
  }

  public final Selector parse() {
    while (index < value.length) {
      mode = mode.accept(this);
    }
    onListSeparator();
    return selector;
  }

  final void add(char c) {
    buffer.append(c);
  }

  final void addWhitespaceIfNecessary() {
    if (whitespace) {
      buffer.append(' ');
      whitespace = false;
    }
  }

  final void onListSeparator() {
    SimpleSelector newSelector = new SimpleSelector(buffer.toString());
    selector = selector != null ? selector.add(newSelector) : newSelector;
    buffer.setLength(0);
    whitespace = false;
  }

  final char read() {
    return value[index++];
  }

  final void setWhitespace() {
    whitespace = true;
  }

  private enum Mode {

    START {
      @Override
      final Mode accept(SelectorParser parser) {
        char c = parser.read();

        if (Character.isWhitespace(c)) {
          return this;
        }

        parser.add(c);
        return SELECTOR;
      }
    },

    SELECTOR {
      @Override
      final Mode accept(SelectorParser parser) {
        char c = parser.read();

        if (Character.isWhitespace(c)) {
          parser.setWhitespace();
          return this;
        } else if (c == ',') {
          parser.onListSeparator();
          return START;
        } else {
          parser.addWhitespaceIfNecessary();
          parser.add(c);
          return this;
        }
      }
    };

    abstract Mode accept(SelectorParser parser);

  }

}