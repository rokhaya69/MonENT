import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InspecteurComponent } from '../list/inspecteur.component';
import { InspecteurDetailComponent } from '../detail/inspecteur-detail.component';
import { InspecteurUpdateComponent } from '../update/inspecteur-update.component';
import { InspecteurRoutingResolveService } from './inspecteur-routing-resolve.service';

const inspecteurRoute: Routes = [
  {
    path: '',
    component: InspecteurComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InspecteurDetailComponent,
    resolve: {
      inspecteur: InspecteurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InspecteurUpdateComponent,
    resolve: {
      inspecteur: InspecteurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InspecteurUpdateComponent,
    resolve: {
      inspecteur: InspecteurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(inspecteurRoute)],
  exports: [RouterModule],
})
export class InspecteurRoutingModule {}
