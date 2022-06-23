import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SurveillantComponent } from './list/surveillant.component';
import { SurveillantDetailComponent } from './detail/surveillant-detail.component';
import { SurveillantUpdateComponent } from './update/surveillant-update.component';
import { SurveillantDeleteDialogComponent } from './delete/surveillant-delete-dialog.component';
import { SurveillantRoutingModule } from './route/surveillant-routing.module';

@NgModule({
  imports: [SharedModule, SurveillantRoutingModule],
  declarations: [SurveillantComponent, SurveillantDetailComponent, SurveillantUpdateComponent, SurveillantDeleteDialogComponent],
  entryComponents: [SurveillantDeleteDialogComponent],
})
export class SurveillantModule {}
