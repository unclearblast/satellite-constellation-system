package com.example.spacecenter.service;

import com.example.spacecenter.domain.Constellation;
import com.example.spacecenter.domain.Satellite;
import com.example.spacecenter.repository.ConstellationRepository;
import com.example.spacecenter.repository.SatelliteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SatelliteService {

    private final SatelliteRepository satelliteRepository;
    private final ConstellationRepository constellationRepository;

    // 1. Кэширование спутника по ID (10 мин)
    @Cacheable(value = "satellite", key = "#id")
    public Optional<Satellite> getSatelliteById(Long id) {
        return satelliteRepository.findById(id);
    }

    // 2. Кэширование группировки по имени (15 мин)
    @Cacheable(value = "constellation", key = "#name")
    public Optional<Constellation> getConstellationByName(String name) {
        return constellationRepository.findByName(name);
    }

    // 3. Кэширование полного списка спутников (5 мин)
    @Cacheable(value = "satellites", key = "'all'")
    public List<Satellite> getAllSatellites() {
        return satelliteRepository.findAll();
    }

    // Кастомный ключ: поиск спутника по имени в группировке
    @Cacheable(value = "satellite",
               key = "#constellationName + '::' + #satelliteName")
    public Optional<Satellite> findByConstellationAndName(String constellationName, String satelliteName) {
        return satelliteRepository.findByConstellationNameAndName(constellationName, satelliteName);
    }

    // ---- Инвалидация кэша ----

    // Создание спутника: очищаем весь кэш satellites::all
    @Transactional
    @CacheEvict(value = "satellites", allEntries = true)
    public Satellite createSatellite(Satellite satellite) {
        return satelliteRepository.save(satellite);
    }

    // Обновление спутника: удаляем из кэша satellite::{id}
    @Transactional
    @CacheEvict(value = "satellite", key = "#satellite.id")
    public Satellite updateSatellite(Satellite satellite) {
        return satelliteRepository.save(satellite);
    }

    // Удаление спутника: удаляем satellite::{id} и очищаем satellites::all
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "satellite", key = "#id"),
            @CacheEvict(value = "satellites", allEntries = true)
    })
    public void deleteSatellite(Long id) {
        satelliteRepository.deleteById(id);
    }

    // Изменение состава группировки: удаляем кэш группировки и общий список
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "constellation", key = "#name"),
            @CacheEvict(value = "satellites", allEntries = true)
    })
    public void updateConstellationComposition(String name, List<Long> satelliteIds) {
        Constellation constellation = constellationRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Constellation not found"));
        List<Satellite> satellites = satelliteRepository.findAllById(satelliteIds);
        constellation.setSatellites(satellites);
        constellationRepository.save(constellation);
    }
}
