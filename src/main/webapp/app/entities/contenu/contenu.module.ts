import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContenuComponent } from './list/contenu.component';
import { ContenuDetailComponent } from './detail/contenu-detail.component';
import { ContenuUpdateComponent } from './update/contenu-update.component';
import { ContenuDeleteDialogComponent } from './delete/contenu-delete-dialog.component';
import { ContenuRoutingModule } from './route/contenu-routing.module';

@NgModule({
  imports: [SharedModule, ContenuRoutingModule],
  declarations: [ContenuComponent, ContenuDetailComponent, ContenuUpdateComponent, ContenuDeleteDialogComponent],
  entryComponents: [ContenuDeleteDialogComponent],
})
export class ContenuModule {}
