package liqp

import com.google.common.collect.ImmutableList
import liqp.filter.AbsFilter
import liqp.filter.AppendFilter
import liqp.filter.AtLeastFilter
import liqp.filter.AtMostFilter
import liqp.filter.CapitalizeFilter
import liqp.filter.CeilFilter
import liqp.filter.CompactFilter
import liqp.filter.ConcatFilter
import liqp.filter.DateFilter
import liqp.filter.DefaultFilter
import liqp.filter.DividedBy
import liqp.filter.DowncaseFilter
import liqp.filter.EscapeFilter
import liqp.filter.EscapeOnceFilter
import liqp.filter.Filters
import liqp.filter.FirstFilter
import liqp.filter.FloorFilter
import liqp.filter.HFilter
import liqp.filter.JoinFilter
import liqp.filter.LastFilter
import liqp.filter.LstripFilter
import liqp.filter.MapFilter
import liqp.filter.MinusFilter
import liqp.filter.ModuloFilter
import liqp.filter.NewlineToBrFilter
import liqp.filter.NumFilter
import liqp.filter.ParenthesisFilter
import liqp.filter.PlusFilter
import liqp.filter.PrependFilter
import liqp.filter.RemoveFilter
import liqp.filter.RemoveFirstFilter
import liqp.filter.ReplaceFilter
import liqp.filter.ReplaceFirstFilter
import liqp.filter.ReverseFilter
import liqp.filter.RoundFilter
import liqp.filter.RstripFilter
import liqp.filter.SizeFilter
import liqp.filter.SliceFilter
import liqp.filter.SortFilter
import liqp.filter.SortNaturalFilter
import liqp.filter.SplitFilter
import liqp.filter.StripFilter
import liqp.filter.StripNewlinesFilter
import liqp.filter.TimesFilter
import liqp.filter.TrimFilter
import liqp.filter.TruncateFilter
import liqp.filter.TruncatewordsFilter
import liqp.filter.UncapitalizeFilter
import liqp.filter.UniqFilter
import liqp.filter.UpcaseFilter
import liqp.filter.UrlDecodeFilter
import liqp.filter.UrlEncodeFilter
import liqp.tag.Include
import liqp.tag.Tags
import liqp.tags.Assign
import liqp.tags.Break
import liqp.tags.Capture
import liqp.tags.Case
import liqp.tags.Comment
import liqp.tags.Continue
import liqp.tags.Cycle
import liqp.tags.Decrement
import liqp.tags.For
import liqp.tags.If
import liqp.tags.Ifchanged
import liqp.tags.Increment
import liqp.tags.Raw
import liqp.tags.Tablerow
import liqp.tags.Unless

object LiquidDefaults {
  @JvmStatic
  val defaultFilters: Filters by lazy {
    // Register all standard filter.
    Filters(ImmutableList.of(
        AbsFilter(),
        AppendFilter(),
        AtLeastFilter(),
        AtMostFilter(),
        CapitalizeFilter(),
        UncapitalizeFilter(),
        CeilFilter(),
        CompactFilter(),
        ConcatFilter(),
        DateFilter(),
        DefaultFilter(),
        DividedBy(),
        DowncaseFilter(),
        EscapeFilter(),
        EscapeOnceFilter(),
        FirstFilter(),
        FloorFilter(),
        HFilter(),
        JoinFilter(),
        LastFilter(),
        LstripFilter(),
        MapFilter(),
        MinusFilter(),
        ModuloFilter(),
        NewlineToBrFilter(),
        NumFilter(),
        ParenthesisFilter(),
        PlusFilter(),
        PrependFilter(),
        RemoveFilter(),
        RemoveFirstFilter(),
        ReplaceFilter(),
        ReplaceFirstFilter(),
        ReverseFilter(),
        RoundFilter(),
        RstripFilter(),
        SizeFilter(),
        SliceFilter(),
        SortFilter(),
        SortNaturalFilter(),
        SplitFilter(),
        StripFilter(),
        StripNewlinesFilter(),
        TimesFilter(),
        TrimFilter(),
        TruncateFilter(),
        TruncatewordsFilter(),
        UniqFilter(),
        UpcaseFilter(),
        UrlDecodeFilter(),
        UrlEncodeFilter()))
  }

  @JvmStatic
  val defaultTags = Tags(ImmutableList.of(
      Assign(),
      Break(),
      Capture(),
      Case(),
      Comment(),
      Continue(),
      Cycle(),
      Decrement(),
      For(),
      If(),
      Ifchanged(),
      Include(),
      Increment(),
      Raw(),
      Tablerow(), Unless()))
}
