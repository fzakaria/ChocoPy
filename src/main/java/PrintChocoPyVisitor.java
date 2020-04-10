import org.antlr.v4.runtime.ParserRuleContext;

public class PrintChocoPyVisitor extends ChocoPyParserBaseVisitor<Void> {

    @Override
    public Void visitProgram(ChocoPyParser.ProgramContext ctx) {
        return super.visitProgram(ctx);
    }

    @Override
    public Void visitClass_def(ChocoPyParser.Class_defContext ctx) {
        return super.visitClass_def(ctx);
    }

    @Override
    public Void visitClass_body(ChocoPyParser.Class_bodyContext ctx) {
        return super.visitClass_body(ctx);
    }

    @Override
    public Void visitFunc_def(ChocoPyParser.Func_defContext ctx) {
        return super.visitFunc_def(ctx);
    }

    @Override
    public Void visitFunc_body(ChocoPyParser.Func_bodyContext ctx) {
        return super.visitFunc_body(ctx);
    }

    @Override
    public Void visitTyped_var(ChocoPyParser.Typed_varContext ctx) {
        return super.visitTyped_var(ctx);
    }

    @Override
    public Void visitType(ChocoPyParser.TypeContext ctx) {
        return super.visitType(ctx);
    }

    @Override
    public Void visitGlobal_decl(ChocoPyParser.Global_declContext ctx) {
        return super.visitGlobal_decl(ctx);
    }

    @Override
    public Void visitNonlocal_decl(ChocoPyParser.Nonlocal_declContext ctx) {
        return super.visitNonlocal_decl(ctx);
    }

    @Override
    public Void visitVar_def(ChocoPyParser.Var_defContext ctx) {
        return super.visitVar_def(ctx);
    }

    @Override
    public Void visitStmt(ChocoPyParser.StmtContext ctx) {
        return super.visitStmt(ctx);
    }

    @Override
    public Void visitSimple_stmt(ChocoPyParser.Simple_stmtContext ctx) {
        return super.visitSimple_stmt(ctx);
    }

    @Override
    public Void visitBlock(ChocoPyParser.BlockContext ctx) {
        return super.visitBlock(ctx);
    }

    @Override
    public Void visitLiteral(ChocoPyParser.LiteralContext ctx) {
        return super.visitLiteral(ctx);
    }

    @Override
    public Void visitExpr(ChocoPyParser.ExprContext ctx) {
        return super.visitExpr(ctx);
    }

    @Override
    public Void visitCexpr(ChocoPyParser.CexprContext ctx) {
        return super.visitCexpr(ctx);
    }

    @Override
    public Void visitBin_op(ChocoPyParser.Bin_opContext ctx) {
        return super.visitBin_op(ctx);
    }

    @Override
    public Void visitMember_expr(ChocoPyParser.Member_exprContext ctx) {
        return super.visitMember_expr(ctx);
    }

    @Override
    public Void visitIndex_expr(ChocoPyParser.Index_exprContext ctx) {
        return super.visitIndex_expr(ctx);
    }

    @Override
    public Void visitTarget(ChocoPyParser.TargetContext ctx) {
        return super.visitTarget(ctx);
    }

    private String parenthesize(String name, ParserRuleContext... contents) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (ParserRuleContext context : contents) {
            builder.append(" ");
            builder.append(context.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }
}
