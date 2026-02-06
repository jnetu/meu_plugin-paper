# Sistema de Skill Social - Bateria Social

## 📖 Como Funciona

O sistema de **Social** implementa uma mecânica de "bateria social" que simula a energia social do jogador:

### Conceito
- Cada jogador tem uma **bateria social** que **recarrega com o tempo**
- Ao **falar no chat**, o jogador **consome** a bateria e **ganha XP** proporcional à carga
- Quanto mais tempo sem falar, maior o XP ganho na próxima mensagem

### Comportamento

| Situação | Bateria | XP Ganho | Feedback |
|----------|---------|----------|----------|
| Acabou de falar | 0% | ~0 XP | Nenhum |
| 1 min sem falar | ~16% | ~16 XP | Silencioso |
| 5 min sem falar | ~83% | ~83 XP | ActionBar + Som |
| 10 min sem falar | 100% | 100 XP | ActionBar + Som |

### XP Silencioso vs. Com Feedback

**XP < 50 (bateria < 50%):**
- ✅ XP é adicionado normalmente
- ❌ Sem ActionBar
- ❌ Sem som
- ✅ Visível em `/skill social`
- ✅ Level up funciona normalmente

**XP ≥ 50 (bateria ≥ 50%):**
- ✅ XP é adicionado normalmente
- ✅ ActionBar mostrando ganho
- ✅ Som do AuraSkills
- ✅ Som extra de experiência (orb pickup)
- ✅ Level up funciona normalmente

## ⚙️ Configuração

### Arquivo: `sources/social.yml`

```yaml
sources:
  falar_chat:
    type: meu_plugin/chat_battery
    skill: meu_plugin/social
    xp: 100.0              # XP máximo (bateria 100%)
    recharge_seconds: 600  # Tempo para recarregar 100% (10 minutos)
```

### Ajustes Recomendados

**Para servidor casual (menos spam):**
```yaml
xp: 50.0
recharge_seconds: 300  # 5 minutos
```

**Para servidor competitivo (mais recompensa):**
```yaml
xp: 200.0
recharge_seconds: 900  # 15 minutos
```

**Para testes:**
```yaml
xp: 100.0
recharge_seconds: 60  # 1 minuto
```

## 🎮 Exemplo de Uso no Jogo

```
Jogador acabou de entrar no servidor:
[00:00] Player: "oi galera!"
→ Bateria: 0% → XP: 0 (nada acontece)

[00:30] Player: "alguém quer minerar?"
→ Bateria: ~5% → XP: ~5 (silencioso)

[03:00] Player: "achei diamantes!"
→ Bateria: ~30% → XP: ~30 (silencioso)

[06:00] Player: "vou fazer uma farm de ferro"
→ Bateria: ~60% → XP: ~60 (ActionBar + Som!)

[10:00] Player: "preciso de ajuda aqui"
→ Bateria: 100% → XP: 100 (ActionBar + Som!)
```

## 🔧 Detalhes Técnicos

### Thread Safety
- Usa `ConcurrentHashMap` para armazenar dados
- `ReentrantLock` para sincronização por jogador
- Cálculo de XP na thread assíncrona do chat
- Adição de XP na thread principal (obrigatório)

### Fórmula de Recarga

```java
recarga = tempo_passado / tempo_total_recarga
carga_atual = min(1.0, carga_anterior + recarga)
```

**Exemplo:**
- Tempo de recarga configurado: 600 segundos (10 min)
- Tempo desde última mensagem: 300 segundos (5 min)
- Recarga = 300 / 600 = 0.5 (50%)

### Limite de Som

O som extra só toca quando:
```java
xp_ganho >= xp_configurado / 2
```

Isso evita spam de som em mensagens rápidas.

## 📊 Vantagens do Sistema

1. **Anti-Spam Natural:** Jogadores não ganham XP spammando chat
2. **Recompensa Interação Significativa:** Mensagens espaçadas = mais XP
3. **Feedback Inteligente:** XP baixo = silencioso, XP alto = visível
4. **Balanceamento Automático:** Tempo offline = bateria recarregada
5. **Performance:** Thread-safe, cálculos leves

## 🐛 Troubleshooting

**XP não está sendo adicionado:**
- Verifique se `social.yml` existe em `plugins/meu_plugin/sources/`
- Veja o console para warnings do AuraSkills

**Level up não funciona:**
- O sistema usa `setSkillXp()` que **automaticamente** chama `checkLevelUp()`
- Se mesmo assim não funcionar, ative o código comentado de fallback

**Bateria recarrega muito rápido/devagar:**
- Ajuste `recharge_seconds` no `social.yml`
- Lembre-se: valor em **segundos**, não milissegundos

## 📝 Notas do Desenvolvedor

- O sistema foi inspirado no `JumpingLeveler` do próprio AuraSkills
- A diferença é que usamos `setSkillXp()` ao invés de `addXp()` do LevelManager
- `setSkillXp()` chama `checkLevelUp()` internamente, garantindo level ups corretos
- O código está **otimizado e limpo**, sem gambiarras

---

**Desenvolvido por:** jnetu  
**Versão:** 1.0  
**Compatibilidade:** AuraSkills 2.3.10+, Minecraft 1.21+
