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
import sn.thiane.mefpai.ent.domain.Directeur;
import sn.thiane.mefpai.ent.domain.enumeration.Sexe;
import sn.thiane.mefpai.ent.repository.DirecteurRepository;

/**
 * Integration tests for the {@link DirecteurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DirecteurResourceIT {

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

    private static final String ENTITY_API_URL = "/api/directeurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DirecteurRepository directeurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDirecteurMockMvc;

    private Directeur directeur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Directeur createEntity(EntityManager em) {
        Directeur directeur = new Directeur()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .email(DEFAULT_EMAIL)
            .adresse(DEFAULT_ADRESSE)
            .telephone(DEFAULT_TELEPHONE)
            .sexe(DEFAULT_SEXE)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE);
        return directeur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Directeur createUpdatedEntity(EntityManager em) {
        Directeur directeur = new Directeur()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .sexe(UPDATED_SEXE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);
        return directeur;
    }

    @BeforeEach
    public void initTest() {
        directeur = createEntity(em);
    }

    @Test
    @Transactional
    void createDirecteur() throws Exception {
        int databaseSizeBeforeCreate = directeurRepository.findAll().size();
        // Create the Directeur
        restDirecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directeur)))
            .andExpect(status().isCreated());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeCreate + 1);
        Directeur testDirecteur = directeurList.get(directeurList.size() - 1);
        assertThat(testDirecteur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testDirecteur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testDirecteur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDirecteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testDirecteur.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testDirecteur.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testDirecteur.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testDirecteur.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createDirecteurWithExistingId() throws Exception {
        // Create the Directeur with an existing ID
        directeur.setId(1L);

        int databaseSizeBeforeCreate = directeurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directeur)))
            .andExpect(status().isBadRequest());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = directeurRepository.findAll().size();
        // set the field null
        directeur.setNom(null);

        // Create the Directeur, which fails.

        restDirecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directeur)))
            .andExpect(status().isBadRequest());

        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = directeurRepository.findAll().size();
        // set the field null
        directeur.setPrenom(null);

        // Create the Directeur, which fails.

        restDirecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directeur)))
            .andExpect(status().isBadRequest());

        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = directeurRepository.findAll().size();
        // set the field null
        directeur.setEmail(null);

        // Create the Directeur, which fails.

        restDirecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directeur)))
            .andExpect(status().isBadRequest());

        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = directeurRepository.findAll().size();
        // set the field null
        directeur.setAdresse(null);

        // Create the Directeur, which fails.

        restDirecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directeur)))
            .andExpect(status().isBadRequest());

        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = directeurRepository.findAll().size();
        // set the field null
        directeur.setTelephone(null);

        // Create the Directeur, which fails.

        restDirecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directeur)))
            .andExpect(status().isBadRequest());

        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = directeurRepository.findAll().size();
        // set the field null
        directeur.setSexe(null);

        // Create the Directeur, which fails.

        restDirecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directeur)))
            .andExpect(status().isBadRequest());

        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDirecteurs() throws Exception {
        // Initialize the database
        directeurRepository.saveAndFlush(directeur);

        // Get all the directeurList
        restDirecteurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directeur.getId().intValue())))
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
    void getDirecteur() throws Exception {
        // Initialize the database
        directeurRepository.saveAndFlush(directeur);

        // Get the directeur
        restDirecteurMockMvc
            .perform(get(ENTITY_API_URL_ID, directeur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(directeur.getId().intValue()))
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
    void getNonExistingDirecteur() throws Exception {
        // Get the directeur
        restDirecteurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDirecteur() throws Exception {
        // Initialize the database
        directeurRepository.saveAndFlush(directeur);

        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();

        // Update the directeur
        Directeur updatedDirecteur = directeurRepository.findById(directeur.getId()).get();
        // Disconnect from session so that the updates on updatedDirecteur are not directly saved in db
        em.detach(updatedDirecteur);
        updatedDirecteur
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .sexe(UPDATED_SEXE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restDirecteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDirecteur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDirecteur))
            )
            .andExpect(status().isOk());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
        Directeur testDirecteur = directeurList.get(directeurList.size() - 1);
        assertThat(testDirecteur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDirecteur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testDirecteur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDirecteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testDirecteur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testDirecteur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testDirecteur.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testDirecteur.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();
        directeur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirecteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, directeur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();
        directeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirecteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();
        directeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirecteurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directeur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDirecteurWithPatch() throws Exception {
        // Initialize the database
        directeurRepository.saveAndFlush(directeur);

        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();

        // Update the directeur using partial update
        Directeur partialUpdatedDirecteur = new Directeur();
        partialUpdatedDirecteur.setId(directeur.getId());

        partialUpdatedDirecteur
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .sexe(UPDATED_SEXE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restDirecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirecteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDirecteur))
            )
            .andExpect(status().isOk());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
        Directeur testDirecteur = directeurList.get(directeurList.size() - 1);
        assertThat(testDirecteur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testDirecteur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testDirecteur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDirecteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testDirecteur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testDirecteur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testDirecteur.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testDirecteur.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateDirecteurWithPatch() throws Exception {
        // Initialize the database
        directeurRepository.saveAndFlush(directeur);

        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();

        // Update the directeur using partial update
        Directeur partialUpdatedDirecteur = new Directeur();
        partialUpdatedDirecteur.setId(directeur.getId());

        partialUpdatedDirecteur
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .sexe(UPDATED_SEXE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restDirecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirecteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDirecteur))
            )
            .andExpect(status().isOk());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
        Directeur testDirecteur = directeurList.get(directeurList.size() - 1);
        assertThat(testDirecteur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDirecteur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testDirecteur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDirecteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testDirecteur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testDirecteur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testDirecteur.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testDirecteur.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();
        directeur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, directeur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();
        directeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDirecteur() throws Exception {
        int databaseSizeBeforeUpdate = directeurRepository.findAll().size();
        directeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirecteurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(directeur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Directeur in the database
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDirecteur() throws Exception {
        // Initialize the database
        directeurRepository.saveAndFlush(directeur);

        int databaseSizeBeforeDelete = directeurRepository.findAll().size();

        // Delete the directeur
        restDirecteurMockMvc
            .perform(delete(ENTITY_API_URL_ID, directeur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Directeur> directeurList = directeurRepository.findAll();
        assertThat(directeurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
