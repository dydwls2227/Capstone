// ImageProcessingService.java
package com.example.oauth2prac.service;

import com.example.oauth2prac.dto.SegmentationRequestDto;
import com.example.oauth2prac.dto.SegmentationResponseDTO;
import com.example.oauth2prac.entity.OriginalImage;
import com.example.oauth2prac.entity.SegmentedImage;
import com.example.oauth2prac.repository.SegmentedImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class ImageProcessingService {

    private final WebClient.Builder webClientBuilder;
    private final SegmentedImageRepository segmentedImageRepository;

    // application.yml 등에서 FastAPI 서버 주소를 주입받습니다.
    @Value("${fastapi.server.url}")
    private String fastapiServerUrl;

    /**
     * FastAPI 서버에 이미지 세그멘테이션을 요청합니다.
     * @param originalImage 원본 이미지 엔티티
     * @return 생성된 세그멘테이션 이미지의 URL
     */
    public Mono<String> requestSegmentation(OriginalImage originalImage) {
        WebClient webClient = webClientBuilder.baseUrl(fastapiServerUrl).build();

        SegmentationRequestDto requestDto = new SegmentationRequestDto(originalImage.getImageUrl());

        return webClient.post()
                .uri("/segment") // FastAPI의 엔드포인트 경로
                .bodyValue(requestDto)
                .retrieve() // 응답을 받기 시작
                .bodyToMono(SegmentationResponseDTO.class) // 응답 본문을 DTO로 변환
                .flatMap(responseDto -> {
                    // 응답 받은 URL로 SegmentedImage 엔티티를 생성하고 저장
                    SegmentedImage segmentedImage = SegmentedImage.builder()
                            .originalImage(originalImage)
                            .user(originalImage.getUser())
                            .imageUrl(responseDto.getSegmentedImageUrl())
                            .build();
                    segmentedImageRepository.save(segmentedImage);

                    // 저장된 이미지 URL을 반환
                    return Mono.just(segmentedImage.getImageUrl());
                });
    }
}