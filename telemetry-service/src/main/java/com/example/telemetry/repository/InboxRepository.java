package com.example.telemetry.repository;

import com.example.telemetry.domain.inbox.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboxRepository extends JpaRepository<Inbox, String> {
    boolean existsById(String eventId);
}
