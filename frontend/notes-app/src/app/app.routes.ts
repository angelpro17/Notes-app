import type { Routes } from "@angular/router"
import { RegisterComponent } from "./login/components/register/register.component"
import { PageNotFoundComponent } from "./home/pages/page-not-found/page-not-found.component"
import { LoginComponent } from "./login/components/login/login.component"
import {NotesDashboardComponent} from './notes/components/notes-dashboard/notes-dashboard/notes-dashboard.component';
import {authGuard} from "./login/auth.guard"

export const routes: Routes = [
  {
    path: "",
    redirectTo: "/login",
    pathMatch: "full",
  },
  {
    path: "login",
    component: LoginComponent,
  },
  {
    path: "register",
    component: RegisterComponent,
  },
  {
    path: "notes",
    component: NotesDashboardComponent,
    canActivate: [authGuard], // Proteger la ruta notes
  },
  {
    path: "**",
    component: PageNotFoundComponent,
  },
]
