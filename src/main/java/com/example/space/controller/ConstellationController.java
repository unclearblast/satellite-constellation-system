package com.example.spacecenter.controller;

import com.example.spacecenter.domain.constellation.SatelliteConstellation;
import com.example.spacecenter.repository.ConstellationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/constellations")
@RequiredArgsConstructor
public class ConstellationController {

    private final ConstellationRepository constellationRepository;

    @GetMapping
    public List<SatelliteConstellation> getAll() {
        return constellationRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SatelliteConstellation> getById(@PathVariable Long id) {
        return constellationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SatelliteConstellation> create(@Valid @RequestBody SatelliteConstellation constellation) {
        SatelliteConstellation saved = constellationRepository.save(constellation);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SatelliteConstellation> update(@PathVariable Long id,
                                                         @Valid @RequestBody SatelliteConstellation constellation) {
        return constellationRepository.findById(id)
                .map(existing -> {
                    existing.setName(constellation.getName());
                    existing.setDescription(constellation.getDescription());
                    return ResponseEntity.ok(constellationRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (constellationRepository.existsById(id)) {
            constellationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
