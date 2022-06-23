package sn.thiane.mefpai.ent.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.thiane.mefpai.ent.IntegrationTest;
import sn.thiane.mefpai.ent.domain.Seance;
import sn.thiane.mefpai.ent.domain.enumeration.Jour;
import sn.thiane.mefpai.ent.repository.SeanceRepository;

/**
 * Integration tests for the {@link SeanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SeanceResourceIT {

    private static final Jour DEFAULT_JOUR_SEANCE = Jour.Lundi;
    private static final Jour UPDATED_JOUR_SEANCE = Jour.Mardi;

    private static final LocalDate DEFAULT_DATE_SEANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_SEANCE = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/seances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeanceMockMvc;

    private Seance seance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seance createEntity(EntityManager em) {
        Seance seance = new Seance()
            .jourSeance(DEFAULT_JOUR_SEANCE)
            .dateSeance(DEFAULT_DATE_SEANCE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN);
        return seance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seance createUpdatedEntity(EntityManager em) {
        Seance seance = new Seance()
            .jourSeance(UPDATED_JOUR_SEANCE)
            .dateSeance(UPDATED_DATE_SEANCE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);
        return seance;
    }

    @BeforeEach
    public void initTest() {
        seance = createEntity(em);
    }

    @Test
    @Transactional
    void createSeance() throws Exception {
        int databaseSizeBeforeCreate = seanceRepository.findAll().size();
        // Create the Seance
        restSeanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seance)))
            .andExpect(status().isCreated());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeCreate + 1);
        Seance testSeance = seanceList.get(seanceList.size() - 1);
        assertThat(testSeance.getJourSeance()).isEqualTo(DEFAULT_JOUR_SEANCE);
        assertThat(testSeance.getDateSeance()).isEqualTo(DEFAULT_DATE_SEANCE);
        assertThat(testSeance.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testSeance.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void createSeanceWithExistingId() throws Exception {
        // Create the Seance with an existing ID
        seance.setId(1L);

        int databaseSizeBeforeCreate = seanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seance)))
            .andExpect(status().isBadRequest());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkJourSeanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = seanceRepository.findAll().size();
        // set the field null
        seance.setJourSeance(null);

        // Create the Seance, which fails.

        restSeanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seance)))
            .andExpect(status().isBadRequest());

        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSeanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = seanceRepository.findAll().size();
        // set the field null
        seance.setDateSeance(null);

        // Create the Seance, which fails.

        restSeanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seance)))
            .andExpect(status().isBadRequest());

        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = seanceRepository.findAll().size();
        // set the field null
        seance.setDateDebut(null);

        // Create the Seance, which fails.

        restSeanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seance)))
            .andExpect(status().isBadRequest());

        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = seanceRepository.findAll().size();
        // set the field null
        seance.setDateFin(null);

        // Create the Seance, which fails.

        restSeanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seance)))
            .andExpect(status().isBadRequest());

        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSeances() throws Exception {
        // Initialize the database
        seanceRepository.saveAndFlush(seance);

        // Get all the seanceList
        restSeanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seance.getId().intValue())))
            .andExpect(jsonPath("$.[*].jourSeance").value(hasItem(DEFAULT_JOUR_SEANCE.toString())))
            .andExpect(jsonPath("$.[*].dateSeance").value(hasItem(DEFAULT_DATE_SEANCE.toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    void getSeance() throws Exception {
        // Initialize the database
        seanceRepository.saveAndFlush(seance);

        // Get the seance
        restSeanceMockMvc
            .perform(get(ENTITY_API_URL_ID, seance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(seance.getId().intValue()))
            .andExpect(jsonPath("$.jourSeance").value(DEFAULT_JOUR_SEANCE.toString()))
            .andExpect(jsonPath("$.dateSeance").value(DEFAULT_DATE_SEANCE.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSeance() throws Exception {
        // Get the seance
        restSeanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSeance() throws Exception {
        // Initialize the database
        seanceRepository.saveAndFlush(seance);

        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();

        // Update the seance
        Seance updatedSeance = seanceRepository.findById(seance.getId()).get();
        // Disconnect from session so that the updates on updatedSeance are not directly saved in db
        em.detach(updatedSeance);
        updatedSeance
            .jourSeance(UPDATED_JOUR_SEANCE)
            .dateSeance(UPDATED_DATE_SEANCE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);

        restSeanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSeance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSeance))
            )
            .andExpect(status().isOk());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
        Seance testSeance = seanceList.get(seanceList.size() - 1);
        assertThat(testSeance.getJourSeance()).isEqualTo(UPDATED_JOUR_SEANCE);
        assertThat(testSeance.getDateSeance()).isEqualTo(UPDATED_DATE_SEANCE);
        assertThat(testSeance.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testSeance.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void putNonExistingSeance() throws Exception {
        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();
        seance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeance() throws Exception {
        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();
        seance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeance() throws Exception {
        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();
        seance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeanceWithPatch() throws Exception {
        // Initialize the database
        seanceRepository.saveAndFlush(seance);

        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();

        // Update the seance using partial update
        Seance partialUpdatedSeance = new Seance();
        partialUpdatedSeance.setId(seance.getId());

        partialUpdatedSeance.dateSeance(UPDATED_DATE_SEANCE).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);

        restSeanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeance))
            )
            .andExpect(status().isOk());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
        Seance testSeance = seanceList.get(seanceList.size() - 1);
        assertThat(testSeance.getJourSeance()).isEqualTo(DEFAULT_JOUR_SEANCE);
        assertThat(testSeance.getDateSeance()).isEqualTo(UPDATED_DATE_SEANCE);
        assertThat(testSeance.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testSeance.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void fullUpdateSeanceWithPatch() throws Exception {
        // Initialize the database
        seanceRepository.saveAndFlush(seance);

        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();

        // Update the seance using partial update
        Seance partialUpdatedSeance = new Seance();
        partialUpdatedSeance.setId(seance.getId());

        partialUpdatedSeance
            .jourSeance(UPDATED_JOUR_SEANCE)
            .dateSeance(UPDATED_DATE_SEANCE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);

        restSeanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeance))
            )
            .andExpect(status().isOk());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
        Seance testSeance = seanceList.get(seanceList.size() - 1);
        assertThat(testSeance.getJourSeance()).isEqualTo(UPDATED_JOUR_SEANCE);
        assertThat(testSeance.getDateSeance()).isEqualTo(UPDATED_DATE_SEANCE);
        assertThat(testSeance.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testSeance.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingSeance() throws Exception {
        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();
        seance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeance() throws Exception {
        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();
        seance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeance() throws Exception {
        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();
        seance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeanceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(seance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeance() throws Exception {
        // Initialize the database
        seanceRepository.saveAndFlush(seance);

        int databaseSizeBeforeDelete = seanceRepository.findAll().size();

        // Delete the seance
        restSeanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, seance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
