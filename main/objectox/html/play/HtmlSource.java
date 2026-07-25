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
package objectox.html.play;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import objectos.html.Html;
import objectos.html.play.HtmlEvent;
import objectox.html.rec.HtmlSink;

public final class HtmlSource implements Html {

  private final byte[] code;

  private final Object[] objects;

  public HtmlSource(HtmlSink sink) {
    code = sink.code();

    objects = sink.objects();
  }

  @Override
  public final Stream<HtmlEvent> events() {
    final Player player;
    player = new Player(this);

    final int attrs;
    attrs = Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL;

    final Spliterator<HtmlEvent> spliterator;
    spliterator = Spliterators.spliteratorUnknownSize(player, attrs);

    return StreamSupport.stream(spliterator, false);
  }

  public final byte getByte(int index) {
    return code[index];
  }

  public final boolean hasByte(int index) {
    return index < code.length;
  }

}
