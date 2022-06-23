package sn.thiane.mefpai.ent.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.thiane.mefpai.ent.domain.enumeration.TypeEvalu;

/**
 * A Evaluation.
 */
@Entity
@Table(name = "evaluation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Evaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_evalu", nullable = false)
    private String nomEvalu;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_evalu", nullable = false)
    private TypeEvalu typeEvalu;

    @NotNull
    @Column(name = "date_eva", nullable = false)
    private LocalDate dateEva;

    @NotNull
    @Column(name = "heure_deb_eva", nullable = false)
    private Instant heureDebEva;

    @NotNull
    @Column(name = "heure_fin_eva", nullable = false)
    private Instant heureFinEva;

    @OneToMany(mappedBy = "evaluation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "apprenant", "evaluation" }, allowSetters = true)
    private Set<Note> notes = new HashSet<>();

    @OneToMany(mappedBy = "evaluation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "seance", "apprenant", "evaluation" }, allowSetters = true)
    private Set<Absence> absences = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "cours", "syllabi", "evaluations", "programme" }, allowSetters = true)
    private Matiere matiere;

    @ManyToOne
    @JsonIgnoreProperties(value = { "seances", "apprenants", "ressources", "evaluations", "classe" }, allowSetters = true)
    private Groupe groupe;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "cours", "classes", "evaluations", "etablissement" }, allowSetters = true)
    private Professeur professeur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "seances", "evaluations" }, allowSetters = true)
    private Salle salle;

    @ManyToMany(mappedBy = "evaluations")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "ressources", "classes", "evaluations" }, allowSetters = true)
    private Set<Surveillant> surveillants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Evaluation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEvalu() {
        return this.nomEvalu;
    }

    public Evaluation nomEvalu(String nomEvalu) {
        this.setNomEvalu(nomEvalu);
        return this;
    }

    public void setNomEvalu(String nomEvalu) {
        this.nomEvalu = nomEvalu;
    }

    public TypeEvalu getTypeEvalu() {
        return this.typeEvalu;
    }

    public Evaluation typeEvalu(TypeEvalu typeEvalu) {
        this.setTypeEvalu(typeEvalu);
        return this;
    }

    public void setTypeEvalu(TypeEvalu typeEvalu) {
        this.typeEvalu = typeEvalu;
    }

    public LocalDate getDateEva() {
        return this.dateEva;
    }

    public Evaluation dateEva(LocalDate dateEva) {
        this.setDateEva(dateEva);
        return this;
    }

    public void setDateEva(LocalDate dateEva) {
        this.dateEva = dateEva;
    }

    public Instant getHeureDebEva() {
        return this.heureDebEva;
    }

    public Evaluation heureDebEva(Instant heureDebEva) {
        this.setHeureDebEva(heureDebEva);
        return this;
    }

    public void setHeureDebEva(Instant heureDebEva) {
        this.heureDebEva = heureDebEva;
    }

    public Instant getHeureFinEva() {
        return this.heureFinEva;
    }

    public Evaluation heureFinEva(Instant heureFinEva) {
        this.setHeureFinEva(heureFinEva);
        return this;
    }

    public void setHeureFinEva(Instant heureFinEva) {
        this.heureFinEva = heureFinEva;
    }

    public Set<Note> getNotes() {
        return this.notes;
    }

    public void setNotes(Set<Note> notes) {
        if (this.notes != null) {
            this.notes.forEach(i -> i.setEvaluation(null));
        }
        if (notes != null) {
            notes.forEach(i -> i.setEvaluation(this));
        }
        this.notes = notes;
    }

    public Evaluation notes(Set<Note> notes) {
        this.setNotes(notes);
        return this;
    }

    public Evaluation addNote(Note note) {
        this.notes.add(note);
        note.setEvaluation(this);
        return this;
    }

    public Evaluation removeNote(Note note) {
        this.notes.remove(note);
        note.setEvaluation(null);
        return this;
    }

    public Set<Absence> getAbsences() {
        return this.absences;
    }

    public void setAbsences(Set<Absence> absences) {
        if (this.absences != null) {
            this.absences.forEach(i -> i.setEvaluation(null));
        }
        if (absences != null) {
            absences.forEach(i -> i.setEvaluation(this));
        }
        this.absences = absences;
    }

    public Evaluation absences(Set<Absence> absences) {
        this.setAbsences(absences);
        return this;
    }

    public Evaluation addAbsence(Absence absence) {
        this.absences.add(absence);
        absence.setEvaluation(this);
        return this;
    }

    public Evaluation removeAbsence(Absence absence) {
        this.absences.remove(absence);
        absence.setEvaluation(null);
        return this;
    }

    public Matiere getMatiere() {
        return this.matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public Evaluation matiere(Matiere matiere) {
        this.setMatiere(matiere);
        return this;
    }

    public Groupe getGroupe() {
        return this.groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public Evaluation groupe(Groupe groupe) {
        this.setGroupe(groupe);
        return this;
    }

    public Professeur getProfesseur() {
        return this.professeur;
    }

    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }

    public Evaluation professeur(Professeur professeur) {
        this.setProfesseur(professeur);
        return this;
    }

    public Salle getSalle() {
        return this.salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public Evaluation salle(Salle salle) {
        this.setSalle(salle);
        return this;
    }

    public Set<Surveillant> getSurveillants() {
        return this.surveillants;
    }

    public void setSurveillants(Set<Surveillant> surveillants) {
        if (this.surveillants != null) {
            this.surveillants.forEach(i -> i.removeEvaluation(this));
        }
        if (surveillants != null) {
            surveillants.forEach(i -> i.addEvaluation(this));
        }
        this.surveillants = surveillants;
    }

    public Evaluation surveillants(Set<Surveillant> surveillants) {
        this.setSurveillants(surveillants);
        return this;
    }

    public Evaluation addSurveillant(Surveillant surveillant) {
        this.surveillants.add(surveillant);
        surveillant.getEvaluations().add(this);
        return this;
    }

    public Evaluation removeSurveillant(Surveillant surveillant) {
        this.surveillants.remove(surveillant);
        surveillant.getEvaluations().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evaluation)) {
            return false;
        }
        return id != null && id.equals(((Evaluation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Evaluation{" +
            "id=" + getId() +
            ", nomEvalu='" + getNomEvalu() + "'" +
            ", typeEvalu='" + getTypeEvalu() + "'" +
            ", dateEva='" + getDateEva() + "'" +
            ", heureDebEva='" + getHeureDebEva() + "'" +
            ", heureFinEva='" + getHeureFinEva() + "'" +
            "}";
    }
}
