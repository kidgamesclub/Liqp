package liqp.tags

const val OFFSET = "offset"
const val COLS = "cols"
const val LIMIT = "limit"

/**
 * tablerowloop.length       # => length of the entire for loop
 * tablerowloop.index        # => index of the current iteration
 * tablerowloop.index0       # => index of the current iteration (zero based)
 * tablerowloop.rindex       # => how many items are still left?
 * tablerowloop.rindex0      # => how many items are still left? (zero based)
 * tablerowloop.first        # => is this the first iteration?
 * tablerowloop.last         # => is this the last iteration?
 * tablerowloop.col          # => index of column in the current row
 * tablerowloop.col0         # => index of column in the current row (zero based)
 * tablerowloop.col_first    # => is this the first column in the row?
 * tablerowloop.col_last     # => is this the last column in the row?
 */
const val TABLEROWLOOP = "tablerowloop"
const val LENGTH = "length"
const val INDEX = "index"
const val INDEX0 = "index0"
const val RINDEX = "rindex"
const val RINDEX0 = "rindex0"
const val FIRST = "first"
const val LAST = "last"
const val COL = "col"
const val COL0 = "col0"
const val COL_FIRST = "col_first"
const val COL_LAST = "col_last"
