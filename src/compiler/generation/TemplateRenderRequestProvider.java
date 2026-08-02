package compiler.generation;

import compiler.template.TemplateCall;
import java.util.Map;

public interface TemplateRenderRequestProvider {

    TemplateRenderRequest provide(
            TemplateCall call
    );
}
