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

import static objectos.way.Http.Method.GET;

import module objectos.way;

/// This class is not part of the Objectos Way JAR file. It is placed in the
/// main source tree to ease the development.
final class ScriptModule implements Http.Routing.Module {

  @Override
  public final void configure(Http.Routing routing) {
    routing.path("/script/000", GET, new Script000());
    routing.path("/script/001", GET, new Script001());
    routing.path("/script/002", GET, new Script002());
    routing.path("/script/003", GET, new Script003());
    routing.path("/script/004", GET, new Script004());
    routing.path("/script/005", GET, new Script005());
    routing.path("/script/006", GET, new Script006());
    routing.path("/script/007", GET, new Script007());
    routing.path("/script/008", GET, new Script008());
    routing.path("/script/009", GET, new Script009());
    routing.path("/script/010", GET, new Script010());
    routing.path("/script/011", GET, new Script011());
    routing.path("/script/012", GET, new Script012());
    routing.path("/script/013", GET, new Script013());
    routing.path("/script/014", GET, new Script014());
    routing.path("/script/015", GET, new Script015());
    routing.path("/script/016", GET, new Script016());
    routing.path("/script/017", GET, new Script017());
    routing.path("/script/018", GET, new Script018());
    routing.path("/script/019", GET, new Script019());
    routing.path("/script/020", GET, new Script020());
    routing.path("/script/021", GET, new Script021());
    routing.path("/script/022", GET, new Script022());
    routing.path("/script/023", GET, new Script023());
    routing.path("/script/024", GET, new Script024());
    routing.path("/script/026", GET, new Script026());
    routing.path("/script/027", GET, new Script027());
    routing.path("/script/history/00", GET, new ScriptJsHistory00());
    routing.path("/script/history/01", GET, new ScriptJsHistory01());
  }

}
