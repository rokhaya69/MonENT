import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProfesseur } from '../professeur.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-professeur-detail',
  templateUrl: './professeur-detail.component.html',
})
export class ProfesseurDetailComponent implements OnInit {
  professeur: IProfesseur | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ professeur }) => {
      this.professeur = professeur;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
