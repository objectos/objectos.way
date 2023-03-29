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
package objectos.html.internal;

import objectos.html.tmpl.Instruction.AlignmentBaselineAttribute;
import objectos.html.tmpl.Instruction.AmbiguousInstruction;
import objectos.html.tmpl.Instruction.AutocompleteAttribute;
import objectos.html.tmpl.Instruction.BaselineShiftAttribute;
import objectos.html.tmpl.Instruction.ClipPathAttribute;
import objectos.html.tmpl.Instruction.ClipRuleAttribute;
import objectos.html.tmpl.Instruction.ColorAttribute;
import objectos.html.tmpl.Instruction.ColorInterpolationAttribute;
import objectos.html.tmpl.Instruction.ColorInterpolationFiltersAttribute;
import objectos.html.tmpl.Instruction.CrossoriginAttribute;
import objectos.html.tmpl.Instruction.CursorAttribute;
import objectos.html.tmpl.Instruction.DAttribute;
import objectos.html.tmpl.Instruction.DirectionAttribute;
import objectos.html.tmpl.Instruction.DisabledAttribute;
import objectos.html.tmpl.Instruction.DisplayAttribute;
import objectos.html.tmpl.Instruction.DominantBaselineAttribute;
import objectos.html.tmpl.Instruction.ElementContents;
import objectos.html.tmpl.Instruction.FillAttribute;
import objectos.html.tmpl.Instruction.FillOpacityAttribute;
import objectos.html.tmpl.Instruction.FillRuleAttribute;
import objectos.html.tmpl.Instruction.FilterAttribute;
import objectos.html.tmpl.Instruction.FloodColorAttribute;
import objectos.html.tmpl.Instruction.FloodOpacityAttribute;
import objectos.html.tmpl.Instruction.FontFamilyAttribute;
import objectos.html.tmpl.Instruction.FontSizeAdjustAttribute;
import objectos.html.tmpl.Instruction.FontSizeAttribute;
import objectos.html.tmpl.Instruction.FontStretchAttribute;
import objectos.html.tmpl.Instruction.FontStyleAttribute;
import objectos.html.tmpl.Instruction.FontVariantAttribute;
import objectos.html.tmpl.Instruction.FontWeightAttribute;
import objectos.html.tmpl.Instruction.FormAttribute;
import objectos.html.tmpl.Instruction.GlobalAttribute;
import objectos.html.tmpl.Instruction.GlyphOrientationHorizontalAttribute;
import objectos.html.tmpl.Instruction.GlyphOrientationVerticalAttribute;
import objectos.html.tmpl.Instruction.HeightAttribute;
import objectos.html.tmpl.Instruction.HrefAttribute;
import objectos.html.tmpl.Instruction.ImageRenderingAttribute;
import objectos.html.tmpl.Instruction.LetterSpacingAttribute;
import objectos.html.tmpl.Instruction.LightingColorAttribute;
import objectos.html.tmpl.Instruction.MarkerEndAttribute;
import objectos.html.tmpl.Instruction.MarkerMidAttribute;
import objectos.html.tmpl.Instruction.MarkerStartAttribute;
import objectos.html.tmpl.Instruction.MaskAttribute;
import objectos.html.tmpl.Instruction.MaskTypeAttribute;
import objectos.html.tmpl.Instruction.NameAttribute;
import objectos.html.tmpl.Instruction.NoOpInstruction;
import objectos.html.tmpl.Instruction.OpacityAttribute;
import objectos.html.tmpl.Instruction.OverflowAttribute;
import objectos.html.tmpl.Instruction.PaintOrderAttribute;
import objectos.html.tmpl.Instruction.PlaceholderAttribute;
import objectos.html.tmpl.Instruction.PointerEventsAttribute;
import objectos.html.tmpl.Instruction.ReadonlyAttribute;
import objectos.html.tmpl.Instruction.ReferrerpolicyAttribute;
import objectos.html.tmpl.Instruction.RequiredAttribute;
import objectos.html.tmpl.Instruction.ShapeRenderingAttribute;
import objectos.html.tmpl.Instruction.SrcAttribute;
import objectos.html.tmpl.Instruction.StopColorAttribute;
import objectos.html.tmpl.Instruction.StopOpacityAttribute;
import objectos.html.tmpl.Instruction.StrokeAttribute;
import objectos.html.tmpl.Instruction.StrokeDasharrayAttribute;
import objectos.html.tmpl.Instruction.StrokeDashoffsetAttribute;
import objectos.html.tmpl.Instruction.StrokeLinecapAttribute;
import objectos.html.tmpl.Instruction.StrokeLinejoinAttribute;
import objectos.html.tmpl.Instruction.StrokeMiterlimitAttribute;
import objectos.html.tmpl.Instruction.StrokeOpacityAttribute;
import objectos.html.tmpl.Instruction.StrokeWidthAttribute;
import objectos.html.tmpl.Instruction.TargetAttribute;
import objectos.html.tmpl.Instruction.TextAnchorAttribute;
import objectos.html.tmpl.Instruction.TextDecorationAttribute;
import objectos.html.tmpl.Instruction.TextOverflowAttribute;
import objectos.html.tmpl.Instruction.TextRenderingAttribute;
import objectos.html.tmpl.Instruction.TransformAttribute;
import objectos.html.tmpl.Instruction.TransformOriginAttribute;
import objectos.html.tmpl.Instruction.TypeAttribute;
import objectos.html.tmpl.Instruction.UnicodeBidiAttribute;
import objectos.html.tmpl.Instruction.ValueAttribute;
import objectos.html.tmpl.Instruction.VectorEffectAttribute;
import objectos.html.tmpl.Instruction.VisibilityAttribute;
import objectos.html.tmpl.Instruction.WhiteSpaceAttribute;
import objectos.html.tmpl.Instruction.WidthAttribute;
import objectos.html.tmpl.Instruction.WordSpacingAttribute;
import objectos.html.tmpl.Instruction.WritingModeAttribute;

