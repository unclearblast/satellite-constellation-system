package com.example.spacecenter.controller;

import com.example.spacecenter.domain.satellite.Satellite;
import com.example.spacecenter.repository.SatelliteRepository;
import com.example.spacecenter.repository.ConstellationRepository;
import com.example.spacecenter.repository.EnergySystemRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/satellites")
@RequiredArgsConstructor
public class SatelliteController {

    private final SatelliteRepository satelliteRepository;
    private final ConstellationRepository constellationRepository;
    private final EnergySystemRepository energySystemRepository;

    @GetMapping
    public List<Satellite> getAll() {
        return satelliteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Satellite> getById(@PathVariable Long id) {
        return satelliteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Satellite> create(@Valid @RequestBody Satellite satellite) {
        // Убедимся, что энергосистема сохранена (каскад из Satellite)
        Satellite saved = satelliteRepository.save(satellite);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Satellite> update(@PathVariable Long id,
                                            @Valid @RequestBody Satellite satellite) {
        return satelliteRepository.findById(id)
                .map(existing -> {
                    existing.setName(satellite.getName());
                    existing.setLaunchDate(satellite.getLaunchDate());
                    existing.setState(satellite.getState());
                    // Для простоты не обновляем связи, можно расширить
                    return ResponseEntity.ok(satelliteRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (satelliteRepository.existsById(id)) {
            satelliteRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/assign-constellation/{constellationId}")
    public ResponseEntity<Satellite> assignConstellation(@PathVariable Long id,
                                                         @PathVariable Long constellationId) {
        var satOpt = satelliteRepository.findById(id);
        var conOpt = constellationRepository.findById(constellationId);
        if (satOpt.isPresent() && conOpt.isPresent()) {
            Satellite sat = satOpt.get();
            sat.setConstellation(conOpt.get());
            return ResponseEntity.ok(satelliteRepository.save(sat));
        }
        return ResponseEntity.notFound().build();
    }
}
