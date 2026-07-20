/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectos.html.rec;

/// A fragment that takes two arguments.
///
/// @param <T1> the type of the first argument
/// @param <T2> the type of the second argument
@FunctionalInterface
public non-sealed interface Fragment2<T1, T2> extends Fragment {

  /// Invokes this set of instructions.
  ///
  /// @param arg1 the first argument
  /// @param arg2 the second argument
  void invoke(T1 arg1, T2 arg2);

}