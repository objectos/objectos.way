/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html;

/**
 * A delayed set of template instructions.
 *
 * <p>
 * The set of instructions MUST be of the same template instance where this
 * fragment will be included.
 *
 * @see Html#include(FragmentLambda)
 */
@FunctionalInterface
public interface FragmentLambda {

  /**
   * Invokes this set of instructions.
   */
  void invoke();

}