/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.css;

import objectos.selfgen.css.spec.CssSpec;

class PropertiesModule extends CssSpec {

  @Override
  protected final void definition() {
    // A
    install(new AlignContentPropertyModule());
    install(new AlignItemsPropertyModule());
    install(new AlignSelfPropertyModule());
    install(new AppearancePropertyModule());

    // B
    install(new BackgroundAttachmentPropertyModule());
    install(new BackgroundClipPropertyModule());
    install(new BackgroundColorPropertyModule());
    install(new BackgroundImagePropertyModule());
    install(new BackgroundOriginPropertyModule());
    install(new BackgroundPositionPropertyModule());
    install(new BackgroundRepeatPropertyModule());
    install(new BackgroundSizePropertyModule());
    install(new BackgroundPropertyModule()); // must come after all bg
                                             // properties
    install(new BorderCollapsePropertyModule());
    install(new BorderColorPropertyModule());
    install(new BorderRadiusPropertyModule());
    install(new BorderStylePropertyModule());
    install(new BorderWidthPropertyModule());
    install(new BorderPropertyModule()); // must come after all border
                                         // properties
    install(new BottomPropertyModule());
    install(new BoxShadowPropertyModule());
    install(new BoxSizingPropertyModule());

    // C
    install(new ClearPropertyModule());
    install(new ColorPropertyModule());
    install(new ContentPropertyModule());
    install(new CursorPropertyModule());

    // D
    install(new DisplayPropertyModule());

    // F
    install(new FlexPropertyModule());
    install(new FlexBasisPropertyModule());
    install(new FlexDirectionPropertyModule());
    install(new FlexGrowPropertyModule());
    install(new FlexShrinkPropertyModule());
    install(new FlexWrapPropertyModule());
    install(new FlexFlowPropertyModule()); // must come after FlexDirection, FlexWrap

    install(new FloatPropertyModule());
    install(new FontFamilyPropertyModule());
    install(new FontPropertyModule());
    install(new FontSizePropertyModule());
    install(new FontStylePropertyModule());
    install(new FontWeightPropertyModule());

    // H
    install(new HeightPropertyModule());

    // J
    install(new JustifyContentPropertyModule());
    install(new JustifyItemsPropertyModule());
    install(new JustifySelfPropertyModule());

    // L
    install(new LeftPropertyModule());
    install(new LetterSpacingPropertyModule());
    install(new LineHeightPropertyModule());
    install(new ListStyleImagePropertyModule());
    install(new ListStylePositionPropertyModule());
    install(new ListStyleTypePropertyModule());
    install(new ListStylePropertyModule()); // must come after
                                            // image/position/type

    // M
    install(new MarginPropertyModule());
    install(new MaxHeightPropertyModule());

    // O
    install(new ObjectFitPropertyModule());
    install(new OpacityPropertyModule());
    install(new OutlineColorPropertyModule());
    install(new OutlineOffsetPropertyModule());
    install(new OutlineStylePropertyModule());
    install(new OutlineWidthPropertyModule());
    install(new OutlinePropertyModule()); // must come after
                                          // color/offset/style/width
    install(new OverflowPropertyModule());

    // P
    install(new PaddingPropertyModule());
    install(new PositionPropertyModule());

    // R
    install(new ResizePropertyModule());
    install(new RightPropertyModule());

    // T
    install(new TabSizePropertyModule());
    install(new TextAlignPropertyModule());
    install(new TextDecorationColorPropertyModule());
    install(new TextDecorationLinePropertyModule());
    install(new TextDecorationStylePropertyModule());
    install(new TextDecorationThicknessPropertyModule());
    install(new TextDecorationPropertyModule()); // must come after
                                                 // color/line/style/thickness
    install(new TextIndentPropertyModule());
    install(new TextShadowPropertyModule());
    install(new TextSizeAdjustPropertyModule());
    install(new TextTransformPropertyModule());
    install(new TopPropertyModule());
    install(new TransformPropertyModule());

    // V
    install(new VerticalAlignPropertyModule());

    // W
    install(new WhiteSpacePropertyModule());

    // Z
    install(new ZIndexPropertyModule());
  }

}
