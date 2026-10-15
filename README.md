# Battleship with Java

Jogo de Batalha Naval desenvolvido em Java para dois jogadores no mesmo terminal. Cada jogador posiciona sua frota em um tabuleiro de 10 × 10 e alterna os disparos até afundar todos os navios adversários.

## Funcionalidades

- Partida local para dois jogadores.
- Posicionamento horizontal ou vertical dos cinco navios de cada frota.
- Validação de coordenadas, tamanho dos navios e distância entre embarcações.
- Tabuleiro adversário com navios ocultos (fog of war).
- Identificação de acertos, erros, navios afundados e vitória.
- Troca de turno com confirmação pela tecla Enter.

## Requisitos

- JDK 8 ou superior, com `java` e `javac` disponíveis no terminal.
- Git para clonar o repositório.

O jogo utiliza apenas a biblioteca padrão do Java e não requer dependências externas.

## Como executar

Clone o repositório e entre na pasta:

```bash
git clone https://github.com/fhenrique77/Battleship-with-Java.git
cd Battleship-with-Java
```

Compile o código a partir da raiz do repositório:

```bash
javac -encoding UTF-8 -d out Main.java
```

Inicie o jogo:

```bash
java -cp out battleship.Main
```

Os comandos também funcionam no PowerShell. A opção `-d out` cria a estrutura do pacote `battleship` dentro da pasta de saída.

## Como jogar

1. O jogador 1 posiciona os cinco navios, informando as coordenadas inicial e final separadas por espaço, como `A1 A5`.
2. Ao terminar, pressiona Enter e passa o terminal para o jogador 2, que posiciona sua própria frota.
3. Em cada turno, o jogador vê o tabuleiro adversário com os navios ocultos e seu próprio tabuleiro completo.
4. Para disparar, informa uma coordenada, como `B7`.
5. Após o disparo, pressiona Enter para passar a vez. Vence quem atingir as 17 posições ocupadas pela frota adversária.

As mensagens exibidas pelo jogo estão em inglês.

### Coordenadas e posicionamento

- As linhas vão de `A` a `J` e as colunas de `1` a `10`.
- Letras minúsculas também são aceitas.
- Os navios devem ocupar uma linha ou uma coluna, com o tamanho exato solicitado.
- As coordenadas podem ser fornecidas em qualquer ordem: `A1 A5` e `A5 A1` representam o mesmo posicionamento.
- Navios não podem se sobrepor nem encostar, inclusive pelas diagonais.
- Coordenadas inválidas solicitam uma nova tentativa.
- Um disparo repetido não conta como um novo acerto, mas consome o turno.

### Frota de cada jogador

| Navio no jogo | Nome em português | Tamanho |
| --- | --- | --- |
| Aircraft Carrier | Porta-aviões | 5 posições |
| Battleship | Encouraçado | 4 posições |
| Submarine | Submarino | 3 posições |
| Cruiser | Cruzador | 3 posições |
| Destroyer | Contratorpedeiro | 2 posições |

### Símbolos do tabuleiro

| Símbolo | Significado |
| --- | --- |
| `~` | Água ou posição ainda desconhecida no tabuleiro adversário |
| `O` | Parte de um navio, visível no próprio tabuleiro |
| `X` | Parte de um navio atingida |
| `M` | Disparo na água |

## Estrutura do projeto

```text
Battleship-with-Java/
├── Main.java
├── README.md
└── .gitignore
```

`Main.java` declara o pacote `battleship` e reúne a lógica de posicionamento, exibição dos tabuleiros, disparos e alternância dos jogadores. As classes internas `Player` e `Ship` armazenam o estado dos jogadores e das embarcações.

## Tecnologias e conceitos

- Java e biblioteca padrão (`Scanner`, `Arrays` e `Locale`).
- Matrizes para representar tabuleiros e identificar navios.
- Classes para organizar o estado da partida.
- Expressões regulares para validar coordenadas.
- Laços e condicionais para controlar turnos e verificar a vitória.

## Autor

[Felipe Henrique Oliveira Sousa](https://github.com/fhenrique77)
