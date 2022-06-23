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
import sn.thiane.mefpai.ent.domain.Cours;
import sn.thiane.mefpai.ent.repository.CoursRepository;
import sn.thiane.mefpai.ent.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.thiane.mefpai.ent.domain.Cours}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CoursResource {

    private final Logger log = LoggerFactory.getLogger(CoursResource.class);

    private static final String ENTITY_NAME = "cours";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoursRepository coursRepository;

    public CoursResource(CoursRepository coursRepository) {
        this.coursRepository = coursRepository;
    }

    /**
     * {@code POST  /cours} : Create a new cours.
     *
     * @param cours the cours to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cours, or with status {@code 400 (Bad Request)} if the cours has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cours")
    public ResponseEntity<Cours> createCours(@Valid @RequestBody Cours cours) throws URISyntaxException {
        log.debug("REST request to save Cours : {}", cours);
        if (cours.getId() != null) {
            throw new BadRequestAlertException("A new cours cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cours result = coursRepository.save(cours);
        return ResponseEntity
            .created(new URI("/api/cours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cours/:id} : Updates an existing cours.
     *
     * @param id the id of the cours to save.
     * @param cours the cours to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cours,
     * or with status {@code 400 (Bad Request)} if the cours is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cours couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cours/{id}")
    public ResponseEntity<Cours> updateCours(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Cours cours)
        throws URISyntaxException {
        log.debug("REST request to update Cours : {}, {}", id, cours);
        if (cours.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cours.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cours result = coursRepository.save(cours);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cours.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cours/:id} : Partial updates given fields of an existing cours, field will ignore if it is null
     *
     * @param id the id of the cours to save.
     * @param cours the cours to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cours,
     * or with status {@code 400 (Bad Request)} if the cours is not valid,
     * or with status {@code 404 (Not Found)} if the cours is not found,
     * or with status {@code 500 (Internal Server Error)} if the cours couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cours/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cours> partialUpdateCours(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Cours cours
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cours partially : {}, {}", id, cours);
        if (cours.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cours.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cours> result = coursRepository
            .findById(cours.getId())
            .map(existingCours -> {
                if (cours.getLibelleCours() != null) {
                    existingCours.setLibelleCours(cours.getLibelleCours());
                }

                return existingCours;
            })
            .map(coursRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cours.getId().toString())
        );
    }

    /**
     * {@code GET  /cours} : get all the cours.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cours in body.
     */
    @GetMapping("/cours")
    public ResponseEntity<List<Cours>> getAllCours(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Cours");
        Page<Cours> page;
        if (eagerload) {
            page = coursRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = coursRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cours/:id} : get the "id" cours.
     *
     * @param id the id of the cours to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cours, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cours/{id}")
    public ResponseEntity<Cours> getCours(@PathVariable Long id) {
        log.debug("REST request to get Cours : {}", id);
        Optional<Cours> cours = coursRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(cours);
    }

    /**
     * {@code DELETE  /cours/:id} : delete the "id" cours.
     *
     * @param id the id of the cours to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cours/{id}")
    public ResponseEntity<Void> deleteCours(@PathVariable Long id) {
        log.debug("REST request to delete Cours : {}", id);
        coursRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
