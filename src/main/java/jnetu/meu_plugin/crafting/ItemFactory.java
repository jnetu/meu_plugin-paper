package jnetu.meu_plugin.crafting;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;

/**
 * Factory para criar itens customizados com efeitos especiais
 */
public class ItemFactory {

    // IDs únicos para os itens customizados
    private static final float ESSENCIA_ID = 2001.0f;
    private static final float ESSENCIA_PEDRA_ID = 2002.0f;

    /**
     * Adiciona o efeito de brilho (enchant glow) a um item
     */
    public static ItemStack adicionarBrilho(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        // Adiciona um encantamento falso e esconde ele
        meta.addEnchant(Enchantment.LURE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Define Custom Model Data usando a nova API (1.21.5+)
     */
    private static void setCustomModelDataModerno(ItemMeta meta, float modelId) {
        try {
            // Obtém o componente (cria um vazio se não existir)
            CustomModelDataComponent component = meta.getCustomModelDataComponent();

            // Define o float
            component.setFloats(List.of(modelId));

            // Aplica o componente de volta ao meta
            meta.setCustomModelDataComponent(component);

        } catch (Exception e) {
            // Fallback para versões antigas
            meta.setCustomModelData((int) modelId);
        }
    }

    /**
     * Obtém Custom Model Data usando a nova API (1.21.5+)
     */
    private static float getCustomModelDataModerno(ItemMeta meta) {
        try {
            // Verifica se tem o componente definido
            if (meta.hasCustomModelDataComponent()) {
                CustomModelDataComponent component = meta.getCustomModelDataComponent();
                List<Float> floats = component.getFloats();

                if (!floats.isEmpty()) {
                    return floats.get(0);
                }
            }
        } catch (Exception e) {
            // Fallback para versões antigas
            if (meta.hasCustomModelData()) {
                return (float) meta.getCustomModelData();
            }
        }
        return 0.0f;
    }

    /**
     * Cria a Essência (item base)
     */
    public static ItemStack criarEssencia() {
        ItemStack essencia = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta meta = essencia.getItemMeta();

        meta.displayName(Component.text("✦ Essência", NamedTextColor.LIGHT_PURPLE)
                .decoration(TextDecoration.ITALIC, false));

        meta.lore(List.of(
                Component.text("Uma essência mágica rara", NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("encontrada nas profundezas", NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text(""),
                Component.text("⛏ Minere pedra lisa para obter", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
        ));

        setCustomModelDataModerno(meta, ESSENCIA_ID);
        essencia.setItemMeta(meta);

        return adicionarBrilho(essencia);
    }

    /**
     * Cria a Essência de Pedra (item craftado e consumível)
     */
    public static ItemStack criarEssenciaPedra() {
        ItemStack essenciaPedra = new ItemStack(Material.HEART_OF_THE_SEA);
        ItemMeta meta = essenciaPedra.getItemMeta();

        meta.displayName(Component.text("✦ Essência de Pedra", NamedTextColor.AQUA)
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true));

        meta.lore(List.of(
                Component.text("Uma fusão poderosa entre", NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("pedregulho e essência mágica", NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text(""),
                Component.text("⚡ Efeito: Haste I (3 min)", NamedTextColor.GOLD)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text(""),
                Component.text("Clique direito para consumir", NamedTextColor.YELLOW)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text(""),
                Component.text("Craftado com 8x Pedregulho", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("e 1x Essência no centro", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
        ));

        setCustomModelDataModerno(meta, ESSENCIA_PEDRA_ID);
        essenciaPedra.setItemMeta(meta);

        return adicionarBrilho(essenciaPedra);
    }

    /**
     * Verifica se um item é uma Essência
     */
    public static boolean isEssencia(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();

        float modelData = getCustomModelDataModerno(meta);
        return Math.abs(modelData - ESSENCIA_ID) < 0.01f;
    }

    /**
     * Verifica se um item é uma Essência de Pedra
     */
    public static boolean isEssenciaPedra(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();

        float modelData = getCustomModelDataModerno(meta);
        return Math.abs(modelData - ESSENCIA_PEDRA_ID) < 0.01f;
    }
}
