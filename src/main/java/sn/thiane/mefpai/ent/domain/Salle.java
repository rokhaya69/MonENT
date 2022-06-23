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
 * A Salle.
 */
@Entity
@Table(name = "salle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Salle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_salle", nullable = false)
    private String libelleSalle;

    @OneToMany(mappedBy = "salle")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contenu", "cours", "salle", "groupe", "absences" }, allowSetters = true)
    private Set<Seance> seances = new HashSet<>();

    @OneToMany(mappedBy = "salle")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "notes", "absences", "matiere", "groupe", "professeur", "salle", "surveillants" }, allowSetters = true)
    private Set<Evaluation> evaluations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Salle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleSalle() {
        return this.libelleSalle;
    }

    public Salle libelleSalle(String libelleSalle) {
        this.setLibelleSalle(libelleSalle);
        return this;
    }

    public void setLibelleSalle(String libelleSalle) {
        this.libelleSalle = libelleSalle;
    }

    public Set<Seance> getSeances() {
        return this.seances;
    }

    public void setSeances(Set<Seance> seances) {
        if (this.seances != null) {
            this.seances.forEach(i -> i.setSalle(null));
        }
        if (seances != null) {
            seances.forEach(i -> i.setSalle(this));
        }
        this.seances = seances;
    }

    public Salle seances(Set<Seance> seances) {
        this.setSeances(seances);
        return this;
    }

    public Salle addSeance(Seance seance) {
        this.seances.add(seance);
        seance.setSalle(this);
        return this;
    }

    public Salle removeSeance(Seance seance) {
        this.seances.remove(seance);
        seance.setSalle(null);
        return this;
    }

    public Set<Evaluation> getEvaluations() {
        return this.evaluations;
    }

    public void setEvaluations(Set<Evaluation> evaluations) {
        if (this.evaluations != null) {
            this.evaluations.forEach(i -> i.setSalle(null));
        }
        if (evaluations != null) {
            evaluations.forEach(i -> i.setSalle(this));
        }
        this.evaluations = evaluations;
    }

    public Salle evaluations(Set<Evaluation> evaluations) {
        this.setEvaluations(evaluations);
        return this;
    }

    public Salle addEvaluation(Evaluation evaluation) {
        this.evaluations.add(evaluation);
        evaluation.setSalle(this);
        return this;
    }

    public Salle removeEvaluation(Evaluation evaluation) {
        this.evaluations.remove(evaluation);
        evaluation.setSalle(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Salle)) {
            return false;
        }
        return id != null && id.equals(((Salle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Salle{" +
            "id=" + getId() +
            ", libelleSalle='" + getLibelleSalle() + "'" +
            "}";
    }
}
