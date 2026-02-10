package jnetu.meu_plugin.crafting;

import jnetu.meu_plugin.Meu_plugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

/**
 * Listener que bloqueia o uso de itens customizados em receitas vanilla
 */
public class RecipeBlockListener implements Listener {

    private final Meu_plugin plugin;

    public RecipeBlockListener(Meu_plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void aoPreparaCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        ItemStack result = inventory.getResult();

        // Se não há resultado, não precisa verificar
        if (result == null || result.getType() == Material.AIR) {
            return;
        }

        // Verifica se o resultado é um item vanilla (Conduit, por exemplo)
        // e se algum dos ingredientes é nosso item customizado
        if (result.getType() == Material.CONDUIT) {
            if (contemItemCustomizado(inventory.getMatrix())) {
                // Cancela a receita
                inventory.setResult(null);

                // Avisa o jogador
                if (event.getView().getPlayer() instanceof Player player) {
                    player.sendMessage(Component.text("❌ ", NamedTextColor.RED)
                            .append(Component.text("A Essência de Pedra não pode ser usada em receitas vanilla!",
                                    NamedTextColor.GRAY)));
                }

                plugin.getLogger().info("Bloqueado crafting de Conduit com Essência de Pedra");
            }

            return;
        }// Você pode adicionar mais bloqueios aqui para outras receitas vanilla



        // bloquear QUALQUER receita vanilla que use o item:
        if (!isReceitaCustomizada(result) && contemItemCustomizado(inventory.getMatrix())) {
            inventory.setResult(null);
            if (event.getView().getPlayer() instanceof Player player) {
                player.sendMessage(Component.text("❌ Este item customizado não pode ser usado em receitas vanilla!",
                        NamedTextColor.RED));
            }
            return;
        }

    }

    /**
     * Verifica se há algum item customizado na matriz de crafting
     */
    private boolean contemItemCustomizado(ItemStack[] matrix) {
        for (ItemStack item : matrix) {
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            // Verifica se é Essência ou Essência de Pedra
            if (ItemFactory.isEssencia(item) || ItemFactory.isEssenciaPedra(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se o resultado é uma receita customizada
     */
    private boolean isReceitaCustomizada(ItemStack result) {
        return ItemFactory.isEssencia(result) || ItemFactory.isEssenciaPedra(result);
    }
}
