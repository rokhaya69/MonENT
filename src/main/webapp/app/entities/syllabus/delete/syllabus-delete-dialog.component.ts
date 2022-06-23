import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISyllabus } from '../syllabus.model';
import { SyllabusService } from '../service/syllabus.service';

@Component({
  templateUrl: './syllabus-delete-dialog.component.html',
})
export class SyllabusDeleteDialogComponent {
  syllabus?: ISyllabus;

  constructor(protected syllabusService: SyllabusService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.syllabusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
