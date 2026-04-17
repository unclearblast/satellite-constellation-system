package com.example.missionscheduler.client;

import com.example.missionscheduler.dto.MissionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class SpaceOperationClient {

    private final RestClient restClient;

    public void executeMission(MissionRequest request) {
        restClient.post()
                .uri("/missions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}
