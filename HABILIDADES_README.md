## Nova Habilidade

```java
public class VooHabilidade implements Habilidade {
    // Implementar métodos da interface
}
```

#### registre no HabilidadesManager:

```java
registrarHabilidade(new VooHabilidade(plugin));
```

## Novo Item:
```java
public class ArcoFogoItem implements ItemCustomizado {
    // Implementar métodos da interface
}
```

#### registre no ItensManager:
```java
registrarItem(new ArcoFogoItem(plugin));
```