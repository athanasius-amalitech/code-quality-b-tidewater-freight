# Tidewater Freight: Shipping Cost Service

Prices a shipment from its zone, weight and service level. The starter version was
AI-written: it priced errors at GHS 0 and its tests could not fail. This version
fails loudly, prices in `BigDecimal`, and is guarded by four build gates.

- [ANALYSIS.md](ANALYSIS.md): what was wrong, ranked, written before any change.
- [RATIONALE.md](RATIONALE.md): the decisions, how a new zone slots in, what was kept, fixed and rewritten.

## Running it

JDK 21 and Maven. No quoting needed: the commands were run as written in PowerShell,
cmd and Git Bash.

Demo:

```bash
mvn compile exec:java
```

Build with every gate (tests, coverage, mutation testing, style, format):

```bash
mvn clean verify
```

Fix formatting before a commit:

```bash
mvn formatter:format
```

Reports after `mvn verify`: `target/site/jacoco/index.html` (coverage) and
`target/pit-reports/index.html` (mutation testing).

## What the demo prints

Prices keep their values, now with two decimals. The unknown zone fails instead of
costing 0. The calculator no longer prints its own lines.

```diff
- Shipping DOMESTIC (10.0kg, STANDARD): GHS 50.0
- Result DOMESTIC 10kg STANDARD       -> 50.0
+ Result DOMESTIC 10kg STANDARD       -> 50.00
- Shipping INTERNATIONAL (40.0kg, EXPRESS): GHS 967.5
- Result INTERNATIONAL 40kg EXPRESS   -> 967.5
+ Result INTERNATIONAL 40kg EXPRESS   -> 967.50
- Result unknown zone MOON 10kg STD   -> 0.0
+ Could not price shipment: Unknown zone: MOON
```

## Code layout

Packages by feature, under `com.tidewater.freight`.

| Package | Contents | Job |
|---|---|---|
| (root) | `ShippingException` | Base of every failure, so a caller can catch them all in one place |
| `shipment` | `Shipment`, `ServiceLevel`, `InvalidShipmentException` | What is being sent; rejects bad input on construction |
| `tariff` | `ZoneTariff`, `FlatRateTariff`, `TariffRepository`, `InMemoryTariffRepository`, `PropertiesTariffLoader`, `UnknownZoneException`, `TariffConfigurationException` | What each zone charges, read from `src/main/resources/tariffs.properties` |
| `pricing` | `ShippingCalculator` | Tariff, heavy surcharge, service level, rounding |
| `demo` | `Demo` | Example run; excluded from coverage and mutation gates |

## Build gates

`mvn verify` fails if any of these fails. Details in [RATIONALE.md](RATIONALE.md#6-improvement-beyond-the-brief-the-standard-enforces-itself).

| Gate | Checks |
|---|---|
| Format | Every file matches the shared [AmaliTech formatter profile](https://github.com/athanasius-amalitech/vscode-java-formatter), pinned to one commit in `pom.xml` |
| Style | Checkstyle rules from the standard, in [config/checkstyle.xml](config/checkstyle.xml) |
| Coverage | 100% of lines and branches |
| Mutation | At least 90% of PIT mutants killed |

## Adding a zone

| Zone prices like... | Change |
|---|---|
| The others (rate per kg plus a fee) | One line in `tariffs.properties`, for example `COASTAL=12,15` |
| Something new | A class that `implements ZoneTariff`, added to the map the repository is built from |

Add a test with the agreed price. No Java changes for a flat-rate zone, and the
calculator never changes.
