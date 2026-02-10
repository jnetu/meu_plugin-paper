package jnetu.meu_plugin.crafting;

import org.bukkit.inventory.Recipe;

/**
 * Interface para receitas customizadas
 */
public interface CustomRecipe {

    /**
     * ID único da receita
     */
    String getId();

    /**
     * Nome da receita
     */
    String getNome();

    /**
     * Cria e retorna a receita do Bukkit
     */
    Recipe criarReceita();
}
