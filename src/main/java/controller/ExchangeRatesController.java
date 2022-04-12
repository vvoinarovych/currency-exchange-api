package controller;
import model.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.exchange_rate_client.ExchangeRateClient;

@RestController
public class ExchangeRatesController {

    @Autowired
    ExchangeRateClient exchangeRateClient;

    @GetMapping("/test")
    public ExchangeRate testCall(@RequestParam("base") String base, @RequestParam("target") String target){
        return exchangeRateClient.getCurrentExchangeRate(base,target);
    }

    @GetMapping( "/api/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok().body("hello");
    }
}
