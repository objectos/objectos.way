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
import java.util.List;
import objectos.util.UnmodifiableList;

class TailFormattingAction extends FormattingAction {

  private TailFormattingAction() {
    super(new NoopFormattingAction());
  }

  static TailFormattingAction getInstance() {
    return new TailFormattingAction();
  }

  @Override
  public final void consume(FormattingSource source) {
    while (source.hasElements()) {
      consumeElement(source.getElement());
    }
  }

  @Override
  public final void consumeElement(BodyElement element) {
    storeElement(element);
  }

  private static class NoopFormattingAction extends FormattingAction {

    private NoopFormattingAction() {
      super(null);
    }

    @Override
    public final void consume(FormattingSource source) {
      throw new UnsupportedOperationException();
    }

    @Override
    public final void consumeElement(BodyElement element) {
      throw new UnsupportedOperationException();
    }

    @Override
    public final List<BodyElement> stream() {
      return UnmodifiableList.of();
    }

  }

}