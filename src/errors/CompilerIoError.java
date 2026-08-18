package errors;

public final class CompilerIoError
        extends CompilerException {

    public CompilerIoError(
            String file,
            String message,
            Throwable cause
    ) {
        super(
                CompilerStage.IO,
                file,
                -1,
                message,
                cause
        );
    }
}