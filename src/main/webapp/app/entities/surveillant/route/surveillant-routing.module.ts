import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SurveillantComponent } from '../list/surveillant.component';
import { SurveillantDetailComponent } from '../detail/surveillant-detail.component';
import { SurveillantUpdateComponent } from '../update/surveillant-update.component';
import { SurveillantRoutingResolveService } from './surveillant-routing-resolve.service';

const surveillantRoute: Routes = [
  {
    path: '',
    component: SurveillantComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SurveillantDetailComponent,
    resolve: {
      surveillant: SurveillantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SurveillantUpdateComponent,
    resolve: {
      surveillant: SurveillantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SurveillantUpdateComponent,
    resolve: {
      surveillant: SurveillantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(surveillantRoute)],
  exports: [RouterModule],
})
export class SurveillantRoutingModule {}
