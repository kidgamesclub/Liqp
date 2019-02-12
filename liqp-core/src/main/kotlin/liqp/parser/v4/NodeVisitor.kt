package liqp.parser.v4

import liqp.EmptyNode
import liqp.filter.Filters
import liqp.filter.LFilter
import liqp.lookup.Index
import liqp.lookup.Property
import liqp.node.LNode
import liqp.nodes.AndNode
import liqp.nodes.AtomNode
import liqp.nodes.AttributeNode
import liqp.nodes.BlockNode
import liqp.nodes.ContainsNode
import liqp.nodes.EqNode
import liqp.nodes.FilterNode
import liqp.nodes.GtEqNode
import liqp.nodes.GtNode
import liqp.nodes.KeyValueNode
import liqp.nodes.LookupNode
import liqp.nodes.LtEqNode
import liqp.nodes.LtNode
import liqp.nodes.NEqNode
import liqp.nodes.OrNode
import liqp.nodes.OutputNode
import liqp.nodes.TagNode
import liqp.tag.Tags
import liquid.parser.v4.LiquidParser.And
import liquid.parser.v4.LiquidParser.AssignmentContext
import liquid.parser.v4.LiquidParser.Atom_othersContext
import liquid.parser.v4.LiquidParser.AttributeContext
import liquid.parser.v4.LiquidParser.BlockContext
import liquid.parser.v4.LiquidParser.Capture_tag_IdContext
import liquid.parser.v4.LiquidParser.Capture_tag_StrContext
import liquid.parser.v4.LiquidParser.Case_tagContext
import liquid.parser.v4.LiquidParser.Comment_tagContext
import liquid.parser.v4.LiquidParser.Cycle_tagContext
import liquid.parser.v4.LiquidParser.Eq
import liquid.parser.v4.LiquidParser.Expr_containsContext
import liquid.parser.v4.LiquidParser.Expr_eqContext
import liquid.parser.v4.LiquidParser.Expr_logicContext
import liquid.parser.v4.LiquidParser.Expr_relContext
import liquid.parser.v4.LiquidParser.Expr_termContext
import liquid.parser.v4.LiquidParser.File_name_or_output_StrContext
import liquid.parser.v4.LiquidParser.File_name_or_output_other_than_tag_end_out_startContext
import liquid.parser.v4.LiquidParser.File_name_or_output_outputContext
import liquid.parser.v4.LiquidParser.FilterContext
import liquid.parser.v4.LiquidParser.For_arrayContext
import liquid.parser.v4.LiquidParser.For_rangeContext
import liquid.parser.v4.LiquidParser.Gt
import liquid.parser.v4.LiquidParser.GtEq
import liquid.parser.v4.LiquidParser.If_tagContext
import liquid.parser.v4.LiquidParser.Include_tagContext
import liquid.parser.v4.LiquidParser.Lookup_IdContext
import liquid.parser.v4.LiquidParser.Lookup_StrContext
import liquid.parser.v4.LiquidParser.Lookup_id_indexesContext
import liquid.parser.v4.LiquidParser.Lt
import liquid.parser.v4.LiquidParser.LtEq
import liquid.parser.v4.LiquidParser.NEq
import liquid.parser.v4.LiquidParser.Or
import liquid.parser.v4.LiquidParser.Other_tagContext
import liquid.parser.v4.LiquidParser.Other_tag_blockContext
import liquid.parser.v4.LiquidParser.OutputContext
import liquid.parser.v4.LiquidParser.Param_expr_exprContext
import liquid.parser.v4.LiquidParser.Param_expr_key_valueContext
import liquid.parser.v4.LiquidParser.ParseContext
import liquid.parser.v4.LiquidParser.Raw_tagContext
import liquid.parser.v4.LiquidParser.Table_tagContext
import liquid.parser.v4.LiquidParser.Term_DoubleNumContext
import liquid.parser.v4.LiquidParser.Term_EmptyContext
import liquid.parser.v4.LiquidParser.Term_FalseContext
import liquid.parser.v4.LiquidParser.Term_LongNumContext
import liquid.parser.v4.LiquidParser.Term_NilContext
import liquid.parser.v4.LiquidParser.Term_StrContext
import liquid.parser.v4.LiquidParser.Term_TrueContext
import liquid.parser.v4.LiquidParser.Term_exprContext
import liquid.parser.v4.LiquidParser.Term_lookupContext
import liquid.parser.v4.LiquidParser.Unless_tagContext
import liquid.parser.v4.LiquidParserBaseVisitor
import org.antlr.v4.runtime.tree.TerminalNode
import java.util.*

