package br.com.caelum.clines.api.aircraft;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import br.com.caelum.clines.shared.domain.Aircraft;
import br.com.caelum.clines.shared.domain.AircraftModel;

@DataJpaTest
class AircraftRepositoryTest {

    private static final String AIRCRAFT_CODE = "ASLDJ123";

    @Autowired
    private AircraftRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private AircraftModel BOEING;

    @BeforeEach
    void setup() {
        this.BOEING = new AircraftModel("Boeing 737 800");
        this.entityManager.persist(this.BOEING);
    }

    @Test
    void shouldReturnAircraftByCodeWhenExistsAnAircraftInDB() {
        final var aircraft = new Aircraft(AIRCRAFT_CODE, this.BOEING);

        this.entityManager.persist(aircraft);

        final var optionalAircraft = this.repository.findByCode(AIRCRAFT_CODE);

        assertThat(optionalAircraft).isPresent();

        final var returnedAircraft = optionalAircraft.get();

        assertNotNull(returnedAircraft.getId());
        assertEquals(AIRCRAFT_CODE, returnedAircraft.getCode());
        assertEquals(this.BOEING, returnedAircraft.getModel());
    }

    @Test
    void shouldReturnAnEmptyOptionalWhenNotExistsAircraftByCode() {
        final var optionalAircraft = this.repository.findByCode(AIRCRAFT_CODE);

        assertThat(optionalAircraft).isEmpty();
    }


    @Test
    void shouldSaveANewAircraft() {
        final var aircraft = new Aircraft(AIRCRAFT_CODE, this.BOEING);

        assertNull(aircraft.getId());

        this.repository.save(aircraft);

        assertNotNull(aircraft.getId());

        assertEquals(AIRCRAFT_CODE, aircraft.getCode());
        assertEquals(this.BOEING, aircraft.getModel());
    }

}