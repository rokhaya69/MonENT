import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IParent, Parent } from '../parent.model';
import { ParentService } from '../service/parent.service';

@Injectable({ providedIn: 'root' })
export class ParentRoutingResolveService implements Resolve<IParent> {
  constructor(protected service: ParentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IParent> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((parent: HttpResponse<Parent>) => {
          if (parent.body) {
            return of(parent.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Parent());
  }
}
