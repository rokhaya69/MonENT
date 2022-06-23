import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProgramme } from '../programme.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-programme-detail',
  templateUrl: './programme-detail.component.html',
})
export class ProgrammeDetailComponent implements OnInit {
  programme: IProgramme | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ programme }) => {
      this.programme = programme;
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
