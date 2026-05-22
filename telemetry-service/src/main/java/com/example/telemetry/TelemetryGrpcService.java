package com.example.telemetry;

import com.example.telemetry.proto.TelemetryRequest;
import com.example.telemetry.proto.TelemetryUpdate;
import com.example.telemetry.proto.TelemetryServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@GrpcService
public class TelemetryGrpcService extends TelemetryServiceGrpc.TelemetryServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(TelemetryGrpcService.class);
    private final Random random = new Random();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // Список спутников, которые отслеживаем (ID)
    private final List<Long> satelliteIds = List.of(1L, 2L, 3L);

    @Override
    public void streamTelemetry(TelemetryRequest request,
                                StreamObserver<TelemetryUpdate> responseObserver) {
        log.info("New client connected to telemetry stream");

        final AtomicLong counter = new AtomicLong(0);
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                // Для каждого спутника генерируем свои показатели
                for (Long satId : satelliteIds) {
                    double insideTemp = 15 + random.nextDouble() * 20;   // 15..35 °C
                    double outsideTemp = -30 + random.nextDouble() * 100; // -30..70 °C
                    long timestamp = System.currentTimeMillis();

                    TelemetryUpdate update = TelemetryUpdate.newBuilder()
                            .setSatelliteId(satId)
                            .setInsideTemperature(insideTemp)
                            .setOutsideTemperature(outsideTemp)
                            .setTimestamp(timestamp)
                            .build();

                    responseObserver.onNext(update);
                    log.debug("Sent telemetry for satellite {}: in={}, out={}",
                            satId, insideTemp, outsideTemp);
                }

                // Остановка после 60 итераций (демонстрация, можно бесконечно)
                if (counter.incrementAndGet() >= 60) {
                    log.info("Telemetry stream completed after 2 minutes");
                    responseObserver.onCompleted();
                    scheduler.shutdown();
                }
            }
        };

        // Публикация каждые 2 секунды
        scheduler.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);

        // Отменяем планировщик при отмене стрима клиентом
        responseObserver.setOnCancelHandler(() -> {
            log.warn("Client cancelled telemetry stream");
            scheduler.shutdown();
        });
    }
}
