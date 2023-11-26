/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.mysql;

import java.util.Set;
import objectos.util.list.GrowableList;

public final class NoopOption
    implements
    ClientOrConfigEditorOption,
    ServerOption {

  private static final NoopOption INSTANCE = new NoopOption();

  private NoopOption() {}

  public static NoopOption noop() {
    return INSTANCE;
  }

  @Override
  public final void acceptClientJob(AbstractClientJob<?> job) {
    // noop
  }

  @Override
  public final void acceptConfigurationFile(GrowableList<String> lines) {
    // noop
  }

  @SuppressWarnings("exports")
  @Override
  public final void acceptExecution(Execution execution) {
    // noop
  }

  @Override
  public final void acceptProcessBuilder(ProcessBuilder builder) {
    // noop
  }

  @Override
  public final void addKeyTo(Set<String> keys) {
    // noop
  }

  @Override
  public final boolean is(String key) {
    return false;
  }

}
