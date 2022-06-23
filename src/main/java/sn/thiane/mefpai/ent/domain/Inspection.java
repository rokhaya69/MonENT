package sn.thiane.mefpai.ent.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.thiane.mefpai.ent.domain.enumeration.TypeInspec;

/**
 * A Inspection.
 */
@Entity
@Table(name = "inspection")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Inspection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_inspec", nullable = false)
    private String nomInspec;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_inspec", nullable = false)
    private TypeInspec typeInspec;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @JsonIgnoreProperties(value = { "departement" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Commune commune;

    @JsonIgnoreProperties(value = { "user", "ressources" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Inspecteur inspecteur;

    @OneToMany(mappedBy = "inspection")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "proviseur", "directeur", "commune", "professeurs", "filieres", "series", "ressources", "inspection" },
        allowSetters = true
    )
    private Set<Etablissement> etablissements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inspection id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomInspec() {
        return this.nomInspec;
    }

    public Inspection nomInspec(String nomInspec) {
        this.setNomInspec(nomInspec);
        return this;
    }

    public void setNomInspec(String nomInspec) {
        this.nomInspec = nomInspec;
    }

    public TypeInspec getTypeInspec() {
        return this.typeInspec;
    }

    public Inspection typeInspec(TypeInspec typeInspec) {
        this.setTypeInspec(typeInspec);
        return this;
    }

    public void setTypeInspec(TypeInspec typeInspec) {
        this.typeInspec = typeInspec;
    }

    public String getEmail() {
        return this.email;
    }

    public Inspection email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Inspection telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Commune getCommune() {
        return this.commune;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
    }

    public Inspection commune(Commune commune) {
        this.setCommune(commune);
        return this;
    }

    public Inspecteur getInspecteur() {
        return this.inspecteur;
    }

    public void setInspecteur(Inspecteur inspecteur) {
        this.inspecteur = inspecteur;
    }

    public Inspection inspecteur(Inspecteur inspecteur) {
        this.setInspecteur(inspecteur);
        return this;
    }

    public Set<Etablissement> getEtablissements() {
        return this.etablissements;
    }

    public void setEtablissements(Set<Etablissement> etablissements) {
        if (this.etablissements != null) {
            this.etablissements.forEach(i -> i.setInspection(null));
        }
        if (etablissements != null) {
            etablissements.forEach(i -> i.setInspection(this));
        }
        this.etablissements = etablissements;
    }

    public Inspection etablissements(Set<Etablissement> etablissements) {
        this.setEtablissements(etablissements);
        return this;
    }

    public Inspection addEtablissement(Etablissement etablissement) {
        this.etablissements.add(etablissement);
        etablissement.setInspection(this);
        return this;
    }

    public Inspection removeEtablissement(Etablissement etablissement) {
        this.etablissements.remove(etablissement);
        etablissement.setInspection(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inspection)) {
            return false;
        }
        return id != null && id.equals(((Inspection) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inspection{" +
            "id=" + getId() +
            ", nomInspec='" + getNomInspec() + "'" +
            ", typeInspec='" + getTypeInspec() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
