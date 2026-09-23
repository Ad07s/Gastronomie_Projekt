--Gastronomie REST-API--

Dieses Backend-Projekt ist eine REST-Schnittstelle zur Verwaltung von Gastronomiebetrieben, Speisekarten und Adressen. Ich habe die Anwendung im Rahmen des Moduls Software-Architektur an der Hochschule Karlsruhe entwickelt, um zentrale Architekturmuster, Concurrency-Konzepte und saubere Datenfluss-Strukturen mit modernem Java und Spring Boot praktisch umzusetzen.
Architektur und Entwurfsentscheidungen
Die Anwendung ist strikt als Drei-Schichten-Architektur (Web/Controller, Service/Logik, Repository/Persistenz) aufgebaut und folgt Kernkonzepten des Domain-Driven Designs (DDD).

1. Domain-Driven Design und Aggregate Roots
Aggregate Root: Die Entität Restaurant fungiert als zentrale Aggregatwurzel.

Gekapselter Zugriff: Die Entitäten Adresse (1:1) und Speise (1:N) besitzen keine eigenen Spring-Data-Repositories. Sämtliche Schreib- und Lesezugriffe laufen ausschließlich über das Restaurant, um fachliche Konsistenz sicherzustellen.

2. Entkopplung über DTOs und MapStruct
Unveränderlichkeit: Für den Datenaustausch über HTTP verwende ich Java Records als DTOs, um Immutability zu garantieren.   

Keine Entity-Exponierung: Interne JPA-Entitäten werden nicht an den Client weitergegeben.   

MapStruct: Die Transformation zwischen Entitäten und DTOs erfolgt zur Compile-Zeit über MapStruct, was Reflection-Overhead zur Laufzeit vermeidet.
Validierung: Validierungsregeln (@NotNull, @NotBlank, etc.) greifen defensiv direkt bei eingehenden Requests am Controller.

3. Concurrency und Caching
Optimistic Locking: Zur Vermeidung von Lost Updates bei gleichzeitigen Schreibzugriffen nutze ich eine @Version-Spalte auf der Entitätsebene.   

ETags und HTTP-Header:
Lesezugriffe (GET) liefern einen ETag basierend auf der aktuellen Version im Header mit.   

Aktualisierungen (PUT) verlangen zwingend einen passenden If-Match-Header. Weicht die Version ab, bricht der Request ab und wirft eine OptimisticLockingFailureException.   

Bedingte GET-Requests mit If-None-Match antworten mit 304 Not Modified, falls sich der Datenbestand nicht geändert hat.

4. Persistenz und Performance
Lazy Loading: Relationale Beziehungen sind standardmäßig auf fetch = FetchType.LAZY gesetzt, um unnötige Joins zu verhindern.   

Fetch-Joins: Um das N+1-Query-Problem sowie LazyInitializationExceptions bei der Serialisierung zu vermeiden, lade ich abhängige Daten über gezielte JOIN FETCH-Abfragen im Repository.   

Reihenfolge von Listen: Die Speisekarte verwendet @OrderColumn, um die exakte Position der Speisen persistent in einer Index-Spalte abzuspeichern.   

Transaktionsgrenzen: Geschäftslogik im Service ist über @Transactional abgesichert, sodass bei unerwarteten Fehlern ein automatisches Rollback greift.   

Tech Stack
Backend: Java 21+, Spring Boot 3 (Spring Web, Spring Data JPA, Bean Validation)
Persistenz: PostgreSQL, Hibernate
Migration & Tooling: Flyway, Docker Compose, Gradle   
Mapping & Tests: MapStruct, JUnit 5, Mockito
API-Tests: Bruno

Lokales Setup
Voraussetzungen
Docker und Docker Compose
JDK 21 oder höher

1. Repository klonen
   
