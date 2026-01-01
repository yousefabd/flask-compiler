package python.models.trailer;

import java.util.ArrayList;

import python.models.ASTNode;

public class CallArguments extends Arguments 
{
    public ArrayList<Argument> args;

    public CallArguments(ArrayList<Argument> args, int line){
        super("CallArguments", line);
        this.args = args;
    }

    public CallArguments(int line) {
        super("CallArguments", line);
        this.args = new ArrayList<>();
    }

    protected CallArguments(String name, int line) {
        super("CallArguments." + name, line);
        this.args = new ArrayList<>();
    }

    public void addArgument(Argument arg){
        this.args.add(arg);
    }

    public String toString() {
        if(this.args == null || this.args.size() == 0) return "";
        return "Arguments: ";
    }

    public ArrayList<ASTNode> getChildren() {
        if(this.args == null) return new ArrayList<>();
        return new ArrayList<>(this.args);
    }
}