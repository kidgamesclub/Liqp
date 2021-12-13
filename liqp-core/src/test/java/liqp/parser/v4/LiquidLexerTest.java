package liqp.parser.v4;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.List;
import liquid.parser.v4.LiquidLexer;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

public class LiquidLexerTest {

    // OutStart
    //  : ( {stripSpacesAroundTags}? WhitespaceChar* '{{'
    //    | WhitespaceChar* '{{-'
    //    | '{{'
    //    ) -> pushMode(IN_TAG)
    //  ;
    @Test
    public void testOutStart() {

        MatcherAssert.assertThat(singleToken("{{").getType(), is(LiquidLexer.OutStart));
        MatcherAssert.assertThat(singleToken("{{-").getType(), is(LiquidLexer.OutStart));

        // Leading space
        boolean stripSpacesAroundTags = false;
        MatcherAssert.assertThat(tokenise(" {{", stripSpacesAroundTags, true).get(0).getType(), is(LiquidLexer.Other));
        MatcherAssert.assertThat(tokenise(" {{", stripSpacesAroundTags, true).get(1).getType(), is(LiquidLexer.OutStart));
        stripSpacesAroundTags = true;
        MatcherAssert.assertThat(tokenise(" {{", stripSpacesAroundTags, true).get(0).getType(), is(LiquidLexer.OutStart));
    }

    // TagStart
    //  : ( {stripSpacesAroundTags}? WhitespaceChar* '{%'
    //    | WhitespaceChar* '{%-'
    //    | '{%'
    //    ) -> pushMode(IN_TAG)
    //  ;
    @Test
    public void testTagStart() {

        MatcherAssert.assertThat(singleToken("{%").getType(), is(LiquidLexer.TagStart));
        MatcherAssert.assertThat(singleToken("{%-").getType(), is(LiquidLexer.TagStart));

        // Leading space
        boolean stripSpacesAroundTags = false;
        MatcherAssert.assertThat(tokenise(" {%", stripSpacesAroundTags, true).get(0).getType(), is(LiquidLexer.Other));
        MatcherAssert.assertThat(tokenise(" {%", stripSpacesAroundTags, true).get(1).getType(), is(LiquidLexer.TagStart));
        stripSpacesAroundTags = true;
        MatcherAssert.assertThat(tokenise(" {%", stripSpacesAroundTags, true).get(0).getType(), is(LiquidLexer.TagStart));
    }

    // Other
    //  : .
    //  ;
    @Test
    public void testNoSpace() {
        MatcherAssert.assertThat(singleToken("x").getType(), is(LiquidLexer.Other));
        MatcherAssert.assertThat(singleToken("{").getType(), is(LiquidLexer.Other));
        MatcherAssert.assertThat(singleToken("?").getType(), is(LiquidLexer.Other));
        MatcherAssert.assertThat(singleToken(" ").getType(), is(LiquidLexer.Other));
        MatcherAssert.assertThat(singleToken("\t").getType(), is(LiquidLexer.Other));
        MatcherAssert.assertThat(singleToken("\r").getType(), is(LiquidLexer.Other));
        MatcherAssert.assertThat(singleToken("\n").getType(), is(LiquidLexer.Other));
    }

    // mode IN_TAG;
    //
    //   OutStart2 : '{{' -> pushMode(IN_TAG);
    @Test
    public void testOutStart2() {
        MatcherAssert.assertThat(tokenise("{%{{").get(1).getType(), is(LiquidLexer.OutStart2));
        MatcherAssert.assertThat(tokenise("{{{{").get(1).getType(), is(LiquidLexer.OutStart2));
    }

    //   TagStart2 : '{%' -> pushMode(IN_TAG);
    @Test
    public void testTagStart2() {
        MatcherAssert.assertThat(tokenise("{%{%").get(1).getType(), is(LiquidLexer.TagStart2));
        MatcherAssert.assertThat(tokenise("{{{%").get(1).getType(), is(LiquidLexer.TagStart2));
    }

