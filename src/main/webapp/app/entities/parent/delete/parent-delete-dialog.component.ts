import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IParent } from '../parent.model';
import { ParentService } from '../service/parent.service';

@Component({
  templateUrl: './parent-delete-dialog.component.html',
})
export class ParentDeleteDialogComponent {
  parent?: IParent;

  constructor(protected parentService: ParentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.parentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
