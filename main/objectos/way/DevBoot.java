/*
 * This file is part of Objectos UI.
 * Copyright (C) 2026 Objectos Software LTDA.
 *
 * Objectos UI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Objectos UI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Objectos UI.  If not, see <https://www.gnu.org/licenses/>.
 */
package objectos.way;

import objectos.way.dev.DevModule;

/// This class is not part of the Objectos Way JAR file.
/// It is placed in the main source tree to ease the development.
public final class DevBoot {

  private DevBoot() {}

  public static Http.Handler boot(App.Injector injector, Module original) {
    final Module reloaded;
    reloaded = DevBoot.class.getModule();

    reloaded.addReads(original);

    final DevModule dev;
    dev = new DevModule(injector);

    return Http.Handler.of(dev);
  }

}