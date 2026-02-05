package jnetu.meu_plugin;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.skill.Skills;
import dev.aurelium.auraskills.api.user.SkillsUser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public final class Aura_meu implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Apenas jogadores.");
            return true;
        }
        AuraSkillsApi api;
        try {
            api = AuraSkillsApi.get();
        } catch (IllegalStateException e) {
            player.sendMessage(Component.text("Error: AuraSkills não encontrado!", NamedTextColor.RED));
            return true;
        }
        SkillsUser user = api.getUser(player.getUniqueId());

        if (user == null) {
            player.sendMessage("Erro ao carregar seus dados de skills.");
            return true;
        }
        int nivelFarming = user.getSkillLevel(Skills.FARMING);

        user.addSkillXp(Skills.MINING, 100.0);

        // Feedback
        player.sendMessage(Component.text("--- Integração AuraSkills ---", NamedTextColor.GOLD));
        player.sendMessage(Component.text("Seu nível de Farming: " + nivelFarming, NamedTextColor.GREEN));
        player.sendMessage(Component.text("Você ganhou 100 XP em Mining!", NamedTextColor.AQUA));
        player.sendMessage(Component.text("XP Atual Mining: " + user.getSkillXp(Skills.MINING), NamedTextColor.YELLOW));

        return true;
    }
}
