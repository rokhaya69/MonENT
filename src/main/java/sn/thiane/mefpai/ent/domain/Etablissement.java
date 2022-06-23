package sn.thiane.mefpai.ent.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.thiane.mefpai.ent.domain.enumeration.TypeEtab;

/**
 * A Etablissement.
 */
@Entity
@Table(name = "etablissement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Etablissement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_etab", nullable = false)
    private String nomEtab;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_etab", nullable = false)
    private TypeEtab typeEtab;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @JsonIgnoreProperties(value = { "user", "ressources", "classes" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Proviseur proviseur;

    @JsonIgnoreProperties(value = { "user", "ressources", "classes" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Directeur directeur;

    @JsonIgnoreProperties(value = { "departement" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Commune commune;

    @OneToMany(mappedBy = "etablissement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "cours", "classes", "evaluations", "etablissement" }, allowSetters = true)
    private Set<Professeur> professeurs = new HashSet<>();

    @OneToMany(mappedBy = "etablissement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classes", "etablissement" }, allowSetters = true)
    private Set<Filiere> filieres = new HashSet<>();

    @OneToMany(mappedBy = "etablissement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classes", "etablissement" }, allowSetters = true)
    private Set<Serie> series = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_etablissement__ressource",
        joinColumns = @JoinColumn(name = "etablissement_id"),
        inverseJoinColumns = @JoinColumn(name = "ressource_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "apprenant", "groupe", "cours", "persoAdmin", "proviseur", "directeur", "inspecteur", "etablissements" },
        allowSetters = true
    )
    private Set<Ressource> ressources = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "commune", "inspecteur", "etablissements" }, allowSetters = true)
    private Inspection inspection;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Etablissement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEtab() {
        return this.nomEtab;
    }

    public Etablissement nomEtab(String nomEtab) {
        this.setNomEtab(nomEtab);
        return this;
    }

    public void setNomEtab(String nomEtab) {
        this.nomEtab = nomEtab;
    }

    public TypeEtab getTypeEtab() {
        return this.typeEtab;
    }

    public Etablissement typeEtab(TypeEtab typeEtab) {
        this.setTypeEtab(typeEtab);
        return this;
    }

    public void setTypeEtab(TypeEtab typeEtab) {
        this.typeEtab = typeEtab;
    }

    public String getEmail() {
        return this.email;
    }

    public Etablissement email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Etablissement telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Proviseur getProviseur() {
        return this.proviseur;
    }

    public void setProviseur(Proviseur proviseur) {
        this.proviseur = proviseur;
    }

    public Etablissement proviseur(Proviseur proviseur) {
        this.setProviseur(proviseur);
        return this;
    }

    public Directeur getDirecteur() {
        return this.directeur;
    }

    public void setDirecteur(Directeur directeur) {
        this.directeur = directeur;
    }

    public Etablissement directeur(Directeur directeur) {
        this.setDirecteur(directeur);
        return this;
    }

    public Commune getCommune() {
        return this.commune;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
    }

    public Etablissement commune(Commune commune) {
        this.setCommune(commune);
        return this;
    }

    public Set<Professeur> getProfesseurs() {
        return this.professeurs;
    }

    public void setProfesseurs(Set<Professeur> professeurs) {
        if (this.professeurs != null) {
            this.professeurs.forEach(i -> i.setEtablissement(null));
        }
        if (professeurs != null) {
            professeurs.forEach(i -> i.setEtablissement(this));
        }
        this.professeurs = professeurs;
    }

    public Etablissement professeurs(Set<Professeur> professeurs) {
        this.setProfesseurs(professeurs);
        return this;
    }

    public Etablissement addProfesseur(Professeur professeur) {
        this.professeurs.add(professeur);
        professeur.setEtablissement(this);
        return this;
    }

    public Etablissement removeProfesseur(Professeur professeur) {
        this.professeurs.remove(professeur);
        professeur.setEtablissement(null);
        return this;
    }

    public Set<Filiere> getFilieres() {
        return this.filieres;
    }

    public void setFilieres(Set<Filiere> filieres) {
        if (this.filieres != null) {
            this.filieres.forEach(i -> i.setEtablissement(null));
        }
        if (filieres != null) {
            filieres.forEach(i -> i.setEtablissement(this));
        }
        this.filieres = filieres;
    }

    public Etablissement filieres(Set<Filiere> filieres) {
        this.setFilieres(filieres);
        return this;
    }

    public Etablissement addFiliere(Filiere filiere) {
        this.filieres.add(filiere);
        filiere.setEtablissement(this);
        return this;
    }

    public Etablissement removeFiliere(Filiere filiere) {
        this.filieres.remove(filiere);
        filiere.setEtablissement(null);
        return this;
    }

    public Set<Serie> getSeries() {
        return this.series;
    }

    public void setSeries(Set<Serie> series) {
        if (this.series != null) {
            this.series.forEach(i -> i.setEtablissement(null));
        }
        if (series != null) {
            series.forEach(i -> i.setEtablissement(this));
        }
        this.series = series;
    }

    public Etablissement series(Set<Serie> series) {
        this.setSeries(series);
        return this;
    }

    public Etablissement addSerie(Serie serie) {
        this.series.add(serie);
        serie.setEtablissement(this);
        return this;
    }

    public Etablissement removeSerie(Serie serie) {
        this.series.remove(serie);
        serie.setEtablissement(null);
        return this;
    }

    public Set<Ressource> getRessources() {
        return this.ressources;
    }

    public void setRessources(Set<Ressource> ressources) {
        this.ressources = ressources;
    }

    public Etablissement ressources(Set<Ressource> ressources) {
        this.setRessources(ressources);
        return this;
    }

    public Etablissement addRessource(Ressource ressource) {
        this.ressources.add(ressource);
        ressource.getEtablissements().add(this);
        return this;
    }

    public Etablissement removeRessource(Ressource ressource) {
        this.ressources.remove(ressource);
        ressource.getEtablissements().remove(this);
        return this;
    }

    public Inspection getInspection() {
        return this.inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
    }

    public Etablissement inspection(Inspection inspection) {
        this.setInspection(inspection);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etablissement)) {
            return false;
        }
        return id != null && id.equals(((Etablissement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etablissement{" +
            "id=" + getId() +
            ", nomEtab='" + getNomEtab() + "'" +
            ", typeEtab='" + getTypeEtab() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
