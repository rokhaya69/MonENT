import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISyllabus, Syllabus } from '../syllabus.model';
import { SyllabusService } from '../service/syllabus.service';

@Injectable({ providedIn: 'root' })
export class SyllabusRoutingResolveService implements Resolve<ISyllabus> {
  constructor(protected service: SyllabusService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISyllabus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((syllabus: HttpResponse<Syllabus>) => {
          if (syllabus.body) {
            return of(syllabus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Syllabus());
  }
}
