package com.skhu.service;

import com.skhu.domain.ChatRoom;
import com.skhu.domain.User;
import com.skhu.dto.*;
import com.skhu.domain.Chat;
import com.skhu.repository.ChatRepository;

import com.skhu.repository.ChatRoomRepository;
import com.skhu.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final RestTemplate restTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.gpt-api-key}")
    private String apiKey;

    @Transactional
    public String chat(String question) {
        FlaskResponse flaskResponse = getPrompt(question);
        GPTRequest gptRequest = new GPTRequest(model, flaskResponse.getPrompt(), 256);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity<GPTRequest> entity = new HttpEntity<>(gptRequest, headers);

        GPTResponse gptResponse = restTemplate.postForObject("https://api.openai.com/v1/chat/completions", entity, GPTResponse.class);
        String answer = gptResponse.getChoices().get(0).getMessage().getContent();
        save(question, answer);

        return answer;
    }

    @Transactional
    public void save(String question, String answer) {
        chatRepository.save(Chat.builder()
                .question(question)
                .answer(answer)
                .build());
    }

    @Transactional
    public FlaskResponse getPrompt(String question) {
        FlaskRequest flaskRequest = new FlaskRequest(question);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FlaskRequest> entity = new HttpEntity<>(flaskRequest, headers);
        FlaskResponse flaskResponse = restTemplate.postForObject("http://localhost:8085/prompt", entity, FlaskResponse.class);
        return flaskResponse;
    }

    @Transactional
    public ChatRoomResponseDto createChatRoom(String title, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow();

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .title(title)
                .user(user)
                .build());
        return ChatRoomResponseDto.builder()
                .title(chatRoom.getTitle())
                .userId(chatRoom.getUser().getId())
                .id(chatRoom.getId())
                .build();
    }


}
