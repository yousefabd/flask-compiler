package python.models.trailer;

import java.util.ArrayList;

public class CallArguments extends Arguments {
    public ArrayList<Argument> args;
    public CallArguments(ArrayList<Argument> args){
        this.args = args;
    }
}