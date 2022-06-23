import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISeance } from '../seance.model';

@Component({
  selector: 'jhi-seance-detail',
  templateUrl: './seance-detail.component.html',
})
export class SeanceDetailComponent implements OnInit {
  seance: ISeance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ seance }) => {
      this.seance = seance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
