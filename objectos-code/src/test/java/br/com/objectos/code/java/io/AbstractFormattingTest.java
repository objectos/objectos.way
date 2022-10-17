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

import br.com.objectos.code.java.declaration.ClassBodyElement;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.util.Iterator;
import java.util.NoSuchElementException;
import objectos.util.UnmodifiableList;
import objectos.util.UnmodifiableIterator;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractFormattingTest
    extends AbstractCodeJavaTest
    implements FormattingSource {

  private Iterator<ClassBodyElement> iterator;

  @Override
  public final ClassBodyElement getElement() {
    return iterator.next();
  }

  @Override
  public final boolean hasElements() {
    return iterator.hasNext();
  }

  @BeforeMethod
  public void setUpFormattingElement() {
    iterator = new UnmodifiableIterator<ClassBodyElement>() {
      @Override
      public boolean hasNext() {
        return false;
      }

      @Override
      public ClassBodyElement next() {
        throw new NoSuchElementException();
      }
    };
  }

  final FormattingAction action(Formatting action) {
    return action.newAction(TailFormattingAction.getInstance());
  }

  final void withElements(ClassBodyElement... elements) {
    iterator = UnmodifiableList.copyOf(elements).iterator();
  }

}