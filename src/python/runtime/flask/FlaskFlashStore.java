package python.runtime.flask;

import jinja2.runtime.FlashMessage;

import java.util.ArrayList;
import java.util.List;

public final class FlaskFlashStore {

    private final List<FlashMessage> pendingMessages =
            new ArrayList<>();

    public void add(
            Object message,
            String category
    ) {
        pendingMessages.add(
                new FlashMessage(
                        category,
                        message
                )
        );
    }

    public List<FlashMessage> consume() {
        List<FlashMessage> messages =
                List.copyOf(pendingMessages);

        pendingMessages.clear();

        return messages;
    }
}