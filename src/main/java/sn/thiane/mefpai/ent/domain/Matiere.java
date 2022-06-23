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
 * A Matiere.
 */
@Entity
@Table(name = "matiere")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Matiere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "intitule_matiere", nullable = false)
    private String intituleMatiere;

    @OneToMany(mappedBy = "matiere")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "seances", "ressources", "matiere", "classe", "professeur", "syllabus" }, allowSetters = true)
    private Set<Cours> cours = new HashSet<>();

    @OneToMany(mappedBy = "matiere")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cours", "programme", "matiere" }, allowSetters = true)
    private Set<Syllabus> syllabi = new HashSet<>();

    @OneToMany(mappedBy = "matiere")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "notes", "absences", "matiere", "groupe", "professeur", "salle", "surveillants" }, allowSetters = true)
    private Set<Evaluation> evaluations = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "filiere", "serie", "syllabi", "matieres" }, allowSetters = true)
    private Programme programme;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Matiere id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntituleMatiere() {
        return this.intituleMatiere;
    }

    public Matiere intituleMatiere(String intituleMatiere) {
        this.setIntituleMatiere(intituleMatiere);
        return this;
    }

    public void setIntituleMatiere(String intituleMatiere) {
        this.intituleMatiere = intituleMatiere;
    }

    public Set<Cours> getCours() {
        return this.cours;
    }

    public void setCours(Set<Cours> cours) {
        if (this.cours != null) {
            this.cours.forEach(i -> i.setMatiere(null));
        }
        if (cours != null) {
            cours.forEach(i -> i.setMatiere(this));
        }
        this.cours = cours;
    }

    public Matiere cours(Set<Cours> cours) {
        this.setCours(cours);
        return this;
    }

    public Matiere addCours(Cours cours) {
        this.cours.add(cours);
        cours.setMatiere(this);
        return this;
    }

    public Matiere removeCours(Cours cours) {
        this.cours.remove(cours);
        cours.setMatiere(null);
        return this;
    }

    public Set<Syllabus> getSyllabi() {
        return this.syllabi;
    }

    public void setSyllabi(Set<Syllabus> syllabi) {
        if (this.syllabi != null) {
            this.syllabi.forEach(i -> i.setMatiere(null));
        }
        if (syllabi != null) {
            syllabi.forEach(i -> i.setMatiere(this));
        }
        this.syllabi = syllabi;
    }

    public Matiere syllabi(Set<Syllabus> syllabi) {
        this.setSyllabi(syllabi);
        return this;
    }

    public Matiere addSyllabus(Syllabus syllabus) {
        this.syllabi.add(syllabus);
        syllabus.setMatiere(this);
        return this;
    }

    public Matiere removeSyllabus(Syllabus syllabus) {
        this.syllabi.remove(syllabus);
        syllabus.setMatiere(null);
        return this;
    }

    public Set<Evaluation> getEvaluations() {
        return this.evaluations;
    }

    public void setEvaluations(Set<Evaluation> evaluations) {
        if (this.evaluations != null) {
            this.evaluations.forEach(i -> i.setMatiere(null));
        }
        if (evaluations != null) {
            evaluations.forEach(i -> i.setMatiere(this));
        }
        this.evaluations = evaluations;
    }

    public Matiere evaluations(Set<Evaluation> evaluations) {
        this.setEvaluations(evaluations);
        return this;
    }

    public Matiere addEvaluation(Evaluation evaluation) {
        this.evaluations.add(evaluation);
        evaluation.setMatiere(this);
        return this;
    }

    public Matiere removeEvaluation(Evaluation evaluation) {
        this.evaluations.remove(evaluation);
        evaluation.setMatiere(null);
        return this;
    }

    public Programme getProgramme() {
        return this.programme;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }

    public Matiere programme(Programme programme) {
        this.setProgramme(programme);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Matiere)) {
            return false;
        }
        return id != null && id.equals(((Matiere) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Matiere{" +
            "id=" + getId() +
            ", intituleMatiere='" + getIntituleMatiere() + "'" +
            "}";
    }
}
