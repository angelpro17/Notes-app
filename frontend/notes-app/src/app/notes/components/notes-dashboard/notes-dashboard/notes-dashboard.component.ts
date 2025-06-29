import { Component, type OnInit, type OnDestroy } from "@angular/core"
import { CommonModule, DatePipe } from "@angular/common"
import { Router } from "@angular/router"
import {Observable, Subscription} from "rxjs"
import { MatToolbarModule } from "@angular/material/toolbar"
import { MatButtonModule } from "@angular/material/button"
import { MatIconModule } from "@angular/material/icon"
import { MatCardModule } from "@angular/material/card"
import { MatTabsModule } from "@angular/material/tabs"
import { MatChipsModule } from "@angular/material/chips"
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatSelectModule } from "@angular/material/select"
import { MatOptionModule } from "@angular/material/core"
import {NoteDialogComponent, NoteDialogData} from '../../note-dialog/note-dialog.component';
import {CategoryDialogComponent, CategoryDialogData} from '../../category-dialog/category-dialog.component';
import {Category, Note, NotesService} from '../../../service/notes.service';
import {AuthService} from '../../../../login/service/auth.service';
import {MatTooltip} from '@angular/material/tooltip';
import {ToolbarComponent} from '../../../../home/components/toolbar/toolbar.component';

@Component({
  selector: "app-notes-dashboard",
  standalone: true,
  imports: [
    CommonModule,
    DatePipe,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatTabsModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatFormFieldModule,
    MatSelectModule,
    MatOptionModule,
    NoteDialogComponent,
    CategoryDialogComponent,
    MatTooltip,
    ToolbarComponent
  ],
  templateUrl: "./notes-dashboard.component.html",
  styleUrls: ["./notes-dashboard.component.css"],
})
export class NotesDashboardComponent implements OnInit, OnDestroy {
  notes: Note[] = []
  categories: Category[] = []
  activeNotes: Note[] = []
  archivedNotes: Note[] = []

  selectedCategoryId: number | null = null
  isLoading = false

  showNoteDialog = false
  showCategoryDialog = false
  noteDialogData: NoteDialogData = { categories: [], isEdit: false }
  categoryDialogData: CategoryDialogData = { isEdit: false }

  private subscriptions: Subscription[] = []

  currentUser: any = null

  constructor(
    private notesService: NotesService,
    private authService: AuthService,
    private router: Router,
  ) {}

  ngOnInit() {
    this.currentUser = this.authService.getLoggedInUser()
    console.log("👤 Usuario actual:", this.currentUser)

    this.loadData()
    this.subscribeToData()
  }

  ngOnDestroy() {
    this.subscriptions.forEach((sub) => sub.unsubscribe())
  }

  private subscribeToData() {
    const notesSub = this.notesService.notes$.subscribe((notes) => {
      this.notes = notes
      this.filterNotes()
    })

    const categoriesSub = this.notesService.categories$.subscribe((categories) => {
      this.categories = categories
    })

    this.subscriptions.push(notesSub, categoriesSub)
  }

  private loadData() {
    this.isLoading = true

    Promise.all([
      this.notesService.getAllNotes().toPromise(),
      this.notesService.getAllCategories().toPromise(),
    ]).finally(() => {
      this.isLoading = false
    })
  }

  private filterNotes() {
    if (this.selectedCategoryId) {
      const filteredNotes = this.notesService.getNotesByCategory(this.selectedCategoryId)
      this.activeNotes = filteredNotes.filter((note) => !note.archived)
      this.archivedNotes = filteredNotes.filter((note) => note.archived)
    } else {
      this.activeNotes = this.notesService.getActiveNotes()
      this.archivedNotes = this.notesService.getArchivedNotes()
    }
  }

  filterByCategory(categoryId: number | null) {
    this.selectedCategoryId = categoryId
    this.filterNotes()
  }

  // ===== NOTE ACTIONS =====
  openNoteDialog(note?: Note) {
    this.noteDialogData = {
      note: note,
      categories: this.categories,
      isEdit: !!note,
    }
    this.showNoteDialog = true
  }

  onNoteSave(noteData: Partial<Note>) {
    if (this.noteDialogData.isEdit && this.noteDialogData.note) {
      this.notesService.updateNote(this.noteDialogData.note.id!, noteData).subscribe({
        next: () => {
          this.showNoteDialog = false
        },
        error: (error) => console.error("Error updating note:", error),
      })
    } else {
      this.notesService.createNote(noteData).subscribe({
        next: () => {
          this.showNoteDialog = false
        },
        error: (error) => console.error("Error creating note:", error),
      })
    }
  }

  onNoteCancel() {
    this.showNoteDialog = false
  }

  editNote(note: Note) {
    this.openNoteDialog(note)
  }

// En el componente, estos métodos deben llamar al servicio:
  archiveNote(note: Note) {
    this.notesService.archiveNote(note.id!).subscribe({
      error: (error) => console.error("Error archiving note:", error),
    })
  }

  unarchiveNote(note: Note) {
    this.notesService.unarchiveNote(note.id!).subscribe({
      error: (error) => console.error("Error unarchiving note:", error),
    })
  }

  deleteNote(note: Note) {
    if (confirm("¿Estás seguro de que quieres eliminar esta nota?")) {
      this.notesService.deleteNote(note.id!).subscribe({
        error: (error) => console.error("Error deleting note:", error),
      })
    }
  }

  // ===== CATEGORY ACTIONS =====
  openCategoryDialog(category?: Category) {
    this.categoryDialogData = {
      category: category,
      isEdit: !!category,
    }
    this.showCategoryDialog = true
  }

  onCategorySave(categoryData: Partial<Category>) {
    if (this.categoryDialogData.isEdit && this.categoryDialogData.category) {
      this.notesService.updateCategory(this.categoryDialogData.category.id!, categoryData).subscribe({
        next: () => {
          this.showCategoryDialog = false
        },
        error: (error) => console.error("Error updating category:", error),
      })
    } else {
      this.notesService.createCategory(categoryData).subscribe({
        next: () => {
          this.showCategoryDialog = false
        },
        error: (error) => console.error("Error creating category:", error),
      })
    }
  }

  onCategoryCancel() {
    this.showCategoryDialog = false
  }

  editCategory(category: Category) {
    this.openCategoryDialog(category)
  }

  deleteCategory(category: Category) {
    if (confirm("¿Estás seguro de que quieres eliminar esta categoría?")) {
      this.notesService.deleteCategory(category.id!).subscribe({
        error: (error) => console.error("Error deleting category:", error),
      })
    }
  }

  // ===== AUTH ACTIONS =====
  logout() {
    this.authService.logout()
    this.router.navigate(["/login"])
  }

  trackByNoteId(index: number, note: Note): number {
    return note.id || index
  }

  trackByCategoryId(index: number, category: Category): number {
    return category.id || index
  }

  getNotesByCategory(categoryId: number): Note[] {
    return this.notes.filter((note) => note.categories?.some((category) => category.id === categoryId))
  }
}