    //   OutEnd
    //    : ( {stripSpacesAroundTags}? '}}' WhitespaceChar*
    //      | '-}}' WhitespaceChar*
    //      | '}}'
    //      ) -> popMode
    //    ;
    @Test
    public void testOutEnd() {

        MatcherAssert.assertThat(tokenise("{%}}").get(1).getType(), is(LiquidLexer.OutEnd));
        MatcherAssert.assertThat(tokenise("{{}}").get(1).getType(), is(LiquidLexer.OutEnd));

        MatcherAssert.assertThat(tokenise("{%--}}").get(1).getType(), is(LiquidLexer.OutEnd));
        MatcherAssert.assertThat(tokenise("{{--}}").get(1).getType(), is(LiquidLexer.OutEnd));

        // Trailing spaces

        boolean stripSpacesAroundTags = false;
        Token token = tokenise("{{}} ", stripSpacesAroundTags, true).get(2);
        MatcherAssert.assertThat(token.getType(), is(LiquidLexer.Other));
        MatcherAssert.assertThat(token.getText(), is(" "));

        stripSpacesAroundTags = true;
        token = tokenise("{{}} ", stripSpacesAroundTags, true).get(1);
        MatcherAssert.assertThat(token.getType(), is(LiquidLexer.OutEnd));
        MatcherAssert.assertThat(token.getText(), is("}} "));
    }

    //   TagEnd
    //    : ( {stripSpacesAroundTags}? '%}' WhitespaceChar*
    //      | '-%}' WhitespaceChar*
    //      | '%}'
    //      ) -> popMode
    //    ;
    @Test
    public void testTagEnd() {

        MatcherAssert.assertThat(tokenise("{%%}").get(1).getType(), is(LiquidLexer.TagEnd));
        MatcherAssert.assertThat(tokenise("{{%}").get(1).getType(), is(LiquidLexer.TagEnd));

        MatcherAssert.assertThat(tokenise("{%--%}").get(1).getType(), is(LiquidLexer.TagEnd));
        MatcherAssert.assertThat(tokenise("{{--%}").get(1).getType(), is(LiquidLexer.TagEnd));

        // Trailing spaces

        boolean stripSpacesAroundTags = false;
        Token token = tokenise("{{%} ", stripSpacesAroundTags, true).get(2);
        MatcherAssert.assertThat(token.getType(), is(LiquidLexer.Other));
        MatcherAssert.assertThat(token.getText(), is(" "));

        stripSpacesAroundTags = true;
        token = tokenise("{{%} ", stripSpacesAroundTags, true).get(1);
        MatcherAssert.assertThat(token.getType(), is(LiquidLexer.TagEnd));
        MatcherAssert.assertThat(token.getText(), is("%} "));
    }

    //   Str : SStr | DStr;
    @Test
    public void testStr() {
        MatcherAssert.assertThat(tokenise("{{'dasdasdas'").get(1).getType(), is(LiquidLexer.Str));
        MatcherAssert.assertThat(tokenise("{{\"\n\"").get(1).getType(), is(LiquidLexer.Str));
    }

    //   DotDot    : '..';
    @Test
    public void testDotDot() {
        MatcherAssert.assertThat(tokenise("{{..").get(1).getType(), is(LiquidLexer.DotDot));
        MatcherAssert.assertThat(tokenise("{{1..9").get(2).getType(), is(LiquidLexer.DotDot));
    }

    //   Dot       : '.';
    @Test
    public void testDot() {
        MatcherAssert.assertThat(tokenise("{{.").get(1).getType(), is(LiquidLexer.Dot));
    }

    //   NEq       : '!=' | '<>';
    @Test
    public void testNEq() {
        MatcherAssert.assertThat(tokenise("{{!=").get(1).getType(), is(LiquidLexer.NEq));
    }

    //   Eq        : '==';
    @Test
    public void testEq() {
        MatcherAssert.assertThat(tokenise("{{==").get(1).getType(), is(LiquidLexer.Eq));
    }

    //   EqSign    : '=';
    @Test
    public void testEqSign() {
        MatcherAssert.assertThat(tokenise("{{=").get(1).getType(), is(LiquidLexer.EqSign));
    }

    //   GtEq      : '>=';
    @Test
    public void testGtEq() {
        MatcherAssert.assertThat(tokenise("{{>=").get(1).getType(), is(LiquidLexer.GtEq));
    }

