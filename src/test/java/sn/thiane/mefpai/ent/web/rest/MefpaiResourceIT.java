package sn.thiane.mefpai.ent.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.util.Base64Utils;
import sn.thiane.mefpai.ent.IntegrationTest;
import sn.thiane.mefpai.ent.domain.Mefpai;
import sn.thiane.mefpai.ent.domain.enumeration.Sexe;
import sn.thiane.mefpai.ent.repository.MefpaiRepository;

/**
 * Integration tests for the {@link MefpaiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MefpaiResourceIT {

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

    private static final String ENTITY_API_URL = "/api/mefpais";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MefpaiRepository mefpaiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMefpaiMockMvc;

    private Mefpai mefpai;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mefpai createEntity(EntityManager em) {
        Mefpai mefpai = new Mefpai()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .email(DEFAULT_EMAIL)
            .adresse(DEFAULT_ADRESSE)
            .telephone(DEFAULT_TELEPHONE)
            .sexe(DEFAULT_SEXE)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE);
        return mefpai;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mefpai createUpdatedEntity(EntityManager em) {
        Mefpai mefpai = new Mefpai()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .sexe(UPDATED_SEXE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);
        return mefpai;
    }

    @BeforeEach
    public void initTest() {
        mefpai = createEntity(em);
    }

    @Test
    @Transactional
    void createMefpai() throws Exception {
        int databaseSizeBeforeCreate = mefpaiRepository.findAll().size();
        // Create the Mefpai
        restMefpaiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mefpai)))
            .andExpect(status().isCreated());

        // Validate the Mefpai in the database
        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeCreate + 1);
        Mefpai testMefpai = mefpaiList.get(mefpaiList.size() - 1);
        assertThat(testMefpai.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testMefpai.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testMefpai.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMefpai.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testMefpai.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testMefpai.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testMefpai.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testMefpai.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createMefpaiWithExistingId() throws Exception {
        // Create the Mefpai with an existing ID
        mefpai.setId(1L);

        int databaseSizeBeforeCreate = mefpaiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMefpaiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mefpai)))
            .andExpect(status().isBadRequest());

        // Validate the Mefpai in the database
        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = mefpaiRepository.findAll().size();
        // set the field null
        mefpai.setNom(null);

        // Create the Mefpai, which fails.

        restMefpaiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mefpai)))
            .andExpect(status().isBadRequest());

        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = mefpaiRepository.findAll().size();
        // set the field null
        mefpai.setPrenom(null);

        // Create the Mefpai, which fails.

        restMefpaiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mefpai)))
            .andExpect(status().isBadRequest());

        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = mefpaiRepository.findAll().size();
        // set the field null
        mefpai.setEmail(null);

        // Create the Mefpai, which fails.

        restMefpaiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mefpai)))
            .andExpect(status().isBadRequest());

        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = mefpaiRepository.findAll().size();
        // set the field null
        mefpai.setAdresse(null);

        // Create the Mefpai, which fails.

        restMefpaiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mefpai)))
            .andExpect(status().isBadRequest());

        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = mefpaiRepository.findAll().size();
        // set the field null
        mefpai.setTelephone(null);

        // Create the Mefpai, which fails.

        restMefpaiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mefpai)))
            .andExpect(status().isBadRequest());

        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = mefpaiRepository.findAll().size();
        // set the field null
        mefpai.setSexe(null);

        // Create the Mefpai, which fails.

        restMefpaiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mefpai)))
            .andExpect(status().isBadRequest());

        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMefpais() throws Exception {
        // Initialize the database
        mefpaiRepository.saveAndFlush(mefpai);

        // Get all the mefpaiList
        restMefpaiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mefpai.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))));
    }

    @Test
    @Transactional
    void getMefpai() throws Exception {
        // Initialize the database
        mefpaiRepository.saveAndFlush(mefpai);

        // Get the mefpai
        restMefpaiMockMvc
            .perform(get(ENTITY_API_URL_ID, mefpai.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mefpai.getId().intValue()))
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
    void getNonExistingMefpai() throws Exception {
        // Get the mefpai
        restMefpaiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMefpai() throws Exception {
        // Initialize the database
        mefpaiRepository.saveAndFlush(mefpai);

        int databaseSizeBeforeUpdate = mefpaiRepository.findAll().size();

        // Update the mefpai
        Mefpai updatedMefpai = mefpaiRepository.findById(mefpai.getId()).get();
        // Disconnect from session so that the updates on updatedMefpai are not directly saved in db
        em.detach(updatedMefpai);
        updatedMefpai
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .sexe(UPDATED_SEXE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restMefpaiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMefpai.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMefpai))
            )
            .andExpect(status().isOk());

        // Validate the Mefpai in the database
        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeUpdate);
        Mefpai testMefpai = mefpaiList.get(mefpaiList.size() - 1);
        assertThat(testMefpai.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testMefpai.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testMefpai.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMefpai.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testMefpai.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testMefpai.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testMefpai.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testMefpai.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingMefpai() throws Exception {
        int databaseSizeBeforeUpdate = mefpaiRepository.findAll().size();
        mefpai.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMefpaiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mefpai.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mefpai))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mefpai in the database
        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMefpai() throws Exception {
        int databaseSizeBeforeUpdate = mefpaiRepository.findAll().size();
        mefpai.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMefpaiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mefpai))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mefpai in the database
        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMefpai() throws Exception {
        int databaseSizeBeforeUpdate = mefpaiRepository.findAll().size();
        mefpai.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMefpaiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mefpai)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mefpai in the database
        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMefpaiWithPatch() throws Exception {
        // Initialize the database
        mefpaiRepository.saveAndFlush(mefpai);

        int databaseSizeBeforeUpdate = mefpaiRepository.findAll().size();

        // Update the mefpai using partial update
        Mefpai partialUpdatedMefpai = new Mefpai();
        partialUpdatedMefpai.setId(mefpai.getId());

        partialUpdatedMefpai.nom(UPDATED_NOM).adresse(UPDATED_ADRESSE).sexe(UPDATED_SEXE);

        restMefpaiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMefpai.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMefpai))
            )
            .andExpect(status().isOk());

        // Validate the Mefpai in the database
        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeUpdate);
        Mefpai testMefpai = mefpaiList.get(mefpaiList.size() - 1);
        assertThat(testMefpai.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testMefpai.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testMefpai.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMefpai.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testMefpai.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testMefpai.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testMefpai.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testMefpai.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateMefpaiWithPatch() throws Exception {
        // Initialize the database
        mefpaiRepository.saveAndFlush(mefpai);

        int databaseSizeBeforeUpdate = mefpaiRepository.findAll().size();

        // Update the mefpai using partial update
        Mefpai partialUpdatedMefpai = new Mefpai();
        partialUpdatedMefpai.setId(mefpai.getId());

        partialUpdatedMefpai
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .sexe(UPDATED_SEXE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restMefpaiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMefpai.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMefpai))
            )
            .andExpect(status().isOk());

        // Validate the Mefpai in the database
        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeUpdate);
        Mefpai testMefpai = mefpaiList.get(mefpaiList.size() - 1);
        assertThat(testMefpai.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testMefpai.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testMefpai.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMefpai.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testMefpai.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testMefpai.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testMefpai.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testMefpai.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingMefpai() throws Exception {
        int databaseSizeBeforeUpdate = mefpaiRepository.findAll().size();
        mefpai.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMefpaiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mefpai.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mefpai))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mefpai in the database
        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMefpai() throws Exception {
        int databaseSizeBeforeUpdate = mefpaiRepository.findAll().size();
        mefpai.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMefpaiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mefpai))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mefpai in the database
        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMefpai() throws Exception {
        int databaseSizeBeforeUpdate = mefpaiRepository.findAll().size();
        mefpai.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMefpaiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mefpai)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mefpai in the database
        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMefpai() throws Exception {
        // Initialize the database
        mefpaiRepository.saveAndFlush(mefpai);

        int databaseSizeBeforeDelete = mefpaiRepository.findAll().size();

        // Delete the mefpai
        restMefpaiMockMvc
            .perform(delete(ENTITY_API_URL_ID, mefpai.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mefpai> mefpaiList = mefpaiRepository.findAll();
        assertThat(mefpaiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
