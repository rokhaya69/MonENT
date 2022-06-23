import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProfesseurComponent } from './list/professeur.component';
import { ProfesseurDetailComponent } from './detail/professeur-detail.component';
import { ProfesseurUpdateComponent } from './update/professeur-update.component';
import { ProfesseurDeleteDialogComponent } from './delete/professeur-delete-dialog.component';
import { ProfesseurRoutingModule } from './route/professeur-routing.module';

@NgModule({
  imports: [SharedModule, ProfesseurRoutingModule],
  declarations: [ProfesseurComponent, ProfesseurDetailComponent, ProfesseurUpdateComponent, ProfesseurDeleteDialogComponent],
  entryComponents: [ProfesseurDeleteDialogComponent],
})
export class ProfesseurModule {}
