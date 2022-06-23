package sn.thiane.mefpai.ent.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.thiane.mefpai.ent.domain.enumeration.Sexe;

/**
 * A Surveillant.
 */
@Entity
@Table(name = "surveillant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Surveillant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sexe", nullable = false)
    private Sexe sexe;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "persoAdmin")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "apprenant", "groupe", "cours", "persoAdmin", "proviseur", "directeur", "inspecteur", "etablissements" },
        allowSetters = true
    )
    private Set<Ressource> ressources = new HashSet<>();

    @OneToMany(mappedBy = "surveillant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "cours", "groupes", "filiere", "serie", "proviseur", "directeur", "surveillant", "professeur" },
        allowSetters = true
    )
    private Set<Classe> classes = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_surveillant__evaluation",
        joinColumns = @JoinColumn(name = "surveillant_id"),
        inverseJoinColumns = @JoinColumn(name = "evaluation_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "notes", "absences", "matiere", "groupe", "professeur", "salle", "surveillants" }, allowSetters = true)
    private Set<Evaluation> evaluations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Surveillant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Surveillant nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Surveillant prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return this.email;
    }

    public Surveillant email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Surveillant adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Surveillant telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Sexe getSexe() {
        return this.sexe;
    }

    public Surveillant sexe(Sexe sexe) {
        this.setSexe(sexe);
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Surveillant photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Surveillant photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Surveillant user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Ressource> getRessources() {
        return this.ressources;
    }

    public void setRessources(Set<Ressource> ressources) {
        if (this.ressources != null) {
            this.ressources.forEach(i -> i.setPersoAdmin(null));
        }
        if (ressources != null) {
            ressources.forEach(i -> i.setPersoAdmin(this));
        }
        this.ressources = ressources;
    }

    public Surveillant ressources(Set<Ressource> ressources) {
        this.setRessources(ressources);
        return this;
    }

    public Surveillant addRessource(Ressource ressource) {
        this.ressources.add(ressource);
        ressource.setPersoAdmin(this);
        return this;
    }

    public Surveillant removeRessource(Ressource ressource) {
        this.ressources.remove(ressource);
        ressource.setPersoAdmin(null);
        return this;
    }

    public Set<Classe> getClasses() {
        return this.classes;
    }

    public void setClasses(Set<Classe> classes) {
        if (this.classes != null) {
            this.classes.forEach(i -> i.setSurveillant(null));
        }
        if (classes != null) {
            classes.forEach(i -> i.setSurveillant(this));
        }
        this.classes = classes;
    }

    public Surveillant classes(Set<Classe> classes) {
        this.setClasses(classes);
        return this;
    }

    public Surveillant addClasse(Classe classe) {
        this.classes.add(classe);
        classe.setSurveillant(this);
        return this;
    }

    public Surveillant removeClasse(Classe classe) {
        this.classes.remove(classe);
        classe.setSurveillant(null);
        return this;
    }

    public Set<Evaluation> getEvaluations() {
        return this.evaluations;
    }

    public void setEvaluations(Set<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

    public Surveillant evaluations(Set<Evaluation> evaluations) {
        this.setEvaluations(evaluations);
        return this;
    }

    public Surveillant addEvaluation(Evaluation evaluation) {
        this.evaluations.add(evaluation);
        evaluation.getSurveillants().add(this);
        return this;
    }

    public Surveillant removeEvaluation(Evaluation evaluation) {
        this.evaluations.remove(evaluation);
        evaluation.getSurveillants().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Surveillant)) {
            return false;
        }
        return id != null && id.equals(((Surveillant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Surveillant{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            "}";
    }
}
