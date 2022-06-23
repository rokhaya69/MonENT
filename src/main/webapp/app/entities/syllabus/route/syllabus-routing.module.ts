import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SyllabusComponent } from '../list/syllabus.component';
import { SyllabusDetailComponent } from '../detail/syllabus-detail.component';
import { SyllabusUpdateComponent } from '../update/syllabus-update.component';
import { SyllabusRoutingResolveService } from './syllabus-routing-resolve.service';

const syllabusRoute: Routes = [
  {
    path: '',
    component: SyllabusComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SyllabusDetailComponent,
    resolve: {
      syllabus: SyllabusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SyllabusUpdateComponent,
    resolve: {
      syllabus: SyllabusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SyllabusUpdateComponent,
    resolve: {
      syllabus: SyllabusRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(syllabusRoute)],
  exports: [RouterModule],
})
export class SyllabusRoutingModule {}
