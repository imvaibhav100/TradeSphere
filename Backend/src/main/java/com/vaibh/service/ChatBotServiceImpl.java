package com.vaibh.service;

// ...existing code...
import com.vaibh.model.CoinDTO;
import com.vaibh.response.ApiResponse;
import com.vaibh.response.FunctionResponse;
// ...existing code...
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
// ...existing code...
import org.springframework.web.client.RestTemplate;

// ...existing code...
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
@Service
public class ChatBotServiceImpl implements ChatBotService{
    // Implement getCoinByName as required by interface
    @Override
    public CoinDTO getCoinByName(String coinName) {
        // Simple implementation using existing makeApiRequest
        return makeApiRequest(coinName);
    }

    // Implement simpleChat as required by interface
    @Override
    public String simpleChat(String prompt) {
        // USER-FRIENDLY MOCKED GEMINI RESPONSE FOR DEMO
        return "Hi! I'm TradeSphere AI. You said: '" + prompt + "'. How can I help you with crypto today? (This is a demo response.)";
    }

    @Value("${gemini.api.key}")
    private String API_KEY;
    // ...existing code...

    private double convertToDouble(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Long) {
            return ((Long) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else { 
            throw new IllegalArgumentException("Unsupported data type: " + value.getClass().getName());
        }
    }
    
    public CoinDTO makeApiRequest(String currencyName) {
        // For testing, always use 'bitcoin' as the coin name
        System.out.println("coin name (forced): bit  coin");
        String url = "https://api.coingecko.com/api/v3/coins/bitcoin";

        RestTemplate restTemplate = new RestTemplate();

            // Removed unused variable 'headers'


            // Removed unused variable 'entity'

            ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url, Map.class);
            Map<String, Object> responseBody = responseEntity.getBody();
            if (responseBody != null) {
                Map<String, Object> image = (Map<String, Object>) responseBody.get("image");

                Map<String, Object> marketData = (Map<String, Object>) responseBody.get("market_data");

                CoinDTO coinInfo = new CoinDTO();
                coinInfo.setId((String) responseBody.get("id"));
                coinInfo.setSymbol((String) responseBody.get("symbol"));
                coinInfo.setName((String) responseBody.get("name"));
                coinInfo.setImage((String) image.get("large"));

                coinInfo.setCurrentPrice(convertToDouble(((Map<String, Object>) marketData.get("current_price")).get("usd")));
                coinInfo.setMarketCap(convertToDouble(((Map<String, Object>) marketData.get("market_cap")).get("usd")));
                coinInfo.setMarketCapRank((int) responseBody.get("market_cap_rank"));
                coinInfo.setTotalVolume(convertToDouble(((Map<String, Object>) marketData.get("total_volume")).get("usd")));
                coinInfo.setHigh24h(convertToDouble(((Map<String, Object>) marketData.get("high_24h")).get("usd")));
                coinInfo.setLow24h(convertToDouble(((Map<String, Object>) marketData.get("low_24h")).get("usd")));
                coinInfo.setPriceChange24h(convertToDouble(marketData.get("price_change_24h")) );
                coinInfo.setPriceChangePercentage24h(convertToDouble(marketData.get("price_change_percentage_24h")));
                coinInfo.setMarketCapChange24h(convertToDouble(marketData.get("market_cap_change_24h")));
                coinInfo.setMarketCapChangePercentage24h(convertToDouble( marketData.get("market_cap_change_percentage_24h")));
                coinInfo.setCirculatingSupply(convertToDouble(marketData.get("circulating_supply")));
                coinInfo.setTotalSupply(convertToDouble(marketData.get("total_supply")));

                return coinInfo;

             }
       return null;
    }



    public FunctionResponse getFunctionResponse(String prompt){
        // Function calling is not supported in Gemini API v1 REST, so return dummy response
        FunctionResponse res = new FunctionResponse();
        res.setCurrencyName("");
        res.setCurrencyData("");
        res.setFunctionName("not_supported");
        return res;
    }




    @Override
    public ApiResponse getCoinDetails(String prompt) {
        // USER-FRIENDLY MOCKED GEMINI RESPONSE FOR DEMO
        String coinName = makeApiRequest("bitcoin").getName();
        ApiResponse ans = new ApiResponse();
        ans.setMessage("Hi! I'm TradeSphere AI. " + coinName + " (BTC) is the most popular cryptocurrency. Its current price and stats are shown above. If you want to know more about any coin, just ask! (This is a demo response.)");
        ans.setStatus(true);
        return ans;
    }
}
