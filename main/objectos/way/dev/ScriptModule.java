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

import static objectos.http.RequestMethod.GET;

import objectos.http.Routing;

/// This class is not part of the Objectos Way JAR file. It is placed in the
/// main source tree to ease the development.
final class ScriptModule {

  public final void configure(Routing r) {
    r.at("/script/000", GET, new Script000());
    r.at("/script/001", GET, new Script001());
    r.at("/script/002", GET, new Script002());
    r.at("/script/003", GET, new Script003());
    r.at("/script/004", GET, new Script004());
    r.at("/script/005", GET, new Script005());
    r.at("/script/006", GET, new Script006());
    r.at("/script/007", GET, new Script007());
    r.at("/script/017", GET, new Script017());
    r.at("/script/018", GET, new Script018());
    r.at("/script/019", GET, new Script019());
    r.at("/script/020", GET, new Script020());
    r.at("/script/021", GET, new Script021());
    r.at("/script/022", GET, new Script022());
    r.at("/script/024", GET, new Script024());
    r.at("/script/027", GET, new Script027());
    r.at("/script/follow/00", GET, new ScriptFollow00());
    r.at("/script/follow/01", GET, new ScriptFollow01());
    r.at("/script/follow/02", GET, new ScriptFollow02());
    r.at("/script/follow/03", GET, new ScriptFollow03());
    r.at("/script/follow/04", GET, new ScriptFollow04());
    r.at("/script/follow/05", GET, new ScriptFollow05());
    r.at("/script/global/00", GET, new ScriptGlobalThis00());
    r.at("/script/element/00", GET, new ScriptJsElement00());
    r.at("/script/element/01", GET, new ScriptJsElement01());
    r.at("/script/element/02", GET, new ScriptJsElement02());
    r.at("/script/element/03", GET, new ScriptJsElement03());
    r.at("/script/element/render/00", GET, new ScriptJsElementRender00());
    r.at("/script/element/render/01", GET, new ScriptJsElementRender01());
    r.at("/script/element/render/02", GET, new ScriptJsElementRender02());
    r.at("/script/element/render/03", GET, new ScriptJsElementRender03());
    r.at("/script/history/00", GET, new ScriptJsHistory00());
    r.at("/script/history/01", GET, new ScriptJsHistory01());
    r.at("/script/location/00", GET, new ScriptJsLocation00());
    r.at("/script/location/01", GET, new ScriptJsLocation01());
    r.at("/script/window/00", GET, new ScriptJsWindow00());
    r.at("/script/popstate/index", GET, new ScriptPopstateIndex());
    r.at("/script/popstate/00", GET, new ScriptPopstate00());
    final ScriptSubmit00 submit00;
    submit00 = new ScriptSubmit00();
    r.at("/script/submit/00", submit00);
    r.at("/script/submit/00/after", GET, submit00);
    r.at("/script/submit/01", GET, new ScriptSubmit01());
    r.at("/script/submit/02", new ScriptSubmit02());
    r.at("/script/submit/03", new ScriptSubmit03());
  }

}
