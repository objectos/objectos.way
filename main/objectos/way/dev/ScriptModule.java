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
package objectos.way.dev;

import java.util.function.Consumer;
import objectos.http.HttpRouting;

/// This class is not part of the Objectos Way JAR file. It is placed in the
/// main source tree to ease the development.
final class ScriptModule implements Consumer<HttpRouting> {

  @Override
  public final void accept(HttpRouting routing) {
    routing.path("/script/000", path -> path.GET(new Script000()));
    routing.path("/script/001", path -> path.GET(new Script001()));
    routing.path("/script/002", path -> path.GET(new Script002()));
    routing.path("/script/003", path -> path.GET(new Script003()));
    routing.path("/script/004", path -> path.GET(new Script004()));
    routing.path("/script/005", path -> path.GET(new Script005()));
    routing.path("/script/006", path -> path.GET(new Script006()));
    routing.path("/script/007", path -> path.GET(new Script007()));
    routing.path("/script/017", path -> path.GET(new Script017()));
    routing.path("/script/018", path -> path.GET(new Script018()));
    routing.path("/script/019", path -> path.GET(new Script019()));
    routing.path("/script/020", path -> path.GET(new Script020()));
    routing.path("/script/021", path -> path.GET(new Script021()));
    routing.path("/script/022", path -> path.GET(new Script022()));
    routing.path("/script/024", path -> path.GET(new Script024()));
    routing.path("/script/027", path -> path.GET(new Script027()));
    routing.path("/script/follow/00", path -> path.GET(new ScriptFollow00()));
    routing.path("/script/follow/01", path -> path.GET(new ScriptFollow01()));
    routing.path("/script/follow/02", path -> path.GET(new ScriptFollow02()));
    routing.path("/script/follow/03", path -> path.GET(new ScriptFollow03()));
    routing.path("/script/follow/04", path -> path.GET(new ScriptFollow04()));
    routing.path("/script/follow/05", path -> path.GET(new ScriptFollow05()));
    routing.path("/script/global/00", path -> path.GET(new ScriptGlobalThis00()));
    routing.path("/script/element/00", path -> path.GET(new ScriptJsElement00()));
    routing.path("/script/element/01", path -> path.GET(new ScriptJsElement01()));
    routing.path("/script/element/02", path -> path.GET(new ScriptJsElement02()));
    routing.path("/script/element/03", path -> path.GET(new ScriptJsElement03()));
    routing.path("/script/element/render/00", path -> path.GET(new ScriptJsElementRender00()));
    routing.path("/script/element/render/01", path -> path.GET(new ScriptJsElementRender01()));
    routing.path("/script/element/render/02", path -> path.GET(new ScriptJsElementRender02()));
    routing.path("/script/element/render/03", path -> path.GET(new ScriptJsElementRender03()));
    routing.path("/script/history/00", path -> path.GET(new ScriptJsHistory00()));
    routing.path("/script/history/01", path -> path.GET(new ScriptJsHistory01()));
    routing.path("/script/location/00", path -> path.GET(new ScriptJsLocation00()));
    routing.path("/script/location/01", path -> path.GET(new ScriptJsLocation01()));
    routing.path("/script/window/00", path -> path.GET(new ScriptJsWindow00()));
    routing.path("/script/popstate/index", path -> path.GET(new ScriptPopstateIndex()));
    routing.path("/script/popstate/00", path -> path.GET(new ScriptPopstate00()));
    final ScriptSubmit00 submit00;
    submit00 = new ScriptSubmit00();
    routing.path("/script/submit/00", path -> path.handler(submit00));
    routing.path("/script/submit/00/after", path -> path.GET(submit00));
    routing.path("/script/submit/01", path -> path.GET(new ScriptSubmit01()));
    routing.path("/script/submit/02", path -> path.handler(new ScriptSubmit02()));
    routing.path("/script/submit/03", path -> path.handler(new ScriptSubmit03()));
  }

}
