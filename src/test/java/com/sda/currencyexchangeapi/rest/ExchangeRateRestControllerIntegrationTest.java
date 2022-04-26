package com.sda.currencyexchangeapi.rest;
import com.sda.currencyexchangeapi.model.ExchangeRate;
import com.sda.currencyexchangeapi.repo.ExchangeRateRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExchangeRateRestControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExchangeRateRepository rateRepository;

    @BeforeAll
    static void setUp(@Autowired ExchangeRateRepository exchangeRateRepository){
        ExchangeRate exchangeRate1 = ExchangeRate.builder()
                .withRate(4.12)
                .withBaseCurrency("USD")
                .withTargetCurrency("CHF")
                .withEffectiveDate(LocalDate.parse("2022-03-15"))
                .build();
        ExchangeRate exchangeRate2 = ExchangeRate.builder()
                .withRate(5.12)
                .withBaseCurrency("USD")
                .withTargetCurrency("CHF")
                .withEffectiveDate(LocalDate.parse("2022-03-25"))
                .build();
        ExchangeRate exchangeRate3 = ExchangeRate.builder()
                .withRate(5.12)
                .withBaseCurrency("USD")
                .withTargetCurrency("CHF")
                .withEffectiveDate(LocalDate.parse("2022-03-27"))
                .build();
        ExchangeRate exchangeRate4 = ExchangeRate.builder()
                .withRate(5.12)
                .withBaseCurrency("USD")
                .withTargetCurrency("EUR")
                .withEffectiveDate(LocalDate.parse("2022-03-22"))
                .build();

        exchangeRateRepository.save(exchangeRate1);
        exchangeRateRepository.save(exchangeRate2);
        exchangeRateRepository.save(exchangeRate3);
        exchangeRateRepository.save(exchangeRate4);
    }

    @DisplayName("Should return initial number of records in data base")
    @Order(1)
    @Test
    void shouldReturnSizeOfDataBase(){
        assertThat(rateRepository.findAll().size()).isEqualTo(4);
    }

    @DisplayName("Should get current exchange rate for USD and PLN pair and save to data base")
    @Order(2)
    @Test
    void getCurrentExchangeRate() throws Exception {
        int sizeBefore = rateRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:" + port + "/api/current/USD/PLN")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base").exists())
                .andExpect(jsonPath("$.target").exists())
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.rate").isNumber());

        assertThat(sizeBefore).isEqualTo(4);
        assertThat(rateRepository.findAll().size()).isEqualTo(5);
    }


    @DisplayName("Should get historical (2022-03-15) exchange rate for USD and ZAR pair and write to data base")
    @Order(3)
    @Test
    void getHistoricalExchangeRate() throws Exception {
        int sizeBefore = rateRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:" + port + "/api/historic/USD/ZAR/2022-03-15")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base").exists())
                .andExpect(jsonPath("$.target").exists())
                .andExpect(jsonPath("$.date").isNotEmpty())
                .andExpect(jsonPath("$.rate").isNumber());

        assertThat(sizeBefore).isEqualTo(5);
        assertThat(rateRepository.findAll().size()).isEqualTo(6);
    }

    @DisplayName("Should get already existing historical (2022-03-15) exchange rate for USD and ZAR pair")
    @Order(4)
    @Test
    void getHistoricalExchangeRateWhenDataAlreadyExistInDb() throws Exception {
        int sizeBefore = rateRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:" + port + "/api/historic/USD/ZAR/2022-03-15")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base").exists())
                .andExpect(jsonPath("$.target").exists())
                .andExpect(jsonPath("$.date").isNotEmpty())
                .andExpect(jsonPath("$.rate").isNumber());

        assertThat(sizeBefore).isEqualTo(6);
        assertThat(rateRepository.findAll().size()).isEqualTo(6);
    }

    @DisplayName("Should get exchange rates from 2022-03-11 to 2022-03-26 for USD and CHF pair")
    @Order(5)
    @Test
    void getExistingRatesBetweenDates() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:" + port +
                                "/api/between/USD/CHF/2022-03-11/2022-03-26")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].base", hasItems("USD")))
                .andExpect(jsonPath("$[*].target", hasItems("CHF")))
                .andExpect(jsonPath("$[*].date", hasItems("2022-03-15", "2022-03-25")))
                .andExpect(jsonPath("$[*].rate").isArray());
    }

    @DisplayName("Should return error message when there is no data for that period in data base")
    @Order(6)
    @Test
    void getNotExistingRatesBetweenDates() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:" + port +
                                "/api/between/USD/PLN/2022-03-11/2022-03-26")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("No data for that period")));
    }
}