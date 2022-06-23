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
 * A Syllabus.
 */
@Entity
@Table(name = "syllabus")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Syllabus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_syllabus", nullable = false)
    private String nomSyllabus;

    @Lob
    @Column(name = "syllabus")
    private byte[] syllabus;

    @Column(name = "syllabus_content_type")
    private String syllabusContentType;

    @OneToMany(mappedBy = "syllabus")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "seances", "ressources", "matiere", "classe", "professeur", "syllabus" }, allowSetters = true)
    private Set<Cours> cours = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "filiere", "serie", "syllabi", "matieres" }, allowSetters = true)
    private Programme programme;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cours", "syllabi", "evaluations", "programme" }, allowSetters = true)
    private Matiere matiere;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Syllabus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomSyllabus() {
        return this.nomSyllabus;
    }

    public Syllabus nomSyllabus(String nomSyllabus) {
        this.setNomSyllabus(nomSyllabus);
        return this;
    }

    public void setNomSyllabus(String nomSyllabus) {
        this.nomSyllabus = nomSyllabus;
    }

    public byte[] getSyllabus() {
        return this.syllabus;
    }

    public Syllabus syllabus(byte[] syllabus) {
        this.setSyllabus(syllabus);
        return this;
    }

    public void setSyllabus(byte[] syllabus) {
        this.syllabus = syllabus;
    }

    public String getSyllabusContentType() {
        return this.syllabusContentType;
    }

    public Syllabus syllabusContentType(String syllabusContentType) {
        this.syllabusContentType = syllabusContentType;
        return this;
    }

    public void setSyllabusContentType(String syllabusContentType) {
        this.syllabusContentType = syllabusContentType;
    }

    public Set<Cours> getCours() {
        return this.cours;
    }

    public void setCours(Set<Cours> cours) {
        if (this.cours != null) {
            this.cours.forEach(i -> i.setSyllabus(null));
        }
        if (cours != null) {
            cours.forEach(i -> i.setSyllabus(this));
        }
        this.cours = cours;
    }

    public Syllabus cours(Set<Cours> cours) {
        this.setCours(cours);
        return this;
    }

    public Syllabus addCours(Cours cours) {
        this.cours.add(cours);
        cours.setSyllabus(this);
        return this;
    }

    public Syllabus removeCours(Cours cours) {
        this.cours.remove(cours);
        cours.setSyllabus(null);
        return this;
    }

    public Programme getProgramme() {
        return this.programme;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }

    public Syllabus programme(Programme programme) {
        this.setProgramme(programme);
        return this;
    }

    public Matiere getMatiere() {
        return this.matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public Syllabus matiere(Matiere matiere) {
        this.setMatiere(matiere);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Syllabus)) {
            return false;
        }
        return id != null && id.equals(((Syllabus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Syllabus{" +
            "id=" + getId() +
            ", nomSyllabus='" + getNomSyllabus() + "'" +
            ", syllabus='" + getSyllabus() + "'" +
            ", syllabusContentType='" + getSyllabusContentType() + "'" +
            "}";
    }
}
