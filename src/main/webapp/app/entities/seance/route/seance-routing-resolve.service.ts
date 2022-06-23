import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISeance, Seance } from '../seance.model';
import { SeanceService } from '../service/seance.service';

@Injectable({ providedIn: 'root' })
export class SeanceRoutingResolveService implements Resolve<ISeance> {
  constructor(protected service: SeanceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISeance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((seance: HttpResponse<Seance>) => {
          if (seance.body) {
            return of(seance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Seance());
  }
}
