import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISurveillant } from '../surveillant.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-surveillant-detail',
  templateUrl: './surveillant-detail.component.html',
})
export class SurveillantDetailComponent implements OnInit {
  surveillant: ISurveillant | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ surveillant }) => {
      this.surveillant = surveillant;
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