    //   Gt        : '>';
    @Test
    public void testGt() {
        MatcherAssert.assertThat(tokenise("{{>").get(1).getType(), is(LiquidLexer.Gt));
    }

    //   LtEq      : '<=';
    @Test
    public void testLtEq() {
        MatcherAssert.assertThat(tokenise("{{<=").get(1).getType(), is(LiquidLexer.LtEq));
    }

    //   Lt        : '<';
    @Test
    public void testLt() {
        MatcherAssert.assertThat(tokenise("{{<").get(1).getType(), is(LiquidLexer.Lt));
    }

    //   Minus     : '-';
    @Test
    public void testMinus() {
        MatcherAssert.assertThat(tokenise("{{ -").get(2).getType(), is(LiquidLexer.Minus));
    }

    //   Pipe      : '|';
    @Test
    public void testPipe() {
        MatcherAssert.assertThat(tokenise("{{|").get(1).getType(), is(LiquidLexer.Pipe));
    }

    //   Col       : ':';
    @Test
    public void testCol() {
        MatcherAssert.assertThat(tokenise("{{:").get(1).getType(), is(LiquidLexer.Col));
    }

    //   Comma     : ',';
    @Test
    public void testComma() {
        MatcherAssert.assertThat(tokenise("{{,").get(1).getType(), is(LiquidLexer.Comma));
    }

    //   OPar      : '(';
    @Test
    public void testOPar() {
        MatcherAssert.assertThat(tokenise("{{(").get(1).getType(), is(LiquidLexer.OPar));
    }

    //   CPar      : ')';
    @Test
    public void testCPar() {
        MatcherAssert.assertThat(tokenise("{{)").get(1).getType(), is(LiquidLexer.CPar));
    }

    //   OBr       : '[';
    @Test
    public void testOBr() {
        MatcherAssert.assertThat(tokenise("{{[").get(1).getType(), is(LiquidLexer.OBr));
    }

    //   CBr       : ']';
    @Test
    public void testCBr() {
        MatcherAssert.assertThat(tokenise("{{]").get(1).getType(), is(LiquidLexer.CBr));
    }

    //   QMark     : '?';
    @Test
    public void testQMark() {
        MatcherAssert.assertThat(tokenise("{{?").get(1).getType(), is(LiquidLexer.QMark));
    }

    //   DoubleNum
    //    : '-'? Digit+ '.' Digit+
    //    | '-'? Digit+ '.' {_input.LA(1) != '.'}?
    //    ;
    @Test
    public void testDoubleNum() {

        MatcherAssert.assertThat(tokenise("{{1.").get(1).getType(), is(LiquidLexer.DoubleNum));
        MatcherAssert.assertThat(tokenise("{{123.45").get(1).getType(), is(LiquidLexer.DoubleNum));
        MatcherAssert.assertThat(tokenise("{{-1.").get(1).getType(), is(LiquidLexer.DoubleNum));
        MatcherAssert.assertThat(tokenise("{{-123.45").get(1).getType(), is(LiquidLexer.DoubleNum));

        // Not a DoubleNum!
        MatcherAssert.assertThat(tokenise("{{1..").get(1).getType(), not(is(LiquidLexer.DoubleNum)));
    }

    //   LongNum   : '-'? Digit+;
    @Test
    public void testLongNum() {
        MatcherAssert.assertThat(tokenise("{{1").get(1).getType(), is(LiquidLexer.LongNum));
        MatcherAssert.assertThat(tokenise("{{-123456789").get(1).getType(), is(LiquidLexer.LongNum));
    }

    //   CaptureStart : 'capture';
    @Test
    public void testCaptureStart() {
        MatcherAssert.assertThat(tokenise("{{capture").get(1).getType(), is(LiquidLexer.CaptureStart));
    }

    //   CaptureEnd   : 'endcapture';
    @Test
    public void testCaptureEnd() {
        MatcherAssert.assertThat(tokenise("{{endcapture").get(1).getType(), is(LiquidLexer.CaptureEnd));
    }

    //   CommentStart : 'comment';
    @Test
    public void testCommentStart() {
        MatcherAssert.assertThat(tokenise("{{comment").get(1).getType(), is(LiquidLexer.CommentStart));
    }

