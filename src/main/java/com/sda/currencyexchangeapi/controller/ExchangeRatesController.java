package com.sda.currencyexchangeapi.controller;
import com.sda.currencyexchangeapi.model.ExchangeRateDto;
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
    public ResponseEntity<ExchangeRateDto> testCall(@RequestParam("base") String base,
                                                    @RequestParam("target") String target){
        return ResponseEntity.ok().body(exchangeRateService.getCurrentExchangeRate(base,target));
    }

    @GetMapping( "/api/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok().body("hello");
    }


    @GetMapping("/api/historic")
    public ResponseEntity<ExchangeRateDto> testCall2(@RequestParam("base") String base,
                                                     @RequestParam("target") String target,
                                                     @RequestParam("date") String date)
                                                     {
        return ResponseEntity.ok().body(exchangeRateService.getHistoricalExchangeRate(date, base, target));
    }



}
