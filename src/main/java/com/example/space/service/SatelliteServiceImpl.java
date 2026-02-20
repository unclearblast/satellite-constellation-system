package com.example.space.service;

import com.example.space.domain.satellite.Satellite;
import com.example.space.exception.SpaceOperationException;
import com.example.space.factory.SatelliteFactory;
import com.example.space.param.SatelliteParam;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SatelliteServiceImpl implements SatelliteService {

    private final List<SatelliteFactory> factories;

    public SatelliteServiceImpl(List<SatelliteFactory> factories) {
        this.factories = factories;
    }

    @Override
    public Satellite createSatellite(SatelliteParam param) {

        return factories.stream()
                .filter(factory -> factory.isSatelliteTypeSupported(param.getType()))
                .findFirst()
                .orElseThrow(() ->
                        new SpaceOperationException("Фабрика для типа "
                                + param.getType() + " не найдена"))
                .createSatelliteWithParameter(param);
    }
}