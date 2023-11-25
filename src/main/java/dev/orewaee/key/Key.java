package dev.orewaee.key;

import java.util.Timer;

import dev.orewaee.utils.Generator;

public record Key(
    String code,
    Timer timer
) {
    public Key() {
        this(Generator.generateCode(), new Timer());
    }
}
