import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cours',
        data: { pageTitle: 'entMefpaiv2App.cours.home.title' },
        loadChildren: () => import('./cours/cours.module').then(m => m.CoursModule),
      },
      {
        path: 'classe',
        data: { pageTitle: 'entMefpaiv2App.classe.home.title' },
        loadChildren: () => import('./classe/classe.module').then(m => m.ClasseModule),
      },
      {
        path: 'salle',
        data: { pageTitle: 'entMefpaiv2App.salle.home.title' },
        loadChildren: () => import('./salle/salle.module').then(m => m.SalleModule),
      },
      {
        path: 'matiere',
        data: { pageTitle: 'entMefpaiv2App.matiere.home.title' },
        loadChildren: () => import('./matiere/matiere.module').then(m => m.MatiereModule),
      },
      {
        path: 'groupe',
        data: { pageTitle: 'entMefpaiv2App.groupe.home.title' },
        loadChildren: () => import('./groupe/groupe.module').then(m => m.GroupeModule),
      },
      {
        path: 'seance',
        data: { pageTitle: 'entMefpaiv2App.seance.home.title' },
        loadChildren: () => import('./seance/seance.module').then(m => m.SeanceModule),
      },
      {
        path: 'contenu',
        data: { pageTitle: 'entMefpaiv2App.contenu.home.title' },
        loadChildren: () => import('./contenu/contenu.module').then(m => m.ContenuModule),
      },
      {
        path: 'syllabus',
        data: { pageTitle: 'entMefpaiv2App.syllabus.home.title' },
        loadChildren: () => import('./syllabus/syllabus.module').then(m => m.SyllabusModule),
      },
      {
        path: 'absence',
        data: { pageTitle: 'entMefpaiv2App.absence.home.title' },
        loadChildren: () => import('./absence/absence.module').then(m => m.AbsenceModule),
      },
      {
        path: 'apprenant',
        data: { pageTitle: 'entMefpaiv2App.apprenant.home.title' },
        loadChildren: () => import('./apprenant/apprenant.module').then(m => m.ApprenantModule),
      },
      {
        path: 'surveillant',
        data: { pageTitle: 'entMefpaiv2App.surveillant.home.title' },
        loadChildren: () => import('./surveillant/surveillant.module').then(m => m.SurveillantModule),
      },
      {
        path: 'directeur',
        data: { pageTitle: 'entMefpaiv2App.directeur.home.title' },
        loadChildren: () => import('./directeur/directeur.module').then(m => m.DirecteurModule),
      },
      {
        path: 'proviseur',
        data: { pageTitle: 'entMefpaiv2App.proviseur.home.title' },
        loadChildren: () => import('./proviseur/proviseur.module').then(m => m.ProviseurModule),
      },
      {
        path: 'inspecteur',
        data: { pageTitle: 'entMefpaiv2App.inspecteur.home.title' },
        loadChildren: () => import('./inspecteur/inspecteur.module').then(m => m.InspecteurModule),
      },
      {
        path: 'professeur',
        data: { pageTitle: 'entMefpaiv2App.professeur.home.title' },
        loadChildren: () => import('./professeur/professeur.module').then(m => m.ProfesseurModule),
      },
      {
        path: 'parent',
        data: { pageTitle: 'entMefpaiv2App.parent.home.title' },
        loadChildren: () => import('./parent/parent.module').then(m => m.ParentModule),
      },
      {
        path: 'mefpai',
        data: { pageTitle: 'entMefpaiv2App.mefpai.home.title' },
        loadChildren: () => import('./mefpai/mefpai.module').then(m => m.MefpaiModule),
      },
      {
        path: 'etablissement',
        data: { pageTitle: 'entMefpaiv2App.etablissement.home.title' },
        loadChildren: () => import('./etablissement/etablissement.module').then(m => m.EtablissementModule),
      },
      {
        path: 'filiere',
        data: { pageTitle: 'entMefpaiv2App.filiere.home.title' },
        loadChildren: () => import('./filiere/filiere.module').then(m => m.FiliereModule),
      },
      {
        path: 'serie',
        data: { pageTitle: 'entMefpaiv2App.serie.home.title' },
        loadChildren: () => import('./serie/serie.module').then(m => m.SerieModule),
      },
      {
        path: 'programme',
        data: { pageTitle: 'entMefpaiv2App.programme.home.title' },
        loadChildren: () => import('./programme/programme.module').then(m => m.ProgrammeModule),
      },
      {
        path: 'region',
        data: { pageTitle: 'entMefpaiv2App.region.home.title' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      {
        path: 'departement',
        data: { pageTitle: 'entMefpaiv2App.departement.home.title' },
        loadChildren: () => import('./departement/departement.module').then(m => m.DepartementModule),
      },
      {
        path: 'commune',
        data: { pageTitle: 'entMefpaiv2App.commune.home.title' },
        loadChildren: () => import('./commune/commune.module').then(m => m.CommuneModule),
      },
      {
        path: 'inspection',
        data: { pageTitle: 'entMefpaiv2App.inspection.home.title' },
        loadChildren: () => import('./inspection/inspection.module').then(m => m.InspectionModule),
      },
      {
        path: 'ressource',
        data: { pageTitle: 'entMefpaiv2App.ressource.home.title' },
        loadChildren: () => import('./ressource/ressource.module').then(m => m.RessourceModule),
      },
      {
        path: 'evaluation',
        data: { pageTitle: 'entMefpaiv2App.evaluation.home.title' },
        loadChildren: () => import('./evaluation/evaluation.module').then(m => m.EvaluationModule),
      },
      {
        path: 'note',
        data: { pageTitle: 'entMefpaiv2App.note.home.title' },
        loadChildren: () => import('./note/note.module').then(m => m.NoteModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
