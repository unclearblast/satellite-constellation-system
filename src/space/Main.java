
---

# 📄 src/space/Main.java

```java
package space;

import space.model.*;

public class Main {

    public static void main(String[] args) {

        System.out.println("ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ");
        System.out.println("============================================================");

        System.out.println("СОЗДАНИЕ СПЕЦИАЛИЗИРОВАННЫХ СПУТНИКОВ:");
        System.out.println("---------------------------------------------");

        CommunicationSatellite c1 =
                new CommunicationSatellite("Связь-1", 0.85, 500);
        System.out.println("Создан спутник: Связь-1 (заряд: 85%)");

        CommunicationSatellite c2 =
                new CommunicationSatellite("Связь-2", 0.75, 1000);
        System.out.println("Создан спутник: Связь-2 (заряд: 75%)");

        ImagingSatellite i1 =
                new ImagingSatellite("ДЗЗ-1", 0.92, 2.5);
        System.out.println("Создан спутник: ДЗЗ-1 (заряд: 92%)");

        ImagingSatellite i2 =
                new ImagingSatellite("ДЗЗ-2", 0.45, 1.0);
        System.out.println("Создан спутник: ДЗЗ-2 (заряд: 45%)");

        ImagingSatellite i3 =
                new ImagingSatellite("ДЗЗ-3", 0.15, 0.5);
        System.out.println("Создан спутник: ДЗЗ-3 (заряд: 15%)");

        System.out.println("---------------------------------------------");

        SatelliteConstellation constellation =
                new SatelliteConstellation("RU Basic");
        System.out.println("Создана спутниковая группировка: RU Basic");
        System.out.println("---------------------------------------------");

        System.out.println("ФОРМИРОВАНИЕ ГРУППИРОВКИ:");
        System.out.println("-----------------------------------");

        constellation.addSatellite(c1);
        constellation.addSatellite(c2);
        constellation.addSatellite(i1);
        constellation.addSatellite(i2);
        constellation.addSatellite(i3);

        System.out.println("-----------------------------------");
        System.out.println(constellation.getSatellites());
        System.out.println("-----------------------------------");

        System.out.println("АКТИВАЦИЯ СПУТНИКОВ:");
        System.out.println("-------------------------");

        for (Satellite satellite : constellation.getSatellites()) {
            if (satellite.activate()) {
                System.out.println("✅ " + satellite.getName() + ": Активация успешна");
            } else {
                System.out.println("🛑 " + satellite.getName()
                        + ": Ошибка активации (заряд: "
                        + (int) (satellite.getBatteryLevel() * 100) + "%)");
            }
        }

        System.out.println("ВЫПОЛНЕНИЕ МИССИЙ ГРУППИРОВКИ RU BASIC");
        System.out.println("==================================================");

        constellation.executeAllMissions();

        System.out.println(constellation.getSatellites());
    }
}
