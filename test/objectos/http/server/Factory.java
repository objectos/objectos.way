/*
 * Copyright (C) 2023 Objectos Software LTDA.
 *
 * This file is part of the objectos.www project.
 *
 * objectos.www is NOT free software and is the intellectual property of Objectos Software LTDA.
 *
 * Source is available for educational purposes only.
 */
package objectos.http.server;

import java.io.IOException;

@FunctionalInterface
public interface Factory<T> {

  T create() throws IOException;

}