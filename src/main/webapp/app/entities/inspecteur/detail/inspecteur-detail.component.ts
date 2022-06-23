import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInspecteur } from '../inspecteur.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-inspecteur-detail',
  templateUrl: './inspecteur-detail.component.html',
})
export class InspecteurDetailComponent implements OnInit {
  inspecteur: IInspecteur | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inspecteur }) => {
      this.inspecteur = inspecteur;
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
