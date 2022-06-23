import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InspecteurComponent } from './list/inspecteur.component';
import { InspecteurDetailComponent } from './detail/inspecteur-detail.component';
import { InspecteurUpdateComponent } from './update/inspecteur-update.component';
import { InspecteurDeleteDialogComponent } from './delete/inspecteur-delete-dialog.component';
import { InspecteurRoutingModule } from './route/inspecteur-routing.module';

@NgModule({
  imports: [SharedModule, InspecteurRoutingModule],
  declarations: [InspecteurComponent, InspecteurDetailComponent, InspecteurUpdateComponent, InspecteurDeleteDialogComponent],
  entryComponents: [InspecteurDeleteDialogComponent],
})
export class InspecteurModule {}
