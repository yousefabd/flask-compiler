package jinja2.runtime;

import java.util.Objects;

public record FlashMessage(String category, Object message){
    public FlashMessage {
        Objects.requireNonNull(category);
    }
}
