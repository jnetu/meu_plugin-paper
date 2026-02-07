package jnetu.meu_plugin.skill;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Injeta configurações customizadas nos menus do AuraSkills
 * sem precisar modificar o plugin original.
 *
 * Como funciona:
 * 1. Cria um arquivo de patch no seu plugin (resources/auraskills_patches/)
 * 2. Ao carregar, injeta essas configurações nos menus do AuraSkills
 * 3. Funciona mesmo após updates do AuraSkills!
 */
public class AuraSkillsMenuInjector {

    private final Plugin meuPlugin;
    private final Plugin auraSkillsPlugin;
    private final File auraSkillsMenusFolder;

    public AuraSkillsMenuInjector(Plugin meuPlugin) {
        this.meuPlugin = meuPlugin;
        this.auraSkillsPlugin = Bukkit.getPluginManager().getPlugin("AuraSkills");

        if (auraSkillsPlugin == null) {
            throw new IllegalStateException("AuraSkills não encontrado!");
        }

        this.auraSkillsMenusFolder = new File(auraSkillsPlugin.getDataFolder(), "menus");
    }

    /**
     * Injeta as configurações customizadas dos stats no menu stat_info.yml
     */
    public void injetarContextosCustomizados() {
        try {
            File statInfoFile = new File(auraSkillsMenusFolder, "stat_info.yml");

            if (!statInfoFile.exists()) {
                meuPlugin.getLogger().warning("stat_info.yml não encontrado! Aguardando AuraSkills gerar...");
                return;
            }

            // Faz backup
            File backup = new File(auraSkillsMenusFolder, "stat_info.yml.backup");
            if (!backup.exists()) {
                Files.copy(statInfoFile.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
                meuPlugin.getLogger().info("Backup de stat_info.yml criado");
            }

            YamlConfiguration config = YamlConfiguration.loadConfiguration(statInfoFile);

            // Injeta o contexto do stat Carisma com TODAS as variações
            String basePath = "templates.stat.contexts";

            // Variação: carisma
            if (!config.contains(basePath + ".carisma")) {
                config.set(basePath + ".carisma.material", "player_head");
                meuPlugin.getLogger().info("✓ Contexto 'carisma' injetado");
            }

            // Variação: meu_plugin:carisma (com aspas)
            if (!config.contains(basePath + ".meu_plugin:carisma")) {
                config.set(basePath + ".\"meu_plugin:carisma\".material", "player_head");
                meuPlugin.getLogger().info("✓ Contexto 'meu_plugin:carisma' injetado");
            }

            // Variação: meu_plugin/carisma (com aspas)
            if (!config.contains(basePath + ".meu_plugin/carisma")) {
                config.set(basePath + ".\"meu_plugin/carisma\".material", "player_head");
                meuPlugin.getLogger().info("✓ Contexto 'meu_plugin/carisma' injetado");
            }

            // Variação: meu_plugin_carisma
            if (!config.contains(basePath + ".meu_plugin_carisma")) {
                config.set(basePath + ".meu_plugin_carisma.material", "player_head");
                meuPlugin.getLogger().info("✓ Contexto 'meu_plugin_carisma' injetado");
            }

            // Salva o arquivo modificado
            config.save(statInfoFile);

            meuPlugin.getLogger().info("====================================");
            meuPlugin.getLogger().info("Menu do AuraSkills modificado!");
            meuPlugin.getLogger().info("IMPORTANTE: Reinicie o servidor para");
            meuPlugin.getLogger().info("aplicar as mudanças nos menus.");
            meuPlugin.getLogger().info("====================================");

        } catch (IOException e) {
            meuPlugin.getLogger().severe("Erro ao injetar contextos no AuraSkills: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Injeta o contexto da trait no menu stat_info.yml
     */
    public void injetarContextoTrait() {
        try {
            File statInfoFile = new File(auraSkillsMenusFolder, "stat_info.yml");

            if (!statInfoFile.exists()) {
                return;
            }

            YamlConfiguration config = YamlConfiguration.loadConfiguration(statInfoFile);

            String basePath = "templates.trait.contexts";

            // Adiciona contexto para a trait REDUCAO_BATERIA / chat_battery
            if (!config.contains(basePath + ".chat_battery")) {
                config.set(basePath + ".chat_battery.material", "clock");
                meuPlugin.getLogger().info("✓ Contexto trait 'chat_battery' injetado");
            }

            // Variações com namespace
            if (!config.contains(basePath + ".meu_plugin:chat_battery")) {
                config.set(basePath + ".\"meu_plugin:chat_battery\".material", "clock");
                meuPlugin.getLogger().info("✓ Contexto trait 'meu_plugin:chat_battery' injetado");
            }

            config.save(statInfoFile);

        } catch (IOException e) {
            meuPlugin.getLogger().severe("Erro ao injetar contexto da trait: " + e.getMessage());
        }
    }

    /**
     * Injeta o contexto da skill Social no menu skills.yml
     */
    public void injetarContextoSkill() {
        try {
            File skillsFile = new File(auraSkillsMenusFolder, "skills.yml");

            if (!skillsFile.exists()) {
                meuPlugin.getLogger().warning("skills.yml não encontrado!");
                return;
            }

            YamlConfiguration config = YamlConfiguration.loadConfiguration(skillsFile);

            String basePath = "templates.skill.contexts";

            // Adiciona contexto para a skill Social
            if (!config.contains(basePath + ".social")) {
                config.set(basePath + ".social.material", "player_head");
                config.set(basePath + ".social.group", "third_row");
                config.set(basePath + ".social.order", 4);
                meuPlugin.getLogger().info("✓ Contexto skill 'social' injetado");
            }

            // Variações com namespace
            if (!config.contains(basePath + ".meu_plugin:social")) {
                config.set(basePath + ".\"meu_plugin:social\".material", "player_head");
                config.set(basePath + ".\"meu_plugin:social\".group", "third_row");
                config.set(basePath + ".\"meu_plugin:social\".order", 4);
                meuPlugin.getLogger().info("✓ Contexto skill 'meu_plugin:social' injetado");
            }

            config.save(skillsFile);

        } catch (IOException e) {
            meuPlugin.getLogger().severe("Erro ao injetar contexto da skill: " + e.getMessage());
        }
    }

    /**
     * Remove todos os contextos injetados (útil ao desinstalar o plugin)
     */
    public void removerInjections() {
        try {
            File statInfoFile = new File(auraSkillsMenusFolder, "stat_info.yml");
            File backup = new File(auraSkillsMenusFolder, "stat_info.yml.backup");

            if (backup.exists() && statInfoFile.exists()) {
                Files.copy(backup.toPath(), statInfoFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                meuPlugin.getLogger().info("Configurações do AuraSkills restauradas do backup");
            }

        } catch (IOException e) {
            meuPlugin.getLogger().severe("Erro ao restaurar backup: " + e.getMessage());
        }
    }
}