    //   CommentEnd   : 'endcomment';
    @Test
    public void testCommentEnd() {
        MatcherAssert.assertThat(tokenise("{{endcomment").get(1).getType(), is(LiquidLexer.CommentEnd));
    }

    //   RawStart     : 'raw' WhitespaceChar* '%}' -> pushMode(IN_RAW);
    @Test
    public void testRawStart() {
        MatcherAssert.assertThat(tokenise("{%raw%}").get(1).getType(), is(LiquidLexer.RawStart));
        MatcherAssert.assertThat(tokenise("{%raw  %}").get(1).getType(), is(LiquidLexer.RawStart));
    }

    //   IfStart      : 'if';
    @Test
    public void testIfStart() {
        MatcherAssert.assertThat(tokenise("{{if").get(1).getType(), is(LiquidLexer.IfStart));
    }

    //   Elsif        : 'elsif';
    @Test
    public void testElsif() {
        MatcherAssert.assertThat(tokenise("{{elsif").get(1).getType(), is(LiquidLexer.Elsif));
    }

    //   IfEnd        : 'endif';
    @Test
    public void testIfEnd() {
        MatcherAssert.assertThat(tokenise("{{endif").get(1).getType(), is(LiquidLexer.IfEnd));
    }

    //   UnlessStart  : 'unless';
    @Test
    public void testUnlessStart() {
        MatcherAssert.assertThat(tokenise("{{unless").get(1).getType(), is(LiquidLexer.UnlessStart));
    }

    //   UnlessEnd    : 'endunless';
    @Test
    public void testUnlessEnd() {
        MatcherAssert.assertThat(tokenise("{{endunless").get(1).getType(), is(LiquidLexer.UnlessEnd));
    }

    //   Else         : 'else';
    @Test
    public void testElse() {
        MatcherAssert.assertThat(tokenise("{{else").get(1).getType(), is(LiquidLexer.Else));
    }

    //   Contains     : 'contains';
    @Test
    public void testContains() {
        MatcherAssert.assertThat(tokenise("{{contains").get(1).getType(), is(LiquidLexer.Contains));
    }

    //   CaseStart    : 'case';
    @Test
    public void testCaseStart() {
        MatcherAssert.assertThat(tokenise("{{case").get(1).getType(), is(LiquidLexer.CaseStart));
    }

    //   CaseEnd      : 'endcase';
    @Test
    public void testCaseEnd() {
        MatcherAssert.assertThat(tokenise("{{endcase").get(1).getType(), is(LiquidLexer.CaseEnd));
    }

    //   When         : 'when';
    @Test
    public void testWhen() {
        MatcherAssert.assertThat(tokenise("{{when").get(1).getType(), is(LiquidLexer.When));
    }

    //   Cycle        : 'cycle';
    @Test
    public void testCycle() {
        MatcherAssert.assertThat(tokenise("{{cycle").get(1).getType(), is(LiquidLexer.Cycle));
    }

    //   ForStart     : 'for';
    @Test
    public void testForStart() {
        MatcherAssert.assertThat(tokenise("{{for").get(1).getType(), is(LiquidLexer.ForStart));
    }

    //   ForEnd       : 'endfor';
    @Test
    public void testForEnd() {
        MatcherAssert.assertThat(tokenise("{{endfor").get(1).getType(), is(LiquidLexer.ForEnd));
    }

    //   In           : 'in';
    @Test
    public void testIn() {
        MatcherAssert.assertThat(tokenise("{{in").get(1).getType(), is(LiquidLexer.In));
    }

    //   And          : 'and';
    @Test
    public void testAnd() {
        MatcherAssert.assertThat(tokenise("{{and").get(1).getType(), is(LiquidLexer.And));
    }

    //   Or           : 'or';
    @Test
    public void testOr() {
        MatcherAssert.assertThat(tokenise("{{or").get(1).getType(), is(LiquidLexer.Or));
    }

    //   TableStart   : 'tablerow';
    @Test
    public void testTableStart() {
        MatcherAssert.assertThat(tokenise("{{tablerow").get(1).getType(), is(LiquidLexer.TableStart));
    }

