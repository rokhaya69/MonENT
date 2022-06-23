import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EvaluationComponent } from './list/evaluation.component';
import { EvaluationDetailComponent } from './detail/evaluation-detail.component';
import { EvaluationUpdateComponent } from './update/evaluation-update.component';
import { EvaluationDeleteDialogComponent } from './delete/evaluation-delete-dialog.component';
import { EvaluationRoutingModule } from './route/evaluation-routing.module';

@NgModule({
  imports: [SharedModule, EvaluationRoutingModule],
  declarations: [EvaluationComponent, EvaluationDetailComponent, EvaluationUpdateComponent, EvaluationDeleteDialogComponent],
  entryComponents: [EvaluationDeleteDialogComponent],
})
export class EvaluationModule {}
