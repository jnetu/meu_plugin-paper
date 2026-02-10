package jnetu.meu_plugin.crafting;

import jnetu.meu_plugin.Meu_plugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

/**
 * Receita da Essência de Pedra
 * Padrão 3x3:
 * [C] [C] [C]
 * [C] [E] [C]
 * [C] [C] [C]
 *
 * C = Cobblestone (Pedregulho)
 * E = Essência
 */
public class EssenciaPedraRecipe implements CustomRecipe {

    private final Meu_plugin plugin;

    public EssenciaPedraRecipe(Meu_plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getId() {
        return "essencia_pedra";
    }

    @Override
    public String getNome() {
        return "Essência de Pedra";
    }

    @Override
    public Recipe criarReceita() {
        ItemStack resultado = ItemFactory.criarEssenciaPedra();

        NamespacedKey key = new NamespacedKey(plugin, getId());
        ShapedRecipe recipe = new ShapedRecipe(key, resultado);

        // Define o padrão 3x3
        recipe.shape(
                "CCC",
                "CEC",
                "CCC"
        );

        // Define os ingredientes
        recipe.setIngredient('C', Material.COBBLESTONE);

        // Ingrediente customizado (Essência)
        recipe.setIngredient('E', new RecipeChoice.ExactChoice(ItemFactory.criarEssencia()));

        return recipe;
    }
}
