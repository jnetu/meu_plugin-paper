package jnetu.meu_plugin.skill;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.bukkit.BukkitTraitHandler;
import dev.aurelium.auraskills.api.trait.Trait;
import dev.aurelium.auraskills.api.user.SkillsUser;
import org.bukkit.entity.Player;

public class CarismaHandler implements BukkitTraitHandler {

    private final AuraSkillsApi auraSkills;

    public CarismaHandler(AuraSkillsApi auraSkills) {
        this.auraSkills = auraSkills;
    }

    @Override
    public Trait[] getTraits() {
        return new Trait[] { MinhasTraits.REDUCAO_BATERIA };
    }

    @Override
    public double getBaseLevel(Player player, Trait trait) {
        return 0.0;
    }

    @Override
    public void onReload(Player player, SkillsUser user, Trait trait) {
    }

    public double getTempoDeRecarga(Player player, double tempoOriginalEmSegundos) {
        SkillsUser user = auraSkills.getUser(player.getUniqueId());
        double porcentagemReducao = user.getEffectiveTraitLevel(MinhasTraits.REDUCAO_BATERIA);
        if (porcentagemReducao > 90) porcentagemReducao = 90;
        return tempoOriginalEmSegundos * (1.0 - (porcentagemReducao / 100.0));
    }
}
