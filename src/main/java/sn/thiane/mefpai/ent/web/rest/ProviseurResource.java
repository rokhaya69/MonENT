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
import sn.thiane.mefpai.ent.domain.Proviseur;
import sn.thiane.mefpai.ent.repository.ProviseurRepository;
import sn.thiane.mefpai.ent.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.thiane.mefpai.ent.domain.Proviseur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProviseurResource {

    private final Logger log = LoggerFactory.getLogger(ProviseurResource.class);

    private static final String ENTITY_NAME = "proviseur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProviseurRepository proviseurRepository;

    public ProviseurResource(ProviseurRepository proviseurRepository) {
        this.proviseurRepository = proviseurRepository;
    }

    /**
     * {@code POST  /proviseurs} : Create a new proviseur.
     *
     * @param proviseur the proviseur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proviseur, or with status {@code 400 (Bad Request)} if the proviseur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/proviseurs")
    public ResponseEntity<Proviseur> createProviseur(@Valid @RequestBody Proviseur proviseur) throws URISyntaxException {
        log.debug("REST request to save Proviseur : {}", proviseur);
        if (proviseur.getId() != null) {
            throw new BadRequestAlertException("A new proviseur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Proviseur result = proviseurRepository.save(proviseur);
        return ResponseEntity
            .created(new URI("/api/proviseurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /proviseurs/:id} : Updates an existing proviseur.
     *
     * @param id the id of the proviseur to save.
     * @param proviseur the proviseur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proviseur,
     * or with status {@code 400 (Bad Request)} if the proviseur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proviseur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/proviseurs/{id}")
    public ResponseEntity<Proviseur> updateProviseur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Proviseur proviseur
    ) throws URISyntaxException {
        log.debug("REST request to update Proviseur : {}, {}", id, proviseur);
        if (proviseur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proviseur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proviseurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Proviseur result = proviseurRepository.save(proviseur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proviseur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /proviseurs/:id} : Partial updates given fields of an existing proviseur, field will ignore if it is null
     *
     * @param id the id of the proviseur to save.
     * @param proviseur the proviseur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proviseur,
     * or with status {@code 400 (Bad Request)} if the proviseur is not valid,
     * or with status {@code 404 (Not Found)} if the proviseur is not found,
     * or with status {@code 500 (Internal Server Error)} if the proviseur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/proviseurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Proviseur> partialUpdateProviseur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Proviseur proviseur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Proviseur partially : {}, {}", id, proviseur);
        if (proviseur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proviseur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proviseurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Proviseur> result = proviseurRepository
            .findById(proviseur.getId())
            .map(existingProviseur -> {
                if (proviseur.getNom() != null) {
                    existingProviseur.setNom(proviseur.getNom());
                }
                if (proviseur.getPrenom() != null) {
                    existingProviseur.setPrenom(proviseur.getPrenom());
                }
                if (proviseur.getEmail() != null) {
                    existingProviseur.setEmail(proviseur.getEmail());
                }
                if (proviseur.getAdresse() != null) {
                    existingProviseur.setAdresse(proviseur.getAdresse());
                }
                if (proviseur.getTelephone() != null) {
                    existingProviseur.setTelephone(proviseur.getTelephone());
                }
                if (proviseur.getSexe() != null) {
                    existingProviseur.setSexe(proviseur.getSexe());
                }
                if (proviseur.getPhoto() != null) {
                    existingProviseur.setPhoto(proviseur.getPhoto());
                }
                if (proviseur.getPhotoContentType() != null) {
                    existingProviseur.setPhotoContentType(proviseur.getPhotoContentType());
                }

                return existingProviseur;
            })
            .map(proviseurRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proviseur.getId().toString())
        );
    }

    /**
     * {@code GET  /proviseurs} : get all the proviseurs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of proviseurs in body.
     */
    @GetMapping("/proviseurs")
    public ResponseEntity<List<Proviseur>> getAllProviseurs(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Proviseurs");
        Page<Proviseur> page;
        if (eagerload) {
            page = proviseurRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = proviseurRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /proviseurs/:id} : get the "id" proviseur.
     *
     * @param id the id of the proviseur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proviseur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/proviseurs/{id}")
    public ResponseEntity<Proviseur> getProviseur(@PathVariable Long id) {
        log.debug("REST request to get Proviseur : {}", id);
        Optional<Proviseur> proviseur = proviseurRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(proviseur);
    }

    /**
     * {@code DELETE  /proviseurs/:id} : delete the "id" proviseur.
     *
     * @param id the id of the proviseur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/proviseurs/{id}")
    public ResponseEntity<Void> deleteProviseur(@PathVariable Long id) {
        log.debug("REST request to delete Proviseur : {}", id);
        proviseurRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
