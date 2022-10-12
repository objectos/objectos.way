/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.declaration;

import br.com.objectos.code.annotations.Ignore;
import java.util.EnumMap;
import java.util.Map;

public class Modifiers {

  abstract static class JavaxMapperHolder {

    private final Map<javax.lang.model.element.Modifier, Modifier> MAPPER;

    JavaxMapperHolder() {
      Map<javax.lang.model.element.Modifier, Modifier> map;
      map = new EnumMap<javax.lang.model.element.Modifier, Modifier>(
        javax.lang.model.element.Modifier.class
      );

      map.put(javax.lang.model.element.Modifier.ABSTRACT, _abstract());
      map.put(javax.lang.model.element.Modifier.FINAL, _final());
      map.put(javax.lang.model.element.Modifier.NATIVE, _native());
      map.put(javax.lang.model.element.Modifier.PRIVATE, _private());
      map.put(javax.lang.model.element.Modifier.PROTECTED, _protected());
      map.put(javax.lang.model.element.Modifier.PUBLIC, _public());
      map.put(javax.lang.model.element.Modifier.STATIC, _static());
      map.put(javax.lang.model.element.Modifier.STRICTFP, _strictfp());
      map.put(javax.lang.model.element.Modifier.SYNCHRONIZED, _synchronized());
      map.put(javax.lang.model.element.Modifier.TRANSIENT, _transient());
      map.put(javax.lang.model.element.Modifier.VOLATILE, _volatile());

      addMore(map);

      MAPPER = map;
    }

    abstract void addMore(Map<javax.lang.model.element.Modifier, Modifier> map);

    final Modifier get(javax.lang.model.element.Modifier javaxModifier) {
      return MAPPER.get(javaxModifier);
    }

  }

  public static final AbstractModifier ABSTRACT = AbstractModifier.INSTANCE;

  public static final DefaultModifier DEFAULT = DefaultModifier.INSTANCE;

  public static final FinalModifier FINAL = FinalModifier.INSTANCE;

  public static final NativeModifier NATIVE = NativeModifier.INSTANCE;

  public static final NonSealedModifier NON_SEALED = NonSealedModifier.INSTANCE;

  public static final PrivateModifier PRIVATE = PrivateModifier.INSTANCE;

  public static final ProtectedModifier PROTECTED = ProtectedModifier.INSTANCE;

  public static final PublicModifier PUBLIC = PublicModifier.INSTANCE;

  public static final SealedModifier SEALED = SealedModifier.INSTANCE;

  public static final StaticModifier STATIC = StaticModifier.INSTANCE;

  public static final StrictfpModifier STRICTFP = StrictfpModifier.INSTANCE;

  public static final SynchronizedModifier SYNCHRONIZED = SynchronizedModifier.INSTANCE;

  public static final TransientModifier TRANSIENT = TransientModifier.INSTANCE;

  public static final VolatileModifier VOLATILE = VolatileModifier.INSTANCE;

  private Modifiers() {}

  public static AbstractModifier _abstract() {
    return ABSTRACT;
  }

  @Ignore("Use DefaultSwitchElement for now (from switch statement)")
  public static DefaultModifier _default() {
    return DEFAULT;
  }

  public static FinalModifier _final() {
    return FINAL;
  }

  public static NativeModifier _native() {
    return NATIVE;
  }

  public static NonSealedModifier _nonSealed() {
    return NON_SEALED;
  }

  public static PrivateModifier _private() {
    return PRIVATE;
  }

  public static ProtectedModifier _protected() {
    return PROTECTED;
  }

  public static PublicModifier _public() {
    return PUBLIC;
  }

  public static SealedModifier _sealed() {
    return SEALED;
  }

  public static StaticModifier _static() {
    return STATIC;
  }

  public static StrictfpModifier _strictfp() {
    return STRICTFP;
  }

  public static SynchronizedModifier _synchronized() {
    return SYNCHRONIZED;
  }

  public static TransientModifier _transient() {
    return TRANSIENT;
  }

  public static VolatileModifier _volatile() {
    return VOLATILE;
  }

  public static Modifier of(javax.lang.model.element.Modifier javaxModifier) {
    return ModifiersJavaxMapperHolderSingleton.INSTANCE.get(javaxModifier);
  }

}