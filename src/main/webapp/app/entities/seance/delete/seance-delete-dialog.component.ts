import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISeance } from '../seance.model';
import { SeanceService } from '../service/seance.service';

@Component({
  templateUrl: './seance-delete-dialog.component.html',
})
export class SeanceDeleteDialogComponent {
  seance?: ISeance;

  constructor(protected seanceService: SeanceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.seanceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