class NodeVisitor(val tags: Tags,
                  val filters: Filters) : LiquidParserBaseVisitor<LNode>() {

  var isRootBlock = true
    set(isRootBlock) {
      field = this.isRootBlock
    }

  // parse
  //  : block EOF
  //  ;
  override fun visitParse(ctx: ParseContext): BlockNode {
    return visitBlock(ctx.block())
  }

  // block
  //  : atom*
  //  ;
  override fun visitBlock(ctx: BlockContext): BlockNode {

    var node = BlockNode(ArrayList())

    for (child in ctx.atom()) {
      node = node.withChildNode(visit(child))
    }

    return node
  }

  // atom
  // : ...
  // | other      #atom_others
  // ;
  override fun visitAtom_others(ctx: Atom_othersContext): LNode {
    return AtomNode(ctx.text)
  }

  // custom_tag
  //  : tagStart Id custom_tag_parameters? TagEnd custom_tag_block?
  //  ;
  //
  // custom_tag_parameters
  //  : other_than_tag_end
  //  ;
  //
  // other_than_tag_end
  //  : ~TagEnd+
  //  ;
  override fun visitOther_tag(ctx: Other_tagContext): LNode {

    val expressions = ArrayList<LNode>()

    if (ctx.other_tag_parameters() != null) {
      expressions.add(AtomNode(ctx.other_tag_parameters().text))
    }

    if (ctx.other_tag_block() != null) {
      expressions.add(visitOther_tag_block(ctx.other_tag_block()))
    }

    return TagNode(tags[ctx.Id().text], expressions)
  }

  // custom_tag_block
  //  : atom+? tagStart EndId TagEnd
  //  ;
  override fun visitOther_tag_block(ctx: Other_tag_blockContext): BlockNode {

    var node = BlockNode(ArrayList())

    for (child in ctx.atom()) {
      node = node.withChildNode(visit(child))
    }

    return node
  }

  // raw_tag
  //  : tagStart RawStart raw_body RawEnd TagEnd
  //  ;
  override fun visitRaw_tag(ctx: Raw_tagContext): LNode {
    return TagNode(tags["raw"], AtomNode(ctx.raw_body().text))
  }

  // comment_tag
  //  : tagStart CommentStart TagEnd comment_body tagStart CommentEnd TagEnd
  //  ;
  override fun visitComment_tag(ctx: Comment_tagContext): LNode {
    return TagNode(tags["comment"], AtomNode(ctx.comment_body().text))
  }

  // if_tag
  //  : tagStart IfStart expr TagEnd block elsif_tag* else_tag? tagStart IfEnd TagEnd
  //  ;
  //
  // elsif_tag
  //  : tagStart Elsif expr TagEnd block
  //  ;
  //
  // else_tag
  //  : tagStart Else TagEnd block
  //  ;
  override fun visitIf_tag(ctx: If_tagContext): LNode {

    val nodes = ArrayList<LNode>()

    // if
    nodes.add(visit(ctx.expr()))
    nodes.add(visitBlock(ctx.block()))

    // elsif
    for (elseIf in ctx.elsif_tag()) {
      nodes.add(visit(elseIf.expr()))
      nodes.add(visitBlock(elseIf.block()))
    }

    // else
    if (ctx.else_tag() != null) {
      nodes.add(AtomNode("TRUE"))
      nodes.add(visitBlock(ctx.else_tag().block()))
    }

    return TagNode(tags["if"], nodes)
  }

  // unless_tag
  //  : tagStart UnlessStart expr TagEnd block else_tag? tagStart UnlessEnd TagEnd
  //  ;
  override fun visitUnless_tag(ctx: Unless_tagContext): LNode {
    val nodes = ArrayList<LNode>()

    // unless
    nodes.add(visit(ctx.expr()))
    nodes.add(visitBlock(ctx.block()))

    // else
    if (ctx.else_tag() != null) {
      nodes.add(AtomNode(null))
      nodes.add(visitBlock(ctx.else_tag().block()))
    }

    return TagNode(tags["unless"], nodes)
  }

  // case_tag
  //  : tagStart CaseStart expr TagEnd other? when_tag+ else_tag? tagStart CaseEnd TagEnd
  //  ;
  //
  // when_tag
  //  : tagStart When term ((Or | Comma) term)* TagEnd block
  //  ;
  override fun visitCase_tag(ctx: Case_tagContext): LNode {

    val nodes = ArrayList<LNode>()
    nodes.add(visit(ctx.expr()))

    // when
    for (child in ctx.when_tag()) {

      for (grandChild in child.term()) {
        nodes.add(visit(grandChild))
      }

      nodes.add(visitBlock(child.block()))
    }

    // else
    if (ctx.else_tag() != null) {
      nodes.add(nodes[0])
      nodes.add(visitBlock(ctx.else_tag().block()))
    }

    return TagNode(tags["case"], nodes)
  }

  // cycle_tag
  //  : tagStart Cycle cycle_group expr (Comma expr)* TagEnd
  //  ;
  //
  // cycle_group
  //  : (expr Col)?
  //  ;
  override fun visitCycle_tag(ctx: Cycle_tagContext): LNode {

    val nodes = ArrayList<LNode>()

    nodes.add(if (ctx.cycle_group().expr() == null) AtomNode(null) else visit(ctx.cycle_group().expr()))

    for (child in ctx.expr()) {
      nodes.add(visit(child))
    }

    return TagNode(tags["cycle"], nodes)
  }

  // for_array
  //  : tagStart ForStart Id In lookup attribute* TagEnd
  //    for_block
  //    tagStart ForEnd TagEnd
  //  ;
  //
  // for_block
  //  : a=block (tagStart Else TagEnd b=block)?
  //  ;
  override fun visitFor_array(ctx: For_arrayContext): LNode {

    val expressions = mutableListOf<LNode>()
    expressions.add(AtomNode(true))

    expressions.add(AtomNode(ctx.Id().text))
    expressions.add(visit(ctx.lookup()))

    expressions.add(visitBlock(ctx.for_block().a))
    expressions.add(if (ctx.for_block().Else() == null) EmptyNode else visitBlock(ctx.for_block().b))

    for (attribute in ctx.attribute()) {
      expressions.add(visit(attribute))
    }

    return TagNode(tags["for"], expressions)
  }

  // for_range
  //  : tagStart ForStart Id In OPar expr DotDot expr CPar attribute* TagEnd
  //    block
  //    tagStart ForEnd TagEnd
  //  ;
  override fun visitFor_range(ctx: For_rangeContext): LNode {

    val expressions = ArrayList<LNode>()
    expressions.add(AtomNode(false))

    expressions.add(AtomNode(ctx.Id().text))
    expressions.add(visit(ctx.from))
    expressions.add(visit(ctx.to))

    expressions.add(visitBlock(ctx.block()))

    for (attribute in ctx.attribute()) {
      expressions.add(visit(attribute))
    }

    return TagNode(tags["for"], expressions)
  }

  // attribute
  //  : Id Col expr
  //  ;
  override fun visitAttribute(ctx: AttributeContext): LNode {
    return AttributeNode(AtomNode(ctx.Id().text), visit(ctx.expr()))
  }

  // table_tag
  //  : tagStart TableStart Id In lookup attribute* TagEnd block tagStart TableEnd TagEnd
  //  ;
  override fun visitTable_tag(ctx: Table_tagContext): LNode {

    val expressions = ArrayList<LNode>()

    expressions.add(AtomNode(ctx.Id().text))
    expressions.add(visit(ctx.lookup()))
    expressions.add(visitBlock(ctx.block()))

    for (attribute in ctx.attribute()) {
      expressions.add(visit(attribute))
    }

    return TagNode(tags["tablerow"], expressions)
  }

  // capture_tag
  //  : tagStart CaptureStart Id TagEnd block tagStart CaptureEnd TagEnd  #capture_tag_Id
  //  | ...
  //  ;
  override fun visitCapture_tag_Id(ctx: Capture_tag_IdContext): LNode {
    return TagNode(tags["capture"], AtomNode(ctx.Id().text), visitBlock(ctx.block()))
  }

  // capture_tag
  //  : ...
  //  | tagStart CaptureStart Str TagEnd block tagStart CaptureEnd TagEnd #capture_tag_Str
  //  ;
  override fun visitCapture_tag_Str(ctx: Capture_tag_StrContext): LNode {
    return TagNode(tags["capture"], fromString(ctx.Str()), visitBlock(ctx.block()))
  }

  // include_tag
  //  : tagStart Include file_name_or_output (With Str)? TagEnd
  //  ;
  override fun visitInclude_tag(ctx: Include_tagContext): LNode {
    return if (ctx.Str() != null) {
      TagNode(tags["include"], visit(ctx.file_name_or_output()), fromString(ctx.Str()))
    } else TagNode(tags["include"], visit(ctx.file_name_or_output()))
  }

  // file_name_or_output
  //  : Str                          #file_name_or_output_Str
  //  | ...
  //  ;
  override fun visitFile_name_or_output_Str(ctx: File_name_or_output_StrContext): LNode {
    return fromString(ctx.Str())
  }

  // file_name_or_output
  //  : ...
  //  | output                       #file_name_or_output_output
  //  | ...
  //  ;
  override fun visitFile_name_or_output_output(ctx: File_name_or_output_outputContext): LNode {

    //todo:ericm Resolve
    //    if (flavor != Flavor.JEKYLL)
    //      throw new LiquidException("`{% include ouput %}` can only be used for Flavor.JEKYLL", ctx);

    return visitOutput(ctx.output())
  }

  // file_name_or_output
  //  : ...
  //  | other_than_tag_end_out_start #file_name_or_output_other_than_tag_end_out_start
  //  ;
  //
  // other_than_tag_end_out_start
  //  : ~(TagEnd | OutStart | OutStart2)+
  //  ;
  override fun visitFile_name_or_output_other_than_tag_end_out_start(ctx: File_name_or_output_other_than_tag_end_out_startContext): LNode {

    //todo:ericm Resolve
    //    if (flavor != Flavor.JEKYLL)
    //      throw new LiquidException("`{% include other_than_tag_end_out_start %}` can only be used for Flavor.JEKYLL", ctx);

    return AtomNode(ctx.other_than_tag_end_out_start().text)
  }

  // output
  //  : outStart expr filter* OutEnd
  //  ;
  override fun visitOutput(ctx: OutputContext): OutputNode {
    val filters = ctx.filter().mapNotNull { visitFilter(it) }
    return OutputNode(visit(ctx.expr()), filters)
  }

  // filter
  //  : Pipe Id params?
  //  ;
  //
  // params
  //  : Col param_expr (Comma param_expr)*
  //  ;
  override fun visitFilter(ctx: FilterContext): FilterNode {
    val filterName = ctx.Id().text
    val filter = filters.getOrNull(filterName)
        ?: throw IllegalArgumentException("error on line ${ctx.start.line}, " +
            "index ${ctx.start.charPositionInLine}: " +
            "no filter available named: ${ctx.text}")
    val params = ctx.params()?.param_expr()
        ?.map { visit(it) }
    return FilterNode(filter, params ?: emptyList())
  }

  // param_expr
  //  : id2 Col expr #param_expr_key_value
  //  | ...
  //  ;
  override fun visitParam_expr_key_value(ctx: Param_expr_key_valueContext): LNode {
    return KeyValueNode(ctx.id2().text, visit(ctx.expr()))
  }

  // param_expr
  //  : ...
  //  | expr         #param_expr_expr
  //  ;
  override fun visitParam_expr_expr(ctx: Param_expr_exprContext): LNode {
    return visit(ctx.expr())
  }

  // assignment
  //  : tagStart Assign Id EqSign expr filter* TagEnd
  //  ;
  override fun visitAssignment(ctx: AssignmentContext): LNode {

    val idNode = AtomNode(ctx.Id().text)
    val exprNode = visit(ctx.expr())
    val allNodes = ArrayList<LNode>()

    allNodes.add(idNode)
    allNodes.add(exprNode)

    for (filterContext in ctx.filter()) {
      allNodes.add(visit(filterContext))
    }

    return TagNode(tags["assign"], allNodes)
  }

  // expr
  //  : expr op=(LtEq | Lt | GtEq | Gt) expr #expr_rel
  //  | ...
  //  ;
  override fun visitExpr_rel(ctx: Expr_relContext): LNode {
    when (ctx.op.type) {
      LtEq -> return LtEqNode(visit(ctx.lhs), visit(ctx.rhs))
      Lt -> return LtNode(visit(ctx.lhs), visit(ctx.rhs))
      GtEq -> return GtEqNode(visit(ctx.lhs), visit(ctx.rhs))
      Gt -> return GtNode(visit(ctx.lhs), visit(ctx.rhs))
      else -> throw RuntimeException("unknown operator: " + ctx.op.text)
    }
  }

  // expr
  //  : ...
  //  | expr Contains expr                   #expr_contains
  //  | ...
  //  ;
  override fun visitExpr_contains(ctx: Expr_containsContext): LNode {
    return ContainsNode(visit(ctx.lhs), visit(ctx.rhs))
  }

  // expr
  //  : ...
  //  | expr op=(Eq | NEq) expr              #expr_eq
  //  | ...
  //  ;
  override fun visitExpr_eq(ctx: Expr_eqContext): LNode {
    when (ctx.op.type) {
      Eq -> return EqNode(visit(ctx.lhs), visit(ctx.rhs))
      NEq -> return NEqNode(visit(ctx.lhs), visit(ctx.rhs))
      else -> throw RuntimeException("unknown operator: " + ctx.op.text)
    }
  }

  // expr
  //  : ...
  //  | <assoc=right> expr op=(And | Or) expr #expr_logic
  //  | ...
  //  ;
  override fun visitExpr_logic(ctx: Expr_logicContext): LNode {
    when (ctx.op.type) {
      And -> return AndNode(visit(ctx.lhs), visit(ctx.rhs))
      Or -> return OrNode(visit(ctx.lhs), visit(ctx.rhs))
      else -> throw RuntimeException("unknown operator: " + ctx.op.text)
    }
  }

  // expr
  //  : ...
  //  | term                                 #expr_term
  //  ;
  override fun visitExpr_term(ctx: Expr_termContext): LNode {
    return visit(ctx.term())
  }

  // term
  //  : DoubleNum      #term_DoubleNum
  //  | ...
  //  ;
  override fun visitTerm_DoubleNum(ctx: Term_DoubleNumContext): LNode {
    return AtomNode(ctx.DoubleNum().text.toDouble())
  }

  // term
  //  : ...
  //  | LongNum        #term_LongNum
  //  | ...
  //  ;
  override fun visitTerm_LongNum(ctx: Term_LongNumContext): LNode {
    return AtomNode(ctx.LongNum().text.toLong())
  }

  // term
  //  : ...
  //  | Str            #term_Str
  //  | ...
  //  ;
  override fun visitTerm_Str(ctx: Term_StrContext): LNode {
    return fromString(ctx.Str())
  }

  // term
  //  : ...
  //  | True           #term_True
  //  | ...
  //  ;
  override fun visitTerm_True(ctx: Term_TrueContext): LNode {
    return AtomNode(true)
  }

  // term
  //  : ...
  //  | False          #term_False
  //  | ...
  //  ;
  override fun visitTerm_False(ctx: Term_FalseContext): LNode {
    return AtomNode(false)
  }

  // term
  //  : ...
  //  | Nil            #term_Nil
  //  | ...
  //  ;
  override fun visitTerm_Nil(ctx: Term_NilContext): LNode {
    return AtomNode(null)
  }

  // term
  //  : ...
  //  | lookup         #term_lookup
  //  | ...
  //  ;
  override fun visitTerm_lookup(ctx: Term_lookupContext): LNode {
    return visit(ctx.lookup())
  }

  // term
  //  : ...
  //  | Empty          #term_Empty
  //  | ...
  //  ;
  override fun visitTerm_Empty(ctx: Term_EmptyContext): LNode {
    return EmptyNode
  }

  // term
  //  : ...
  //  | OPar expr CPar #term_expr
  //  ;
  override fun visitTerm_expr(ctx: Term_exprContext): LNode {
    return visit(ctx.expr())
  }

  // lookup
  //  : id index* QMark?   #lookup_id_indexes
  //  | ...
  //  ;
  //
  // index
  //  : Dot id2
  //  | OBr expr CBr
  //  ;
  override fun visitLookup_id_indexes(ctx: Lookup_id_indexesContext): LookupNode {
    val list = ctx.index().mapNotNull {
      if (it.Dot() != null) {
        Property(it.id2().text)
      } else {
        Index(visit(it.expr()))
      }
    }

    return LookupNode(ctx.id().text, list)
  }

  // lookup
  //  : ...
  //  | OBr Str CBr QMark? #lookup_Str
  //  | ...
  //  ;
  override fun visitLookup_Str(ctx: Lookup_StrContext): LookupNode {
    return LookupNode(strip(ctx.Str().text))
  }

  // lookup
  //  : ...
  //  | OBr Id CBr QMark?  #lookup_Id
  //  ;
  override fun visitLookup_Id(ctx: Lookup_IdContext): LookupNode {
    return LookupNode("@" + ctx.Id().text)
  }

  private fun fromString(str: TerminalNode): AtomNode {
    return AtomNode(strip(str.text))
  }

  private fun strip(str: String): String {
    return str.substring(1, str.length - 1)
  }
}
