package liqp

import com.google.common.collect.ImmutableList
import liqp.filter.Abs
import liqp.filter.Append
import liqp.filter.AtLeast
import liqp.filter.AtMost
import liqp.filter.Capitalize
import liqp.filter.Ceil
import liqp.filter.Compact
import liqp.filter.Concat
import liqp.filter.Date
import liqp.filter.Default
import liqp.filter.DividedBy
import liqp.filter.Downcase
import liqp.filter.Escape
import liqp.filter.EscapeOnce
import liqp.filter.Filters
import liqp.filter.First
import liqp.filter.Floor
import liqp.filter.H
import liqp.filter.Join
import liqp.filter.Last
import liqp.filter.Lstrip
import liqp.filter.Minus
import liqp.filter.Modulo
import liqp.filter.NewlineToBr
import liqp.filter.Plus
import liqp.filter.Prepend
import liqp.filter.Remove
import liqp.filter.RemoveFirst
import liqp.filter.Replace
import liqp.filter.ReplaceFirst
import liqp.filter.Reverse
import liqp.filter.Round
import liqp.filter.Rstrip
import liqp.filter.Size
import liqp.filter.Slice
import liqp.filter.Sort
import liqp.filter.SortNatural
import liqp.filter.Split
import liqp.filter.Strip
import liqp.filter.StripNewlines
import liqp.filter.Times
import liqp.filter.Truncate
import liqp.filter.Truncatewords
import liqp.filter.Uncapitalize
import liqp.filter.Uniq
import liqp.filter.Upcase
import liqp.filter.UrlDecode
import liqp.filter.UrlEncode
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
        Abs(),
        Append(),
        AtLeast(),
        AtMost(),
        Capitalize(),
        Uncapitalize(),
        Ceil(),
        Compact(),
        Concat(),
        Date(),
        Default(),
        DividedBy(),
        Downcase(),
        Escape(),
        EscapeOnce(),
        First(),
        Floor(),
        H(),
        Join(),
        Last(),
        Lstrip(),
        liqp.filter.Map(),
        Minus(),
        Modulo(),
        NewlineToBr(),
        Plus(),
        Prepend(),
        Remove(),
        RemoveFirst(),
        Replace(),
        ReplaceFirst(),
        Reverse(),
        Round(),
        Rstrip(),
        Size(),
        Slice(),
        Sort(),
        SortNatural(),
        Split(),
        Strip(),
        StripNewlines(),
        Times(),
        Truncate(),
        Truncatewords(),
        Uniq(),
        Upcase(),
        UrlDecode(),
        UrlEncode()))
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
