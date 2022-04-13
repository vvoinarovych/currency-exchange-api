package com.sda.currencyexchangeapi.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder(setterPrefix = "with")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name="base_currency")
    private String baseCurrency;

    @Column(name="target_currency")
    private String targetCurrency;

    @Column(precision = 6, scale = 3)
    private Double rate;

    @Column(name="effective_date")
    private LocalDate effectiveDate;
}