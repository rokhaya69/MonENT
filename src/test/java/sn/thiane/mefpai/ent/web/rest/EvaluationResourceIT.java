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
import sn.thiane.mefpai.ent.domain.Evaluation;
import sn.thiane.mefpai.ent.domain.enumeration.TypeEvalu;
import sn.thiane.mefpai.ent.repository.EvaluationRepository;

/**
 * Integration tests for the {@link EvaluationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EvaluationResourceIT {

    private static final String DEFAULT_NOM_EVALU = "AAAAAAAAAA";
    private static final String UPDATED_NOM_EVALU = "BBBBBBBBBB";

    private static final TypeEvalu DEFAULT_TYPE_EVALU = TypeEvalu.Devoir;
    private static final TypeEvalu UPDATED_TYPE_EVALU = TypeEvalu.Test;

    private static final LocalDate DEFAULT_DATE_EVA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EVA = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_HEURE_DEB_EVA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HEURE_DEB_EVA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_HEURE_FIN_EVA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HEURE_FIN_EVA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/evaluations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvaluationMockMvc;

    private Evaluation evaluation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evaluation createEntity(EntityManager em) {
        Evaluation evaluation = new Evaluation()
            .nomEvalu(DEFAULT_NOM_EVALU)
            .typeEvalu(DEFAULT_TYPE_EVALU)
            .dateEva(DEFAULT_DATE_EVA)
            .heureDebEva(DEFAULT_HEURE_DEB_EVA)
            .heureFinEva(DEFAULT_HEURE_FIN_EVA);
        return evaluation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evaluation createUpdatedEntity(EntityManager em) {
        Evaluation evaluation = new Evaluation()
            .nomEvalu(UPDATED_NOM_EVALU)
            .typeEvalu(UPDATED_TYPE_EVALU)
            .dateEva(UPDATED_DATE_EVA)
            .heureDebEva(UPDATED_HEURE_DEB_EVA)
            .heureFinEva(UPDATED_HEURE_FIN_EVA);
        return evaluation;
    }

    @BeforeEach
    public void initTest() {
        evaluation = createEntity(em);
    }

    @Test
    @Transactional
    void createEvaluation() throws Exception {
        int databaseSizeBeforeCreate = evaluationRepository.findAll().size();
        // Create the Evaluation
        restEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluation)))
            .andExpect(status().isCreated());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeCreate + 1);
        Evaluation testEvaluation = evaluationList.get(evaluationList.size() - 1);
        assertThat(testEvaluation.getNomEvalu()).isEqualTo(DEFAULT_NOM_EVALU);
        assertThat(testEvaluation.getTypeEvalu()).isEqualTo(DEFAULT_TYPE_EVALU);
        assertThat(testEvaluation.getDateEva()).isEqualTo(DEFAULT_DATE_EVA);
        assertThat(testEvaluation.getHeureDebEva()).isEqualTo(DEFAULT_HEURE_DEB_EVA);
        assertThat(testEvaluation.getHeureFinEva()).isEqualTo(DEFAULT_HEURE_FIN_EVA);
    }

    @Test
    @Transactional
    void createEvaluationWithExistingId() throws Exception {
        // Create the Evaluation with an existing ID
        evaluation.setId(1L);

        int databaseSizeBeforeCreate = evaluationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluation)))
            .andExpect(status().isBadRequest());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomEvaluIsRequired() throws Exception {
        int databaseSizeBeforeTest = evaluationRepository.findAll().size();
        // set the field null
        evaluation.setNomEvalu(null);

        // Create the Evaluation, which fails.

        restEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluation)))
            .andExpect(status().isBadRequest());

        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeEvaluIsRequired() throws Exception {
        int databaseSizeBeforeTest = evaluationRepository.findAll().size();
        // set the field null
        evaluation.setTypeEvalu(null);

        // Create the Evaluation, which fails.

        restEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluation)))
            .andExpect(status().isBadRequest());

        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateEvaIsRequired() throws Exception {
        int databaseSizeBeforeTest = evaluationRepository.findAll().size();
        // set the field null
        evaluation.setDateEva(null);

        // Create the Evaluation, which fails.

        restEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluation)))
            .andExpect(status().isBadRequest());

        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHeureDebEvaIsRequired() throws Exception {
        int databaseSizeBeforeTest = evaluationRepository.findAll().size();
        // set the field null
        evaluation.setHeureDebEva(null);

        // Create the Evaluation, which fails.

        restEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluation)))
            .andExpect(status().isBadRequest());

        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHeureFinEvaIsRequired() throws Exception {
        int databaseSizeBeforeTest = evaluationRepository.findAll().size();
        // set the field null
        evaluation.setHeureFinEva(null);

        // Create the Evaluation, which fails.

        restEvaluationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluation)))
            .andExpect(status().isBadRequest());

        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvaluations() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);

        // Get all the evaluationList
        restEvaluationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluation.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomEvalu").value(hasItem(DEFAULT_NOM_EVALU)))
            .andExpect(jsonPath("$.[*].typeEvalu").value(hasItem(DEFAULT_TYPE_EVALU.toString())))
            .andExpect(jsonPath("$.[*].dateEva").value(hasItem(DEFAULT_DATE_EVA.toString())))
            .andExpect(jsonPath("$.[*].heureDebEva").value(hasItem(DEFAULT_HEURE_DEB_EVA.toString())))
            .andExpect(jsonPath("$.[*].heureFinEva").value(hasItem(DEFAULT_HEURE_FIN_EVA.toString())));
    }

    @Test
    @Transactional
    void getEvaluation() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);

        // Get the evaluation
        restEvaluationMockMvc
            .perform(get(ENTITY_API_URL_ID, evaluation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evaluation.getId().intValue()))
            .andExpect(jsonPath("$.nomEvalu").value(DEFAULT_NOM_EVALU))
            .andExpect(jsonPath("$.typeEvalu").value(DEFAULT_TYPE_EVALU.toString()))
            .andExpect(jsonPath("$.dateEva").value(DEFAULT_DATE_EVA.toString()))
            .andExpect(jsonPath("$.heureDebEva").value(DEFAULT_HEURE_DEB_EVA.toString()))
            .andExpect(jsonPath("$.heureFinEva").value(DEFAULT_HEURE_FIN_EVA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEvaluation() throws Exception {
        // Get the evaluation
        restEvaluationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEvaluation() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);

        int databaseSizeBeforeUpdate = evaluationRepository.findAll().size();

        // Update the evaluation
        Evaluation updatedEvaluation = evaluationRepository.findById(evaluation.getId()).get();
        // Disconnect from session so that the updates on updatedEvaluation are not directly saved in db
        em.detach(updatedEvaluation);
        updatedEvaluation
            .nomEvalu(UPDATED_NOM_EVALU)
            .typeEvalu(UPDATED_TYPE_EVALU)
            .dateEva(UPDATED_DATE_EVA)
            .heureDebEva(UPDATED_HEURE_DEB_EVA)
            .heureFinEva(UPDATED_HEURE_FIN_EVA);

        restEvaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEvaluation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEvaluation))
            )
            .andExpect(status().isOk());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeUpdate);
        Evaluation testEvaluation = evaluationList.get(evaluationList.size() - 1);
        assertThat(testEvaluation.getNomEvalu()).isEqualTo(UPDATED_NOM_EVALU);
        assertThat(testEvaluation.getTypeEvalu()).isEqualTo(UPDATED_TYPE_EVALU);
        assertThat(testEvaluation.getDateEva()).isEqualTo(UPDATED_DATE_EVA);
        assertThat(testEvaluation.getHeureDebEva()).isEqualTo(UPDATED_HEURE_DEB_EVA);
        assertThat(testEvaluation.getHeureFinEva()).isEqualTo(UPDATED_HEURE_FIN_EVA);
    }

    @Test
    @Transactional
    void putNonExistingEvaluation() throws Exception {
        int databaseSizeBeforeUpdate = evaluationRepository.findAll().size();
        evaluation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evaluation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evaluation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvaluation() throws Exception {
        int databaseSizeBeforeUpdate = evaluationRepository.findAll().size();
        evaluation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evaluation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvaluation() throws Exception {
        int databaseSizeBeforeUpdate = evaluationRepository.findAll().size();
        evaluation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEvaluationWithPatch() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);

        int databaseSizeBeforeUpdate = evaluationRepository.findAll().size();

        // Update the evaluation using partial update
        Evaluation partialUpdatedEvaluation = new Evaluation();
        partialUpdatedEvaluation.setId(evaluation.getId());

        partialUpdatedEvaluation.nomEvalu(UPDATED_NOM_EVALU).typeEvalu(UPDATED_TYPE_EVALU);

        restEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaluation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvaluation))
            )
            .andExpect(status().isOk());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeUpdate);
        Evaluation testEvaluation = evaluationList.get(evaluationList.size() - 1);
        assertThat(testEvaluation.getNomEvalu()).isEqualTo(UPDATED_NOM_EVALU);
        assertThat(testEvaluation.getTypeEvalu()).isEqualTo(UPDATED_TYPE_EVALU);
        assertThat(testEvaluation.getDateEva()).isEqualTo(DEFAULT_DATE_EVA);
        assertThat(testEvaluation.getHeureDebEva()).isEqualTo(DEFAULT_HEURE_DEB_EVA);
        assertThat(testEvaluation.getHeureFinEva()).isEqualTo(DEFAULT_HEURE_FIN_EVA);
    }

    @Test
    @Transactional
    void fullUpdateEvaluationWithPatch() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);

        int databaseSizeBeforeUpdate = evaluationRepository.findAll().size();

        // Update the evaluation using partial update
        Evaluation partialUpdatedEvaluation = new Evaluation();
        partialUpdatedEvaluation.setId(evaluation.getId());

        partialUpdatedEvaluation
            .nomEvalu(UPDATED_NOM_EVALU)
            .typeEvalu(UPDATED_TYPE_EVALU)
            .dateEva(UPDATED_DATE_EVA)
            .heureDebEva(UPDATED_HEURE_DEB_EVA)
            .heureFinEva(UPDATED_HEURE_FIN_EVA);

        restEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaluation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvaluation))
            )
            .andExpect(status().isOk());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeUpdate);
        Evaluation testEvaluation = evaluationList.get(evaluationList.size() - 1);
        assertThat(testEvaluation.getNomEvalu()).isEqualTo(UPDATED_NOM_EVALU);
        assertThat(testEvaluation.getTypeEvalu()).isEqualTo(UPDATED_TYPE_EVALU);
        assertThat(testEvaluation.getDateEva()).isEqualTo(UPDATED_DATE_EVA);
        assertThat(testEvaluation.getHeureDebEva()).isEqualTo(UPDATED_HEURE_DEB_EVA);
        assertThat(testEvaluation.getHeureFinEva()).isEqualTo(UPDATED_HEURE_FIN_EVA);
    }

    @Test
    @Transactional
    void patchNonExistingEvaluation() throws Exception {
        int databaseSizeBeforeUpdate = evaluationRepository.findAll().size();
        evaluation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, evaluation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evaluation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvaluation() throws Exception {
        int databaseSizeBeforeUpdate = evaluationRepository.findAll().size();
        evaluation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evaluation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvaluation() throws Exception {
        int databaseSizeBeforeUpdate = evaluationRepository.findAll().size();
        evaluation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(evaluation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evaluation in the database
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvaluation() throws Exception {
        // Initialize the database
        evaluationRepository.saveAndFlush(evaluation);

        int databaseSizeBeforeDelete = evaluationRepository.findAll().size();

        // Delete the evaluation
        restEvaluationMockMvc
            .perform(delete(ENTITY_API_URL_ID, evaluation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        assertThat(evaluationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
