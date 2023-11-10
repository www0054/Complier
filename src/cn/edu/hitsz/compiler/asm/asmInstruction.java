package cn.edu.hitsz.compiler.asm;

import cn.edu.hitsz.compiler.ir.IRImmediate;
import cn.edu.hitsz.compiler.ir.IRVariable;

public class asmInstruction {
    public static String li(Reg a, IRImmediate b){
        return String.format("li %s, %d", a.getName(), b.getValue());
    }

    public static String sub(Reg a, Reg b, Reg c){
        return String.format("sub %s, %s, %s",a.getName(),b.getName(), c.getName());
    }

    public static String mv(Reg a, Reg b){
        return String.format("mv %s, %s",a.getName(),b.getName());
    }

    public static String mul(Reg a, Reg b, Reg c){
        return String.format("mul %s, %s, %s",a.getName(),b.getName(), c.getName());
    }

    public static String addi(Reg a, Reg b, IRImmediate c){
        return String.format("addi %s, %s, %d",a.getName(),b.getName(), c.getValue());
    }
}
