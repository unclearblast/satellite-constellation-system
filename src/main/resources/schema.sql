-- space-operation-center/src/main/resources/schema.sql
-- Создание таблиц вручную, придерживаясь 3НФ

CREATE TABLE IF NOT EXISTS satellite_constellation (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_constellation_name ON satellite_constellation(name);

-- Таблица для иерархии спутников (стратегия SINGLE_TABLE)
CREATE TABLE IF NOT EXISTS satellite (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    launch_date DATE,
    state VARCHAR(20) NOT NULL,
    satellite_type VARCHAR(31) NOT NULL, -- дискриминатор для наследования
    -- поля CommunicationSatellite
    frequency_band VARCHAR(50),
    transponder_count INTEGER,
    -- поля ImagingSatellite
    resolution_meters DOUBLE PRECISION,
    sensor_type VARCHAR(50),
    -- связь с группировкой
    constellation_id BIGINT REFERENCES satellite_constellation(id) ON DELETE SET NULL,
    -- связь с энергосистемой (OneToOne)
    energy_system_id BIGINT UNIQUE
);

CREATE INDEX idx_satellite_constellation ON satellite(constellation_id);
CREATE INDEX idx_satellite_state ON satellite(state);

CREATE TABLE IF NOT EXISTS energy_system (
    id BIGSERIAL PRIMARY KEY,
    power_capacity_watts DOUBLE PRECISION NOT NULL,
    current_power_watts DOUBLE PRECISION NOT NULL,
    battery_capacity_watt_hours DOUBLE PRECISION,
    status VARCHAR(20) NOT NULL,
    satellite_id BIGINT UNIQUE -- обратная ссылка для OneToOne
);

ALTER TABLE satellite 
ADD CONSTRAINT fk_satellite_energy_system 
FOREIGN KEY (energy_system_id) REFERENCES energy_system(id) ON DELETE SET NULL;
