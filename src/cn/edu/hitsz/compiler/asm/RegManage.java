package cn.edu.hitsz.compiler.asm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegManage {
    private static List<Reg> RegList = Arrays.asList(new Reg("t0",false),
            new Reg("t1",false),
            new Reg("t2",false),
            new Reg("t3",false),
            new Reg("t4",false),
            new Reg("t5",false),
            new Reg("t6",false));

    public static Reg a0 = new Reg("a0",false);

    public static Reg getFreeReg(){
        for(Reg reg: RegList){
            if(!reg.getValid()){
                reg.switchValid();
                return reg;
            }
        }
        throw new RuntimeException();
    }

    public static void freeReg(String name){
        for(Reg reg:RegList){
            if(reg.getName().equals(name))reg.switchValid();
        }
    }
}

