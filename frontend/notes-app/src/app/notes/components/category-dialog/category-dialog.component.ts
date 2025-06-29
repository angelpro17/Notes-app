import {Component, EventEmitter, Inject, Input, Output} from '@angular/core';
import {Category} from '../../service/notes.service';
import { MatDialogActions, MatDialogContent, MatDialogTitle} from '@angular/material/dialog';
import {MatFormField, MatHint, MatInput, MatLabel} from '@angular/material/input';
import {FormsModule} from '@angular/forms';
import {MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardTitle} from '@angular/material/card';
import {NgIf} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatDividerModule} from '@angular/material/divider';

export interface CategoryDialogData {
  category?: Category
  isEdit: boolean
}
@Component({
  selector: 'app-category-dialog',
  imports: [

    MatFormField,
    MatLabel,
    MatInput,
    FormsModule,
    MatHint,
    MatLabel,
    MatFormField,
    MatCardActions,
    MatCard,
    MatCardHeader,
    MatCardTitle,
    MatCardContent,
    NgIf,
    MatButton,
    MatIconModule,         // <<< aquí
    MatDividerModule

  ],
  templateUrl: './category-dialog.component.html',
  styleUrl: './category-dialog.component.css'
})
export class CategoryDialogComponent {
  @Input() data: CategoryDialogData = { isEdit: false }
  @Output() save = new EventEmitter<Partial<Category>>()
  @Output() cancel = new EventEmitter<void>()

  categoryData: Partial<Category> = {
    name: "",
    color: "#3f51b5",
  }

  isLoading = false

  ngOnInit() {
    if (this.data.isEdit && this.data.category) {
      this.categoryData = { ...this.data.category }
    }
  }

  onCancel() {
    this.cancel.emit()
  }

  onSave() {
    if (this.categoryData.name) {
      this.isLoading = true
      this.save.emit(this.categoryData)
    }
  }
}
