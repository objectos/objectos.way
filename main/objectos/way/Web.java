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
package objectos.way;

import objectos.lang.object.Check;
import objectos.sql.Page;

/**
 * The Objectos Web main class.
 */
public final class Web {

  // types

  /**
   * Allows for pagination of data tables in an web application.
   */
  public interface Paginator {
    
    Page current();
    
    int firstItem();
    
    int lastItem();
    
    int totalCount();
    
    boolean hasNext();
    
    boolean hasPrevious();
    
    String nextHref();
    
    String previousHref();

  }
  
  /**
   * An web session.
   */
  public interface Session {
    
    /**
     * The identifier of this session.
     * 
     * @return the identifier of this session.
     */
    String id();

    /**
     * Returns the object associated to the specified class instance, or
     * {@code null} if there's no object associated.
     * 
     * @param <T> the type of the object
     * 
     * @param type
     *        the class instance to search for
     * 
     * @return the object associated or {@code null} if there's no object
     *         associated
     */
    <T> T get(Class<T> type);

    /**
     * Returns the object associated to the specified name, or {@code null} if
     * there's no object associated.
     * 
     * @param name
     *        the name to search for
     * 
     * @return the object associated or {@code null} if there's no object
     *         associated
     */
    Object get(String name);

    <T> Object put(Class<T> type, T value);

    Object put(String name, Object value);

    Object remove(String name);

    void invalidate();

  }
  
  private Web() {}

  /**
   * Creates a new paginator instance.
   * 
   * @param target the request target
   * @param pageAttrName the name of the page query parameter
   * @param pageSize the maximum number of elements in each page
   * @param totalCount the number of elements in all of the pages
   * 
   * @return a new paginator instance
   */
  public static Paginator createPaginator(Http.Request.Target target, String pageAttrName, int pageSize, int totalCount) {
    Check.notNull(target, "target == null");
    Check.notNull(pageAttrName, "pageAttrName == null");
    Check.argument(pageSize > 0, "pageSize must be positive");
    Check.argument(totalCount >= 0, "totalCount must be equal or greater than zero");

    return WebPaginator.of(target, pageAttrName, pageSize, totalCount);
  }
  
}