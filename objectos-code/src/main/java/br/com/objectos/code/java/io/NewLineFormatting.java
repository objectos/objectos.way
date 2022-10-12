/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.io;

import br.com.objectos.code.java.declaration.BodyElement;
import br.com.objectos.code.java.element.NewLine;

public class NewLineFormatting extends Formatting {

  private static final NewLineFormatting INSTANCE = new NewLineFormatting();

  private NewLineFormatting() {}

  static Formatting getInstance() {
    return INSTANCE;
  }

  @Override
  final FormattingAction newAction(FormattingAction nextAction) {
    return new NewLineFormattingAction(nextAction);
  }

  public final class NewLineFormattingAction extends FormattingAction {

    private NewLineFormattingAction(FormattingAction nextAction) {
      super(nextAction);
    }

    @SuppressWarnings("exports")
    @Override
    public final void consume(FormattingSource source) {
      outer: while (source.hasElements()) {
        BodyElement element = source.getElement();
        switch (element.kind()) {
          case NEW_LINE:
            continue;
          default:
            storeElement(NewLine.nl());
            propagateElement(element);
            break outer;
        }
      }

      nextAction(source);
    }

    @Override
    public final void consumeElement(BodyElement element) {
      element.acceptNewLineFormattingAction(this);
    }

    public final void consumeNewLine(NewLine newLine) {
      storeElement(newLine);
    }

  }

}