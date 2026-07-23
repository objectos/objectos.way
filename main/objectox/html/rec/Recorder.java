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
package objectox.html.rec;

import objectos.html.AttributeName;
import objectos.html.Component;
import objectos.html.ElementName;
import objectos.html.Markup;
import objectos.html.rec.AttributeMarkup;
import objectos.html.rec.ElementMarkup;
import objectos.html.rec.Fragment0;
import objectos.html.rec.Fragment1;
import objectos.html.rec.Fragment2;
import objectos.html.rec.Fragment3;
import objectos.html.rec.Fragment4;
import objectos.html.rec.Instruction;
import objectos.html.rec.Instruction.OfAmbiguous;
import objectos.html.rec.Instruction.OfFragment;
import objectox.html.Ambiguous;
import objectox.html.HtmlByteProto;
import objectox.html.play.Player;

public final class Recorder {

  private final ByteArray aux;

  private final ByteArray main;

  private final ObjectArray objects;

  private final AmbiguousRecorder ambiguousRecorder;

  private final ZZZAttribute0Recorder attribute0Recorder;

  private final Attribute1Recorder attribute1Recorder;

  private final ComponentRecorder componentRecorder;

  private final ZZZElementRecorder elementRecorder;

  private final FlattenRecorder flattenRecorder;

  private final FragmentRecorder fragmentRecorder;

  private final RawRecorder rawRecorder;

  private final TextRecorder textRecorder;

  private Recorder(
      ByteArray aux,

      ByteArray main,

      ObjectArray objects,

      AmbiguousRecorder ambiguousRecorder,

      ZZZAttribute0Recorder attribute0Recorder,

      Attribute1Recorder attribute1Recorder,

      ComponentRecorder componentRecorder,

      ZZZElementRecorder elementRecorder,

      FlattenRecorder flattenRecorder,

      FragmentRecorder fragmentRecorder,

      RawRecorder rawRecorder,

      TextRecorder textRecorder
  ) {
    this.aux = aux;

    this.main = main;

    this.objects = objects;

    this.ambiguousRecorder = ambiguousRecorder;

    this.attribute0Recorder = attribute0Recorder;

    this.attribute1Recorder = attribute1Recorder;

    this.componentRecorder = componentRecorder;

    this.elementRecorder = elementRecorder;

    this.flattenRecorder = flattenRecorder;

    this.fragmentRecorder = fragmentRecorder;

    this.rawRecorder = rawRecorder;

    this.textRecorder = textRecorder;
  }

  public static Recorder create() {
    final ByteArray aux;
    aux = new ByteArray(128);

    final ByteArray main;
    main = new ByteArray(256);

    final ObjectArray objects;
    objects = new ObjectArray();

    final Encoder encoder;
    encoder = new Encoder(
        main,

        new FlattenEncoder(main),

        new ElementEncoder(main)
    );

    final ElementValueEncoder elementValueEncoder;
    elementValueEncoder = new ElementValueEncoder(aux, main, encoder);

    final AttributeObjectRecorder attributeObjectRecorder;
    attributeObjectRecorder = new AttributeObjectRecorder(aux, objects);

    final ElementInternalRecorder elementInternalRecorder;
    elementInternalRecorder = new ElementInternalRecorder(aux, main);

    final ElementValueRecorder elementValueRecorder;
    elementValueRecorder = new ElementValueRecorder(attributeObjectRecorder, elementInternalRecorder);

    final ForwardOffsetRecorder forwardOffsetRecorder;
    forwardOffsetRecorder = new ForwardOffsetRecorder(main);

    final ReverseOffsetRecorder reverseOffsetRecorder;
    reverseOffsetRecorder = new ReverseOffsetRecorder(main);

    return new Recorder(
        aux,

        main,

        objects,

        new AmbiguousRecorder(main, objects),

        new ZZZAttribute0Recorder(main, objects),

        new Attribute1Recorder(main, objects),

        new ComponentRecorder(main, forwardOffsetRecorder, reverseOffsetRecorder),

        new ZZZElementRecorder(
            new ElementNameRecorder(main),

            elementValueEncoder,

            elementValueRecorder,

            forwardOffsetRecorder,

            reverseOffsetRecorder
        ),

        new FlattenRecorder(
            main,

            elementValueEncoder,

            elementValueRecorder,

            forwardOffsetRecorder,

            reverseOffsetRecorder
        ),

        new FragmentRecorder(main, forwardOffsetRecorder, reverseOffsetRecorder),

        new RawRecorder(main, objects),

        new TextRecorder(main, objects)
    );
  }

  public final OfAmbiguous ambiguous(Ambiguous name, String value) {
    return ambiguousRecorder.record(name, value);
  }

  public final AttributeMarkup attribute0(AttributeName name) {
    return attribute0Recorder.record(name);
  }

  public final AttributeMarkup attribute1(AttributeName name, Object value) {
    return attribute1Recorder.record(name, value);
  }

  public final OfFragment component(Markup ctx, Component... components) {
    return componentRecorder.record(ctx, components);
  }

  public final OfFragment component(Markup ctx, Iterable<? extends Component> components) {
    return componentRecorder.record(ctx, components);
  }

  public final void doctype() {
    main.add(HtmlByteProto.DOCTYPE);
  }

  public final ElementMarkup element(ElementName name, Instruction... contents) {
    return elementRecorder.record(name, contents);
  }

  public final OfFragment f(Fragment0 fragment) {
    return fragmentRecorder.record(fragment);
  }

  public final <T1> OfFragment f(Fragment1<T1> fragment, T1 arg1) {
    return fragmentRecorder.record(fragment, arg1);
  }

  public final <T1, T2> OfFragment f(Fragment2<T1, T2> fragment, T1 arg1, T2 arg2) {
    return fragmentRecorder.record(fragment, arg1, arg2);
  }

  public final <T1, T2, T3> OfFragment f(Fragment3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3) {
    return fragmentRecorder.record(fragment, arg1, arg2, arg3);
  }

  public final <T1, T2, T3, T4> OfFragment f(Fragment4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
    return fragmentRecorder.record(fragment, arg1, arg2, arg3, arg4);
  }

  public final ElementMarkup flatten(Instruction... contents) {
    return flattenRecorder.record(contents);
  }

  public final ElementMarkup flatten(Iterable<? extends Instruction> contents) {
    return flattenRecorder.record(contents);
  }

  public final ElementMarkup raw(String value) {
    return rawRecorder.record(value);
  }

  public final ElementMarkup text(String value) {
    return textRecorder.record(value);
  }

  public final ByteArray aux() {
    return aux;
  }

  public final ByteArray main() {
    return main;
  }

  public final ObjectArray objects() {
    return objects;
  }

  public final Player toPlayer() {
    return new Player(main, objects);
  }

}
