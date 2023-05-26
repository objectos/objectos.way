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
import objectos.util.GrowableList;

class NestableParser {

  private boolean hasParent;

  private final StringBuilder ident = new StringBuilder();
  private int index;

  private Mode mode = Mode.START;
  private Nestable nestable;
  private GrowableList<NestablePart> parts = new GrowableList<>();
  private final char[] value;
  private boolean whitespace;

  NestableParser(String value) {
    this.value = Objects.requireNonNull(value).toCharArray();
  }

  public final Nestable parse() {
    while (index < value.length) {
      mode = mode.accept(this);
    }
    onListSeparator();
    return nestable;
  }

  final void add(char c) {
    ident.append(c);
  }

  final void addCombinator(Combinator combinator) {
    addParent();
    parts.add(combinator);
  }

  final void addDescendant() {
    parts.add(NestableSymbol.DESCENDANT);
  }

  final void addParent() {
    parts.add(NestableSymbol.PARENT);
    hasParent = true;
  }

  final void addWhitespaceIfNecessary() {
    if (whitespace) {
      ident.append(' ');
      whitespace = false;
    }
  }

  final void onListSeparator() {
    if (!hasParent) {
      parts.add(NestableSymbol.PARENT);
      parts.add(NestableSymbol.DESCENDANT);
    }
    parts.add(new Ident(ident.toString()));
    SimpleNestable newNestable = new SimpleNestable(parts.toUnmodifiableList());
    nestable = nestable != null ? nestable.add(newNestable) : newNestable;

    parts = new GrowableList<>();
    ident.setLength(0);
    whitespace = false;
  }

  final char read() {
    return value[index++];
  }

  final void setWhitespace() {
    whitespace = true;
  }

  private enum Mode {

    PARENT {
      @Override
      final Mode accept(NestableParser parser) {
        parser.addParent();

        char c = parser.read();
        if (Character.isWhitespace(c)) {
          parser.addDescendant();
          return START;
        } else {
          parser.add(c);
          return SELECTOR;
        }
      }
    },

    SELECTOR {
      @Override
      final Mode accept(NestableParser parser) {
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
    },

    START {
      @Override
      final Mode accept(NestableParser parser) {
        char c = parser.read();

        if (Character.isWhitespace(c)) {
          return this;
        } else if (c == '&') {
          return PARENT;
        } else if (c == '>') {
          parser.addCombinator(Combinator.CHILD);
          return this;
        } else if (c == '+') {
          parser.addCombinator(Combinator.ADJACENT_SIBLING);
          return this;
        } else if (c == '~') {
          parser.addCombinator(Combinator.GENERAL_SIBLING);
          return this;
        }

        parser.add(c);
        return SELECTOR;
      }
    };

    abstract Mode accept(NestableParser parser);

  }

}