    //   TableEnd     : 'endtablerow';
    @Test
    public void testTableEnd() {
        MatcherAssert.assertThat(tokenise("{{endtablerow").get(1).getType(), is(LiquidLexer.TableEnd));
    }

    //   Assign       : 'assign';
    @Test
    public void testAssign() {
        MatcherAssert.assertThat(tokenise("{{assign").get(1).getType(), is(LiquidLexer.Assign));
    }

    //   True         : 'true';
    @Test
    public void testTrue() {
        MatcherAssert.assertThat(tokenise("{{true").get(1).getType(), is(LiquidLexer.True));
    }

    //   False        : 'false';
    @Test
    public void testFalse() {
        MatcherAssert.assertThat(tokenise("{{false").get(1).getType(), is(LiquidLexer.False));
    }

    //   Nil          : 'nil' | 'null';
    @Test
    public void testNil() {
        MatcherAssert.assertThat(tokenise("{{nil").get(1).getType(), is(LiquidLexer.Nil));
    }

    //   Include      : 'include';
    @Test
    public void testInclude() {
        MatcherAssert.assertThat(tokenise("{{include").get(1).getType(), is(LiquidLexer.Include));
    }

    //   With         : 'with';
    @Test
    public void testWith() {
        MatcherAssert.assertThat(tokenise("{{with").get(1).getType(), is(LiquidLexer.With));
    }

    //   Empty        : 'empty';
    @Test
    public void testEmpty() {
        MatcherAssert.assertThat(tokenise("{{empty").get(1).getType(), is(LiquidLexer.Empty));
    }

    //   EndId        : 'end' Id;
    @Test
    public void testEndId() {
        MatcherAssert.assertThat(tokenise("{{endfoo").get(1).getType(), is(LiquidLexer.EndId));
    }

    //   Id : ( Letter | '_' ) (Letter | '_' | '-' | Digit)*;
    @Test
    public void testId() {
        MatcherAssert.assertThat(tokenise("{{fubar").get(1).getType(), is(LiquidLexer.Id));
    }

    // mode IN_RAW;
    //
    //   RawEnd : '{%' WhitespaceChar* 'endraw' -> popMode;
    @Test
    public void testRawEnd() {
        MatcherAssert.assertThat(tokenise("{%raw%}{%endraw").get(2).getType(), is(LiquidLexer.RawEnd));
        MatcherAssert.assertThat(tokenise("{%raw%}{%    endraw").get(2).getType(), is(LiquidLexer.RawEnd));
    }

    //   OtherRaw : . ;
    @Test
    public void testOtherRaw() {
        MatcherAssert.assertThat(tokenise("{%raw%}?").get(2).getType(), is(LiquidLexer.OtherRaw));
    }

    private static Token singleToken(String source) {
        return singleToken(source, false, true);
    }

    private static Token singleToken(String source, boolean stripSpacesAroundTags, boolean discardEof) {
        List<Token> tokens = tokenise(source, stripSpacesAroundTags, discardEof);

        if (tokens.size() != 1) {
            throw new RuntimeException("expected 1 token in '" + source + "', found " + tokens.size() + ": " + tokens);
        }

        return tokens.get(0);
    }

    private static List<Token> tokenise(String source) {
        return tokenise(source, false, true);
    }

    private static List<Token> tokenise(String source, boolean stripSpacesAroundTags, boolean discardEof) {

        CommonTokenStream tokenStream = commonTokenStream(source, stripSpacesAroundTags);
        tokenStream.fill();
        List<Token> tokens = tokenStream.getTokens();

        if (discardEof) {
            tokens.remove(tokens.size() - 1);
        }

        return tokens;
    }

    static CommonTokenStream commonTokenStream(String source) {
        return commonTokenStream(source, false);
    }

    static CommonTokenStream commonTokenStream(String source, boolean stripSpacesAroundTags) {

        LiquidLexer lexer = new LiquidLexer(CharStreams.fromString(source), stripSpacesAroundTags);

        lexer.addErrorListener(new BaseErrorListener(){
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                throw new RuntimeException(e);
            }
        });

        return new CommonTokenStream(lexer);
    }
}
