package python.models.Import_statement;

import python.models.small_statement.SmallStatement;

public abstract class ImportStatement extends SmallStatement {
    protected ImportStatement(String name, int line) {
        super("ImportStatement." + name, line);
    }
}
