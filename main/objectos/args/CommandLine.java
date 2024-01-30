/*
 * Copyright (C) 2024 Objectos Software LTDA.
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
package objectos.args;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommandLine {

  @SuppressWarnings("unused")
  private final String name;

  private final Option<?>[] options;

  private final Map<String, Option<?>> optionsByName;

  public CommandLine(String name, Option<?>... options) {
    this.name = Objects.requireNonNull(name, "name == null");

    this.options = Arrays.copyOf(options, options.length);

    Map<String, Option<?>> byName;
    byName = new HashMap<>();

    for (Option<?> o : options) { // implicit options null check
      o.acceptByName(byName);
    }

    this.optionsByName = Map.copyOf(byName);
  }

  public final void parse(String[] args) throws CommandLineException {
    int index;
    index = 0;

    int length;
    length = args.length;

    while (index < length) {
      String arg;
      arg = args[index++];

      Option<?> option;
      option = optionsByName.get(arg);

      if (option != null) {
        index = option.accept(args, index);
      }
    }

    ErrorCollector collector;
    collector = new ErrorCollector();

    for (Option<?> option : options) {
      option.validate(collector);
    }

    collector.throwIfNecessary();
  }

}