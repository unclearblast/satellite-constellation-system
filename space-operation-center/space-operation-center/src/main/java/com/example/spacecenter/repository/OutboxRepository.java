package com.example.spacecenter.repository;

import com.example.spacecenter.domain.outbox.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    List<Outbox> findByStatus(Outbox.OutboxStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Outbox o SET o.status = ?2 WHERE o.id = ?1")
    void updateStatus(Long id, Outbox.OutboxStatus status);
}
