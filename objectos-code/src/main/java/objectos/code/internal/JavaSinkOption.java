/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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
package objectos.code.internal;

import objectos.code.JavaSink;
import objectos.code.JavaSink.Option;

public abstract sealed class JavaSinkOption extends JavaSink.Option {

  private static final class OverwriteExisting extends JavaSinkOption {
    @Override
    protected final void acceptOfDirectory(JavaSinkOfDirectory sink) {
      sink.overwriteExising = true;
    }
  }

  private static final class SkipExisting extends JavaSinkOption {
    @Override
    protected final void acceptOfDirectory(JavaSinkOfDirectory sink) {
      sink.skipExising = true;
    }
  }

  public static final Option OVERWRITE_EXISTING = new OverwriteExisting();

  public static final Option SKIP_EXISTING = new SkipExisting();

}
