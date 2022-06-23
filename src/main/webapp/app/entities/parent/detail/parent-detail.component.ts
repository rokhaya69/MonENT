import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParent } from '../parent.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-parent-detail',
  templateUrl: './parent-detail.component.html',
})
export class ParentDetailComponent implements OnInit {
  parent: IParent | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parent }) => {
      this.parent = parent;
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
