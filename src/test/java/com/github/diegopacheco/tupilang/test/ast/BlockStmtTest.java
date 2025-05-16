package com.github.diegopacheco.tupilang.test.ast;

import com.github.diegopacheco.tupilang.ast.BlockStmt;
import com.github.diegopacheco.tupilang.ast.ExpressionStatement;
import com.github.diegopacheco.tupilang.ast.Stmt;
import com.github.diegopacheco.tupilang.ast.VariableExpr;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class BlockStmtTest {

    private BlockStmtTest(){}

    @Test
    public void testCreateEmptyBlock() {
        List<Stmt> emptyStatements = new ArrayList<>();
        BlockStmt block = new BlockStmt(emptyStatements);

        assertNotNull(block);
        assertEquals(0, block.getStatements().size());
        assertEquals("{\n}", block.toString());
    }

    @Test
    public void testCreateBlockWithStatements() {
        ExpressionStatement stmt1 = new ExpressionStatement(new VariableExpr("x"));
        ExpressionStatement stmt2 = new ExpressionStatement(new VariableExpr("y"));
        List<Stmt> statements = Arrays.asList(stmt1, stmt2);

        BlockStmt block = new BlockStmt(statements);

        assertNotNull(block);
        assertEquals(2, block.getStatements().size());
        assertSame(stmt1, block.getStatements().get(0));
        assertSame(stmt2, block.getStatements().get(1));
    }

    @Test
    public void testNestedBlocks() {
        // Create an inner block
        ExpressionStatement innerStmt = new ExpressionStatement(new VariableExpr("inner"));
        BlockStmt innerBlock = new BlockStmt(List.of(innerStmt));

        // Create an outer block containing the inner block
        ExpressionStatement outerStmt = new ExpressionStatement(new VariableExpr("outer"));
        BlockStmt outerBlock = new BlockStmt(Arrays.asList(outerStmt, innerBlock));

        assertEquals(2, outerBlock.getStatements().size());
        assertInstanceOf(BlockStmt.class, outerBlock.getStatements().get(1));

        // Verify structure is preserved
        BlockStmt nestedBlock = (BlockStmt) outerBlock.getStatements().get(1);
        assertEquals(1, nestedBlock.getStatements().size());
        ExpressionStatement nestedStmt = (ExpressionStatement) nestedBlock.getStatements().get(0);
        assertEquals("inner", ((VariableExpr) nestedStmt.getExpression()).getName());
    }
}