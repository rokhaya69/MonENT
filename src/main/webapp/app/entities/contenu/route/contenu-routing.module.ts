import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContenuComponent } from '../list/contenu.component';
import { ContenuDetailComponent } from '../detail/contenu-detail.component';
import { ContenuUpdateComponent } from '../update/contenu-update.component';
import { ContenuRoutingResolveService } from './contenu-routing-resolve.service';

const contenuRoute: Routes = [
  {
    path: '',
    component: ContenuComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContenuDetailComponent,
    resolve: {
      contenu: ContenuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContenuUpdateComponent,
    resolve: {
      contenu: ContenuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContenuUpdateComponent,
    resolve: {
      contenu: ContenuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contenuRoute)],
  exports: [RouterModule],
})
export class ContenuRoutingModule {}
