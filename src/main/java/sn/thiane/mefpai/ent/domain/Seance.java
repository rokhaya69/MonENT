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
import sn.thiane.mefpai.ent.domain.enumeration.Jour;

/**
 * A Seance.
 */
@Entity
@Table(name = "seance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Seance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jour_seance", nullable = false)
    private Jour jourSeance;

    @NotNull
    @Column(name = "date_seance", nullable = false)
    private LocalDate dateSeance;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private Instant dateDebut;

    @NotNull
    @Column(name = "date_fin", nullable = false)
    private Instant dateFin;

    @OneToOne
    @JoinColumn(unique = true)
    private Contenu contenu;

    @ManyToOne
    @JsonIgnoreProperties(value = { "seances", "ressources", "matiere", "classe", "professeur", "syllabus" }, allowSetters = true)
    private Cours cours;

    @ManyToOne
    @JsonIgnoreProperties(value = { "seances", "evaluations" }, allowSetters = true)
    private Salle salle;

    @ManyToOne
    @JsonIgnoreProperties(value = { "seances", "apprenants", "ressources", "evaluations", "classe" }, allowSetters = true)
    private Groupe groupe;

    @OneToMany(mappedBy = "seance")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "seance", "apprenant", "evaluation" }, allowSetters = true)
    private Set<Absence> absences = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Seance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jour getJourSeance() {
        return this.jourSeance;
    }

    public Seance jourSeance(Jour jourSeance) {
        this.setJourSeance(jourSeance);
        return this;
    }

    public void setJourSeance(Jour jourSeance) {
        this.jourSeance = jourSeance;
    }

    public LocalDate getDateSeance() {
        return this.dateSeance;
    }

    public Seance dateSeance(LocalDate dateSeance) {
        this.setDateSeance(dateSeance);
        return this;
    }

    public void setDateSeance(LocalDate dateSeance) {
        this.dateSeance = dateSeance;
    }

    public Instant getDateDebut() {
        return this.dateDebut;
    }

    public Seance dateDebut(Instant dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return this.dateFin;
    }

    public Seance dateFin(Instant dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public Contenu getContenu() {
        return this.contenu;
    }

    public void setContenu(Contenu contenu) {
        this.contenu = contenu;
    }

    public Seance contenu(Contenu contenu) {
        this.setContenu(contenu);
        return this;
    }

    public Cours getCours() {
        return this.cours;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    public Seance cours(Cours cours) {
        this.setCours(cours);
        return this;
    }

    public Salle getSalle() {
        return this.salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public Seance salle(Salle salle) {
        this.setSalle(salle);
        return this;
    }

    public Groupe getGroupe() {
        return this.groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public Seance groupe(Groupe groupe) {
        this.setGroupe(groupe);
        return this;
    }

    public Set<Absence> getAbsences() {
        return this.absences;
    }

    public void setAbsences(Set<Absence> absences) {
        if (this.absences != null) {
            this.absences.forEach(i -> i.setSeance(null));
        }
        if (absences != null) {
            absences.forEach(i -> i.setSeance(this));
        }
        this.absences = absences;
    }

    public Seance absences(Set<Absence> absences) {
        this.setAbsences(absences);
        return this;
    }

    public Seance addAbsence(Absence absence) {
        this.absences.add(absence);
        absence.setSeance(this);
        return this;
    }

    public Seance removeAbsence(Absence absence) {
        this.absences.remove(absence);
        absence.setSeance(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seance)) {
            return false;
        }
        return id != null && id.equals(((Seance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Seance{" +
            "id=" + getId() +
            ", jourSeance='" + getJourSeance() + "'" +
            ", dateSeance='" + getDateSeance() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
