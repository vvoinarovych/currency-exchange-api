package com.sda.currencyexchangeapi.controller;
import com.sda.currencyexchangeapi.model.ExchangeRate;
import com.sda.currencyexchangeapi.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ExchangeRatesController {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRatesController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/api/current")
    public ResponseEntity<ExchangeRate> testCall(@RequestParam("base") String base,
                                                 @RequestParam("target") String target){
        return ResponseEntity.ok().body(exchangeRateService.getCurrentExchangeRate(base,target));
    }

    @GetMapping( "/api/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok().body("hello");
    }
}
