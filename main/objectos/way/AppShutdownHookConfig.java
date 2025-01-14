/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import java.util.Objects;

final class AppShutdownHookConfig implements App.ShutdownHook.Config {

  Note.Sink noteSink = Note.NoOpSink.INSTANCE;

  @Override
  public final void noteSink(Note.Sink value) {
    noteSink = Objects.requireNonNull(value, "value == null");
  }

  final AppShutdownHook build() {
    return new AppShutdownHook(noteSink);
  }

}