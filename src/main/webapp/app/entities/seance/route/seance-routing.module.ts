import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SeanceComponent } from '../list/seance.component';
import { SeanceDetailComponent } from '../detail/seance-detail.component';
import { SeanceUpdateComponent } from '../update/seance-update.component';
import { SeanceRoutingResolveService } from './seance-routing-resolve.service';

const seanceRoute: Routes = [
  {
    path: '',
    component: SeanceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SeanceDetailComponent,
    resolve: {
      seance: SeanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SeanceUpdateComponent,
    resolve: {
      seance: SeanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SeanceUpdateComponent,
    resolve: {
      seance: SeanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(seanceRoute)],
  exports: [RouterModule],
})
export class SeanceRoutingModule {}
