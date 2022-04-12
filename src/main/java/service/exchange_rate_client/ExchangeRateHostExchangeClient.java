package service.exchange_rate_client;

import model.ExchangeRate;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateHostExchangeClient implements ExchangeRateClient{

    private final String CURRENT_EXCHANGE_RATES = "https://api.exchangerate.host/latest?base=%s&symbols=%s";

    @Override
    public ExchangeRate getCurrentExchangeRate(String base, String target) {
        String url = String.format(CURRENT_EXCHANGE_RATES, base, target);
        return buildExchangeRate(url);
    }
}
