import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MefpaiComponent } from '../list/mefpai.component';
import { MefpaiDetailComponent } from '../detail/mefpai-detail.component';
import { MefpaiUpdateComponent } from '../update/mefpai-update.component';
import { MefpaiRoutingResolveService } from './mefpai-routing-resolve.service';

const mefpaiRoute: Routes = [
  {
    path: '',
    component: MefpaiComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MefpaiDetailComponent,
    resolve: {
      mefpai: MefpaiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MefpaiUpdateComponent,
    resolve: {
      mefpai: MefpaiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MefpaiUpdateComponent,
    resolve: {
      mefpai: MefpaiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mefpaiRoute)],
  exports: [RouterModule],
})
export class MefpaiRoutingModule {}
