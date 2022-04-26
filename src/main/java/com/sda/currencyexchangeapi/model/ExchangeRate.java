package com.sda.currencyexchangeapi.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRate that = (ExchangeRate) o;
        return id.equals(that.id) && baseCurrency.equals(that.baseCurrency) && targetCurrency.equals(that.targetCurrency) && rate.equals(that.rate) && effectiveDate.equals(that.effectiveDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, baseCurrency, targetCurrency, rate, effectiveDate);
    }
}