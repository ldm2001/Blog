package com.blog.board_back.service.implement;

import com.blog.board_back.dto.request.ai.SuggestTitleRequestDto;
import com.blog.board_back.dto.response.ai.SuggestTitleResponseDto;
import com.blog.board_back.service.AiService;
import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service // 서비스 빈으로 등록
public class AiServiceImplement implements AiService {

    private static final Logger log = LoggerFactory.getLogger(AiServiceImplement.class);

    // Gemini API 인증 키 (application-secret.properties에서 주입)
    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    // Gemini API 기본 URL (v1 + 2.5-flash: 최신 모델)
    private static final String GEMINI_API_URL_BASE =
        "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=";

    // 애플리케이션 시작 시 API 키 설정 여부 검증
    @PostConstruct
    public void init() {
        if (geminiApiKey == null || geminiApiKey.isEmpty()) {
            log.warn("application.properties에 gemini.api.key가 설정되지 않았습니다!");
        }
    }

    // AI 블로그 제목 추천 서비스 (Gemini API 호출)
    @Override
    public ResponseEntity<SuggestTitleResponseDto> suggestTitle(SuggestTitleRequestDto dto) {
        // API 키 미설정 시 빈 리스트로 500 반환
        if (geminiApiKey == null || geminiApiKey.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SuggestTitleResponseDto(new ArrayList<>()));
        }

        try {
            // HTTP 연결 설정
            URL url = new URL(GEMINI_API_URL_BASE + geminiApiKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // Gemini API 형식에 맞는 요청 JSON 생성
            // 구조: { "contents": [{ "parts": [{ "text": "..." }] }], "generationConfig": {...} }
            String prompt = "다음 블로그 글 본문을 바탕으로 어울리는 블로그 제목 3가지를 추천해주세요.\n"
                    + "번호나 기호 없이 각 제목을 줄바꿈으로만 구분해서 작성해주세요.\n\n"
                    + "[본문]\n" + dto.getContent();

            JSONObject part = new JSONObject();
            part.put("text", prompt);

            JSONArray parts = new JSONArray();
            parts.put(part);

            JSONObject contentObj = new JSONObject();
            contentObj.put("parts", parts);

            JSONArray contents = new JSONArray();
            contents.put(contentObj);

            JSONObject generationConfig = new JSONObject();
            generationConfig.put("temperature", 0.8);
            generationConfig.put("maxOutputTokens", 1024);

            JSONObject requestJson = new JSONObject();
            requestJson.put("contents", contents);
            requestJson.put("generationConfig", generationConfig);

            // 요청 바디 전송
            try (OutputStream os = connection.getOutputStream()) {
                byte[] inputBytes = requestJson.toString().getBytes(StandardCharsets.UTF_8);
                os.write(inputBytes, 0, inputBytes.length);
            }

            int responseCode = connection.getResponseCode();
            StringBuilder apiResponseBuilder = new StringBuilder();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        apiResponseBuilder.append(line);
                    }
                }

                String rawBody = apiResponseBuilder.toString();
                log.info("[Gemini API Response] body={}", rawBody);

                // Gemini 응답 파싱
                // 구조: { "candidates": [{ "content": { "parts": [{ "text": "..." }], "role": "model" } }] }
                JSONObject jsonResponse = new JSONObject(rawBody);

                // promptFeedback에 blockReason이 있으면 안전 필터로 차단된 것
                if (jsonResponse.has("promptFeedback")) {
                    JSONObject feedback = jsonResponse.getJSONObject("promptFeedback");
                    if (feedback.has("blockReason")) {
                        log.warn("[Gemini] 안전 필터 차단: {}", feedback.getString("blockReason"));
                        return ResponseEntity.ok(new SuggestTitleResponseDto(new ArrayList<>()));
                    }
                }

                JSONArray candidatesArray = jsonResponse.optJSONArray("candidates");

                List<String> titleList = new ArrayList<>();
                if (candidatesArray != null && candidatesArray.length() > 0) {
                    JSONObject candidate = candidatesArray.getJSONObject(0);

                    // SAFETY 차단만 빈 리스트 반환, STOP/MAX_TOKENS는 콘텐츠 있으면 사용
                    String finishReason = candidate.optString("finishReason", "STOP");
                    if ("SAFETY".equals(finishReason) || "RECITATION".equals(finishReason)) {
                        log.warn("[Gemini] finishReason={} — 제목 추출 불가", finishReason);
                        return ResponseEntity.ok(new SuggestTitleResponseDto(new ArrayList<>()));
                    }

                    JSONObject content = candidate.optJSONObject("content");
                    if (content != null) {
                        JSONArray responseParts = content.optJSONArray("parts");
                        if (responseParts != null && responseParts.length() > 0) {
                            String rawText = responseParts.getJSONObject(0).optString("text", "").trim();
                            log.info("[Gemini] 추출된 텍스트: {}", rawText);

                            for (String line : rawText.split("\n")) {
                                String cleaned = line.trim();
                                if (!cleaned.isEmpty()) {
                                    titleList.add(cleaned);
                                }
                            }
                        }
                    }
                }

                log.info("[Gemini] 최종 추천 제목 수: {}", titleList.size());
                return ResponseEntity.ok(new SuggestTitleResponseDto(titleList));

            } else {
                // 오류 응답 내용 출력
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    String line;
                    StringBuilder errorBody = new StringBuilder();
                    while ((line = br.readLine()) != null) errorBody.append(line);
                    log.error("[Gemini API Error] status={} body={}", responseCode, errorBody);
                }
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new SuggestTitleResponseDto(new ArrayList<>()));
            }

        } catch (Exception e) {
            log.error("AiService error", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new SuggestTitleResponseDto(new ArrayList<>()));
        }
    }
}
