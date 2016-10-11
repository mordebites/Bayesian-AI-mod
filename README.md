# minecraft_lily

** ITALIANO **

## Descrizione:
Una mod per Minecraft incentrata su Lily, un NPC che sfida il giocatore ad un minigioco stealth.
Scopo del gioco è trovare e colpire Lily in un labirinto senza che la sua allerta arrivi al massimo e prima che finisca il tempo.
L'IA di Lily usa una rete bayesiana per decidere che azione intraprendere all'istante successivo (si può impostare anche l'uso di un albero decisionale o di un automa a stati finiti equivalenti alla rete).

---

## Come usare la mod:
Al momento la mod non è stata esportata per funzionare nel Forge Mod Loader, il codice sorgente va usato nell'ambiente di sviluppo dotato di workspace di Forge (developer kit) versione 1.9 e funziona con Minecraft 1.9.

La mod va usata ESCLUSIVAMENTE IN SINGLE PLAYER.

---

## Che mappa usare:
Nella cartella "map saves" si trova un file compresso, il cui contenuto va estratto e inserito in run/saves del proprio workspace di Forge. 

La mod funziona ESCLUSIVAMENTE PER LA MAPPA FORNITA.
 
---

## Come giocare:
La mod funziona correttamente solo in modalità CREATIVA. Per giocare, usare l'uovo di Lily - è giallo e rosa, se non è presente nell'inventario del giocatore, cercare "lily" nella barra di ricerca - per spawnarla nel labirinto la cui entrata è alle coordinate 187, 4, 690. Premere M per giocare e seguire le indicazioni a schermo.

---

## Dipendenze:
Il codice fa uso di SMILE, una libreria per gestire reti bayesiane sviluppata da BayesFusion, LLC, usata nel progetto con licenza accademica. Il wrapper per Java jSMILE va incluso nel build path del progetto che fa uso del codice sorgente.
Visitare https://download.bayesfusion.com/files.html?category=Academia per ulteriori informazioni.

------------------------------

** ENGLISH **

## Description:
A Minecraft mod focused on Lily, a NPC who challenges the player to beat her in a stealth minigame.
Goal of the game is to find and hit Lily in a labyrinth  without her alert level reaching maximum and before time is over.
Lily's AI uses a Bayesian net to decide which action to perform during next instant. You can set the AI to use a decision tree or a finite state machine that behave like the net.

--- 

## How to use the mod:
At the moment, the mod has not been exported to work in Forge Mod Loader, the source code must be used in the IDE set with the Forge workspace (developer kit) version 1.9 with Minecraft 1.9.

The mod works EXCLUSIVELY IN SINGLE PLAYER MODE.

---

## Which map to use:
The folder "map saves" contains a compressed file, the content of which must be extracted and inserted into the run/saves folder of your Forge workspace.

The mod works EXCLUSIVELY WITH THE PROVIDED MAP.

---

## How to play:
The mod works ONLY IN CREATIVE MODE. To player, spawn the yellow and pink egg - if it's not in player's inventory, search "lily" in the search bar - in the labyrinth; the door is at coordinates 187, 4, 690. Press M to play then follow the instructions on the screen.

---

## Dependencies:
The code uses SMILE, a library to handler bayesian networks developed by BayesFusion, LLC, used in the project with academic license. The Java wrapper jSMILE must be included in the build path of the project.
Visit https://download.bayesfusion.com/files.html?category=Academia for more info about the library.