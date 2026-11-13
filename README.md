# Villager Growth (Fabric, Minecraft Java 1.21.11)

Rechtsklik een baby villager met een golden carrot:
- Pasgeboren baby: 20 golden carrots om volwassen te worden.
- Elke carrot verkort de resterende groeitijd met 1200 ticks (1/20 van 24000),
  dus hoe verder de baby al is, hoe minder carrots er nog nodig zijn.
- Actionbar toont hoeveel carrots er nog nodig zijn.

## Bouwen
Java 21 nodig. Gebruik bij voorkeur de Fabric template-generator
(https://fabricmc.net/develop/template) voor een werkende Gradle wrapper en actuele versies,
en kopieer daarna `src/` erin. Of: `gradle wrapper && ./gradlew build`.
De jar staat dan in build/libs/.
