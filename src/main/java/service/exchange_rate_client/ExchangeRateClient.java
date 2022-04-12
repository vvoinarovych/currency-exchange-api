package service.exchange_rate_client;

import model.ExchangeRate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public interface ExchangeRateClient {

    default ExchangeRate buildExchangeRate(String url){
        return new RestTemplate().getForObject(url, ExchangeRate.class);
    }

    ExchangeRate getCurrentExchangeRate(String base, String target);

}
