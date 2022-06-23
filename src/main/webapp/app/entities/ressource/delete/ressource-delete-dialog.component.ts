import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRessource } from '../ressource.model';
import { RessourceService } from '../service/ressource.service';

@Component({
  templateUrl: './ressource-delete-dialog.component.html',
})
export class RessourceDeleteDialogComponent {
  ressource?: IRessource;

  constructor(protected ressourceService: RessourceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ressourceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
