package cn.edu.hitsz.compiler.lexer;

import cn.edu.hitsz.compiler.NotImplementedException;
import cn.edu.hitsz.compiler.symtab.SymbolTable;
import cn.edu.hitsz.compiler.utils.FileUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

/**
 * TODO: 实验一: 实现词法分析
 * <br>
 * 你可能需要参考的框架代码如下:
 *
 * @see Token 词法单元的实现
 * @see TokenKind 词法单元类型的实现
 */
public class LexicalAnalyzer {
    private final SymbolTable symbolTable;
    private String buff;

    private List<Token> tokens;

    public LexicalAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }


    /**
     * 从给予的路径中读取并加载文件内容
     *
     * @param path 路径
     */
    public void loadFile(String path) {

        // 可自由实现各类缓冲区
        // 或直接采用完整读入方法
        buff = FileUtils.readFile(path);
//        throw new NotImplementedException();
    }

    /**
     * 执行词法分析, 准备好用于返回的 token 列表 <br>
     * 需要维护实验一所需的符号表条目, 而得在语法分析中才能确定的符号表条目的成员可以先设置为 null
     */
    public void run() {

        char[] s = buff.toCharArray();
        LinkedList<String> temp_buff = new LinkedList<>();
        tokens = new LinkedList<Token>();

        for(char c:s){
            switch(c){
                case '+': case '=': case '-': case '*': case '/': case '(':
                case ')':
                    tokens.add(Token.simple(String.valueOf(c)));
                    break;
                case ' ': case '\n': case ';':
                    if(temp_buff.size()>0) {
                        try {
                            tokens.add(Token.simple(String.join("", temp_buff)));
                        } catch (Exception e) {
                            if (Character.isDigit(temp_buff.get(0).charAt(0))) {
                                tokens.add(Token.normal("IntConst", String.join("", temp_buff)));
                            } else {
                                tokens.add(Token.normal("id", String.join("", temp_buff)));
                                if(!symbolTable.has(String.join("", temp_buff))){
                                    symbolTable.add(String.join("", temp_buff));
                                }
                            }
                        }
                        temp_buff.clear();
                    }
                    if (String.valueOf(c).equals(";")) {
                        tokens.add(Token.simple("Semicolon"));
                    }
                    break;
                default:
                    temp_buff.add(String.valueOf(c));
                    break;
            }
        }
        tokens.add(Token.eof());
    }


    /**
     * 获得词法分析的结果, 保证在调用了 run 方法之后调用
     *
     * @return Token 列表
     */
    public Iterable<Token> getTokens() {

        // 词法分析过程可以使用 Stream 或 Iterator 实现按需分析
        // 亦可以直接分析完整个文件
        // 总之实现过程能转化为一列表即可
        return tokens;
//        throw new NotImplementedException();
    }

    public void dumpTokens(String path) {
        FileUtils.writeLines(
            path,
            StreamSupport.stream(getTokens().spliterator(), false).map(Token::toString).toList()
        );
    }


}
