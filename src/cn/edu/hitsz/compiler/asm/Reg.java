package cn.edu.hitsz.compiler.asm;

public class Reg {
    private String name;
    private boolean valid;

    public Reg(String name, boolean valid) {
        this.name = name;
        this.valid = valid;
    }

    public String getName() {
        return name;
    }

    public boolean getValid() {
        return valid;
    }

    public void switchValid(){
        this.valid = !this.valid;
    }
}
