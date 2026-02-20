satellite-constellation-system/
│
├── build.gradle.kts
├── settings.gradle.kts
│
└── src/main/java/com/example/space/
    ├── Main.java
    │
    ├── domain/
    │   ├── constellation/
    │   │   └── SatelliteConstellation.java
    │   │
    │   └── satellite/
    │       ├── Satellite.java
    │       ├── SatelliteState.java
    │       ├── EnergySystem.java
    │       ├── CommunicationSatellite.java
    │       └── ImagingSatellite.java
    │
    ├── factory/
    │   ├── SatelliteFactory.java
    │   ├── CommunicationSatelliteFactory.java
    │   └── ImagingSatelliteFactory.java
    │
    ├── repository/
    │   └── ConstellationRepository.java
    │
    └── service/
        └── SpaceOperationCenterService.java
