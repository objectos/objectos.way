/*
 * Copyright (C) 2022 Objectos Software LTDA.
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
package objectos.util;

/**
 * An array-based {@code abstract} implementation of {@link BaseCollection}.
 *
 * @param <E> type of the elements in this collection
 */
public abstract class AbstractBaseCollection<E> extends AbstractBaseCollectionJava8<E> {

  /**
   * Sole constructor
   */
  protected AbstractBaseCollection() {
    super();
  }

}