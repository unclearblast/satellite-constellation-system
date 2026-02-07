
---

## 📁 project-structure.txt

```txt
satellite-system
│
├── build.gradle.kts
├── settings.gradle.kts
├── README.md
│
├── src
│   ├── main
│   │   └── java
│   │       └── seminars
│   │           │
│   │           ├── Main.java
│   │           │
│   │           ├── domain
│   │           │   ├── constellation
│   │           │   │   └── SatelliteConstellation.java
│   │           │   │
│   │           │   └── satellite
│   │           │       ├── Satellite.java
│   │           │       ├── CommunicationSatellite.java
│   │           │       ├── ImagingSatellite.java
│   │           │       ├── EnergySystem.java
│   │           │       └── SatelliteState.java
│   │           │
│   │           ├── repository
│   │           │   └── ConstellationRepository.java
│   │           │
│   │           └── service
│   │               └── SpaceOperationCenterService.java
│   │
│   └── test
│       └── java
│           └── seminars
│               │
│               ├── ConstellationRepositoryUnitTest.java
│               ├── ConstellationRepositoryMockTest.java
│               ├── ConstellationRepositoryIntegrationTest.java
│               │
│               ├── SpaceOperationCenterServiceMockTest.java
│               └── SpaceOperationCenterServiceIntegrationTest.java
│
└── build
    └── reports
        ├── tests
        │   └── test
        │       └── index.html
        │
        └── jacoco
            └── test
                └── html
                    └── index.html