public enum InternalInstruction implements
    AlignmentBaselineAttribute,
    AutocompleteAttribute,
    BaselineShiftAttribute,
    ClipPathAttribute,
    ClipRuleAttribute,
    ColorAttribute,
    ColorInterpolationAttribute,
    ColorInterpolationFiltersAttribute,
    CrossoriginAttribute,
    CursorAttribute,
    DAttribute,
    DirectionAttribute,
    DisabledAttribute,
    DisplayAttribute,
    DominantBaselineAttribute,
    FillAttribute,
    FillOpacityAttribute,
    FillRuleAttribute,
    FilterAttribute,
    FloodColorAttribute,
    FloodOpacityAttribute,
    FontFamilyAttribute,
    FontSizeAttribute,
    FontSizeAdjustAttribute,
    FontStretchAttribute,
    FontStyleAttribute,
    FontVariantAttribute,
    FontWeightAttribute,
    FormAttribute,
    GlyphOrientationHorizontalAttribute,
    GlyphOrientationVerticalAttribute,
    HeightAttribute,
    HrefAttribute,
    ImageRenderingAttribute,
    LetterSpacingAttribute,
    LightingColorAttribute,
    MarkerEndAttribute,
    MarkerMidAttribute,
    MarkerStartAttribute,
    MaskAttribute,
    MaskTypeAttribute,
    NameAttribute,
    OpacityAttribute,
    OverflowAttribute,
    PaintOrderAttribute,
    PlaceholderAttribute,
    PointerEventsAttribute,
    ReadonlyAttribute,
    ReferrerpolicyAttribute,
    RequiredAttribute,
    ShapeRenderingAttribute,
    SrcAttribute,
    StopColorAttribute,
    StopOpacityAttribute,
    StrokeAttribute,
    StrokeDasharrayAttribute,
    StrokeDashoffsetAttribute,
    StrokeLinecapAttribute,
    StrokeLinejoinAttribute,
    StrokeMiterlimitAttribute,
    StrokeOpacityAttribute,
    StrokeWidthAttribute,
    TargetAttribute,
    TextAnchorAttribute,
    TextDecorationAttribute,
    TextOverflowAttribute,
    TextRenderingAttribute,
    TransformAttribute,
    TransformOriginAttribute,
    TypeAttribute,
    UnicodeBidiAttribute,
    ValueAttribute,
    VectorEffectAttribute,
    VisibilityAttribute,
    WhiteSpaceAttribute,
    WidthAttribute,
    WordSpacingAttribute,
    WritingModeAttribute,
    AmbiguousInstruction,
    GlobalAttribute,
    ElementContents,
    NoOpInstruction {
  INSTANCE;
}
