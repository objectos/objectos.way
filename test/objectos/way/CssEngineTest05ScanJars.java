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

import static org.testng.Assert.assertEquals;

import java.util.Set;
import objectos.way.Note.NoOpSink;
import objectos.way.Note.Ref1;
import org.slf4j.Logger;
import org.testng.annotations.Test;

public class CssEngineTest05ScanJars {

  @Test
  public void scanJarFile01() {
    class ThisNoteSink extends NoOpSink {
      final Set<Object> values;

      ThisNoteSink(Set<Object> values) { this.values = values; }

      @Override
      public <T1> void send(Ref1<T1> note, T1 value) {
        values.add(value);
      }
    }

    Set<Object> values;
    values = Util.createSet();

    ThisNoteSink noteSink;
    noteSink = new ThisNoteSink(values);

    assertEquals(
        CssEngine.generate(config -> {
          config.noteSink(noteSink);

          config.scanJarFileOf(Logger.class);

          config.skipLayer(Css.Layer.THEME);
          config.skipLayer(Css.Layer.BASE);
          config.skipLayer(Css.Layer.COMPONENTS);
        }),

        """
        @layer utilities {
        }
        """
    );

    assertEquals(values.contains("org/slf4j/Logger.class"), true);
    assertEquals(values.contains("org/slf4j/Marker.class"), true);
  }

}