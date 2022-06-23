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
import sn.thiane.mefpai.ent.domain.Syllabus;
import sn.thiane.mefpai.ent.repository.SyllabusRepository;
import sn.thiane.mefpai.ent.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.thiane.mefpai.ent.domain.Syllabus}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SyllabusResource {

    private final Logger log = LoggerFactory.getLogger(SyllabusResource.class);

    private static final String ENTITY_NAME = "syllabus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SyllabusRepository syllabusRepository;

    public SyllabusResource(SyllabusRepository syllabusRepository) {
        this.syllabusRepository = syllabusRepository;
    }

    /**
     * {@code POST  /syllabi} : Create a new syllabus.
     *
     * @param syllabus the syllabus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new syllabus, or with status {@code 400 (Bad Request)} if the syllabus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/syllabi")
    public ResponseEntity<Syllabus> createSyllabus(@Valid @RequestBody Syllabus syllabus) throws URISyntaxException {
        log.debug("REST request to save Syllabus : {}", syllabus);
        if (syllabus.getId() != null) {
            throw new BadRequestAlertException("A new syllabus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Syllabus result = syllabusRepository.save(syllabus);
        return ResponseEntity
            .created(new URI("/api/syllabi/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /syllabi/:id} : Updates an existing syllabus.
     *
     * @param id the id of the syllabus to save.
     * @param syllabus the syllabus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated syllabus,
     * or with status {@code 400 (Bad Request)} if the syllabus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the syllabus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/syllabi/{id}")
    public ResponseEntity<Syllabus> updateSyllabus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Syllabus syllabus
    ) throws URISyntaxException {
        log.debug("REST request to update Syllabus : {}, {}", id, syllabus);
        if (syllabus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, syllabus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!syllabusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Syllabus result = syllabusRepository.save(syllabus);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, syllabus.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /syllabi/:id} : Partial updates given fields of an existing syllabus, field will ignore if it is null
     *
     * @param id the id of the syllabus to save.
     * @param syllabus the syllabus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated syllabus,
     * or with status {@code 400 (Bad Request)} if the syllabus is not valid,
     * or with status {@code 404 (Not Found)} if the syllabus is not found,
     * or with status {@code 500 (Internal Server Error)} if the syllabus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/syllabi/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Syllabus> partialUpdateSyllabus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Syllabus syllabus
    ) throws URISyntaxException {
        log.debug("REST request to partial update Syllabus partially : {}, {}", id, syllabus);
        if (syllabus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, syllabus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!syllabusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Syllabus> result = syllabusRepository
            .findById(syllabus.getId())
            .map(existingSyllabus -> {
                if (syllabus.getNomSyllabus() != null) {
                    existingSyllabus.setNomSyllabus(syllabus.getNomSyllabus());
                }
                if (syllabus.getSyllabus() != null) {
                    existingSyllabus.setSyllabus(syllabus.getSyllabus());
                }
                if (syllabus.getSyllabusContentType() != null) {
                    existingSyllabus.setSyllabusContentType(syllabus.getSyllabusContentType());
                }

                return existingSyllabus;
            })
            .map(syllabusRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, syllabus.getId().toString())
        );
    }

    /**
     * {@code GET  /syllabi} : get all the syllabi.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of syllabi in body.
     */
    @GetMapping("/syllabi")
    public ResponseEntity<List<Syllabus>> getAllSyllabi(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Syllabi");
        Page<Syllabus> page;
        if (eagerload) {
            page = syllabusRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = syllabusRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /syllabi/:id} : get the "id" syllabus.
     *
     * @param id the id of the syllabus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the syllabus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/syllabi/{id}")
    public ResponseEntity<Syllabus> getSyllabus(@PathVariable Long id) {
        log.debug("REST request to get Syllabus : {}", id);
        Optional<Syllabus> syllabus = syllabusRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(syllabus);
    }

    /**
     * {@code DELETE  /syllabi/:id} : delete the "id" syllabus.
     *
     * @param id the id of the syllabus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/syllabi/{id}")
    public ResponseEntity<Void> deleteSyllabus(@PathVariable Long id) {
        log.debug("REST request to delete Syllabus : {}", id);
        syllabusRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
