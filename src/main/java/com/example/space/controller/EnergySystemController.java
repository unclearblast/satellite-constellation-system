package com.example.spacecenter.controller;

import com.example.spacecenter.domain.energy.EnergySystem;
import com.example.spacecenter.repository.EnergySystemRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/energy-systems")
@RequiredArgsConstructor
public class EnergySystemController {

    private final EnergySystemRepository energySystemRepository;

    @GetMapping
    public List<EnergySystem> getAll() {
        return energySystemRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnergySystem> getById(@PathVariable Long id) {
        return energySystemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EnergySystem> create(@Valid @RequestBody EnergySystem energySystem) {
        EnergySystem saved = energySystemRepository.save(energySystem);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnergySystem> update(@PathVariable Long id,
                                               @Valid @RequestBody EnergySystem energySystem) {
        return energySystemRepository.findById(id)
                .map(existing -> {
                    existing.setPowerCapacityWatts(energySystem.getPowerCapacityWatts());
                    existing.setCurrentPowerWatts(energySystem.getCurrentPowerWatts());
                    existing.setBatteryCapacityWattHours(energySystem.getBatteryCapacityWattHours());
                    existing.setStatus(energySystem.getStatus());
                    return ResponseEntity.ok(energySystemRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (energySystemRepository.existsById(id)) {
            energySystemRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
