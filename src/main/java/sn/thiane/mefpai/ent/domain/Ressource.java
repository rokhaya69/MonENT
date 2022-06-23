package sn.thiane.mefpai.ent.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.thiane.mefpai.ent.domain.enumeration.TypeRessource;

/**
 * A Ressource.
 */
@Entity
@Table(name = "ressource")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ressource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libel_ressource", nullable = false)
    private String libelRessource;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_ressource", nullable = false)
    private TypeRessource typeRessource;

    @Lob
    @Column(name = "lien_ressource", nullable = false)
    private byte[] lienRessource;

    @NotNull
    @Column(name = "lien_ressource_content_type", nullable = false)
    private String lienRessourceContentType;

    @Column(name = "date_mise")
    private Instant dateMise;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "absences", "ressources", "groupe", "parent" }, allowSetters = true)
    private Apprenant apprenant;

    @ManyToOne
    @JsonIgnoreProperties(value = { "seances", "apprenants", "ressources", "evaluations", "classe" }, allowSetters = true)
    private Groupe groupe;

    @ManyToOne
    @JsonIgnoreProperties(value = { "seances", "ressources", "matiere", "classe", "professeur", "syllabus" }, allowSetters = true)
    private Cours cours;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "ressources", "classes", "evaluations" }, allowSetters = true)
    private Surveillant persoAdmin;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "ressources", "classes" }, allowSetters = true)
    private Proviseur proviseur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "ressources", "classes" }, allowSetters = true)
    private Directeur directeur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "ressources" }, allowSetters = true)
    private Inspecteur inspecteur;

    @ManyToMany(mappedBy = "ressources")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "proviseur", "directeur", "commune", "professeurs", "filieres", "series", "ressources", "inspection" },
        allowSetters = true
    )
    private Set<Etablissement> etablissements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ressource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelRessource() {
        return this.libelRessource;
    }

    public Ressource libelRessource(String libelRessource) {
        this.setLibelRessource(libelRessource);
        return this;
    }

    public void setLibelRessource(String libelRessource) {
        this.libelRessource = libelRessource;
    }

    public TypeRessource getTypeRessource() {
        return this.typeRessource;
    }

    public Ressource typeRessource(TypeRessource typeRessource) {
        this.setTypeRessource(typeRessource);
        return this;
    }

    public void setTypeRessource(TypeRessource typeRessource) {
        this.typeRessource = typeRessource;
    }

    public byte[] getLienRessource() {
        return this.lienRessource;
    }

    public Ressource lienRessource(byte[] lienRessource) {
        this.setLienRessource(lienRessource);
        return this;
    }

    public void setLienRessource(byte[] lienRessource) {
        this.lienRessource = lienRessource;
    }

    public String getLienRessourceContentType() {
        return this.lienRessourceContentType;
    }

    public Ressource lienRessourceContentType(String lienRessourceContentType) {
        this.lienRessourceContentType = lienRessourceContentType;
        return this;
    }

    public void setLienRessourceContentType(String lienRessourceContentType) {
        this.lienRessourceContentType = lienRessourceContentType;
    }

    public Instant getDateMise() {
        return this.dateMise;
    }

    public Ressource dateMise(Instant dateMise) {
        this.setDateMise(dateMise);
        return this;
    }

    public void setDateMise(Instant dateMise) {
        this.dateMise = dateMise;
    }

    public Apprenant getApprenant() {
        return this.apprenant;
    }

    public void setApprenant(Apprenant apprenant) {
        this.apprenant = apprenant;
    }

    public Ressource apprenant(Apprenant apprenant) {
        this.setApprenant(apprenant);
        return this;
    }

    public Groupe getGroupe() {
        return this.groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public Ressource groupe(Groupe groupe) {
        this.setGroupe(groupe);
        return this;
    }

    public Cours getCours() {
        return this.cours;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    public Ressource cours(Cours cours) {
        this.setCours(cours);
        return this;
    }

    public Surveillant getPersoAdmin() {
        return this.persoAdmin;
    }

    public void setPersoAdmin(Surveillant surveillant) {
        this.persoAdmin = surveillant;
    }

    public Ressource persoAdmin(Surveillant surveillant) {
        this.setPersoAdmin(surveillant);
        return this;
    }

    public Proviseur getProviseur() {
        return this.proviseur;
    }

    public void setProviseur(Proviseur proviseur) {
        this.proviseur = proviseur;
    }

    public Ressource proviseur(Proviseur proviseur) {
        this.setProviseur(proviseur);
        return this;
    }

    public Directeur getDirecteur() {
        return this.directeur;
    }

    public void setDirecteur(Directeur directeur) {
        this.directeur = directeur;
    }

    public Ressource directeur(Directeur directeur) {
        this.setDirecteur(directeur);
        return this;
    }

    public Inspecteur getInspecteur() {
        return this.inspecteur;
    }

    public void setInspecteur(Inspecteur inspecteur) {
        this.inspecteur = inspecteur;
    }

    public Ressource inspecteur(Inspecteur inspecteur) {
        this.setInspecteur(inspecteur);
        return this;
    }

    public Set<Etablissement> getEtablissements() {
        return this.etablissements;
    }

    public void setEtablissements(Set<Etablissement> etablissements) {
        if (this.etablissements != null) {
            this.etablissements.forEach(i -> i.removeRessource(this));
        }
        if (etablissements != null) {
            etablissements.forEach(i -> i.addRessource(this));
        }
        this.etablissements = etablissements;
    }

    public Ressource etablissements(Set<Etablissement> etablissements) {
        this.setEtablissements(etablissements);
        return this;
    }

    public Ressource addEtablissement(Etablissement etablissement) {
        this.etablissements.add(etablissement);
        etablissement.getRessources().add(this);
        return this;
    }

    public Ressource removeEtablissement(Etablissement etablissement) {
        this.etablissements.remove(etablissement);
        etablissement.getRessources().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ressource)) {
            return false;
        }
        return id != null && id.equals(((Ressource) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ressource{" +
            "id=" + getId() +
            ", libelRessource='" + getLibelRessource() + "'" +
            ", typeRessource='" + getTypeRessource() + "'" +
            ", lienRessource='" + getLienRessource() + "'" +
            ", lienRessourceContentType='" + getLienRessourceContentType() + "'" +
            ", dateMise='" + getDateMise() + "'" +
            "}";
    }
}
