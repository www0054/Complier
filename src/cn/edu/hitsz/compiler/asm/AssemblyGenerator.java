package cn.edu.hitsz.compiler.asm;

import cn.edu.hitsz.compiler.NotImplementedException;
import cn.edu.hitsz.compiler.ir.IRImmediate;
import cn.edu.hitsz.compiler.ir.IRValue;
import cn.edu.hitsz.compiler.ir.IRVariable;
import cn.edu.hitsz.compiler.ir.Instruction;
import cn.edu.hitsz.compiler.utils.FileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * TODO: 实验四: 实现汇编生成
 * <br>
 * 在编译器的整体框架中, 代码生成可以称作后端, 而前面的所有工作都可称为前端.
 * <br>
 * 在前端完成的所有工作中, 都是与目标平台无关的, 而后端的工作为将前端生成的目标平台无关信息
 * 根据目标平台生成汇编代码. 前后端的分离有利于实现编译器面向不同平台生成汇编代码. 由于前后
 * 端分离的原因, 有可能前端生成的中间代码并不符合目标平台的汇编代码特点. 具体到本项目你可以
 * 尝试加入一个方法将中间代码调整为更接近 risc-v 汇编的形式, 这样会有利于汇编代码的生成.
 * <br>
 * 为保证实现上的自由, 框架中并未对后端提供基建, 在具体实现时可自行设计相关数据结构.
 *
 * @see AssemblyGenerator#run() 代码生成与寄存器分配
 */
public class AssemblyGenerator {

    List<Instruction> IR;
    Map<IRVariable, Integer> variableTimes = new HashMap<>();
    BMap<IRVariable, Reg> variableRegBMap = new BMap<>();

    List<String> asmList = new ArrayList<>();

    /**
     * 加载前端提供的中间代码
     * <br>
     * 视具体实现而定, 在加载中或加载后会生成一些在代码生成中会用到的信息. 如变量的引用
     * 信息. 这些信息可以通过简单的映射维护, 或者自行增加记录信息的数据结构.
     *
     * @param originInstructions 前端提供的中间代码
     */
    public void loadIR(List<Instruction> originInstructions) {
        IR = PreProcess(originInstructions);
    }


    /**
     * 执行代码生成.
     * <br>
     * 根据理论课的做法, 在代码生成时同时完成寄存器分配的工作. 若你觉得这样的做法不好,
     * 也可以将寄存器分配和代码生成分开进行.
     * <br>
     * 提示: 寄存器分配中需要的信息较多, 关于全局的与代码生成过程无关的信息建议在代码生
     * 成前完成建立, 与代码生成的过程相关的信息可自行设计数据结构进行记录并动态维护.
     */
    public void run() {
        Reg dest, a1, a2;
        for(Instruction inst: IR){
            switch (inst.getKind().name()){
                case "MOV":
                    if(inst.getFrom() instanceof IRImmediate){
                        if(variableRegBMap.containsKey(inst.getResult()))dest = variableRegBMap.getByKey(inst.getResult());
                        else {
                            dest = RegManage.getFreeReg();
                            variableRegBMap.add(inst.getResult(), dest);
                        }
                        asmList.add(asmInstruction.li(dest, (IRImmediate) inst.getFrom()));
                    }else {
                        if(variableRegBMap.containsKey(inst.getResult()))dest = variableRegBMap.getByKey(inst.getResult());
                        else {
                            dest = RegManage.getFreeReg();
                            variableRegBMap.add(inst.getResult(), dest);
                        }
                        a1 = variableRegBMap.getByKey((IRVariable) inst.getFrom());
                        asmList.add(asmInstruction.mv(dest, a1));
                        freeVariableReg((IRVariable) inst.getFrom());
                    }
                    break;
                case "SUB":
                    if(variableRegBMap.containsKey(inst.getResult()))dest = variableRegBMap.getByKey(inst.getResult());
                    else {
                        dest = RegManage.getFreeReg();
                        variableRegBMap.add(inst.getResult(), dest);
                    }
                    a1 = variableRegBMap.getByKey((IRVariable) inst.getLHS());
                    a2 = variableRegBMap.getByKey((IRVariable) inst.getRHS());
                    asmList.add(asmInstruction.sub(dest, a1, a2));
                    freeVariableReg((IRVariable) inst.getLHS());
                    freeVariableReg((IRVariable) inst.getRHS());
                    break;
                case "MUL":
                    if(variableRegBMap.containsKey(inst.getResult()))dest = variableRegBMap.getByKey(inst.getResult());
                    else {
                        dest = RegManage.getFreeReg();
                        variableRegBMap.add(inst.getResult(), dest);
                    }
                    a1 = variableRegBMap.getByKey((IRVariable) inst.getLHS());
                    a2 = variableRegBMap.getByKey((IRVariable) inst.getRHS());
                    asmList.add(asmInstruction.mul(dest, a1, a2));
                    freeVariableReg((IRVariable) inst.getLHS());
                    freeVariableReg((IRVariable) inst.getRHS());
                    break;
                case "ADD":
                    if(variableRegBMap.containsKey(inst.getResult()))dest = variableRegBMap.getByKey(inst.getResult());
                    else {
                        dest = RegManage.getFreeReg();
                        variableRegBMap.add(inst.getResult(), dest);
                    }
                    a1 = variableRegBMap.getByKey((IRVariable) inst.getLHS());
                    asmList.add(asmInstruction.addi(dest, a1, (IRImmediate) inst.getRHS()));
                    freeVariableReg((IRVariable) inst.getLHS());
                    break;
                case "RET":
                    a1 = variableRegBMap.getByKey((IRVariable) inst.getReturnValue());
                    asmList.add(asmInstruction.mv(RegManage.a0, a1));
                    break;
            }
        }
    }


