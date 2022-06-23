import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProviseur } from '../proviseur.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-proviseur-detail',
  templateUrl: './proviseur-detail.component.html',
})
export class ProviseurDetailComponent implements OnInit {
  proviseur: IProviseur | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proviseur }) => {
      this.proviseur = proviseur;
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
