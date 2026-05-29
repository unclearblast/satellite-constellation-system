package com.example.spacecenter.service;

import com.example.spacecenter.domain.outbox.Outbox;
import com.example.spacecenter.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class OutboxScheduler {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${outbox.scheduler.fixed-delay:5000}")
    private long fixedDelay;

    @Scheduled(fixedDelayString = "${outbox.scheduler.fixed-delay:5000}")
    @Transactional
    public void processOutbox() {
        List<Outbox> pendingRecords = outboxRepository.findByStatus(Outbox.OutboxStatus.PENDING);
        if (pendingRecords.isEmpty()) {
            return;
        }
        log.info("Processing {} pending outbox records", pendingRecords.size());

        for (Outbox record : pendingRecords) {
            try {
                // Отправляем в Kafka. Ключ сообщения = aggregateId (satellite_id) для идемпотентности
                kafkaTemplate.send("satellite-events", record.getAggregateId(), record.getPayload());
                outboxRepository.updateStatus(record.getId(), Outbox.OutboxStatus.SENT);
                log.info("Outbox record {} sent to Kafka", record.getId());
            } catch (Exception e) {
                log.error("Failed to send outbox record {} to Kafka", record.getId(), e);
                // Оставляем статус PENDING – повторим в следующий раз
            }
        }
    }
}
