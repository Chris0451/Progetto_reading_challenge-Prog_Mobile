# 📚 Reading Challenge

Applicazione Android sviluppata in **Jetpack Compose** con architettura **MVVM**.  
Il progetto integra **Firebase (Auth + Firestore)** per la gestione degli utenti e dei dati, e la **Google Books API** per la ricerca e i metadati dei libri.  

---

## 🚀 Tecnologie principali

- **Linguaggio**: Kotlin  
- **UI**: Jetpack Compose  
- **Architettura**: MVVM + Dependency Injection  
- **Backend**:
  - Firebase Authentication
  - Firebase Firestore
- **API esterne**: Google Books API  
- **Gestione dipendenze**: Gradle Version Catalog (`libs.versions.toml`)  
- **Iniezione dipendenze**: Hilt/Dagger (moduli in `di/`)  

---

## 📂 Struttura del progetto

com.project.reading_challenge
│
├── MainActivity.kt
│
├── di/ # Dependency Injection
│ ├── FirebaseModule.kt # Provider per FirebaseAuth, Firestore
│ ├── ApiModule.kt # Provider per GoogleBooksApi
│ ├── RepositoryModule.kt # Provider per i Repository
│ └── ApiKeysModule.kt # Provider per BOOKS_API_KEY
│
├── data/
│ ├── remote/ # Data source esterni (Firebase, Google Books)
│ │ ├── FirebaseAuthDataSource.kt
│ │ ├── FirestoreDataSource.kt
│ │ └── GoogleBooksApi.kt
│ │
│ ├── repo/ # Repository con logica di business
│ │ ├── UserPreferencesRepository.kt
│ │ └── CatalogRepository.kt
│
├── domain/
│ ├── model/ # Modelli del dominio
│ │ ├── UserProfile.kt
│ │ ├── UserPreferences.kt
│ │ ├── BookSnapshot.kt
│ │ ├── UserBook.kt
│ │ ├── Shelf.kt
│ │ ├── FriendLink.kt
│ │ └── Review.kt
│
└── ui/
├── screens/ # Schermate Compose
│ ├── auth/
│ │ ├── AuthScreen.kt
│ │ └── AuthViewModel.kt
│ │
│ ├── catalog/
│ │ ├── CatalogScreen.kt
│ │ ├── CatalogViewModel.kt
│ │ └── CatalogUiState.kt
│ │
│ └── home/
│ └── HomeScreen.kt
│
└── theme/ # Gestione tema Compose
├── Color.kt
├── Theme.kt
└── Type.kt

---

## 🗄️ Risorse Android

res/
├── layout/ # (opzionale, Compose non lo usa di default)
├── drawable/ # Icone e immagini locali
├── values/
│ ├── colors.xml
│ ├── themes.xml
│ └── strings.xml
└── mipmap/ # Icone app


---

## ⚙️ Configurazione

### 🔑 API Key
Il progetto richiede una chiave API di **Google Books**.  
Aggiungila nel file `local.properties`:


### 🛠️ Gradle
- `app/build.gradle.kts` → contiene `buildConfigField` per la Books API Key  
- `gradle/libs.versions.toml` → gestisce tutte le versioni delle dipendenze  
- `settings.gradle.kts` → configurazione moduli  

---

## 📌 Funzionalità principali

- Registrazione/Login con Firebase Authentication  
- Salvataggio e gestione dei dati utente su Firestore  
- Catalogo libri basato su **Google Books API**  
- Creazione di **scaffali personalizzati** per organizzare i libri  
- Possibilità di lasciare recensioni e gestire lo stato di lettura  
- Sezione **Amici** per condividere progressi di lettura  

---

## 📎 Link utili

- 🔗 [Repository GitHub](https://github.com/Chris0451/Progetto_reading_challenge-Prog_Mobile)  
- 📖 [Google Books API Documentation](https://developers.google.com/books)  
- 🔥 [Firebase Documentation](https://firebase.google.com/docs)  

---

## 👤 Autore

**Cristian Di Cintio** – Progetto per il corso di *Programmazione Mobile*  
Università Politecnica delle Marche – 2025
