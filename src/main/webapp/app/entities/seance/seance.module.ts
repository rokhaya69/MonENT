import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SeanceComponent } from './list/seance.component';
import { SeanceDetailComponent } from './detail/seance-detail.component';
import { SeanceUpdateComponent } from './update/seance-update.component';
import { SeanceDeleteDialogComponent } from './delete/seance-delete-dialog.component';
import { SeanceRoutingModule } from './route/seance-routing.module';

@NgModule({
  imports: [SharedModule, SeanceRoutingModule],
  declarations: [SeanceComponent, SeanceDetailComponent, SeanceUpdateComponent, SeanceDeleteDialogComponent],
  entryComponents: [SeanceDeleteDialogComponent],
})
export class SeanceModule {}
