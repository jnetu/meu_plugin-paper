package jnetu.meu_plugin.itens;

import jnetu.meu_plugin.Meu_plugin;
import jnetu.meu_plugin.crafting.ItemFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Essência de Pedra - Item consumível que dá Haste I
 */
public class EssenciaPedraItem implements ItemCustomizado {

    private final Meu_plugin plugin;

    public EssenciaPedraItem(Meu_plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public int getCustomModelData() {
        return 2002; // Mantemos para compatibilidade
    }

    @Override
    public String getNome() {
        return "Essência de Pedra";
    }

    @Override
    public ItemStack criar() {
        return ItemFactory.criarEssenciaPedra();
    }

    @Override
    public void aoInteragir(Player player, PlayerInteractEvent event) {
        // Verifica se é clique direito
        if (!event.getAction().isRightClick()) return;

        ItemStack item = event.getItem();
        if (item == null || !ItemFactory.isEssenciaPedra(item)) return;

        // Cancela o evento para não interferir com outras coisas
        event.setCancelled(true);

        // Aplica o efeito de Haste I por 3 minutos (3600 ticks)
        int duracao = 3600; // 180 segundos = 3 minutos
        int amplificador = 0; // Haste I (0 = nível 1)

        player.addPotionEffect(new PotionEffect(
                PotionEffectType.HASTE,
                duracao,
                amplificador,
                false, // ambient
                true,  // particles
                true   // icon
        ));

        // Remove um item da mão
        item.setAmount(item.getAmount() - 1);

        // Feedback visual e sonoro
        player.sendMessage(Component.text("✦ ", NamedTextColor.AQUA)
                .append(Component.text("Você consumiu uma ", NamedTextColor.GRAY))
                .append(Component.text("Essência de Pedra", NamedTextColor.AQUA))
                .append(Component.text("!", NamedTextColor.GRAY)));

        player.sendMessage(Component.text("⛏ ", NamedTextColor.GOLD)
                .append(Component.text("Haste I ativado por 3 minutos!", NamedTextColor.YELLOW)));

        // Som de consumo
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.0f, 1.0f);
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 0.5f, 1.5f);

        // Partículas
        player.getWorld().spawnParticle(
                Particle.ENCHANT,
                player.getLocation().add(0, 1, 0),
                50, // quantidade
                0.5, 0.5, 0.5, // spread
                0.1 // velocidade
        );

        player.getWorld().spawnParticle(
                Particle.HAPPY_VILLAGER,
                player.getLocation().add(0, 1, 0),
                20,
                0.3, 0.3, 0.3,
                0.05
        );
    }
}
