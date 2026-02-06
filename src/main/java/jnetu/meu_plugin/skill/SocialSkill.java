package jnetu.meu_plugin.skill;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.item.ItemContext;
import dev.aurelium.auraskills.api.registry.NamespacedId;
import dev.aurelium.auraskills.api.registry.NamespacedRegistry;
import dev.aurelium.auraskills.api.skill.CustomSkill;
import dev.aurelium.auraskills.api.skill.Skill;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SocialSkill implements Listener{

    private final JavaPlugin plugin;
    //private final Skill skill;
//    private final Map<UUID, SocialData> socialBattery = new ConcurrentHashMap<>();
//
//    private static final long TEMPO_RECARGA_MS = 10 * 60 * 1000; // 10 Minutos para 100%
//    private static final double XP_MAXIMO = 100.0; // XP ganho se a carga estiver em 100%

    public SocialSkill(JavaPlugin plugin) {
        this.plugin = plugin;

//        this.skill = CustomSkill.builder(NamespacedId.of("meu_plugin", "social"))
//                .displayName("Social")
//                .description("Converse para ganhar XP. Quanto mais tempo sem falar, mais XP!")
//                .item(ItemContext.builder()
//                        .material("player_head")
//                        .pos("4,4")
//                        .build())
//                .build();

    }


//    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//    public void aoFalarNoChat(AsyncPlayerChatEvent event) {
//        Player player = event.getPlayer();
//        UUID uuid = player.getUniqueId();
//        SocialData data = atualizarBateria(uuid);
//        double carga;
//        synchronized (data) {
//            carga = data.cargaAtual;
//            data.cargaAtual = 0.0;
//            data.ultimoUpdate = System.currentTimeMillis();
//        }
//        double xpGanho = carga * XP_MAXIMO;
//        if (xpGanho < 0.1) return;
//        Bukkit.getScheduler().runTask(plugin, () -> {
//            if (player.isOnline()) {
//                AuraSkillsApi.get().getUser(uuid).addSkillXp(MinhasSkills.SOCIAL, xpGanho);
//            }
//        });
//    }

//    private SocialData atualizarBateria(UUID uuid) {
//        SocialData data = socialBattery.computeIfAbsent(uuid, k -> new SocialData(0.0, System.currentTimeMillis()));
//
//        synchronized (data) {
//            long agora = System.currentTimeMillis();
//            long tempoPassado = agora - data.ultimoUpdate;
//            double recarga = (double) tempoPassado / TEMPO_RECARGA_MS;
//            data.cargaAtual = Math.min(1.0, data.cargaAtual + recarga);
//            data.ultimoUpdate = agora;
//        }
//
//        return data;
//    }

//    @EventHandler
//    public void aoEntrar(PlayerJoinEvent event) {
//        socialBattery.put(event.getPlayer().getUniqueId(), new SocialData(0.0, System.currentTimeMillis()));
//    }
//
//    @EventHandler
//    public void aoSair(PlayerQuitEvent event) {
//        socialBattery.remove(event.getPlayer().getUniqueId());
//    }
//
//    static class SocialData {
//        double cargaAtual;
//        long ultimoUpdate;
//
//        public SocialData(double cargaAtual, long ultimoUpdate) {
//            this.cargaAtual = cargaAtual;
//            this.ultimoUpdate = ultimoUpdate;
//        }
//    }
}