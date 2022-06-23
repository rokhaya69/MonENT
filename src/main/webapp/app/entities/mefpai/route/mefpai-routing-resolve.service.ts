import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMefpai, Mefpai } from '../mefpai.model';
import { MefpaiService } from '../service/mefpai.service';

@Injectable({ providedIn: 'root' })
export class MefpaiRoutingResolveService implements Resolve<IMefpai> {
  constructor(protected service: MefpaiService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMefpai> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mefpai: HttpResponse<Mefpai>) => {
          if (mefpai.body) {
            return of(mefpai.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Mefpai());
  }
}
