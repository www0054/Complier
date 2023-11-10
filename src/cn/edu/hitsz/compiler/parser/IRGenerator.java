package cn.edu.hitsz.compiler.parser;

import cn.edu.hitsz.compiler.NotImplementedException;
import cn.edu.hitsz.compiler.ir.IRImmediate;
import cn.edu.hitsz.compiler.ir.IRValue;
import cn.edu.hitsz.compiler.ir.IRVariable;
import cn.edu.hitsz.compiler.ir.Instruction;
import cn.edu.hitsz.compiler.lexer.Token;
import cn.edu.hitsz.compiler.parser.table.Production;
import cn.edu.hitsz.compiler.parser.table.Status;
import cn.edu.hitsz.compiler.symtab.SymbolTable;
import cn.edu.hitsz.compiler.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// TODO: 实验三: 实现 IR 生成

/**
 *
 */
public class IRGenerator implements ActionObserver {

    SymbolTable table;
    List<IRValue> Stack = new ArrayList<>();
    List<Instruction> IRList = new ArrayList<>();

    IRVariable temp;

    @Override
    public void whenShift(Status currentStatus, Token currentToken) {
        if(currentToken.getKind().getIdentifier().equals("id")){
            Stack.add(IRVariable.named(currentToken.getText()));
        }
        else if(currentToken.getKind().getIdentifier().equals("IntConst")){
            Stack.add(IRImmediate.of(Integer.parseInt(currentToken.getText())));
        }
    }

    @Override
    public void whenReduce(Status currentStatus, Production production) {
        switch (production.index()){
            case 6:
                IRList.add(Instruction.createMov((IRVariable) Stack.get(Stack.size()-2),Stack.get(Stack.size()-1)));
                Stack.remove(Stack.size()-1);
                Stack.remove(Stack.size()-1);
                break;
            case 7:
                IRList.add(Instruction.createRet(Stack.get(Stack.size()-1)));
                Stack.remove(Stack.size()-1);
                break;
            case 8:
                temp = IRVariable.temp();
                IRList.add(Instruction.createAdd(temp, Stack.get(Stack.size()-2),Stack.get(Stack.size()-1)));
                Stack.remove(Stack.size()-1);
                Stack.remove(Stack.size()-1);
                Stack.add(temp);
                break;
            case 9:
                temp = IRVariable.temp();
                IRList.add(Instruction.createSub(temp, Stack.get(Stack.size()-2),Stack.get(Stack.size()-1)));
                Stack.remove(Stack.size()-1);
                Stack.remove(Stack.size()-1);
                Stack.add(temp);
                break;
            case 11:
                temp = IRVariable.temp();
                IRList.add(Instruction.createMul(temp, Stack.get(Stack.size()-2),Stack.get(Stack.size()-1)));
                Stack.remove(Stack.size()-1);
                Stack.remove(Stack.size()-1);
                Stack.add(temp);
                break;
        }
    }


    @Override
    public void whenAccept(Status currentStatus) {
        Stack.clear();
    }

    @Override
    public void setSymbolTable(SymbolTable table) {
        this.table = table;
    }

    public List<Instruction> getIR() {
        return IRList;
    }

    public void dumpIR(String path) {
        FileUtils.writeLines(path, getIR().stream().map(Instruction::toString).toList());
    }
}

