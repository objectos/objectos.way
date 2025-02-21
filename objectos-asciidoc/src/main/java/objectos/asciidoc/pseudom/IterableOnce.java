/*
 * Copyright (C) 2021-2025 Objectos Software LTDA.
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
package objectos.asciidoc.pseudom;

import java.util.Iterator;

/**
 * An {@link Iterable} which can be traversed only once.
 */
public interface IterableOnce<T> extends Iterable<T> {

  /**
   * Returns an iterator over elements of type T.
   *
   * @return an iterator
   *
   * @throws IllegalStateException
   *         if this {@code Iterable} has already been traversed
   */
  @Override
  Iterator<T> iterator();

}