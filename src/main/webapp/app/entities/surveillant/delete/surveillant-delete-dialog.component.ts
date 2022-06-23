import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISurveillant } from '../surveillant.model';
import { SurveillantService } from '../service/surveillant.service';

@Component({
  templateUrl: './surveillant-delete-dialog.component.html',
})
export class SurveillantDeleteDialogComponent {
  surveillant?: ISurveillant;

  constructor(protected surveillantService: SurveillantService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.surveillantService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
