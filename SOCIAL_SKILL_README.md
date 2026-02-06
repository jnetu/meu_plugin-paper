# Sistema de Skill Social - Bateria Social + Carisma

## 📖 Como Funciona

O sistema de **Social** implementa uma mecânica de "bateria social" que simula a energia social do jogador, com um stat **Carisma** que acelera a recarga:

### Conceito
- Cada jogador tem uma **bateria social** que **recarrega com o tempo**
- Ao **falar no chat**, o jogador **consome** a bateria e **ganha XP** proporcional à carga
- O stat **Carisma** reduz o tempo de recarga da bateria
- Quanto mais tempo sem falar (e mais Carisma), maior o XP ganho na próxima mensagem

### ✦ Stat: Carisma

**Como funciona:**
- Cada **5 níveis** de Social concede **+1 Carisma**
- Cada ponto de Carisma reduz **10%** do tempo de recarga da bateria

**Progressão:**

| Nível Social | Carisma | Tempo de Recarga | Redução |
|-------------|---------|------------------|---------|
| 0-4 | 0 | 600s (10 min) | 0% |
| 5-9 | 1 | 540s (9 min) | 10% |
| 10-14 | 2 | 480s (8 min) | 20% |
| 15-19 | 3 | 420s (7 min) | 30% |
| 20-24 | 4 | 360s (6 min) | 40% |
| 25-29 | 5 | 300s (5 min) | 50% |
| 50-54 | 10 | 0s (instantâneo) | 100% |

### Comportamento do XP

| Situação | Bateria | Carisma 0 | Carisma 5 | Feedback |
|----------|---------|-----------|-----------|----------|
| Acabou de falar | 0% | ~0 XP | ~0 XP | Nenhum |
| 1 min sem falar | Variável | ~10 XP | ~20 XP | Silencioso |
| 5 min sem falar | Variável | ~83 XP | 100 XP | ActionBar + Som |
| 10 min sem falar | 100% | 100 XP | 100 XP | ActionBar + Som |

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
    recharge_seconds: 600  # Tempo base para recarregar 100% (10 minutos)
```

### Arquivo: `rewards/social.yml`

```yaml
patterns:
  - type: stat
    stat: meu_plugin/carisma
    value: 1
    pattern:
      interval: 5  # A cada 5 níveis
      start: 5     # Começa no nível 5
```

### Ajustes Recomendados

**Para servidor casual (menos spam):**
```yaml
xp: 50.0
recharge_seconds: 300  # 5 minutos base
# Com Carisma 5 = 2.5 minutos
```

**Para servidor competitivo (mais recompensa):**
```yaml
xp: 200.0
recharge_seconds: 900  # 15 minutos base
# Com Carisma 5 = 7.5 minutos
```

**Para testes:**
```yaml
xp: 100.0
recharge_seconds: 60  # 1 minuto base
# Com Carisma 5 = 30 segundos
```

## 🎮 Exemplo de Uso no Jogo

### Jogador Iniciante (0 Carisma)

```
[00:00] Player: "oi galera!"
→ Bateria: 0% → XP: 0 (nada acontece)

[00:30] Player: "alguém quer minerar?"
→ Bateria: ~5% → XP: ~5 (silencioso)

[06:00] Player: "vou fazer uma farm de ferro"
→ Bateria: ~60% → XP: ~60 (ActionBar + Som!)

[10:00] Player: "preciso de ajuda aqui"
→ Bateria: 100% → XP: 100 (ActionBar + Som!)
```

### Jogador Experiente (5 Carisma - Nível 25+)

```
[00:00] Player: "oi galera!"
→ Bateria: 0% → XP: 0 (nada acontece)

[00:30] Player: "alguém quer minerar?"
→ Bateria: ~10% → XP: ~10 (silencioso)

[03:00] Player: "vou fazer uma farm"
→ Bateria: ~60% → XP: ~60 (ActionBar + Som!)

[05:00] Player: "preciso de ajuda"
→ Bateria: 100% → XP: 100 (ActionBar + Som!)
```

**Diferença:** O jogador experiente recarrega a bateria **2x mais rápido**!

## 🔧 Detalhes Técnicos

### Fórmula de Recarga com Carisma

```java
tempo_recarga_final = tempo_base * (1 - (carisma * 0.10))

