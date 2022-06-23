import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInspecteur, Inspecteur } from '../inspecteur.model';
import { InspecteurService } from '../service/inspecteur.service';

@Injectable({ providedIn: 'root' })
export class InspecteurRoutingResolveService implements Resolve<IInspecteur> {
  constructor(protected service: InspecteurService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInspecteur> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((inspecteur: HttpResponse<Inspecteur>) => {
          if (inspecteur.body) {
            return of(inspecteur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Inspecteur());
  }
}
