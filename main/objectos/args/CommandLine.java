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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import objectos.util.list.GrowableList;

public class CommandLine {

  @SuppressWarnings("unused")
  private String name;

  private List<Option<?>> options;

  public CommandLine() {}

  public CommandLine(String name, Option<?>... options) {
    this.name = Objects.requireNonNull(name, "name == null");

    GrowableList<Option<?>> list;
    list = new GrowableList<>();

    for (int i = 0; i < options.length; i++) {
      Option<?> option;
      option = options[i];

      list.addWithNullMessage(option, "options[", i, "] == null");
    }

    this.options = list;
  }

  public final <E extends Enum<E>> EnumOption<E> newEnumOption(Class<E> type, String name) {
    return register(new EnumOption<>(type, name));
  }

  public final IntegerOption newIntegerOption(String name) {
    return register(new IntegerOption(name));
  }

  public final PathOption newPathOption(String name) {
    return register(new PathOption(name));
  }

  public final <T> SetOption<T> newSetOption(String string, Function<String, ? extends T> converter) {
    return register(new SetOption<>(string, converter));
  }

  public final StringOption newStringOption(String name) {
    return register(new StringOption(name));
  }

  private <T extends Option<?>> T register(T option) {
    if (options == null) {
      options = new GrowableList<>();
    }

    options.add(option);

    return option;
  }

  public final void parse(String[] args) throws CommandLineException {
    int index;
    index = 0;

    int length;
    length = args.length;

    Map<String, Option<?>> optionsByName;
    optionsByName = byName();

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

  private Map<String, Option<?>> byName() {
    Map<String, Option<?>> byName;
    byName = new HashMap<>();

    for (Option<?> o : options) { // implicit options null check
      o.acceptByName(byName);
    }

    return byName;
  }

}