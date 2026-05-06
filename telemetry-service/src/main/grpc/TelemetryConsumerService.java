package com.example.spacecenter.service;

import com.example.spacecenter.domain.satellite.Satellite;
import com.example.spacecenter.repository.SatelliteRepository;
import com.example.telemetry.proto.TelemetryRequest;
import com.example.telemetry.proto.TelemetryServiceGrpc;
import io.grpc.ManagedChannel;
import jakarta.annotation.PostConstruct;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class TelemetryConsumerService {

    private static final Logger log = LoggerFactory.getLogger(TelemetryConsumerService.class);

    @GrpcClient("telemetry-service")
    private TelemetryServiceGrpc.TelemetryServiceStub asyncStub;

    private final SatelliteRepository satelliteRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public TelemetryConsumerService(SatelliteRepository satelliteRepository) {
        this.satelliteRepository = satelliteRepository;
    }

    @PostConstruct
    public void startStreaming() {
        executor.submit(() -> {
            log.info("Starting telemetry streaming from gRPC server");
            TelemetryRequest request = TelemetryRequest.newBuilder().build();
            asyncStub.streamTelemetry(request, new TelemetryStreamObserver());
        });
    }

    private class TelemetryStreamObserver implements io.grpc.stub.StreamObserver<com.example.telemetry.proto.TelemetryUpdate> {
        @Override
        public void onNext(com.example.telemetry.proto.TelemetryUpdate update) {
            Long satId = update.getSatelliteId();
            Double inside = update.getInsideTemperature();
            Double outside = update.getOutsideTemperature();
            log.debug("Received telemetry: sat {}, in={}, out={}", satId, inside, outside);

            satelliteRepository.findById(satId).ifPresent(sat -> {
                sat.setInsideTemperature(inside);
                sat.setOutsideTemperature(outside);
                satelliteRepository.save(sat);
                log.info("Updated satellite {} temperatures", satId);
            });
        }

        @Override
        public void onError(Throwable t) {
            log.error("Telemetry stream error", t);
        }

        @Override
        public void onCompleted() {
            log.info("Telemetry stream completed");
        }
    }
}
