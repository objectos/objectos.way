/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package testing.site;

import java.lang.reflect.Constructor;
import objectos.lang.classloader.ClassReloader;
import objectos.way.HandlerFactory;
import objectos.way.Http;
import testing.zite.TestingSiteInjector;

final class DevHandlerFactory implements HandlerFactory {

  private final ClassReloader classReloader;

  private final TestingSiteInjector injector;

  public DevHandlerFactory(ClassReloader classReloader, TestingSiteInjector injector) {
    this.classReloader = classReloader;

    this.injector = injector;
  }

  @Override
  public final Http.Handler create() throws Exception {
    Class<?> type;
    type = classReloader.get();

    Constructor<?> constructor;
    constructor = type.getConstructor(TestingSiteInjector.class);

    Http.Module module;
    module = (Http.Module) constructor.newInstance(injector);

    return module.compile();
  }

}
