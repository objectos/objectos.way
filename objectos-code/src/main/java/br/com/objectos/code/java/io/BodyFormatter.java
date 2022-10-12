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
import java.util.Iterator;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public abstract class BodyFormatter {

  private static final BodyFormatter DEFAULT = with(
      Formatting.newLine(),
      Formatting.fields(),
      Formatting.constructors(),
      Formatting.methods(),
      Formatting.types()
  );

  private static final BodyFormatter UNFORMATED = new UnformattedClassBodyFormatter();

  BodyFormatter() {}

  public static BodyFormatter defaultFormatter() {
    return DEFAULT;
  }

  public static BodyFormatter unformatted() {
    return UNFORMATED;
  }

  public static BodyFormatter with(Formatting... formattings) {
    return new StandardClassBodyFormatter(UnmodifiableList.copyOf(formattings));
  }

  public abstract <E extends BodyElement> UnmodifiableList<E> format(
      GrowableList<E> elements, Class<E> type);

  private static class StandardClassBodyFormatter extends BodyFormatter {

    private final UnmodifiableList<Formatting> formattings;

    private StandardClassBodyFormatter(UnmodifiableList<Formatting> formattings) {
      this.formattings = formattings;
    }

    @Override
    public final <E extends BodyElement> UnmodifiableList<E> format(
        GrowableList<E> elements, Class<E> type) {
      FormattingAction action = TailFormattingAction.getInstance();
      for (int i = formattings.size() - 1; i >= 0; i--) {
        Formatting formatting = formattings.get(i);
        action = formatting.newAction(action);
      }
      FormattingSource source = new ThisFormattingSource(elements);
      action.consume(source);
      return action.toUnmodifiableList(type);
    }

    private static class ThisFormattingSource implements FormattingSource {
      private final Iterator<? extends BodyElement> elements;

      ThisFormattingSource(GrowableList<? extends BodyElement> elements) {
        this.elements = elements.iterator();
      }

      @Override
      public final BodyElement getElement() {
        return elements.next();
      }

      @Override
      public final boolean hasElements() {
        return elements.hasNext();
      }
    }

  }

  private static class UnformattedClassBodyFormatter extends BodyFormatter {
    @Override
    public final <E extends BodyElement> UnmodifiableList<E> format(
        GrowableList<E> elements, Class<E> type) {
      return elements.toUnmodifiableList();
    }
  }

}