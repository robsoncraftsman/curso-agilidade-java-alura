package br.com.caelum.clines.api.locations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.google.gson.Gson;

import br.com.caelum.clines.shared.domain.Country;
import br.com.caelum.clines.shared.domain.Location;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
class LocationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestEntityManager entityManager;

    private final Gson gson = new Gson();

    @Test
    void shouldReturnLocationById() throws Exception {
        final var location = new Location(Country.BR, "SP", "Poá");

        this.entityManager.persist(location);

        this.mockMvc.perform(get("/location/" + location.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country", equalTo("Brazil")))
                .andExpect(jsonPath("$.state", equalTo("SP")))
                .andExpect(jsonPath("$.city", equalTo("Poá")));
    }

    @Test
    void shouldReturnNotFoundForNotFoundLocation() throws Exception {
        this.mockMvc.perform(get("/location/42"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnHttpStatus201AndHeaderAttributeLocationWhenValidFormIsInformed() throws Exception {
		final var locationForm = new LocationForm(Country.BR.getDescription(), "SP", "Osasco");
        final var locationJson = this.gson.toJson(locationForm);

        this.mockMvc.perform(post("/location/").contentType(MediaType.APPLICATION_JSON).content(locationJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"));
    }

    @Test
    public void listAllLocationsShouldReturnListOfLocations() throws Exception {
        final var location1 = new Location(Country.BR, "SP", "SAO PAULO");
        final var location2 = new Location(Country.BR, "SP", "CAMPINAS");

        this.entityManager.persist(location1);
        this.entityManager.persist(location2);

        final var locationView1 = new LocationView(location1.getId(), Country.BR.getDescription(), "SP", "SAO PAULO");
        final var locationView2 = new LocationView(location2.getId(), Country.BR.getDescription(), "SP", "CAMPINAS");
        final List<LocationView> locations = List.of(locationView2, locationView1);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/location"))
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.content()
                                .json(this.gson.toJson(locations))
                );
    }

    @Test
    public void listAllLocationsShouldReturnAnEmptyList() throws Exception {
        final List<LocationView> locations = Collections.emptyList();

        this.mockMvc.perform(MockMvcRequestBuilders.get("/location"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(this.gson.toJson(locations)));
    }
}