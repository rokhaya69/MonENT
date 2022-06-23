import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContenu } from '../contenu.model';
import { ContenuService } from '../service/contenu.service';

@Component({
  templateUrl: './contenu-delete-dialog.component.html',
})
export class ContenuDeleteDialogComponent {
  contenu?: IContenu;

  constructor(protected contenuService: ContenuService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contenuService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
