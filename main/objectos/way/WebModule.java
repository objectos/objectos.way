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

abstract class WebModule extends Http.Module {

  private final Sql.Transaction.Isolation trxIsolation = Sql.SERIALIZABLE;

  private Sql.Database source;

  protected WebModule() {}

  /**
   * Use the specified data source for obtaining database connections.
   *
   * @param source
   *        the data source to use
   *
   * @throws IllegalStateException
   *         if a source has already been defined
   */
  protected final void source(Sql.Database source) {
    Check.state(this.source == null, "this module's data source has already been defined");

    this.source = Check.notNull(source, "source == null");
  }

  /**
   * Decorates the specified handler around a SQL transaction.
   *
   * @param handler
   *        the HTTP handler to be decorated
   *
   * @return a new HTTP handler that decorates the specified handler around a
   *         SQL transaction
   */
  protected final Http.Handler transactional(Http.Handler handler) {
    Check.state(source != null, "A data source must be defined via the source(Sql.Source) method");

    return http -> {

      Sql.Transaction trx;
      trx = source.beginTransaction(trxIsolation);

      try {
        http.set(Sql.Transaction.class, trx);

        handler.handle(http);

        trx.commit();
      } catch (Throwable error) {
        throw trx.rollbackAndWrap(error);
      } finally {
        trx.close();
      }

    };
  }

}