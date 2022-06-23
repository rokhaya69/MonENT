import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEvaluation, Evaluation } from '../evaluation.model';
import { EvaluationService } from '../service/evaluation.service';

@Injectable({ providedIn: 'root' })
export class EvaluationRoutingResolveService implements Resolve<IEvaluation> {
  constructor(protected service: EvaluationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEvaluation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((evaluation: HttpResponse<Evaluation>) => {
          if (evaluation.body) {
            return of(evaluation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Evaluation());
  }
}
