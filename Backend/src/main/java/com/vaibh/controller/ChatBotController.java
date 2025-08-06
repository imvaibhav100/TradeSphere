package com.vaibh.controller;

import com.vaibh.model.CoinDTO;
import com.vaibh.request.PromptBody;
import com.vaibh.response.ApiResponse;
import com.vaibh.service.ChatBotService;
// ...existing code...
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/chat")
public class ChatBotController {
    @Autowired
    private ChatBotService chatBotService;

    @GetMapping("/coin/{coinName}")
    public ResponseEntity<CoinDTO> getCoinDetails(@PathVariable String coinName){

        CoinDTO coinDTO=chatBotService.getCoinByName(coinName);
        return new ResponseEntity<>(coinDTO, HttpStatus.OK);
    }

    @PostMapping("/bot")
    public ResponseEntity<String> simpleChat(@RequestBody PromptBody promptBody){

        String res = chatBotService.simpleChat(promptBody.getPrompt());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @PostMapping("/bot/coin")
    public ResponseEntity<ApiResponse> getCoinRealtimeTime(@RequestBody PromptBody promptBody){

        ApiResponse res = chatBotService.getCoinDetails(promptBody.getPrompt());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
