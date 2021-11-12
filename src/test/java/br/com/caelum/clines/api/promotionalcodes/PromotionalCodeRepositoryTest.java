package br.com.caelum.clines.api.promotionalcodes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.caelum.clines.shared.domain.PromotionalCode;

@DataJpaTest
@Transactional
class PromotionalCodeRepositoryTest {
    @Autowired
    private PromotionalCodeRepository repository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveNewPromotionalCode() {
        final var start = LocalDate.now();
        final var expiration = LocalDate.now().plusMonths(1);

        final var promotionalCode = new PromotionalCode("CODE", start, expiration, "DESCRIPTION", 10);

        assertNull(promotionalCode.getId());

        this.repository.save(promotionalCode);

        assertNotNull(promotionalCode.getId());

        final var newObject = this.entityManager.find(
                PromotionalCode.class,
                promotionalCode.getId()
        );

        assertThat(newObject.getCode(), equalTo("CODE"));
        assertThat(newObject.getStartDate(), equalTo(start));
        assertThat(newObject.getExpirationDate(), equalTo(expiration));
        assertThat(newObject.getDescription(), equalTo("DESCRIPTION"));
        assertThat(newObject.getDiscount(), equalTo(10));
    }
}