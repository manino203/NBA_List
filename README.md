# NBA List

## Popis projektu

Cieľom tejto aplikácie je zobraziť zoznam hráčov NBA spolu s ich základnými informáciami, ako je meno, priezvisko, pozícia a klub, v ktorom hráč pôsobí. Po dosiahnutí konca zoznamu sa načíta ďalších 35 hráčov. Po kliknutí na konkrétneho hráča sa zobrazí detailný profil s podrobnými informáciami o ňom. Z detailu hráča je tiež možné zobraziť detail klubu, kde nájdete ďalšie informácie o tíme, za ktorý hrá.

Aplikácia využíva API službu [BALLDONTLIE](https://app.balldontlie.io/) na získavanie informácií o hráčoch.

## Technológie

Aplikácia je naprogramovaná v jazyku **Kotlin** a dodržiava design pattern **MVVM**.  Použité technológie:

- **Retrofit**: Pre sieťovú komunikáciu s API.
- **Android Jetpack Compose**: UI
- **Koin**:  Dependency injection

## Inštrukcie na spustenie

1. Naklonujte si tento repozitár:
   ```bash  
     git clone https://github.com/manino203/NBA_List.git
     
2.  Skopírujte svoj API kľúč do súboru:  `/app/src/main/java/com/example/nba_list/data/network/ApiKey.kt`

3. Spustite aplikáciu na fyzickom zariadení alebo emulátore.