package sn.thiane.mefpai.ent.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Programme.
 */
@Entity
@Table(name = "programme")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Programme implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_program", nullable = false)
    private String nomProgram;

    @Lob
    @Column(name = "contenu_program")
    private byte[] contenuProgram;

    @Column(name = "contenu_program_content_type")
    private String contenuProgramContentType;

    @NotNull
    @Column(name = "annee", nullable = false)
    private LocalDate annee;

    @JsonIgnoreProperties(value = { "classes", "etablissement" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Filiere filiere;

    @JsonIgnoreProperties(value = { "classes", "etablissement" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Serie serie;

    @OneToMany(mappedBy = "programme")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cours", "programme", "matiere" }, allowSetters = true)
    private Set<Syllabus> syllabi = new HashSet<>();

    @OneToMany(mappedBy = "programme")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cours", "syllabi", "evaluations", "programme" }, allowSetters = true)
    private Set<Matiere> matieres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Programme id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomProgram() {
        return this.nomProgram;
    }

    public Programme nomProgram(String nomProgram) {
        this.setNomProgram(nomProgram);
        return this;
    }

    public void setNomProgram(String nomProgram) {
        this.nomProgram = nomProgram;
    }

    public byte[] getContenuProgram() {
        return this.contenuProgram;
    }

    public Programme contenuProgram(byte[] contenuProgram) {
        this.setContenuProgram(contenuProgram);
        return this;
    }

    public void setContenuProgram(byte[] contenuProgram) {
        this.contenuProgram = contenuProgram;
    }

    public String getContenuProgramContentType() {
        return this.contenuProgramContentType;
    }

    public Programme contenuProgramContentType(String contenuProgramContentType) {
        this.contenuProgramContentType = contenuProgramContentType;
        return this;
    }

    public void setContenuProgramContentType(String contenuProgramContentType) {
        this.contenuProgramContentType = contenuProgramContentType;
    }

    public LocalDate getAnnee() {
        return this.annee;
    }

    public Programme annee(LocalDate annee) {
        this.setAnnee(annee);
        return this;
    }

    public void setAnnee(LocalDate annee) {
        this.annee = annee;
    }

    public Filiere getFiliere() {
        return this.filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public Programme filiere(Filiere filiere) {
        this.setFiliere(filiere);
        return this;
    }

    public Serie getSerie() {
        return this.serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Programme serie(Serie serie) {
        this.setSerie(serie);
        return this;
    }

    public Set<Syllabus> getSyllabi() {
        return this.syllabi;
    }

    public void setSyllabi(Set<Syllabus> syllabi) {
        if (this.syllabi != null) {
            this.syllabi.forEach(i -> i.setProgramme(null));
        }
        if (syllabi != null) {
            syllabi.forEach(i -> i.setProgramme(this));
        }
        this.syllabi = syllabi;
    }

    public Programme syllabi(Set<Syllabus> syllabi) {
        this.setSyllabi(syllabi);
        return this;
    }

    public Programme addSyllabus(Syllabus syllabus) {
        this.syllabi.add(syllabus);
        syllabus.setProgramme(this);
        return this;
    }

    public Programme removeSyllabus(Syllabus syllabus) {
        this.syllabi.remove(syllabus);
        syllabus.setProgramme(null);
        return this;
    }

    public Set<Matiere> getMatieres() {
        return this.matieres;
    }

    public void setMatieres(Set<Matiere> matieres) {
        if (this.matieres != null) {
            this.matieres.forEach(i -> i.setProgramme(null));
        }
        if (matieres != null) {
            matieres.forEach(i -> i.setProgramme(this));
        }
        this.matieres = matieres;
    }

    public Programme matieres(Set<Matiere> matieres) {
        this.setMatieres(matieres);
        return this;
    }

    public Programme addMatiere(Matiere matiere) {
        this.matieres.add(matiere);
        matiere.setProgramme(this);
        return this;
    }

    public Programme removeMatiere(Matiere matiere) {
        this.matieres.remove(matiere);
        matiere.setProgramme(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Programme)) {
            return false;
        }
        return id != null && id.equals(((Programme) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Programme{" +
            "id=" + getId() +
            ", nomProgram='" + getNomProgram() + "'" +
            ", contenuProgram='" + getContenuProgram() + "'" +
            ", contenuProgramContentType='" + getContenuProgramContentType() + "'" +
            ", annee='" + getAnnee() + "'" +
            "}";
    }
}
