-- Таблица для Transactional Outbox
CREATE TABLE IF NOT EXISTS outbox (
    id BIGSERIAL PRIMARY KEY,
    aggregate_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    payload JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
);

-- Индекс для ускорения выборки PENDING записей
CREATE INDEX IF NOT EXISTS idx_outbox_status ON outbox(status);
