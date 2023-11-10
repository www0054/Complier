package cn.edu.hitsz.compiler.parser;

import cn.edu.hitsz.compiler.NotImplementedException;
import cn.edu.hitsz.compiler.lexer.Token;
import cn.edu.hitsz.compiler.parser.table.Production;
import cn.edu.hitsz.compiler.parser.table.Status;
import cn.edu.hitsz.compiler.symtab.SourceCodeType;
import cn.edu.hitsz.compiler.symtab.SymbolTable;

import java.nio.channels.AcceptPendingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// TODO: 实验三: 实现语义分析
public class SemanticAnalyzer implements ActionObserver {
    SymbolTable table;
    List<Object> Stack = new ArrayList<>();

    @Override
    public void whenAccept(Status currentStatus) {

    }

    @Override
    public void whenReduce(Status currentStatus, Production production) {
//        throw new NotImplementedException();
        switch (production.index()){
            case 4:
                if(table.has(((Token)Stack.get(Stack.size()-1)).getText())){
                    table.get(((Token)Stack.get(Stack.size()-1)).getText()).setType(SourceCodeType.Int);
                }else throw new RuntimeException();
                Stack.remove(Stack.size()-1);
                Stack.remove(Stack.size()-1);
                break;
        }
    }

    @Override
    public void whenShift(Status currentStatus, Token currentToken) {
        Stack.add(currentToken);
    }

    @Override
    public void setSymbolTable(SymbolTable table) {
        // TODO: 设计你可能需要的符号表存储结构
        // 如果需要使用符号表的话, 可以将它或者它的一部分信息存起来, 比如使用一个成员变量存储
        this.table = table;
    }
}