    /**
     * 输出汇编代码到文件
     *
     * @param path 输出文件路径
     */
    public void dump(String path) {
        FileUtils.writeLines(path, asmList);
    }

    private List<Instruction> PreProcess(List<Instruction> list){
        List<Instruction> IR = new ArrayList<>();
        for(Instruction inst: list ){
            if(inst.getKind().name().equals("RET")){
                IR.add(inst);
                break;
            }
            switch (inst.getKind().name()){
                case "SUB":
                    if(inst.getLHS() instanceof IRImmediate){
                        IRVariable temp = IRVariable.temp();
                        IR.add(Instruction.createMov(temp, inst.getLHS()));
                        IR.add(Instruction.createSub(inst.getResult(),temp,inst.getRHS()));
                    }else IR.add(inst);
                    for(IRValue iv: IR.get(IR.size()-1).getOperands()){
                        if(iv instanceof IRVariable) variableTimes.put((IRVariable) iv, variableTimes.getOrDefault((IRVariable)iv, 0)+1);
                    }
                    break;
                case "ADD":
                    if(inst.getLHS() instanceof IRImmediate){
                        IR.add(Instruction.createAdd(inst.getResult(), inst.getRHS(), inst.getLHS()));
                    }else IR.add(inst);
                    for(IRValue iv: IR.get(IR.size()-1).getOperands()){
                        if(iv instanceof IRVariable) variableTimes.put((IRVariable) iv, variableTimes.getOrDefault((IRVariable)iv, 0)+1);
                    }
                    break;
                case "MUL":
                    if(inst.getLHS() instanceof IRImmediate){
                        IR.add(Instruction.createMul(inst.getResult(), inst.getRHS(), inst.getLHS()));
                    }else IR.add(inst);
                    for(IRValue iv: IR.get(IR.size()-1).getOperands()){
                        if(iv instanceof IRVariable) variableTimes.put((IRVariable) iv, variableTimes.getOrDefault((IRVariable)iv, 0)+1);
                    }
                    break;
                default:
                    IR.add(inst);
                    for(IRValue iv: IR.get(IR.size()-1).getOperands()){
                        if(iv instanceof IRVariable) variableTimes.put((IRVariable) iv, variableTimes.getOrDefault((IRVariable)iv, 0)+1);
                    }
                    break;
            }
        }
        return IR;
    }

    private void freeVariableReg(IRVariable a){
        variableTimes.put(a, variableTimes.get(a)-1);
        if(variableTimes.get(a) == 0){
            Reg tmp = variableRegBMap.getByKey(a);
            RegManage.freeReg(tmp.getName());
            variableRegBMap.removeByKey(a);
            variableRegBMap.removeByValue(tmp);
        }
    }
}

