import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContenu, Contenu } from '../contenu.model';
import { ContenuService } from '../service/contenu.service';

@Injectable({ providedIn: 'root' })
export class ContenuRoutingResolveService implements Resolve<IContenu> {
  constructor(protected service: ContenuService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContenu> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contenu: HttpResponse<Contenu>) => {
          if (contenu.body) {
            return of(contenu.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Contenu());
  }
}
