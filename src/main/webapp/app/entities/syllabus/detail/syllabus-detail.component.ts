import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISyllabus } from '../syllabus.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-syllabus-detail',
  templateUrl: './syllabus-detail.component.html',
})
export class SyllabusDetailComponent implements OnInit {
  syllabus: ISyllabus | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ syllabus }) => {
      this.syllabus = syllabus;
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
