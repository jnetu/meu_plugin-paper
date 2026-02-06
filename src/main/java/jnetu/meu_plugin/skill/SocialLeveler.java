package jnetu.meu_plugin.skill;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.skill.Skill;
import dev.aurelium.auraskills.api.source.SkillSource;
import dev.aurelium.auraskills.api.source.SourceType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SocialLeveler implements Listener {

    private final JavaPlugin plugin;
    private final AuraSkillsApi api;
    private final Map<UUID, SocialData> socialBattery = new ConcurrentHashMap<>();

    public SocialLeveler(JavaPlugin plugin, AuraSkillsApi api) {
        this.plugin = plugin;
        this.api = api;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void aoFalarNoChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        SkillSource<SocialSource> skillSourceWrapper = api.getSourceManager().getSingleSourceOfType(SocialSource.class);
        if (skillSourceWrapper == null) return;
        SocialSource sourceConfig = skillSourceWrapper.source();
        SocialData data = atualizarBateria(uuid, sourceConfig.getRechargeMs());
        double carga;
        synchronized (data) {
            carga = data.cargaAtual;
            data.cargaAtual = 0.0;
            data.ultimoUpdate = System.currentTimeMillis();
        }

        double xpGanho = carga * sourceConfig.getXp();//variavel dentro de um yml//;
        if (xpGanho < 0.1) return;
        Skill cashSkill =  skillSourceWrapper.skill();
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (player.isOnline()) {
                api.getUser(uuid).addSkillXp(cashSkill, xpGanho);

            }
        });
    }

    private SocialData atualizarBateria(UUID uuid, long tempoRecargaMs) {
        SocialData data = socialBattery.computeIfAbsent(uuid, k -> new SocialData(0.0, System.currentTimeMillis()));

        synchronized (data) {
            long agora = System.currentTimeMillis();
            long tempoPassado = agora - data.ultimoUpdate;

            // Cálculo baseado no tempo configurado no YML
            double recarga = (double) tempoPassado / tempoRecargaMs;

            data.cargaAtual = Math.min(1.0, data.cargaAtual + recarga);
            data.ultimoUpdate = agora;
        }
        return data;
    }

    @EventHandler
    public void aoEntrar(PlayerJoinEvent event) {
        socialBattery.put(event.getPlayer().getUniqueId(), new SocialData(0.0, System.currentTimeMillis()));
    }

    @EventHandler
    public void aoSair(PlayerQuitEvent event) {
        socialBattery.remove(event.getPlayer().getUniqueId());
    }

    private static class SocialData {
        double cargaAtual;
        long ultimoUpdate;

        public SocialData(double cargaAtual, long ultimoUpdate) {
            this.cargaAtual = cargaAtual;
            this.ultimoUpdate = ultimoUpdate;
        }
    }
}