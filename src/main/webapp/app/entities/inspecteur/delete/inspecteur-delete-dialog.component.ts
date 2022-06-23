import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInspecteur } from '../inspecteur.model';
import { InspecteurService } from '../service/inspecteur.service';

@Component({
  templateUrl: './inspecteur-delete-dialog.component.html',
})
export class InspecteurDeleteDialogComponent {
  inspecteur?: IInspecteur;

  constructor(protected inspecteurService: InspecteurService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inspecteurService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
