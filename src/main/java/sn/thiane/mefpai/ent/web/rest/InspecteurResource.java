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
import sn.thiane.mefpai.ent.domain.Inspecteur;
import sn.thiane.mefpai.ent.repository.InspecteurRepository;
import sn.thiane.mefpai.ent.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.thiane.mefpai.ent.domain.Inspecteur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InspecteurResource {

    private final Logger log = LoggerFactory.getLogger(InspecteurResource.class);

    private static final String ENTITY_NAME = "inspecteur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InspecteurRepository inspecteurRepository;

    public InspecteurResource(InspecteurRepository inspecteurRepository) {
        this.inspecteurRepository = inspecteurRepository;
    }

    /**
     * {@code POST  /inspecteurs} : Create a new inspecteur.
     *
     * @param inspecteur the inspecteur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inspecteur, or with status {@code 400 (Bad Request)} if the inspecteur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inspecteurs")
    public ResponseEntity<Inspecteur> createInspecteur(@Valid @RequestBody Inspecteur inspecteur) throws URISyntaxException {
        log.debug("REST request to save Inspecteur : {}", inspecteur);
        if (inspecteur.getId() != null) {
            throw new BadRequestAlertException("A new inspecteur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Inspecteur result = inspecteurRepository.save(inspecteur);
        return ResponseEntity
            .created(new URI("/api/inspecteurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inspecteurs/:id} : Updates an existing inspecteur.
     *
     * @param id the id of the inspecteur to save.
     * @param inspecteur the inspecteur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspecteur,
     * or with status {@code 400 (Bad Request)} if the inspecteur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inspecteur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inspecteurs/{id}")
    public ResponseEntity<Inspecteur> updateInspecteur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Inspecteur inspecteur
    ) throws URISyntaxException {
        log.debug("REST request to update Inspecteur : {}, {}", id, inspecteur);
        if (inspecteur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspecteur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspecteurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Inspecteur result = inspecteurRepository.save(inspecteur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspecteur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /inspecteurs/:id} : Partial updates given fields of an existing inspecteur, field will ignore if it is null
     *
     * @param id the id of the inspecteur to save.
     * @param inspecteur the inspecteur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspecteur,
     * or with status {@code 400 (Bad Request)} if the inspecteur is not valid,
     * or with status {@code 404 (Not Found)} if the inspecteur is not found,
     * or with status {@code 500 (Internal Server Error)} if the inspecteur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inspecteurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Inspecteur> partialUpdateInspecteur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Inspecteur inspecteur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Inspecteur partially : {}, {}", id, inspecteur);
        if (inspecteur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspecteur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspecteurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Inspecteur> result = inspecteurRepository
            .findById(inspecteur.getId())
            .map(existingInspecteur -> {
                if (inspecteur.getNom() != null) {
                    existingInspecteur.setNom(inspecteur.getNom());
                }
                if (inspecteur.getPrenom() != null) {
                    existingInspecteur.setPrenom(inspecteur.getPrenom());
                }
                if (inspecteur.getEmail() != null) {
                    existingInspecteur.setEmail(inspecteur.getEmail());
                }
                if (inspecteur.getAdresse() != null) {
                    existingInspecteur.setAdresse(inspecteur.getAdresse());
                }
                if (inspecteur.getTelephone() != null) {
                    existingInspecteur.setTelephone(inspecteur.getTelephone());
                }
                if (inspecteur.getSexe() != null) {
                    existingInspecteur.setSexe(inspecteur.getSexe());
                }
                if (inspecteur.getPhoto() != null) {
                    existingInspecteur.setPhoto(inspecteur.getPhoto());
                }
                if (inspecteur.getPhotoContentType() != null) {
                    existingInspecteur.setPhotoContentType(inspecteur.getPhotoContentType());
                }

                return existingInspecteur;
            })
            .map(inspecteurRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspecteur.getId().toString())
        );
    }

    /**
     * {@code GET  /inspecteurs} : get all the inspecteurs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inspecteurs in body.
     */
    @GetMapping("/inspecteurs")
    public ResponseEntity<List<Inspecteur>> getAllInspecteurs(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Inspecteurs");
        Page<Inspecteur> page;
        if (eagerload) {
            page = inspecteurRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = inspecteurRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inspecteurs/:id} : get the "id" inspecteur.
     *
     * @param id the id of the inspecteur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inspecteur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inspecteurs/{id}")
    public ResponseEntity<Inspecteur> getInspecteur(@PathVariable Long id) {
        log.debug("REST request to get Inspecteur : {}", id);
        Optional<Inspecteur> inspecteur = inspecteurRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(inspecteur);
    }

    /**
     * {@code DELETE  /inspecteurs/:id} : delete the "id" inspecteur.
     *
     * @param id the id of the inspecteur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inspecteurs/{id}")
    public ResponseEntity<Void> deleteInspecteur(@PathVariable Long id) {
        log.debug("REST request to delete Inspecteur : {}", id);
        inspecteurRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
