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
package objectos.code;

import objectos.code.JavaTemplate.Renderer;

final class Pass2 {

  @SuppressWarnings("unused")
  private int[] source;

  @SuppressWarnings("unused")
  private int sourceIndex;

  @SuppressWarnings("unused")
  private Object[] objects;

  @SuppressWarnings("unused")
  private Renderer processor;

  public final void execute(int[] source, Object[] objects, JavaTemplate.Renderer processor) {
    this.source = source;
    this.objects = objects;
    this.processor = processor;

    sourceIndex = 0;

    execute0();
  }

  private void execute0() {

  }

}