Exemplos:
- 600s com 0 carisma = 600 * 1.0 = 600s
- 600s com 1 carisma = 600 * 0.9 = 540s
- 600s com 5 carisma = 600 * 0.5 = 300s
- 600s com 10 carisma = 600 * 0.0 = 0s (instantâneo!)
```

### Progressão Matemática

```
Nível 5:  1 Carisma → 10% mais rápido
Nível 10: 2 Carisma → 20% mais rápido
Nível 15: 3 Carisma → 30% mais rápido
Nível 20: 4 Carisma → 40% mais rápido
Nível 25: 5 Carisma → 50% mais rápido (metade do tempo!)
Nível 50: 10 Carisma → 100% mais rápido (instantâneo!)
```

### Thread Safety
- Usa `ConcurrentHashMap` para armazenar dados
- `ReentrantLock` para sincronização por jogador
- Cálculo de XP na thread assíncrona do chat
- Adição de XP na thread principal (obrigatório)

## 📊 Vantagens do Sistema

1. **Anti-Spam Natural:** Jogadores não ganham XP spammando chat
2. **Progressão Recompensadora:** Carisma alto = mais interações frequentes
3. **Feedback Inteligente:** XP baixo = silencioso, XP alto = visível
4. **Balanceamento Dinâmico:** Iniciantes esperam mais, veteranos interagem mais
5. **Performance:** Thread-safe, cálculos leves
6. **Incentivo para Upar:** Quanto maior o nível, mais útil fica a skill

## 🎯 Estratégias de Jogo

### Iniciante (0-4 Carisma)
- Foque em mensagens significativas
- Espere ~10 minutos entre conversas para XP máximo
- Use o tempo para fazer outras atividades

### Intermediário (1-3 Carisma)
- Pode conversar mais frequentemente
- ~7-9 minutos para recarga completa
- Balance conversa com outras skills

### Avançado (4-6 Carisma)
- Interações frequentes são viáveis
- ~4-6 minutos para recarga
- Participe ativamente de conversas

### Mestre (7+ Carisma)
- Recarga quase instantânea
- Pode conversar livremente
- Ganhe XP constantemente

## 🐛 Troubleshooting

**Carisma não está sendo concedido:**
- Verifique se `rewards/social.yml` existe em `plugins/meu_plugin/`
- Confirme que o stat está registrado (veja logs do console)
- Use `/skills stats` para verificar seus stats

**Recarga não está mais rápida:**
- Verifique seu nível de Carisma com `/skills stats`
- Lembre-se: só ganha Carisma nos níveis 5, 10, 15, 20, etc.
- Recarregue o plugin com `/reload confirm` (não recomendado) ou reinicie

**XP não está sendo adicionado:**
- Veja troubleshooting na seção anterior
- Carisma NÃO afeta o XP ganho, apenas o tempo de recarga

## 📝 Arquitetura do Sistema

```
Player fala no chat
    ↓
Calcula Carisma do player
    ↓
Ajusta tempo de recarga baseado no Carisma
    ↓
Calcula % da bateria recarregada
    ↓
XP = carga * xp_base
    ↓
Se XP >= 50: ActionBar + Som
Se XP < 50: Silencioso
```

## 🔄 Changelog

### v1.1 - Sistema de Carisma
- ✨ Adicionado stat Carisma
- ✨ Carisma reduz tempo de recarga (10% por ponto)
- ✨ Rewards automáticos: +1 Carisma a cada 5 níveis
- 📝 Documentação atualizada
- 🎨 Símbolo especial para Carisma: ✦

### v1.0 - Lançamento Inicial
- ✨ Sistema de bateria social
- ✨ XP silencioso para ganhos baixos
- ✨ Level up automático
- 📊 Thread-safe com ReentrantLock

---

**Desenvolvido por:** jnetu  
**Versão:** 1.1  
**Compatibilidade:** AuraSkills 2.3.10+, Minecraft 1.21+
