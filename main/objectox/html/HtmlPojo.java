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
package objectox.html;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import objectos.html.Html;
import objectos.html.play.Formatter;
import objectos.html.play.Event;
import objectox.html.play.Player;
import objectox.html.rec.HtmlSink;

public final class HtmlPojo implements Html {

  private final byte[] code;

  private final Object[] objects;

  public HtmlPojo(HtmlSink sink) {
    code = sink.code();

    objects = sink.objects();
  }

  @Override
  public final Stream<Event> events() {
    final Player player;
    player = Player.of(code, objects);

    final int attrs;
    attrs = Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL;

    final Spliterator<Event> spliterator;
    spliterator = Spliterators.spliteratorUnknownSize(player, attrs);

    return StreamSupport.stream(spliterator, false);
  }

  @Override
  public final void binaryTo(OutputStream out) throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final Reader openStream(Formatter formatter) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final String toJsonString() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final String toString() {
    return super.toString();
  }

  @Override
  public final String toTestableText() {
    throw new UnsupportedOperationException("Implement me");
  }

}
