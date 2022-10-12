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
import objectos.util.GrowableList;

final class BodyElementFormatting extends Formatting {

  static final BodyElementFormatting CONSTRUCTORS
      = new BodyElementFormatting(BodyElement.Kind.CONSTRUCTOR);
  static final BodyElementFormatting FIELDS
      = new BodyElementFormatting(BodyElement.Kind.FIELD);
  static final BodyElementFormatting METHODS
      = new BodyElementFormatting(BodyElement.Kind.METHOD);
  static final BodyElementFormatting TYPES
      = new BodyElementFormatting(BodyElement.Kind.TYPE);

  private final BodyElement.Kind kind;

  private final PostAction postAction;

  private BodyElementFormatting(BodyElement.Kind kind) {
    this(kind, NewLinePostAction.INSTANCE);
  }

  private BodyElementFormatting(BodyElement.Kind kind,
                                PostAction postAction) {
    this.kind = kind;
    this.postAction = postAction;
  }

  @Override
  final FormattingAction newAction(FormattingAction nextAction) {
    return new ClassBodyElementFormattingAction(nextAction);
  }

  private class ClassBodyElementFormattingAction extends FormattingAction {

    private ClassBodyElementFormattingAction(FormattingAction nextAction) {
      super(nextAction);
    }

    @Override
    public final void consume(FormattingSource source) {
      while (source.hasElements()) {
        BodyElement element;
        element = source.getElement();

        consumeElement(element);
      }

      nextAction(source);
    }

    @Override
    public final void consumeElement(BodyElement element) {
      if (element.kind() == kind) {
        storeElement(element);
      } else {
        propagateElement(element);
      }
    }

    @Override
    final GrowableList<BodyElement> elements() {
      return postAction.applyTo(super.elements());
    }

  }

  private static class NewLinePostAction extends PostAction {
    private static final NewLinePostAction INSTANCE = new NewLinePostAction();

    @Override
    final GrowableList<BodyElement> applyTo(GrowableList<BodyElement> elements) {
      GrowableList<BodyElement> result = new GrowableList<>();

      for (BodyElement element : elements) {
        result.add(element);

        result.add(NewLine.nl());
      }

      return result;
    }
  }

  private abstract static class PostAction {
    abstract GrowableList<BodyElement> applyTo(GrowableList<BodyElement> elements);
  }

}