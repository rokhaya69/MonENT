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
import sn.thiane.mefpai.ent.domain.Surveillant;
import sn.thiane.mefpai.ent.repository.SurveillantRepository;
import sn.thiane.mefpai.ent.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.thiane.mefpai.ent.domain.Surveillant}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SurveillantResource {

    private final Logger log = LoggerFactory.getLogger(SurveillantResource.class);

    private static final String ENTITY_NAME = "surveillant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SurveillantRepository surveillantRepository;

    public SurveillantResource(SurveillantRepository surveillantRepository) {
        this.surveillantRepository = surveillantRepository;
    }

    /**
     * {@code POST  /surveillants} : Create a new surveillant.
     *
     * @param surveillant the surveillant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new surveillant, or with status {@code 400 (Bad Request)} if the surveillant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/surveillants")
    public ResponseEntity<Surveillant> createSurveillant(@Valid @RequestBody Surveillant surveillant) throws URISyntaxException {
        log.debug("REST request to save Surveillant : {}", surveillant);
        if (surveillant.getId() != null) {
            throw new BadRequestAlertException("A new surveillant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Surveillant result = surveillantRepository.save(surveillant);
        return ResponseEntity
            .created(new URI("/api/surveillants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /surveillants/:id} : Updates an existing surveillant.
     *
     * @param id the id of the surveillant to save.
     * @param surveillant the surveillant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surveillant,
     * or with status {@code 400 (Bad Request)} if the surveillant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the surveillant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/surveillants/{id}")
    public ResponseEntity<Surveillant> updateSurveillant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Surveillant surveillant
    ) throws URISyntaxException {
        log.debug("REST request to update Surveillant : {}, {}", id, surveillant);
        if (surveillant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surveillant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surveillantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Surveillant result = surveillantRepository.save(surveillant);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, surveillant.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /surveillants/:id} : Partial updates given fields of an existing surveillant, field will ignore if it is null
     *
     * @param id the id of the surveillant to save.
     * @param surveillant the surveillant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surveillant,
     * or with status {@code 400 (Bad Request)} if the surveillant is not valid,
     * or with status {@code 404 (Not Found)} if the surveillant is not found,
     * or with status {@code 500 (Internal Server Error)} if the surveillant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/surveillants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Surveillant> partialUpdateSurveillant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Surveillant surveillant
    ) throws URISyntaxException {
        log.debug("REST request to partial update Surveillant partially : {}, {}", id, surveillant);
        if (surveillant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surveillant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surveillantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Surveillant> result = surveillantRepository
            .findById(surveillant.getId())
            .map(existingSurveillant -> {
                if (surveillant.getNom() != null) {
                    existingSurveillant.setNom(surveillant.getNom());
                }
                if (surveillant.getPrenom() != null) {
                    existingSurveillant.setPrenom(surveillant.getPrenom());
                }
                if (surveillant.getEmail() != null) {
                    existingSurveillant.setEmail(surveillant.getEmail());
                }
                if (surveillant.getAdresse() != null) {
                    existingSurveillant.setAdresse(surveillant.getAdresse());
                }
                if (surveillant.getTelephone() != null) {
                    existingSurveillant.setTelephone(surveillant.getTelephone());
                }
                if (surveillant.getSexe() != null) {
                    existingSurveillant.setSexe(surveillant.getSexe());
                }
                if (surveillant.getPhoto() != null) {
                    existingSurveillant.setPhoto(surveillant.getPhoto());
                }
                if (surveillant.getPhotoContentType() != null) {
                    existingSurveillant.setPhotoContentType(surveillant.getPhotoContentType());
                }

                return existingSurveillant;
            })
            .map(surveillantRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, surveillant.getId().toString())
        );
    }

    /**
     * {@code GET  /surveillants} : get all the surveillants.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of surveillants in body.
     */
    @GetMapping("/surveillants")
    public ResponseEntity<List<Surveillant>> getAllSurveillants(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Surveillants");
        Page<Surveillant> page;
        if (eagerload) {
            page = surveillantRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = surveillantRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /surveillants/:id} : get the "id" surveillant.
     *
     * @param id the id of the surveillant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the surveillant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/surveillants/{id}")
    public ResponseEntity<Surveillant> getSurveillant(@PathVariable Long id) {
        log.debug("REST request to get Surveillant : {}", id);
        Optional<Surveillant> surveillant = surveillantRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(surveillant);
    }

    /**
     * {@code DELETE  /surveillants/:id} : delete the "id" surveillant.
     *
     * @param id the id of the surveillant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/surveillants/{id}")
    public ResponseEntity<Void> deleteSurveillant(@PathVariable Long id) {
        log.debug("REST request to delete Surveillant : {}", id);
        surveillantRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
