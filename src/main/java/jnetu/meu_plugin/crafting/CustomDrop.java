package jnetu.meu_plugin.crafting;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Interface para drops customizados
 */
public interface CustomDrop {

    /**
     * Tipo de bloco que dropa o item
     */
    Material getBlockType();

    /**
     * Chance de drop (0.0 a 1.0)
     */
    double getChance();

    /**
     * Itens que podem dropar
     */
    List<ItemStack> getDrops();

    /**
     * Verifica se o bloco deve dropar o item
     */
    default boolean shouldDrop(Block block, Player player) {
        return block.getType() == getBlockType() && Math.random() < getChance();
    }

    /**
     * Executa quando o drop ocorre
     */
    default void onDrop(BlockBreakEvent event, ItemStack drop) {
        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
    }
}
