package sn.thiane.mefpai.ent.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.thiane.mefpai.ent.domain.enumeration.NomFiliere;
import sn.thiane.mefpai.ent.domain.enumeration.Qualification;

/**
 * A Filiere.
 */
@Entity
@Table(name = "filiere")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Filiere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nom_filiere", nullable = false)
    private NomFiliere nomFiliere;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_qualif", nullable = false)
    private Qualification niveauQualif;

    @Column(name = "autre_filiere")
    private String autreFiliere;

    @Column(name = "autre_qualif")
    private String autreQualif;

    @OneToMany(mappedBy = "filiere")
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

    public Filiere id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NomFiliere getNomFiliere() {
        return this.nomFiliere;
    }

    public Filiere nomFiliere(NomFiliere nomFiliere) {
        this.setNomFiliere(nomFiliere);
        return this;
    }

    public void setNomFiliere(NomFiliere nomFiliere) {
        this.nomFiliere = nomFiliere;
    }

    public Qualification getNiveauQualif() {
        return this.niveauQualif;
    }

    public Filiere niveauQualif(Qualification niveauQualif) {
        this.setNiveauQualif(niveauQualif);
        return this;
    }

    public void setNiveauQualif(Qualification niveauQualif) {
        this.niveauQualif = niveauQualif;
    }

    public String getAutreFiliere() {
        return this.autreFiliere;
    }

    public Filiere autreFiliere(String autreFiliere) {
        this.setAutreFiliere(autreFiliere);
        return this;
    }

    public void setAutreFiliere(String autreFiliere) {
        this.autreFiliere = autreFiliere;
    }

    public String getAutreQualif() {
        return this.autreQualif;
    }

    public Filiere autreQualif(String autreQualif) {
        this.setAutreQualif(autreQualif);
        return this;
    }

    public void setAutreQualif(String autreQualif) {
        this.autreQualif = autreQualif;
    }

    public Set<Classe> getClasses() {
        return this.classes;
    }

    public void setClasses(Set<Classe> classes) {
        if (this.classes != null) {
            this.classes.forEach(i -> i.setFiliere(null));
        }
        if (classes != null) {
            classes.forEach(i -> i.setFiliere(this));
        }
        this.classes = classes;
    }

    public Filiere classes(Set<Classe> classes) {
        this.setClasses(classes);
        return this;
    }

    public Filiere addClasse(Classe classe) {
        this.classes.add(classe);
        classe.setFiliere(this);
        return this;
    }

    public Filiere removeClasse(Classe classe) {
        this.classes.remove(classe);
        classe.setFiliere(null);
        return this;
    }

    public Etablissement getEtablissement() {
        return this.etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public Filiere etablissement(Etablissement etablissement) {
        this.setEtablissement(etablissement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Filiere)) {
            return false;
        }
        return id != null && id.equals(((Filiere) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Filiere{" +
            "id=" + getId() +
            ", nomFiliere='" + getNomFiliere() + "'" +
            ", niveauQualif='" + getNiveauQualif() + "'" +
            ", autreFiliere='" + getAutreFiliere() + "'" +
            ", autreQualif='" + getAutreQualif() + "'" +
            "}";
    }
}
