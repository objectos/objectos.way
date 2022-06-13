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
 * An array-based {@link br.com.objectos.core.collection.ImmutableCollection}
 * and {@link java.util.List} implementation.
 *
 * @param <E>
 *        type of the elements in this list
 */
public final class ImmutableList<E> extends ImmutableListJava8<E> {

  ImmutableList() {
    super();
  }

  ImmutableList(java.lang.Object[] array) {
    super(array);
  }

}