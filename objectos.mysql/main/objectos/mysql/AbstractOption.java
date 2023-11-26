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

import java.util.List;
import java.util.Set;
import objectos.util.list.GrowableList;

abstract class AbstractOption implements Option {

  final String value;
  private final String key;

  AbstractOption(String key) {
    this(key, null);
  }

  AbstractOption(String key, String value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public void acceptClientJob(AbstractClientJob<?> job) {
    String option;
    option = toOptionImpl("--" + key);

    job.addCommand(option);
  }

  @Override
  public final void acceptConfigurationFile(GrowableList<String> lines) {
    String option;
    option = toOptionImpl(key);

    lines.add(option);
  }

  @Override
  public void acceptExecution(Execution execution) {
    String option;
    option = toOptionImpl("--" + key);

    execution.addOption(option);
  }

  @Override
  public final void acceptProcessBuilder(ProcessBuilder builder) {
    List<String> command;
    command = builder.command();

    String option;
    option = toOptionImpl("--" + key);

    command.add(option);
  }

  @Override
  public final void addKeyTo(Set<String> keys) {
    keys.add(key);
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof AbstractOption)) {
      return false;
    }

    AbstractOption that;
    that = (AbstractOption) obj;

    return getClass().equals(that.getClass())
        && key.equals(that.key)
        && value != null ? value.equals(that.value) : that.value == null;
  }

  @Override
  public final int hashCode() {
    return key.hashCode();
  }

  @Override
  public final boolean is(String key) {
    return this.key.equals(key);
  }

  @Override
  public final String toString() {
    return toOptionImpl(key);
  }

  private String toOptionImpl(String option) {
    String result = option;

    if (value != null) {
      result = result + '=' + value;
    }

    return result;
  }

}
