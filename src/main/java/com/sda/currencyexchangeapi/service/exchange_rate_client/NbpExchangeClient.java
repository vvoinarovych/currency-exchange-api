package com.sda.currencyexchangeapi.service.exchange_rate_client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sda.currencyexchangeapi.model.ExchangeRate;
import com.sda.currencyexchangeapi.rest.exception.ExchangeRateProcessingError;
import com.sda.currencyexchangeapi.utils.Utility;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

@Log4j2
@Component("PLN")
public class NbpExchangeClient implements ExchangeRateClient {

    private final String CURRENT_EXCHANGE_RATES = "https://api.nbp.pl/api/exchangerates/rates/a/%s?format=json";
    private final String HISTORICAL_EXCHANGE_RATES = "https://api.nbp.pl/api/exchangerates/rates/a/%s/%s?format=json";

    @Override
    public ExchangeRate getCurrentExchangeRate(String base, String target) {
        try {
            URL url = new URL(String.format(CURRENT_EXCHANGE_RATES, target));
            ExchangeRate exchangeRate = buildRate(base, url);
            log.info("NBP Exchange client  for Historical Data used, url: "+url);
            return exchangeRate;
        } catch (IOException e) {
            throw new ExchangeRateProcessingError("Could not get data for chosen currencies");
        }
    }

    @Override
    public ExchangeRate getHistoricalExchangeRate(String base, String target, String date) {
        try {
            URL url = new URL(String.format(HISTORICAL_EXCHANGE_RATES, target, date));
            ExchangeRate exchangeRate = buildRate(base, url);
            log.info("NBP Exchange client  for Historical Data used, url: "+url);
            return exchangeRate;
        } catch (IOException e) {
            throw new ExchangeRateProcessingError("NPB does not have data for that day(it could be holiday");
        }
    }

    private ExchangeRate buildRate(String base, URL url) throws IOException {

        ObjectNode node = new ObjectMapper().readValue(url, ObjectNode.class);

        return ExchangeRate.builder()
                .withRate(Utility.round((1 / node.get("rates").get(0).get("mid").asDouble()), 4))
                .withBaseCurrency(base.toUpperCase())
                .withTargetCurrency(node.get("code").asText())
                .withEffectiveDate(LocalDate.parse(node.get("rates").get(0).get("effectiveDate").asText()))
                .build();
    }
}
