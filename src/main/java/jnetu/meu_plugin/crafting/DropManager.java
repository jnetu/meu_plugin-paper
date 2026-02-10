package jnetu.meu_plugin.crafting;

import jnetu.meu_plugin.Meu_plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Gerenciador de drops customizados
 */
public class DropManager {

    private final Meu_plugin plugin;
    private final List<CustomDrop> drops;
    private final EssenciaDropListener essenciaDropListener;

    public DropManager(Meu_plugin plugin) {
        this.plugin = plugin;
        this.drops = new ArrayList<>();

        // Registra o drop de Essência
        this.essenciaDropListener = new EssenciaDropListener(plugin);
        registrarDrop(essenciaDropListener);
    }

    /**
     * Registra um drop customizado
     */
    public void registrarDrop(CustomDrop drop) {
        drops.add(drop);

        // Se for um Listener, registra os eventos
        if (drop instanceof org.bukkit.event.Listener listener) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
            plugin.getLogger().info("✅ Drop customizado registrado: " +
                    drop.getBlockType().name() +
                    " (Chance: " + String.format("%.1f%%", drop.getChance() * 100) + ")");
        }
    }

    /**
     * Obtém o listener de Essência
     */
    public EssenciaDropListener getEssenciaDropListener() {
        return essenciaDropListener;
    }

    /**
     * Recarrega todas as configurações de drops
     */
    public void recarregarConfigs() {
        essenciaDropListener.recarregarConfig();
        plugin.getLogger().info("✅ Configurações de drops recarregadas!");
    }
}
