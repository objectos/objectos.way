/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;

final class ShutdownHookLogger implements System.Logger {

  final List<Throwable> exceptions = new ArrayList<Throwable>();

  @Override
  public String getName() { return null; }

  @Override
  public boolean isLoggable(Level level) { return false; }

  @Override
  public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {}

  @Override
  public void log(Level level, ResourceBundle bundle, String format, Object... params) {}

  @Override
  public void log(Level level, Supplier<String> msgSupplier, Throwable thrown) {
    exceptions.add(thrown);
  }

}