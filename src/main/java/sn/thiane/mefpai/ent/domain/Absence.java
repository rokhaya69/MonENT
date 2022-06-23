package sn.thiane.mefpai.ent.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Absence.
 */
@Entity
@Table(name = "absence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Absence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "motif", nullable = false)
    private String motif;

    @NotNull
    @Column(name = "justifie", nullable = false)
    private Boolean justifie;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contenu", "cours", "salle", "groupe", "absences" }, allowSetters = true)
    private Seance seance;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "absences", "ressources", "groupe", "parent" }, allowSetters = true)
    private Apprenant apprenant;

    @ManyToOne
    @JsonIgnoreProperties(value = { "notes", "absences", "matiere", "groupe", "professeur", "salle", "surveillants" }, allowSetters = true)
    private Evaluation evaluation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Absence id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotif() {
        return this.motif;
    }

    public Absence motif(String motif) {
        this.setMotif(motif);
        return this;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Boolean getJustifie() {
        return this.justifie;
    }

    public Absence justifie(Boolean justifie) {
        this.setJustifie(justifie);
        return this;
    }

    public void setJustifie(Boolean justifie) {
        this.justifie = justifie;
    }

    public Seance getSeance() {
        return this.seance;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }

    public Absence seance(Seance seance) {
        this.setSeance(seance);
        return this;
    }

    public Apprenant getApprenant() {
        return this.apprenant;
    }

    public void setApprenant(Apprenant apprenant) {
        this.apprenant = apprenant;
    }

    public Absence apprenant(Apprenant apprenant) {
        this.setApprenant(apprenant);
        return this;
    }

    public Evaluation getEvaluation() {
        return this.evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public Absence evaluation(Evaluation evaluation) {
        this.setEvaluation(evaluation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Absence)) {
            return false;
        }
        return id != null && id.equals(((Absence) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Absence{" +
            "id=" + getId() +
            ", motif='" + getMotif() + "'" +
            ", justifie='" + getJustifie() + "'" +
            "}";
    }
}
