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
import sn.thiane.mefpai.ent.domain.Directeur;
import sn.thiane.mefpai.ent.repository.DirecteurRepository;
import sn.thiane.mefpai.ent.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.thiane.mefpai.ent.domain.Directeur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DirecteurResource {

    private final Logger log = LoggerFactory.getLogger(DirecteurResource.class);

    private static final String ENTITY_NAME = "directeur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DirecteurRepository directeurRepository;

    public DirecteurResource(DirecteurRepository directeurRepository) {
        this.directeurRepository = directeurRepository;
    }

    /**
     * {@code POST  /directeurs} : Create a new directeur.
     *
     * @param directeur the directeur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new directeur, or with status {@code 400 (Bad Request)} if the directeur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/directeurs")
    public ResponseEntity<Directeur> createDirecteur(@Valid @RequestBody Directeur directeur) throws URISyntaxException {
        log.debug("REST request to save Directeur : {}", directeur);
        if (directeur.getId() != null) {
            throw new BadRequestAlertException("A new directeur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Directeur result = directeurRepository.save(directeur);
        return ResponseEntity
            .created(new URI("/api/directeurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /directeurs/:id} : Updates an existing directeur.
     *
     * @param id the id of the directeur to save.
     * @param directeur the directeur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated directeur,
     * or with status {@code 400 (Bad Request)} if the directeur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the directeur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/directeurs/{id}")
    public ResponseEntity<Directeur> updateDirecteur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Directeur directeur
    ) throws URISyntaxException {
        log.debug("REST request to update Directeur : {}, {}", id, directeur);
        if (directeur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, directeur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!directeurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Directeur result = directeurRepository.save(directeur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, directeur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /directeurs/:id} : Partial updates given fields of an existing directeur, field will ignore if it is null
     *
     * @param id the id of the directeur to save.
     * @param directeur the directeur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated directeur,
     * or with status {@code 400 (Bad Request)} if the directeur is not valid,
     * or with status {@code 404 (Not Found)} if the directeur is not found,
     * or with status {@code 500 (Internal Server Error)} if the directeur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/directeurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Directeur> partialUpdateDirecteur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Directeur directeur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Directeur partially : {}, {}", id, directeur);
        if (directeur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, directeur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!directeurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Directeur> result = directeurRepository
            .findById(directeur.getId())
            .map(existingDirecteur -> {
                if (directeur.getNom() != null) {
                    existingDirecteur.setNom(directeur.getNom());
                }
                if (directeur.getPrenom() != null) {
                    existingDirecteur.setPrenom(directeur.getPrenom());
                }
                if (directeur.getEmail() != null) {
                    existingDirecteur.setEmail(directeur.getEmail());
                }
                if (directeur.getAdresse() != null) {
                    existingDirecteur.setAdresse(directeur.getAdresse());
                }
                if (directeur.getTelephone() != null) {
                    existingDirecteur.setTelephone(directeur.getTelephone());
                }
                if (directeur.getSexe() != null) {
                    existingDirecteur.setSexe(directeur.getSexe());
                }
                if (directeur.getPhoto() != null) {
                    existingDirecteur.setPhoto(directeur.getPhoto());
                }
                if (directeur.getPhotoContentType() != null) {
                    existingDirecteur.setPhotoContentType(directeur.getPhotoContentType());
                }

                return existingDirecteur;
            })
            .map(directeurRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, directeur.getId().toString())
        );
    }

    /**
     * {@code GET  /directeurs} : get all the directeurs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of directeurs in body.
     */
    @GetMapping("/directeurs")
    public ResponseEntity<List<Directeur>> getAllDirecteurs(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Directeurs");
        Page<Directeur> page;
        if (eagerload) {
            page = directeurRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = directeurRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /directeurs/:id} : get the "id" directeur.
     *
     * @param id the id of the directeur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the directeur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/directeurs/{id}")
    public ResponseEntity<Directeur> getDirecteur(@PathVariable Long id) {
        log.debug("REST request to get Directeur : {}", id);
        Optional<Directeur> directeur = directeurRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(directeur);
    }

    /**
     * {@code DELETE  /directeurs/:id} : delete the "id" directeur.
     *
     * @param id the id of the directeur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/directeurs/{id}")
    public ResponseEntity<Void> deleteDirecteur(@PathVariable Long id) {
        log.debug("REST request to delete Directeur : {}", id);
        directeurRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
