package jnetu.meu_plugin.economy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MoedaEconomy implements Economy {

    // Simples armazenamento em memória (HashMap).
    // OBS: Em produção, você deve salvar isso em config.yml ou Banco de Dados!
    private final Map<UUID, Double> saldos = new HashMap<>();

    @Override
    public boolean isEnabled() { return true; }

    @Override
    public String getName() { return "Moeda"; }

    @Override
    public boolean hasBankSupport() { return false; }

    @Override
    public int fractionalDigits() { return 2; }

    @Override
    public String format(double amount) {
        return String.format("%.2f Moedas", amount);
    }

    @Override
    public String currencyNamePlural() { return "Moedas"; }

    @Override
    public String currencyNameSingular() { return "Moeda"; }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return true; // Todo player "tem" conta, mesmo que seja 0
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return saldos.getOrDefault(player.getUniqueId(), 0.0);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return getBalance(player) >= amount;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if (amount < 0) return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Valor negativo");

        double saldoAtual = getBalance(player);
        if (saldoAtual < amount) {
            return new EconomyResponse(0, saldoAtual, EconomyResponse.ResponseType.FAILURE, "Saldo insuficiente");
        }

        saldos.put(player.getUniqueId(), saldoAtual - amount);
        return new EconomyResponse(amount, saldoAtual - amount, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        if (amount < 0) return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Valor negativo");

        double saldoAtual = getBalance(player);
        saldos.put(player.getUniqueId(), saldoAtual + amount);

        return new EconomyResponse(amount, saldoAtual + amount, EconomyResponse.ResponseType.SUCCESS, null);
    }

    // Métodos boilerplate (obrigatórios, mas não usados para contas de jogador simples)
    @Override public boolean createPlayerAccount(OfflinePlayer player) { return true; }
    @Override public boolean createPlayerAccount(OfflinePlayer player, String worldName) { return true; }

    // Métodos legados (String based) - Redirecionam para os métodos OfflinePlayer
    @Override public boolean hasAccount(String playerName) { return hasAccount(Bukkit.getOfflinePlayer(playerName)); }
    @Override public double getBalance(String playerName) { return getBalance(Bukkit.getOfflinePlayer(playerName)); }
    @Override public boolean has(String playerName, double amount) { return has(Bukkit.getOfflinePlayer(playerName), amount); }
    @Override public EconomyResponse withdrawPlayer(String playerName, double amount) { return withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount); }
    @Override public EconomyResponse depositPlayer(String playerName, double amount) { return depositPlayer(Bukkit.getOfflinePlayer(playerName), amount); }
    @Override public boolean createPlayerAccount(String playerName) { return true; }
    @Override public boolean createPlayerAccount(String playerName, String worldName) { return true; }

    // Métodos de Banco (Ignorados por enquanto)
    @Override public boolean hasBankSupport() { return false; }
    @Override public EconomyResponse createBank(String name, String player) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Sem banco"); }
    @Override public EconomyResponse deleteBank(String name) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Sem banco"); }
    @Override public EconomyResponse bankBalance(String name) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Sem banco"); }
    @Override public EconomyResponse bankHas(String name, double amount) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Sem banco"); }
    @Override public EconomyResponse bankWithdraw(String name, double amount) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Sem banco"); }
    @Override public EconomyResponse bankDeposit(String name, double amount) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Sem banco"); }
    @Override public EconomyResponse isBankOwner(String name, String playerName) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Sem banco"); }
    @Override public EconomyResponse isBankMember(String name, String playerName) { return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Sem banco"); }
    @Override public List<String> getBanks() { return Collections.emptyList(); }
    @Override public boolean hasAccount(String playerName, String worldName) { return hasAccount(playerName); }
    @Override public double getBalance(String playerName, String world) { return getBalance(playerName); }
    @Override public boolean has(String playerName, String worldName, double amount) { return has(playerName, amount); }
    @Override public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) { return withdrawPlayer(playerName, amount); }
    @Override public EconomyResponse depositPlayer(String playerName, String worldName, double amount) { return depositPlayer(playerName, amount); }
}