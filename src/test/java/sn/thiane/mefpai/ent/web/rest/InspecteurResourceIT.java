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
import sn.thiane.mefpai.ent.domain.Inspecteur;
import sn.thiane.mefpai.ent.domain.enumeration.Sexe;
import sn.thiane.mefpai.ent.repository.InspecteurRepository;

/**
 * Integration tests for the {@link InspecteurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InspecteurResourceIT {

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

    private static final String ENTITY_API_URL = "/api/inspecteurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InspecteurRepository inspecteurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInspecteurMockMvc;

    private Inspecteur inspecteur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inspecteur createEntity(EntityManager em) {
        Inspecteur inspecteur = new Inspecteur()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .email(DEFAULT_EMAIL)
            .adresse(DEFAULT_ADRESSE)
            .telephone(DEFAULT_TELEPHONE)
            .sexe(DEFAULT_SEXE)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE);
        return inspecteur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inspecteur createUpdatedEntity(EntityManager em) {
        Inspecteur inspecteur = new Inspecteur()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .sexe(UPDATED_SEXE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);
        return inspecteur;
    }

    @BeforeEach
    public void initTest() {
        inspecteur = createEntity(em);
    }

    @Test
    @Transactional
    void createInspecteur() throws Exception {
        int databaseSizeBeforeCreate = inspecteurRepository.findAll().size();
        // Create the Inspecteur
        restInspecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspecteur)))
            .andExpect(status().isCreated());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeCreate + 1);
        Inspecteur testInspecteur = inspecteurList.get(inspecteurList.size() - 1);
        assertThat(testInspecteur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testInspecteur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testInspecteur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testInspecteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testInspecteur.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testInspecteur.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testInspecteur.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testInspecteur.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createInspecteurWithExistingId() throws Exception {
        // Create the Inspecteur with an existing ID
        inspecteur.setId(1L);

        int databaseSizeBeforeCreate = inspecteurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInspecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspecteur)))
            .andExpect(status().isBadRequest());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = inspecteurRepository.findAll().size();
        // set the field null
        inspecteur.setNom(null);

        // Create the Inspecteur, which fails.

        restInspecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspecteur)))
            .andExpect(status().isBadRequest());

        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = inspecteurRepository.findAll().size();
        // set the field null
        inspecteur.setPrenom(null);

        // Create the Inspecteur, which fails.

        restInspecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspecteur)))
            .andExpect(status().isBadRequest());

        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = inspecteurRepository.findAll().size();
        // set the field null
        inspecteur.setEmail(null);

        // Create the Inspecteur, which fails.

        restInspecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspecteur)))
            .andExpect(status().isBadRequest());

        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseIsRequired() throws Exception {
        int databaseSizeBeforeTest = inspecteurRepository.findAll().size();
        // set the field null
        inspecteur.setAdresse(null);

        // Create the Inspecteur, which fails.

        restInspecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspecteur)))
            .andExpect(status().isBadRequest());

        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = inspecteurRepository.findAll().size();
        // set the field null
        inspecteur.setTelephone(null);

        // Create the Inspecteur, which fails.

        restInspecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspecteur)))
            .andExpect(status().isBadRequest());

        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = inspecteurRepository.findAll().size();
        // set the field null
        inspecteur.setSexe(null);

        // Create the Inspecteur, which fails.

        restInspecteurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspecteur)))
            .andExpect(status().isBadRequest());

        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInspecteurs() throws Exception {
        // Initialize the database
        inspecteurRepository.saveAndFlush(inspecteur);

        // Get all the inspecteurList
        restInspecteurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inspecteur.getId().intValue())))
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
    void getInspecteur() throws Exception {
        // Initialize the database
        inspecteurRepository.saveAndFlush(inspecteur);

        // Get the inspecteur
        restInspecteurMockMvc
            .perform(get(ENTITY_API_URL_ID, inspecteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inspecteur.getId().intValue()))
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
    void getNonExistingInspecteur() throws Exception {
        // Get the inspecteur
        restInspecteurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInspecteur() throws Exception {
        // Initialize the database
        inspecteurRepository.saveAndFlush(inspecteur);

        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();

        // Update the inspecteur
        Inspecteur updatedInspecteur = inspecteurRepository.findById(inspecteur.getId()).get();
        // Disconnect from session so that the updates on updatedInspecteur are not directly saved in db
        em.detach(updatedInspecteur);
        updatedInspecteur
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .sexe(UPDATED_SEXE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restInspecteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInspecteur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInspecteur))
            )
            .andExpect(status().isOk());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
        Inspecteur testInspecteur = inspecteurList.get(inspecteurList.size() - 1);
        assertThat(testInspecteur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testInspecteur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testInspecteur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testInspecteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testInspecteur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testInspecteur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testInspecteur.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testInspecteur.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingInspecteur() throws Exception {
        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();
        inspecteur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspecteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inspecteur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspecteur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInspecteur() throws Exception {
        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();
        inspecteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspecteurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspecteur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInspecteur() throws Exception {
        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();
        inspecteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspecteurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inspecteur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInspecteurWithPatch() throws Exception {
        // Initialize the database
        inspecteurRepository.saveAndFlush(inspecteur);

        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();

        // Update the inspecteur using partial update
        Inspecteur partialUpdatedInspecteur = new Inspecteur();
        partialUpdatedInspecteur.setId(inspecteur.getId());

        partialUpdatedInspecteur
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .sexe(UPDATED_SEXE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restInspecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspecteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInspecteur))
            )
            .andExpect(status().isOk());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
        Inspecteur testInspecteur = inspecteurList.get(inspecteurList.size() - 1);
        assertThat(testInspecteur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testInspecteur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testInspecteur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testInspecteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testInspecteur.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testInspecteur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testInspecteur.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testInspecteur.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateInspecteurWithPatch() throws Exception {
        // Initialize the database
        inspecteurRepository.saveAndFlush(inspecteur);

        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();

        // Update the inspecteur using partial update
        Inspecteur partialUpdatedInspecteur = new Inspecteur();
        partialUpdatedInspecteur.setId(inspecteur.getId());

        partialUpdatedInspecteur
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .telephone(UPDATED_TELEPHONE)
            .sexe(UPDATED_SEXE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restInspecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspecteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInspecteur))
            )
            .andExpect(status().isOk());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
        Inspecteur testInspecteur = inspecteurList.get(inspecteurList.size() - 1);
        assertThat(testInspecteur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testInspecteur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testInspecteur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testInspecteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testInspecteur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testInspecteur.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testInspecteur.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testInspecteur.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingInspecteur() throws Exception {
        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();
        inspecteur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inspecteur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspecteur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInspecteur() throws Exception {
        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();
        inspecteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspecteurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspecteur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInspecteur() throws Exception {
        int databaseSizeBeforeUpdate = inspecteurRepository.findAll().size();
        inspecteur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspecteurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(inspecteur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inspecteur in the database
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInspecteur() throws Exception {
        // Initialize the database
        inspecteurRepository.saveAndFlush(inspecteur);

        int databaseSizeBeforeDelete = inspecteurRepository.findAll().size();

        // Delete the inspecteur
        restInspecteurMockMvc
            .perform(delete(ENTITY_API_URL_ID, inspecteur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Inspecteur> inspecteurList = inspecteurRepository.findAll();
        assertThat(inspecteurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
