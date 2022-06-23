package sn.thiane.mefpai.ent.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import sn.thiane.mefpai.ent.domain.Contenu;
import sn.thiane.mefpai.ent.repository.ContenuRepository;
import sn.thiane.mefpai.ent.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.thiane.mefpai.ent.domain.Contenu}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ContenuResource {

    private final Logger log = LoggerFactory.getLogger(ContenuResource.class);

    private static final String ENTITY_NAME = "contenu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContenuRepository contenuRepository;

    public ContenuResource(ContenuRepository contenuRepository) {
        this.contenuRepository = contenuRepository;
    }

    /**
     * {@code POST  /contenus} : Create a new contenu.
     *
     * @param contenu the contenu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contenu, or with status {@code 400 (Bad Request)} if the contenu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contenus")
    public ResponseEntity<Contenu> createContenu(@RequestBody Contenu contenu) throws URISyntaxException {
        log.debug("REST request to save Contenu : {}", contenu);
        if (contenu.getId() != null) {
            throw new BadRequestAlertException("A new contenu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Contenu result = contenuRepository.save(contenu);
        return ResponseEntity
            .created(new URI("/api/contenus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contenus/:id} : Updates an existing contenu.
     *
     * @param id the id of the contenu to save.
     * @param contenu the contenu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contenu,
     * or with status {@code 400 (Bad Request)} if the contenu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contenu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contenus/{id}")
    public ResponseEntity<Contenu> updateContenu(@PathVariable(value = "id", required = false) final Long id, @RequestBody Contenu contenu)
        throws URISyntaxException {
        log.debug("REST request to update Contenu : {}, {}", id, contenu);
        if (contenu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contenu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contenuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Contenu result = contenuRepository.save(contenu);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contenu.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contenus/:id} : Partial updates given fields of an existing contenu, field will ignore if it is null
     *
     * @param id the id of the contenu to save.
     * @param contenu the contenu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contenu,
     * or with status {@code 400 (Bad Request)} if the contenu is not valid,
     * or with status {@code 404 (Not Found)} if the contenu is not found,
     * or with status {@code 500 (Internal Server Error)} if the contenu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contenus/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Contenu> partialUpdateContenu(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Contenu contenu
    ) throws URISyntaxException {
        log.debug("REST request to partial update Contenu partially : {}, {}", id, contenu);
        if (contenu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contenu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contenuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Contenu> result = contenuRepository
            .findById(contenu.getId())
            .map(existingContenu -> {
                if (contenu.getNomContenu() != null) {
                    existingContenu.setNomContenu(contenu.getNomContenu());
                }
                if (contenu.getContenu() != null) {
                    existingContenu.setContenu(contenu.getContenu());
                }
                if (contenu.getContenuContentType() != null) {
                    existingContenu.setContenuContentType(contenu.getContenuContentType());
                }

                return existingContenu;
            })
            .map(contenuRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contenu.getId().toString())
        );
    }

    /**
     * {@code GET  /contenus} : get all the contenus.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contenus in body.
     */
    @GetMapping("/contenus")
    public ResponseEntity<List<Contenu>> getAllContenus(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Contenus");
        Page<Contenu> page = contenuRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contenus/:id} : get the "id" contenu.
     *
     * @param id the id of the contenu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contenu, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contenus/{id}")
    public ResponseEntity<Contenu> getContenu(@PathVariable Long id) {
        log.debug("REST request to get Contenu : {}", id);
        Optional<Contenu> contenu = contenuRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contenu);
    }

    /**
     * {@code DELETE  /contenus/:id} : delete the "id" contenu.
     *
     * @param id the id of the contenu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contenus/{id}")
    public ResponseEntity<Void> deleteContenu(@PathVariable Long id) {
        log.debug("REST request to delete Contenu : {}", id);
        contenuRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
