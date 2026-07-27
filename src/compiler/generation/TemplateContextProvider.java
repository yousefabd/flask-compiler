package compiler.generation;

import compiler.template.TemplateCall;
import java.util.Map;

public interface TemplateContextProvider {

    Map<String, Object> provideContext(
            TemplateCall call
    );
}
