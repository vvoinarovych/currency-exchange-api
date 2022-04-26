package com.sda.currencyexchangeapi.rest;

import com.sda.currencyexchangeapi.model.ExchangeRateDto;
import com.sda.currencyexchangeapi.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ExchangeRatesRestController {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRatesRestController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }


    @GetMapping("/current/{base}/{target}")
    public ResponseEntity<ExchangeRateDto> getCurrentExchangeRate(@PathVariable(name = "base") String base,
                                                                  @PathVariable(name = "target") String target) {
        return ResponseEntity.ok().body(exchangeRateService.getCurrentExchangeRate(base, target));
    }

    @GetMapping("/historic/{base}/{target}/{date}")
    public ResponseEntity<ExchangeRateDto> getHistoricExchangeRate(@PathVariable(name = "base") String base,
                                                                   @PathVariable(name = "target") String target,
                                                                   @PathVariable(name = "date") String date
    ) {
        return ResponseEntity.ok().body(exchangeRateService.getHistoricalExchangeRate(base, target, date));
    }

    @GetMapping("/between/{base}/{target}/{start}/{end}")
    public ResponseEntity<List<ExchangeRateDto>> getExchangeRatesBetweenDates(@PathVariable(name = "base") String base,
                                                                              @PathVariable(name = "target") String target,
                                                                              @PathVariable(name = "start") String start,
                                                                              @PathVariable(name = "end") String end
    ) {
        return ResponseEntity.ok().body(exchangeRateService.getTimeSeriesExchangeRate(base, target, start, end));
    }


}
