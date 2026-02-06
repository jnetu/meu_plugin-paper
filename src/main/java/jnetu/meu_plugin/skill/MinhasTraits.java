package jnetu.meu_plugin.skill;

import dev.aurelium.auraskills.api.registry.NamespacedId;
import dev.aurelium.auraskills.api.trait.CustomTrait;

public class MinhasTraits {

    // Identificador: meu_plugin:reducao_bateria
    public static final CustomTrait REDUCAO_BATERIA = CustomTrait.builder(NamespacedId.of("meu_plugin", "reducao_bateria"))
            .displayName("Redução de Recarga Social")
            .build();
}
