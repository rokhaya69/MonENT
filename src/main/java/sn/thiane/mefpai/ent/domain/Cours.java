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
 * A Cours.
 */
@Entity
@Table(name = "cours")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cours implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libelle_cours", nullable = false, unique = true)
    private String libelleCours;

    @OneToMany(mappedBy = "cours")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contenu", "cours", "salle", "groupe", "absences" }, allowSetters = true)
    private Set<Seance> seances = new HashSet<>();

    @OneToMany(mappedBy = "cours")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "apprenant", "groupe", "cours", "persoAdmin", "proviseur", "directeur", "inspecteur", "etablissements" },
        allowSetters = true
    )
    private Set<Ressource> ressources = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "cours", "syllabi", "evaluations", "programme" }, allowSetters = true)
    private Matiere matiere;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "cours", "groupes", "filiere", "serie", "proviseur", "directeur", "surveillant", "professeur" },
        allowSetters = true
    )
    private Classe classe;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "cours", "classes", "evaluations", "etablissement" }, allowSetters = true)
    private Professeur professeur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cours", "programme", "matiere" }, allowSetters = true)
    private Syllabus syllabus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cours id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleCours() {
        return this.libelleCours;
    }

    public Cours libelleCours(String libelleCours) {
        this.setLibelleCours(libelleCours);
        return this;
    }

    public void setLibelleCours(String libelleCours) {
        this.libelleCours = libelleCours;
    }

    public Set<Seance> getSeances() {
        return this.seances;
    }

    public void setSeances(Set<Seance> seances) {
        if (this.seances != null) {
            this.seances.forEach(i -> i.setCours(null));
        }
        if (seances != null) {
            seances.forEach(i -> i.setCours(this));
        }
        this.seances = seances;
    }

    public Cours seances(Set<Seance> seances) {
        this.setSeances(seances);
        return this;
    }

    public Cours addSeance(Seance seance) {
        this.seances.add(seance);
        seance.setCours(this);
        return this;
    }

    public Cours removeSeance(Seance seance) {
        this.seances.remove(seance);
        seance.setCours(null);
        return this;
    }

    public Set<Ressource> getRessources() {
        return this.ressources;
    }

    public void setRessources(Set<Ressource> ressources) {
        if (this.ressources != null) {
            this.ressources.forEach(i -> i.setCours(null));
        }
        if (ressources != null) {
            ressources.forEach(i -> i.setCours(this));
        }
        this.ressources = ressources;
    }

    public Cours ressources(Set<Ressource> ressources) {
        this.setRessources(ressources);
        return this;
    }

    public Cours addRessource(Ressource ressource) {
        this.ressources.add(ressource);
        ressource.setCours(this);
        return this;
    }

    public Cours removeRessource(Ressource ressource) {
        this.ressources.remove(ressource);
        ressource.setCours(null);
        return this;
    }

    public Matiere getMatiere() {
        return this.matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public Cours matiere(Matiere matiere) {
        this.setMatiere(matiere);
        return this;
    }

    public Classe getClasse() {
        return this.classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Cours classe(Classe classe) {
        this.setClasse(classe);
        return this;
    }

    public Professeur getProfesseur() {
        return this.professeur;
    }

    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }

    public Cours professeur(Professeur professeur) {
        this.setProfesseur(professeur);
        return this;
    }

    public Syllabus getSyllabus() {
        return this.syllabus;
    }

    public void setSyllabus(Syllabus syllabus) {
        this.syllabus = syllabus;
    }

    public Cours syllabus(Syllabus syllabus) {
        this.setSyllabus(syllabus);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cours)) {
            return false;
        }
        return id != null && id.equals(((Cours) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cours{" +
            "id=" + getId() +
            ", libelleCours='" + getLibelleCours() + "'" +
            "}";
    }
}
