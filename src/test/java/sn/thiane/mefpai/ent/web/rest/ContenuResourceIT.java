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
import sn.thiane.mefpai.ent.domain.Contenu;
import sn.thiane.mefpai.ent.repository.ContenuRepository;

/**
 * Integration tests for the {@link ContenuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContenuResourceIT {

    private static final String DEFAULT_NOM_CONTENU = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CONTENU = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENU = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENU = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTENU_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENU_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/contenus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContenuRepository contenuRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContenuMockMvc;

    private Contenu contenu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contenu createEntity(EntityManager em) {
        Contenu contenu = new Contenu()
            .nomContenu(DEFAULT_NOM_CONTENU)
            .contenu(DEFAULT_CONTENU)
            .contenuContentType(DEFAULT_CONTENU_CONTENT_TYPE);
        return contenu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contenu createUpdatedEntity(EntityManager em) {
        Contenu contenu = new Contenu()
            .nomContenu(UPDATED_NOM_CONTENU)
            .contenu(UPDATED_CONTENU)
            .contenuContentType(UPDATED_CONTENU_CONTENT_TYPE);
        return contenu;
    }

    @BeforeEach
    public void initTest() {
        contenu = createEntity(em);
    }

    @Test
    @Transactional
    void createContenu() throws Exception {
        int databaseSizeBeforeCreate = contenuRepository.findAll().size();
        // Create the Contenu
        restContenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contenu)))
            .andExpect(status().isCreated());

        // Validate the Contenu in the database
        List<Contenu> contenuList = contenuRepository.findAll();
        assertThat(contenuList).hasSize(databaseSizeBeforeCreate + 1);
        Contenu testContenu = contenuList.get(contenuList.size() - 1);
        assertThat(testContenu.getNomContenu()).isEqualTo(DEFAULT_NOM_CONTENU);
        assertThat(testContenu.getContenu()).isEqualTo(DEFAULT_CONTENU);
        assertThat(testContenu.getContenuContentType()).isEqualTo(DEFAULT_CONTENU_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createContenuWithExistingId() throws Exception {
        // Create the Contenu with an existing ID
        contenu.setId(1L);

        int databaseSizeBeforeCreate = contenuRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contenu)))
            .andExpect(status().isBadRequest());

        // Validate the Contenu in the database
        List<Contenu> contenuList = contenuRepository.findAll();
        assertThat(contenuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContenus() throws Exception {
        // Initialize the database
        contenuRepository.saveAndFlush(contenu);

        // Get all the contenuList
        restContenuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contenu.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomContenu").value(hasItem(DEFAULT_NOM_CONTENU)))
            .andExpect(jsonPath("$.[*].contenuContentType").value(hasItem(DEFAULT_CONTENU_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENU))));
    }

    @Test
    @Transactional
    void getContenu() throws Exception {
        // Initialize the database
        contenuRepository.saveAndFlush(contenu);

        // Get the contenu
        restContenuMockMvc
            .perform(get(ENTITY_API_URL_ID, contenu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contenu.getId().intValue()))
            .andExpect(jsonPath("$.nomContenu").value(DEFAULT_NOM_CONTENU))
            .andExpect(jsonPath("$.contenuContentType").value(DEFAULT_CONTENU_CONTENT_TYPE))
            .andExpect(jsonPath("$.contenu").value(Base64Utils.encodeToString(DEFAULT_CONTENU)));
    }

    @Test
    @Transactional
    void getNonExistingContenu() throws Exception {
        // Get the contenu
        restContenuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContenu() throws Exception {
        // Initialize the database
        contenuRepository.saveAndFlush(contenu);

        int databaseSizeBeforeUpdate = contenuRepository.findAll().size();

        // Update the contenu
        Contenu updatedContenu = contenuRepository.findById(contenu.getId()).get();
        // Disconnect from session so that the updates on updatedContenu are not directly saved in db
        em.detach(updatedContenu);
        updatedContenu.nomContenu(UPDATED_NOM_CONTENU).contenu(UPDATED_CONTENU).contenuContentType(UPDATED_CONTENU_CONTENT_TYPE);

        restContenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContenu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContenu))
            )
            .andExpect(status().isOk());

        // Validate the Contenu in the database
        List<Contenu> contenuList = contenuRepository.findAll();
        assertThat(contenuList).hasSize(databaseSizeBeforeUpdate);
        Contenu testContenu = contenuList.get(contenuList.size() - 1);
        assertThat(testContenu.getNomContenu()).isEqualTo(UPDATED_NOM_CONTENU);
        assertThat(testContenu.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testContenu.getContenuContentType()).isEqualTo(UPDATED_CONTENU_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingContenu() throws Exception {
        int databaseSizeBeforeUpdate = contenuRepository.findAll().size();
        contenu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contenu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contenu in the database
        List<Contenu> contenuList = contenuRepository.findAll();
        assertThat(contenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContenu() throws Exception {
        int databaseSizeBeforeUpdate = contenuRepository.findAll().size();
        contenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contenu in the database
        List<Contenu> contenuList = contenuRepository.findAll();
        assertThat(contenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContenu() throws Exception {
        int databaseSizeBeforeUpdate = contenuRepository.findAll().size();
        contenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContenuMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contenu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contenu in the database
        List<Contenu> contenuList = contenuRepository.findAll();
        assertThat(contenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContenuWithPatch() throws Exception {
        // Initialize the database
        contenuRepository.saveAndFlush(contenu);

        int databaseSizeBeforeUpdate = contenuRepository.findAll().size();

        // Update the contenu using partial update
        Contenu partialUpdatedContenu = new Contenu();
        partialUpdatedContenu.setId(contenu.getId());

        partialUpdatedContenu.nomContenu(UPDATED_NOM_CONTENU).contenu(UPDATED_CONTENU).contenuContentType(UPDATED_CONTENU_CONTENT_TYPE);

        restContenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContenu))
            )
            .andExpect(status().isOk());

        // Validate the Contenu in the database
        List<Contenu> contenuList = contenuRepository.findAll();
        assertThat(contenuList).hasSize(databaseSizeBeforeUpdate);
        Contenu testContenu = contenuList.get(contenuList.size() - 1);
        assertThat(testContenu.getNomContenu()).isEqualTo(UPDATED_NOM_CONTENU);
        assertThat(testContenu.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testContenu.getContenuContentType()).isEqualTo(UPDATED_CONTENU_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateContenuWithPatch() throws Exception {
        // Initialize the database
        contenuRepository.saveAndFlush(contenu);

        int databaseSizeBeforeUpdate = contenuRepository.findAll().size();

        // Update the contenu using partial update
        Contenu partialUpdatedContenu = new Contenu();
        partialUpdatedContenu.setId(contenu.getId());

        partialUpdatedContenu.nomContenu(UPDATED_NOM_CONTENU).contenu(UPDATED_CONTENU).contenuContentType(UPDATED_CONTENU_CONTENT_TYPE);

        restContenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContenu))
            )
            .andExpect(status().isOk());

        // Validate the Contenu in the database
        List<Contenu> contenuList = contenuRepository.findAll();
        assertThat(contenuList).hasSize(databaseSizeBeforeUpdate);
        Contenu testContenu = contenuList.get(contenuList.size() - 1);
        assertThat(testContenu.getNomContenu()).isEqualTo(UPDATED_NOM_CONTENU);
        assertThat(testContenu.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testContenu.getContenuContentType()).isEqualTo(UPDATED_CONTENU_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingContenu() throws Exception {
        int databaseSizeBeforeUpdate = contenuRepository.findAll().size();
        contenu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contenu in the database
        List<Contenu> contenuList = contenuRepository.findAll();
        assertThat(contenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContenu() throws Exception {
        int databaseSizeBeforeUpdate = contenuRepository.findAll().size();
        contenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contenu in the database
        List<Contenu> contenuList = contenuRepository.findAll();
        assertThat(contenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContenu() throws Exception {
        int databaseSizeBeforeUpdate = contenuRepository.findAll().size();
        contenu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContenuMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contenu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contenu in the database
        List<Contenu> contenuList = contenuRepository.findAll();
        assertThat(contenuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContenu() throws Exception {
        // Initialize the database
        contenuRepository.saveAndFlush(contenu);

        int databaseSizeBeforeDelete = contenuRepository.findAll().size();

        // Delete the contenu
        restContenuMockMvc
            .perform(delete(ENTITY_API_URL_ID, contenu.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contenu> contenuList = contenuRepository.findAll();
        assertThat(contenuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
