package com.sda.currencyexchangeapi.rest;
import com.sda.currencyexchangeapi.model.ExchangeRateDto;
import com.sda.currencyexchangeapi.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ExchangeRatesRestController {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRatesRestController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/api/current")
    public ResponseEntity<ExchangeRateDto> getCurrentExchangeRate(@RequestParam("base") String base,
                                                                  @RequestParam("target") String target){
        return ResponseEntity.ok().body(exchangeRateService.getCurrentExchangeRate(base, target));
    }

    @GetMapping( "/api/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok().body("hello");
    }
}
