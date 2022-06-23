import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EvaluationComponent } from '../list/evaluation.component';
import { EvaluationDetailComponent } from '../detail/evaluation-detail.component';
import { EvaluationUpdateComponent } from '../update/evaluation-update.component';
import { EvaluationRoutingResolveService } from './evaluation-routing-resolve.service';

const evaluationRoute: Routes = [
  {
    path: '',
    component: EvaluationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EvaluationDetailComponent,
    resolve: {
      evaluation: EvaluationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EvaluationUpdateComponent,
    resolve: {
      evaluation: EvaluationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EvaluationUpdateComponent,
    resolve: {
      evaluation: EvaluationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(evaluationRoute)],
  exports: [RouterModule],
})
export class EvaluationRoutingModule {}
