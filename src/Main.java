// cSpell: disable
import compiler.CompilationPipeline;
import errors.*;

import java.io.IOException;
import java.util.*;


public class Main {

    public static void main(String[] args) throws IOException
    {
        CompilationPipeline pipeline = new CompilationPipeline();
        pipeline.analyze();
    }
}