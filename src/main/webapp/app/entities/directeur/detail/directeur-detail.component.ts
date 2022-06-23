import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDirecteur } from '../directeur.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-directeur-detail',
  templateUrl: './directeur-detail.component.html',
})
export class DirecteurDetailComponent implements OnInit {
  directeur: IDirecteur | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ directeur }) => {
      this.directeur = directeur;
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
