package sn.thiane.mefpai.ent.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, sn.thiane.mefpai.ent.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, sn.thiane.mefpai.ent.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, sn.thiane.mefpai.ent.domain.User.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Authority.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.User.class.getName() + ".authorities");
            createCache(cm, sn.thiane.mefpai.ent.domain.Cours.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Cours.class.getName() + ".seances");
            createCache(cm, sn.thiane.mefpai.ent.domain.Cours.class.getName() + ".ressources");
            createCache(cm, sn.thiane.mefpai.ent.domain.Classe.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Classe.class.getName() + ".cours");
            createCache(cm, sn.thiane.mefpai.ent.domain.Classe.class.getName() + ".groupes");
            createCache(cm, sn.thiane.mefpai.ent.domain.Salle.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Salle.class.getName() + ".seances");
            createCache(cm, sn.thiane.mefpai.ent.domain.Salle.class.getName() + ".evaluations");
            createCache(cm, sn.thiane.mefpai.ent.domain.Matiere.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Matiere.class.getName() + ".cours");
            createCache(cm, sn.thiane.mefpai.ent.domain.Matiere.class.getName() + ".syllabi");
            createCache(cm, sn.thiane.mefpai.ent.domain.Matiere.class.getName() + ".evaluations");
            createCache(cm, sn.thiane.mefpai.ent.domain.Groupe.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Groupe.class.getName() + ".seances");
            createCache(cm, sn.thiane.mefpai.ent.domain.Groupe.class.getName() + ".apprenants");
            createCache(cm, sn.thiane.mefpai.ent.domain.Groupe.class.getName() + ".ressources");
            createCache(cm, sn.thiane.mefpai.ent.domain.Groupe.class.getName() + ".evaluations");
            createCache(cm, sn.thiane.mefpai.ent.domain.Seance.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Seance.class.getName() + ".absences");
            createCache(cm, sn.thiane.mefpai.ent.domain.Contenu.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Syllabus.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Syllabus.class.getName() + ".cours");
            createCache(cm, sn.thiane.mefpai.ent.domain.Absence.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Apprenant.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Apprenant.class.getName() + ".absences");
            createCache(cm, sn.thiane.mefpai.ent.domain.Apprenant.class.getName() + ".ressources");
            createCache(cm, sn.thiane.mefpai.ent.domain.Surveillant.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Surveillant.class.getName() + ".ressources");
            createCache(cm, sn.thiane.mefpai.ent.domain.Surveillant.class.getName() + ".classes");
            createCache(cm, sn.thiane.mefpai.ent.domain.Surveillant.class.getName() + ".evaluations");
            createCache(cm, sn.thiane.mefpai.ent.domain.Directeur.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Directeur.class.getName() + ".ressources");
            createCache(cm, sn.thiane.mefpai.ent.domain.Directeur.class.getName() + ".classes");
            createCache(cm, sn.thiane.mefpai.ent.domain.Proviseur.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Proviseur.class.getName() + ".ressources");
            createCache(cm, sn.thiane.mefpai.ent.domain.Proviseur.class.getName() + ".classes");
            createCache(cm, sn.thiane.mefpai.ent.domain.Inspecteur.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Inspecteur.class.getName() + ".ressources");
            createCache(cm, sn.thiane.mefpai.ent.domain.Professeur.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Professeur.class.getName() + ".cours");
            createCache(cm, sn.thiane.mefpai.ent.domain.Professeur.class.getName() + ".classes");
            createCache(cm, sn.thiane.mefpai.ent.domain.Professeur.class.getName() + ".evaluations");
            createCache(cm, sn.thiane.mefpai.ent.domain.Parent.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Parent.class.getName() + ".apprenants");
            createCache(cm, sn.thiane.mefpai.ent.domain.Mefpai.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Etablissement.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Etablissement.class.getName() + ".professeurs");
            createCache(cm, sn.thiane.mefpai.ent.domain.Etablissement.class.getName() + ".filieres");
            createCache(cm, sn.thiane.mefpai.ent.domain.Etablissement.class.getName() + ".series");
            createCache(cm, sn.thiane.mefpai.ent.domain.Etablissement.class.getName() + ".ressources");
            createCache(cm, sn.thiane.mefpai.ent.domain.Filiere.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Filiere.class.getName() + ".classes");
            createCache(cm, sn.thiane.mefpai.ent.domain.Serie.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Serie.class.getName() + ".classes");
            createCache(cm, sn.thiane.mefpai.ent.domain.Programme.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Programme.class.getName() + ".syllabi");
            createCache(cm, sn.thiane.mefpai.ent.domain.Programme.class.getName() + ".matieres");
            createCache(cm, sn.thiane.mefpai.ent.domain.Region.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Region.class.getName() + ".departements");
            createCache(cm, sn.thiane.mefpai.ent.domain.Departement.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Departement.class.getName() + ".communes");
            createCache(cm, sn.thiane.mefpai.ent.domain.Commune.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Inspection.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Inspection.class.getName() + ".etablissements");
            createCache(cm, sn.thiane.mefpai.ent.domain.Ressource.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Ressource.class.getName() + ".etablissements");
            createCache(cm, sn.thiane.mefpai.ent.domain.Evaluation.class.getName());
            createCache(cm, sn.thiane.mefpai.ent.domain.Evaluation.class.getName() + ".notes");
            createCache(cm, sn.thiane.mefpai.ent.domain.Evaluation.class.getName() + ".absences");
            createCache(cm, sn.thiane.mefpai.ent.domain.Evaluation.class.getName() + ".surveillants");
            createCache(cm, sn.thiane.mefpai.ent.domain.Note.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
