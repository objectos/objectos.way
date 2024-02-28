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

import objectos.http.Handler;
import objectos.http.HandlerFactory;
import testing.site.web.TestingHttpModule;
import testing.zite.TestingSiteInjector;

final class ProdHandlerFactory implements HandlerFactory {

  private final Handler handler;

  public ProdHandlerFactory(TestingSiteInjector injector) {
    TestingHttpModule module;
    module = new TestingHttpModule(injector);

    handler = module.compile();
  }

  @Override
  public final Handler create() throws Exception {
    return handler;
  }

}