package sn.thiane.mefpai.ent.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.thiane.mefpai.ent.domain.enumeration.NomSerie;

/**
 * A Serie.
 */
@Entity
@Table(name = "serie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Serie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nom_serie", nullable = false)
    private NomSerie nomSerie;

    @Column(name = "autre_serie")
    private String autreSerie;

    @OneToMany(mappedBy = "serie")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "cours", "groupes", "filiere", "serie", "proviseur", "directeur", "surveillant", "professeur" },
        allowSetters = true
    )
    private Set<Classe> classes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "proviseur", "directeur", "commune", "professeurs", "filieres", "series", "ressources", "inspection" },
        allowSetters = true
    )
    private Etablissement etablissement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Serie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NomSerie getNomSerie() {
        return this.nomSerie;
    }

    public Serie nomSerie(NomSerie nomSerie) {
        this.setNomSerie(nomSerie);
        return this;
    }

    public void setNomSerie(NomSerie nomSerie) {
        this.nomSerie = nomSerie;
    }

    public String getAutreSerie() {
        return this.autreSerie;
    }

    public Serie autreSerie(String autreSerie) {
        this.setAutreSerie(autreSerie);
        return this;
    }

    public void setAutreSerie(String autreSerie) {
        this.autreSerie = autreSerie;
    }

    public Set<Classe> getClasses() {
        return this.classes;
    }

    public void setClasses(Set<Classe> classes) {
        if (this.classes != null) {
            this.classes.forEach(i -> i.setSerie(null));
        }
        if (classes != null) {
            classes.forEach(i -> i.setSerie(this));
        }
        this.classes = classes;
    }

    public Serie classes(Set<Classe> classes) {
        this.setClasses(classes);
        return this;
    }

    public Serie addClasse(Classe classe) {
        this.classes.add(classe);
        classe.setSerie(this);
        return this;
    }

    public Serie removeClasse(Classe classe) {
        this.classes.remove(classe);
        classe.setSerie(null);
        return this;
    }

    public Etablissement getEtablissement() {
        return this.etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public Serie etablissement(Etablissement etablissement) {
        this.setEtablissement(etablissement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Serie)) {
            return false;
        }
        return id != null && id.equals(((Serie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Serie{" +
            "id=" + getId() +
            ", nomSerie='" + getNomSerie() + "'" +
            ", autreSerie='" + getAutreSerie() + "'" +
            "}";
    }
}
