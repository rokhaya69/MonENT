import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProfesseurComponent } from '../list/professeur.component';
import { ProfesseurDetailComponent } from '../detail/professeur-detail.component';
import { ProfesseurUpdateComponent } from '../update/professeur-update.component';
import { ProfesseurRoutingResolveService } from './professeur-routing-resolve.service';

const professeurRoute: Routes = [
  {
    path: '',
    component: ProfesseurComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProfesseurDetailComponent,
    resolve: {
      professeur: ProfesseurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProfesseurUpdateComponent,
    resolve: {
      professeur: ProfesseurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProfesseurUpdateComponent,
    resolve: {
      professeur: ProfesseurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(professeurRoute)],
  exports: [RouterModule],
})
export class ProfesseurRoutingModule {}
