import { Injectable } from "@angular/core"
import { HttpClient, HttpHeaders } from "@angular/common/http"
import { Observable, BehaviorSubject } from "rxjs"
import { tap } from "rxjs/operators"
import { AuthService } from "../../login/service/auth.service"

export interface Note {
  id?: number
  title: string
  content: string
  createdAt?: string  // ✅ Cambiar a string
  updatedAt?: string  // ✅ Cambiar a string
  archived: boolean
  categories?: Category[]
}

export interface Category {
  id?: number
  name: string
  color?: string
  createdAt?: string  // ✅ Cambiar a string
  updatedAt?: string  // ✅ Cambiar a string
}

@Injectable({
  providedIn: "root",
})
export class NotesService {
  private apiUrl = 'http://localhost:8081/api/v1'

  // BehaviorSubjects para manejar el estado
  private notesSubject = new BehaviorSubject<Note[]>([])
  private categoriesSubject = new BehaviorSubject<Category[]>([])

  // Observables públicos
  public notes$ = this.notesSubject.asObservable()
  public categories$ = this.categoriesSubject.asObservable()

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {
    this.loadInitialData()
  }

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken()
    return new HttpHeaders({
      "Content-Type": "application/json",
      ...(token && { Authorization: `Bearer ${token}` })
    })
  }

  private loadInitialData() {
    this.getAllNotes().subscribe()
    this.getAllCategories().subscribe()
  }

  // ===== NOTES CRUD =====
  getAllNotes(): Observable<Note[]> {
    return this.http.get<Note[]>(`${this.apiUrl}/notes`, { headers: this.getHeaders() })
      .pipe(tap(notes => this.notesSubject.next(notes)))
  }

  getNoteById(id: number): Observable<Note> {
    return this.http.get<Note>(`${this.apiUrl}/notes/${id}`, { headers: this.getHeaders() })
  }

  createNote(note: Partial<Note>): Observable<Note> {
    // ✅ Convertir categories a categoryIds
    const noteData = {
      title: note.title,
      content: note.content,
      categoryIds: note.categories?.map(cat => cat.id).filter(id => id !== undefined) || []
    }

    return this.http.post<Note>(`${this.apiUrl}/notes`, noteData, { headers: this.getHeaders() })
      .pipe(
        tap(newNote => {
          const currentNotes = this.notesSubject.value
          this.notesSubject.next([...currentNotes, newNote])
        })
      )
  }

  updateNote(id: number, note: Partial<Note>): Observable<Note> {
    // ✅ Convertir categories a categoryIds
    const noteData = {
      title: note.title,
      content: note.content,
      categoryIds: note.categories?.map(cat => cat.id).filter(id => id !== undefined) || []
    }

    return this.http.put<Note>(`${this.apiUrl}/notes/${id}`, noteData, { headers: this.getHeaders() })
      .pipe(
        tap(updatedNote => {
          const currentNotes = this.notesSubject.value
          const updatedNotes = currentNotes.map(n => n.id === id ? updatedNote : n)
          this.notesSubject.next(updatedNotes)
        })
      )
  }

  deleteNote(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/notes/${id}`, { headers: this.getHeaders() })
      .pipe(
        tap(() => {
          const currentNotes = this.notesSubject.value
          const filteredNotes = currentNotes.filter(n => n.id !== id)
          this.notesSubject.next(filteredNotes)
        })
      )
  }

  // ✅ Métodos corregidos para archivar
  archiveNote(id: number): Observable<Note> {
    return this.http.patch<Note>(`${this.apiUrl}/notes/${id}/archive`, {}, { headers: this.getHeaders() })
      .pipe(
        tap(archivedNote => {
          const currentNotes = this.notesSubject.value
          const updatedNotes = currentNotes.map(n => n.id === id ? archivedNote : n)
          this.notesSubject.next(updatedNotes)
        })
      )
  }

  unarchiveNote(id: number): Observable<Note> {
    return this.http.patch<Note>(`${this.apiUrl}/notes/${id}/unarchive`, {}, { headers: this.getHeaders() })
      .pipe(
        tap(unarchivedNote => {
          const currentNotes = this.notesSubject.value
          const updatedNotes = currentNotes.map(n => n.id === id ? unarchivedNote : n)
          this.notesSubject.next(updatedNotes)
        })
      )
  }

  // ===== CATEGORIES CRUD =====
  getAllCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.apiUrl}/categories`, { headers: this.getHeaders() })
      .pipe(tap(categories => this.categoriesSubject.next(categories)))
  }

  getCategoryById(id: number): Observable<Category> {
    return this.http.get<Category>(`${this.apiUrl}/categories/${id}`, { headers: this.getHeaders() })
  }

  createCategory(category: Partial<Category>): Observable<Category> {
    return this.http.post<Category>(`${this.apiUrl}/categories`, category, { headers: this.getHeaders() })
      .pipe(
        tap(newCategory => {
          const currentCategories = this.categoriesSubject.value
          this.categoriesSubject.next([...currentCategories, newCategory])
        })
      )
  }

  updateCategory(id: number, category: Partial<Category>): Observable<Category> {
    return this.http.put<Category>(`${this.apiUrl}/categories/${id}`, category, { headers: this.getHeaders() })
      .pipe(
        tap(updatedCategory => {
          const currentCategories = this.categoriesSubject.value
          const updatedCategories = currentCategories.map(c => c.id === id ? updatedCategory : c)
          this.categoriesSubject.next(updatedCategories)
        })
      )
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/categories/${id}`, { headers: this.getHeaders() })
      .pipe(
        tap(() => {
          const currentCategories = this.categoriesSubject.value
          const filteredCategories = currentCategories.filter(c => c.id !== id)
          this.categoriesSubject.next(filteredCategories)
        })
      )
  }

  // ===== UTILITY METHODS =====
  getActiveNotes(): Note[] {
    return this.notesSubject.value.filter((note) => !note.archived)
  }

  getArchivedNotes(): Note[] {
    return this.notesSubject.value.filter((note) => note.archived)
  }

  getNotesByCategory(categoryId: number): Note[] {
    return this.notesSubject.value.filter((note) =>
      note.categories?.some((category) => category.id === categoryId)
    )
  }

  searchNotes(query: string): Note[] {
    const lowercaseQuery = query.toLowerCase()
    return this.notesSubject.value.filter(
      (note) =>
        note.title.toLowerCase().includes(lowercaseQuery) ||
        note.content.toLowerCase().includes(lowercaseQuery)
    )
  }
}
