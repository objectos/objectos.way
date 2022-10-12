/*
 * Copyright 2022 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package br.com.objectos.code.util;

import java.util.List;

public final class GetOnly {

  private GetOnly() {}

  /**
   * Returns the only element of the specified list or throws an exception if
   * the list is empty or if the list contains more than one element.
   *
   * @param <T>
   *        the class of the objects in the list
   * @param list
   *        the list whose only element is to be returned
   *
   * @return the only element of this list
   *
   * @throws IllegalStateException
   *         if the list is empty or if the list contains more than one element
   */
  public static <T> T of(List<T> list) {
    switch (list.size()) {
      case 0:
        throw new IllegalStateException("Could not getOnly: empty.");
      case 1:
        return list.get(0);
      default:
        throw new IllegalStateException("Could not getOnly: more than one element.");
    }
  }

}