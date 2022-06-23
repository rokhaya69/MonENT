package sn.thiane.mefpai.ent.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.thiane.mefpai.ent.domain.Mefpai;
import sn.thiane.mefpai.ent.repository.MefpaiRepository;
import sn.thiane.mefpai.ent.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.thiane.mefpai.ent.domain.Mefpai}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MefpaiResource {

    private final Logger log = LoggerFactory.getLogger(MefpaiResource.class);

    private static final String ENTITY_NAME = "mefpai";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MefpaiRepository mefpaiRepository;

    public MefpaiResource(MefpaiRepository mefpaiRepository) {
        this.mefpaiRepository = mefpaiRepository;
    }

    /**
     * {@code POST  /mefpais} : Create a new mefpai.
     *
     * @param mefpai the mefpai to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mefpai, or with status {@code 400 (Bad Request)} if the mefpai has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mefpais")
    public ResponseEntity<Mefpai> createMefpai(@Valid @RequestBody Mefpai mefpai) throws URISyntaxException {
        log.debug("REST request to save Mefpai : {}", mefpai);
        if (mefpai.getId() != null) {
            throw new BadRequestAlertException("A new mefpai cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mefpai result = mefpaiRepository.save(mefpai);
        return ResponseEntity
            .created(new URI("/api/mefpais/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mefpais/:id} : Updates an existing mefpai.
     *
     * @param id the id of the mefpai to save.
     * @param mefpai the mefpai to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mefpai,
     * or with status {@code 400 (Bad Request)} if the mefpai is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mefpai couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mefpais/{id}")
    public ResponseEntity<Mefpai> updateMefpai(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Mefpai mefpai
    ) throws URISyntaxException {
        log.debug("REST request to update Mefpai : {}, {}", id, mefpai);
        if (mefpai.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mefpai.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mefpaiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mefpai result = mefpaiRepository.save(mefpai);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mefpai.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mefpais/:id} : Partial updates given fields of an existing mefpai, field will ignore if it is null
     *
     * @param id the id of the mefpai to save.
     * @param mefpai the mefpai to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mefpai,
     * or with status {@code 400 (Bad Request)} if the mefpai is not valid,
     * or with status {@code 404 (Not Found)} if the mefpai is not found,
     * or with status {@code 500 (Internal Server Error)} if the mefpai couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mefpais/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Mefpai> partialUpdateMefpai(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Mefpai mefpai
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mefpai partially : {}, {}", id, mefpai);
        if (mefpai.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mefpai.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mefpaiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Mefpai> result = mefpaiRepository
            .findById(mefpai.getId())
            .map(existingMefpai -> {
                if (mefpai.getNom() != null) {
                    existingMefpai.setNom(mefpai.getNom());
                }
                if (mefpai.getPrenom() != null) {
                    existingMefpai.setPrenom(mefpai.getPrenom());
                }
                if (mefpai.getEmail() != null) {
                    existingMefpai.setEmail(mefpai.getEmail());
                }
                if (mefpai.getAdresse() != null) {
                    existingMefpai.setAdresse(mefpai.getAdresse());
                }
                if (mefpai.getTelephone() != null) {
                    existingMefpai.setTelephone(mefpai.getTelephone());
                }
                if (mefpai.getSexe() != null) {
                    existingMefpai.setSexe(mefpai.getSexe());
                }
                if (mefpai.getPhoto() != null) {
                    existingMefpai.setPhoto(mefpai.getPhoto());
                }
                if (mefpai.getPhotoContentType() != null) {
                    existingMefpai.setPhotoContentType(mefpai.getPhotoContentType());
                }

                return existingMefpai;
            })
            .map(mefpaiRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mefpai.getId().toString())
        );
    }

    /**
     * {@code GET  /mefpais} : get all the mefpais.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mefpais in body.
     */
    @GetMapping("/mefpais")
    public ResponseEntity<List<Mefpai>> getAllMefpais(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Mefpais");
        Page<Mefpai> page;
        if (eagerload) {
            page = mefpaiRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = mefpaiRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mefpais/:id} : get the "id" mefpai.
     *
     * @param id the id of the mefpai to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mefpai, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mefpais/{id}")
    public ResponseEntity<Mefpai> getMefpai(@PathVariable Long id) {
        log.debug("REST request to get Mefpai : {}", id);
        Optional<Mefpai> mefpai = mefpaiRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(mefpai);
    }

    /**
     * {@code DELETE  /mefpais/:id} : delete the "id" mefpai.
     *
     * @param id the id of the mefpai to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mefpais/{id}")
    public ResponseEntity<Void> deleteMefpai(@PathVariable Long id) {
        log.debug("REST request to delete Mefpai : {}", id);
        mefpaiRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
