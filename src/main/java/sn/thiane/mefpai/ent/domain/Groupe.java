package sn.thiane.mefpai.ent.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Groupe.
 */
@Entity
@Table(name = "groupe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Groupe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_groupe", nullable = false, unique = true)
    private String nomGroupe;

    @OneToMany(mappedBy = "groupe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contenu", "cours", "salle", "groupe", "absences" }, allowSetters = true)
    private Set<Seance> seances = new HashSet<>();

    @OneToMany(mappedBy = "groupe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "absences", "ressources", "groupe", "parent" }, allowSetters = true)
    private Set<Apprenant> apprenants = new HashSet<>();

    @OneToMany(mappedBy = "groupe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "apprenant", "groupe", "cours", "persoAdmin", "proviseur", "directeur", "inspecteur", "etablissements" },
        allowSetters = true
    )
    private Set<Ressource> ressources = new HashSet<>();

    @OneToMany(mappedBy = "groupe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "notes", "absences", "matiere", "groupe", "professeur", "salle", "surveillants" }, allowSetters = true)
    private Set<Evaluation> evaluations = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "cours", "groupes", "filiere", "serie", "proviseur", "directeur", "surveillant", "professeur" },
        allowSetters = true
    )
    private Classe classe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Groupe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomGroupe() {
        return this.nomGroupe;
    }

    public Groupe nomGroupe(String nomGroupe) {
        this.setNomGroupe(nomGroupe);
        return this;
    }

    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    public Set<Seance> getSeances() {
        return this.seances;
    }

    public void setSeances(Set<Seance> seances) {
        if (this.seances != null) {
            this.seances.forEach(i -> i.setGroupe(null));
        }
        if (seances != null) {
            seances.forEach(i -> i.setGroupe(this));
        }
        this.seances = seances;
    }

    public Groupe seances(Set<Seance> seances) {
        this.setSeances(seances);
        return this;
    }

    public Groupe addSeance(Seance seance) {
        this.seances.add(seance);
        seance.setGroupe(this);
        return this;
    }

    public Groupe removeSeance(Seance seance) {
        this.seances.remove(seance);
        seance.setGroupe(null);
        return this;
    }

    public Set<Apprenant> getApprenants() {
        return this.apprenants;
    }

    public void setApprenants(Set<Apprenant> apprenants) {
        if (this.apprenants != null) {
            this.apprenants.forEach(i -> i.setGroupe(null));
        }
        if (apprenants != null) {
            apprenants.forEach(i -> i.setGroupe(this));
        }
        this.apprenants = apprenants;
    }

    public Groupe apprenants(Set<Apprenant> apprenants) {
        this.setApprenants(apprenants);
        return this;
    }

    public Groupe addApprenant(Apprenant apprenant) {
        this.apprenants.add(apprenant);
        apprenant.setGroupe(this);
        return this;
    }

    public Groupe removeApprenant(Apprenant apprenant) {
        this.apprenants.remove(apprenant);
        apprenant.setGroupe(null);
        return this;
    }

    public Set<Ressource> getRessources() {
        return this.ressources;
    }

    public void setRessources(Set<Ressource> ressources) {
        if (this.ressources != null) {
            this.ressources.forEach(i -> i.setGroupe(null));
        }
        if (ressources != null) {
            ressources.forEach(i -> i.setGroupe(this));
        }
        this.ressources = ressources;
    }

    public Groupe ressources(Set<Ressource> ressources) {
        this.setRessources(ressources);
        return this;
    }

    public Groupe addRessource(Ressource ressource) {
        this.ressources.add(ressource);
        ressource.setGroupe(this);
        return this;
    }

    public Groupe removeRessource(Ressource ressource) {
        this.ressources.remove(ressource);
        ressource.setGroupe(null);
        return this;
    }

    public Set<Evaluation> getEvaluations() {
        return this.evaluations;
    }

    public void setEvaluations(Set<Evaluation> evaluations) {
        if (this.evaluations != null) {
            this.evaluations.forEach(i -> i.setGroupe(null));
        }
        if (evaluations != null) {
            evaluations.forEach(i -> i.setGroupe(this));
        }
        this.evaluations = evaluations;
    }

    public Groupe evaluations(Set<Evaluation> evaluations) {
        this.setEvaluations(evaluations);
        return this;
    }

    public Groupe addEvaluation(Evaluation evaluation) {
        this.evaluations.add(evaluation);
        evaluation.setGroupe(this);
        return this;
    }

    public Groupe removeEvaluation(Evaluation evaluation) {
        this.evaluations.remove(evaluation);
        evaluation.setGroupe(null);
        return this;
    }

    public Classe getClasse() {
        return this.classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Groupe classe(Classe classe) {
        this.setClasse(classe);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Groupe)) {
            return false;
        }
        return id != null && id.equals(((Groupe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Groupe{" +
            "id=" + getId() +
            ", nomGroupe='" + getNomGroupe() + "'" +
            "}";
    }
}
