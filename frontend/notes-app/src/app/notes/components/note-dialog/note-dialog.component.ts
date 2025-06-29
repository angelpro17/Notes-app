import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Category, Note} from '../../service/notes.service';
import { MatDialogActions, MatDialogContent, MatDialogTitle} from '@angular/material/dialog';
import {FormsModule} from '@angular/forms';
import {MatFormField, MatHint, MatInput, MatLabel} from '@angular/material/input';
import {NgForOf, NgIf} from '@angular/common';
import {MatChip, MatChipSet} from '@angular/material/chips';
import {MatButton} from '@angular/material/button';
import {MatOption, MatSelect} from '@angular/material/select';
import {MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardTitle} from '@angular/material/card';
import {MatIcon} from '@angular/material/icon';
export interface NoteDialogData {
  note?: Note
  categories: Category[]
  isEdit: boolean
}
@Component({
  selector: 'app-note-dialog',
  imports: [
    MatFormField,
    FormsModule,
    MatFormField,
    MatLabel,
    MatInput,
    MatHint,
    MatFormField,
    NgIf,
    MatLabel,
    MatSelect,
    MatChipSet,
    MatChip,
    NgForOf,
    MatButton,
    MatCardActions,
    MatCardHeader,
    MatCardTitle,
    MatCard,
    MatCardContent,
    MatOption,
    MatIcon
  ],
  templateUrl: './note-dialog.component.html',
  styleUrl: './note-dialog.component.css'
})
export class NoteDialogComponent implements OnInit {
  @Input() data: NoteDialogData = { categories: [], isEdit: false }
  @Output() save = new EventEmitter<Partial<Note>>()
  @Output() cancel = new EventEmitter<void>()

  noteData: Partial<Note> = {
    title: "",
    content: "",
    archived: false,
  }

  selectedCategoryIds: number[] = []
  isLoading = false

  ngOnInit() {
    if (this.data.isEdit && this.data.note) {
      this.noteData = { ...this.data.note }
      this.selectedCategoryIds = this.data.note.categories?.map((cat: Category) => cat.id!) || []
    }
  }

  getCategoryName(categoryId: number): string {
    const category = this.data.categories.find((cat: Category) => cat.id === categoryId)
    return category ? category.name : ""
  }

  removeCategory(categoryId: number) {
    this.selectedCategoryIds = this.selectedCategoryIds.filter((id: number) => id !== categoryId)
  }

  onCancel() {
    this.cancel.emit()
  }

  onSave() {
    if (this.noteData.title && this.noteData.content) {
      this.isLoading = true

      // Agregar categorías seleccionadas
      this.noteData.categories = this.selectedCategoryIds.map(
        (id: number) => this.data.categories.find((cat: Category) => cat.id === id)!,
      )

      this.save.emit(this.noteData)
    }
  }
}
