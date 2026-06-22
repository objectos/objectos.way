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
package objectox.http.host;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import objectos.http.HostOptions;

public final class HostMapBuilder {

  private final Map<String, HostStage> hosts = new LinkedHashMap<>();

  public final void add(Consumer<? super HostOptions> opts) {
    final HostStageBuilder builder;
    builder = new HostStageBuilder();

    opts.accept(builder);

    final HostStage stage;
    stage = builder.build();

    final String name;
    name = stage.name();

    final HostStage existing;
    existing = hosts.put(name, stage);

    if (existing != null) {
      final String msg;
      msg = "A host with the same name was already registered: %s".formatted(name);

      throw new IllegalArgumentException(msg);
    }
  }

  public final HostMap build(HostGlobals globals) {
    final Collection<HostStage> stages;
    stages = hosts.values();

    final Stream<HostStage> stream;
    stream = stages.stream();

    final Stream<Host> hostStream;
    hostStream = stream.map(stage -> stage.toHost(globals));

    final List<Host> list;
    list = hostStream.toList();

    return HostMap.of(list);
  }

}
