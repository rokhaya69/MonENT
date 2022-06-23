import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISurveillant, Surveillant } from '../surveillant.model';
import { SurveillantService } from '../service/surveillant.service';

@Injectable({ providedIn: 'root' })
export class SurveillantRoutingResolveService implements Resolve<ISurveillant> {
  constructor(protected service: SurveillantService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISurveillant> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((surveillant: HttpResponse<Surveillant>) => {
          if (surveillant.body) {
            return of(surveillant.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Surveillant());
  }
}
