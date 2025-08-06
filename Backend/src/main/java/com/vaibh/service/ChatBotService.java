package com.vaibh.service;

import com.vaibh.model.CoinDTO;
import com.vaibh.response.ApiResponse;

public interface ChatBotService {
    ApiResponse getCoinDetails(String coinName);

    CoinDTO getCoinByName(String coinName);

    String simpleChat(String prompt);
}
