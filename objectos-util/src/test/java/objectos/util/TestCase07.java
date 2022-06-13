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

import java.util.Comparator;

/**
 * Create MutableList.toImmutableSortedList(Comparator<? super E> c) that
 * returns an immutable copy of the mutable list having all elements sorted by
 * the specified comparator. As required (sort of...) by objectos-git WriteTree
 * use-case.
 */
final class TestCase07 {

  static final String DESCRIPTION = "Verify MutableList.toImmutableSortedList(Comparator)";

  static final Comparator<Integer> ORDER = Lists.naturalOrder();

}