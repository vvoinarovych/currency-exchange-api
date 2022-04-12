package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRate {

    private Integer id;
    private String base;
    private String target;
    private LocalDate effectiveDate;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Integer getId() {
        return id;
    }

    public String getBase() {
        return base;
    }

    public String getTarget() {
        return target;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "id=" + id +
                ", base='" + base + '\'' +
                ", target='" + target + '\'' +
                ", effectiveDate=" + effectiveDate +
                '}';
    }
}
