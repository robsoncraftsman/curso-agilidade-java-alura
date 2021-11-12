package br.com.caelum.clines.api.aircraft;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import br.com.caelum.clines.shared.domain.Aircraft;
import br.com.caelum.clines.shared.domain.AircraftModel;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
class AircraftControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestEntityManager entityManager;

    private AircraftModel BOEING;

    @BeforeEach
    void setup() {
        this.BOEING = new AircraftModel("Boeing 737 800");
        this.entityManager.persist(this.BOEING);
    }

    @Test
    void shouldReturn404WhenNotExistAircraftByCode() throws Exception {
        this.mockMvc.perform(get("/aircraft/ASDF1234"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAnAircraftByCode() throws Exception {
        final var aircraft = new Aircraft("ASDF1234", this.BOEING);

        this.entityManager.persist(aircraft);


        this.mockMvc.perform(get("/aircraft/ASDF1234"))
                .andExpect(status().isOk())
                .andDo(log())
                .andExpect(jsonPath("$.code", equalTo("ASDF1234")))
                .andExpect(jsonPath("$.model.id", equalTo(this.BOEING.getId().intValue())))
                .andExpect(jsonPath("$.model.description", equalTo(this.BOEING.getDescription())));
    }
}