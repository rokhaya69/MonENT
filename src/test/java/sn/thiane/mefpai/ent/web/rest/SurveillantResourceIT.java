package sn.thiane.mefpai.ent.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import sn.thiane.mefpai.ent.IntegrationTest;
import sn.thiane.mefpai.ent.domain.Surveillant;
import sn.thiane.mefpai.ent.domain.enumeration.Sexe;
import sn.thiane.mefpai.ent.repository.SurveillantRepository;

/**
 * Integration tests for the {@link SurveillantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SurveillantResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final Sexe DEFAULT_SEXE = Sexe.Masculin;
    private static final Sexe UPDATED_SEXE = Sexe.Feminin;

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/surveillants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SurveillantRepository surveillantRepository;

    @Mock
    private SurveillantRepository surveillantRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSurveillantMockMvc;

    private Surveillant surveillant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Surveillant createEntity(EntityManager em) {
        Surveillant surveillant = new Surveillant()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .email(DEFAULT_EMAIL)
            .adresse(DEFAULT_ADRESSE)
            .telephone(DEFAULT_TELEPHONE)
            .sexe(DEFAULT_SEXE)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE);
        return surveillant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Surveillant createUpdatedEntity(EntityManager em) {
        Surveillant surveillant = new Surveillant()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .sexe(UPDATED_SEXE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);
        return surveillant;
    }

    @BeforeEach
    public void initTest() {
        surveillant = createEntity(em);
    }

    @Test
    @Transactional
    void createSurveillant() throws Exception {
        int databaseSizeBeforeCreate = surveillantRepository.findAll().size();
        // Create the Surveillant
        restSurveillantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surveillant)))
            .andExpect(status().isCreated());

        // Validate the Surveillant in the database
        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeCreate + 1);
        Surveillant testSurveillant = surveillantList.get(surveillantList.size() - 1);
        assertThat(testSurveillant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testSurveillant.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testSurveillant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSurveillant.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testSurveillant.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testSurveillant.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testSurveillant.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testSurveillant.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createSurveillantWithExistingId() throws Exception {
        // Create the Surveillant with an existing ID
        surveillant.setId(1L);

        int databaseSizeBeforeCreate = surveillantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSurveillantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surveillant)))
            .andExpect(status().isBadRequest());

        // Validate the Surveillant in the database
        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveillantRepository.findAll().size();
        // set the field null
        surveillant.setNom(null);

        // Create the Surveillant, which fails.

        restSurveillantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surveillant)))
            .andExpect(status().isBadRequest());

        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveillantRepository.findAll().size();
        // set the field null
        surveillant.setPrenom(null);

        // Create the Surveillant, which fails.

        restSurveillantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surveillant)))
            .andExpect(status().isBadRequest());

        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveillantRepository.findAll().size();
        // set the field null
        surveillant.setEmail(null);

        // Create the Surveillant, which fails.

        restSurveillantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surveillant)))
            .andExpect(status().isBadRequest());

        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveillantRepository.findAll().size();
        // set the field null
        surveillant.setAdresse(null);

        // Create the Surveillant, which fails.

        restSurveillantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surveillant)))
            .andExpect(status().isBadRequest());

        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveillantRepository.findAll().size();
        // set the field null
        surveillant.setTelephone(null);

        // Create the Surveillant, which fails.

        restSurveillantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surveillant)))
            .andExpect(status().isBadRequest());

        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveillantRepository.findAll().size();
        // set the field null
        surveillant.setSexe(null);

        // Create the Surveillant, which fails.

        restSurveillantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surveillant)))
            .andExpect(status().isBadRequest());

        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSurveillants() throws Exception {
        // Initialize the database
        surveillantRepository.saveAndFlush(surveillant);

        // Get all the surveillantList
        restSurveillantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(surveillant.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSurveillantsWithEagerRelationshipsIsEnabled() throws Exception {
        when(surveillantRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSurveillantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(surveillantRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSurveillantsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(surveillantRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSurveillantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(surveillantRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSurveillant() throws Exception {
        // Initialize the database
        surveillantRepository.saveAndFlush(surveillant);

        // Get the surveillant
        restSurveillantMockMvc
            .perform(get(ENTITY_API_URL_ID, surveillant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(surveillant.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)));
    }

    @Test
    @Transactional
    void getNonExistingSurveillant() throws Exception {
        // Get the surveillant
        restSurveillantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSurveillant() throws Exception {
        // Initialize the database
        surveillantRepository.saveAndFlush(surveillant);

        int databaseSizeBeforeUpdate = surveillantRepository.findAll().size();

        // Update the surveillant
        Surveillant updatedSurveillant = surveillantRepository.findById(surveillant.getId()).get();
        // Disconnect from session so that the updates on updatedSurveillant are not directly saved in db
        em.detach(updatedSurveillant);
        updatedSurveillant
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .sexe(UPDATED_SEXE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restSurveillantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSurveillant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSurveillant))
            )
            .andExpect(status().isOk());

        // Validate the Surveillant in the database
        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeUpdate);
        Surveillant testSurveillant = surveillantList.get(surveillantList.size() - 1);
        assertThat(testSurveillant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testSurveillant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testSurveillant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSurveillant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testSurveillant.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testSurveillant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testSurveillant.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testSurveillant.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingSurveillant() throws Exception {
        int databaseSizeBeforeUpdate = surveillantRepository.findAll().size();
        surveillant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurveillantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, surveillant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surveillant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surveillant in the database
        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSurveillant() throws Exception {
        int databaseSizeBeforeUpdate = surveillantRepository.findAll().size();
        surveillant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveillantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(surveillant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surveillant in the database
        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSurveillant() throws Exception {
        int databaseSizeBeforeUpdate = surveillantRepository.findAll().size();
        surveillant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveillantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(surveillant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Surveillant in the database
        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSurveillantWithPatch() throws Exception {
        // Initialize the database
        surveillantRepository.saveAndFlush(surveillant);

        int databaseSizeBeforeUpdate = surveillantRepository.findAll().size();

        // Update the surveillant using partial update
        Surveillant partialUpdatedSurveillant = new Surveillant();
        partialUpdatedSurveillant.setId(surveillant.getId());

        partialUpdatedSurveillant.sexe(UPDATED_SEXE);

        restSurveillantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurveillant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurveillant))
            )
            .andExpect(status().isOk());

        // Validate the Surveillant in the database
        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeUpdate);
        Surveillant testSurveillant = surveillantList.get(surveillantList.size() - 1);
        assertThat(testSurveillant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testSurveillant.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testSurveillant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSurveillant.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testSurveillant.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testSurveillant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testSurveillant.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testSurveillant.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateSurveillantWithPatch() throws Exception {
        // Initialize the database
        surveillantRepository.saveAndFlush(surveillant);

        int databaseSizeBeforeUpdate = surveillantRepository.findAll().size();

        // Update the surveillant using partial update
        Surveillant partialUpdatedSurveillant = new Surveillant();
        partialUpdatedSurveillant.setId(surveillant.getId());

        partialUpdatedSurveillant
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .sexe(UPDATED_SEXE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restSurveillantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurveillant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSurveillant))
            )
            .andExpect(status().isOk());

        // Validate the Surveillant in the database
        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeUpdate);
        Surveillant testSurveillant = surveillantList.get(surveillantList.size() - 1);
        assertThat(testSurveillant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testSurveillant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testSurveillant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSurveillant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testSurveillant.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testSurveillant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testSurveillant.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testSurveillant.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingSurveillant() throws Exception {
        int databaseSizeBeforeUpdate = surveillantRepository.findAll().size();
        surveillant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurveillantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, surveillant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(surveillant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surveillant in the database
        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSurveillant() throws Exception {
        int databaseSizeBeforeUpdate = surveillantRepository.findAll().size();
        surveillant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveillantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(surveillant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Surveillant in the database
        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSurveillant() throws Exception {
        int databaseSizeBeforeUpdate = surveillantRepository.findAll().size();
        surveillant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveillantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(surveillant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Surveillant in the database
        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSurveillant() throws Exception {
        // Initialize the database
        surveillantRepository.saveAndFlush(surveillant);

        int databaseSizeBeforeDelete = surveillantRepository.findAll().size();

        // Delete the surveillant
        restSurveillantMockMvc
            .perform(delete(ENTITY_API_URL_ID, surveillant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Surveillant> surveillantList = surveillantRepository.findAll();
        assertThat(surveillantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
