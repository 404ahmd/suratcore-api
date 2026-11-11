# Tutorial Pengujian API Login & Register Menggunakan Postman Collections

Dokumen ini berisi panduan lengkap langkah demi langkah untuk melakukan pengujian endpoint autentikasi (**Register** dan **Login**) berbasis JWT pada project **Suratcore API** menggunakan Postman Collections.

---

## Daftar Isi
1. [Prasyarat Sebelum Pengujian](#1-prasyarat-sebelum-pengujian)
2. [Metode 1: Menggunakan File Import Otomatis (Cepat)](#2-metode-1-menggunakan-file-import-otomatis-cepat)
3. [Metode 2: Membuat Postman Collection Manual (Step-by-Step)](#3-metode-2-membuat-postman-collection-manual-step-by-step)
   - [Langkah 1: Membuat Collection & Environment Variables](#langkah-1-membuat-collection--environment-variables)
   - [Langkah 2: Menyiapkan Data Divisi (Prasyarat User)](#langkah-2-menyiapkan-data-divisi-prasyarat-user)
   - [Langkah 3: Membuat Request Register](#langkah-3-membuat-request-register)
   - [Langkah 4: Membuat Request Login](#langkah-4-membuat-request-login)
   - [Langkah 5: Menguji Protected Endpoint Menggunakan Token](#langkah-5-menguji-protected-endpoint-menggunakan-token)
   - [Langkah 6: Pengujian Skenario Gagal (Negative Testing)](#langkah-6-pengujian-skenario-gagal-negative-testing)
4. [Tabel Referensi Endpoint](#4-tabel-referensi-endpoint)
5. [Tips & Troubleshooting](#5-tips--troubleshooting)

---

## 1. Prasyarat Sebelum Pengujian

1. **Jalankan Database MySQL**:
   Pastikan service MySQL aktif dan database `suratcore-db` telah dibuat sesuai konfigurasi di `application.properties`:
   ```bash
   mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS \`suratcore-db\`;"
   ```

2. **Jalankan Aplikasi Spring Boot**:
   Jalankan project melalui terminal atau IDE (VS Code):
   ```bash
   ./mvnw spring-boot:run
   ```
   Pastikan aplikasi berjalan di port `8080` (default: `http://localhost:8080`).

---

## 2. Metode 1: Menggunakan File Import Otomatis (Cepat)

File collection siap pakai sudah tersedia di root project: **[`postman_collection.json`](./postman_collection.json)**.

### Langkah Import:
1. Buka aplikasi **Postman**.
2. Klik tombol **Import** di pojok kiri atas.
3. Drag & drop atau pilih file `postman_collection.json` dari direktori project.
4. Klik **Import**.
5. Collection bernama **`Suratcore API - Auth & Division`** akan langsung muncul dan siap digunakan.

---

## 3. Metode 2: Membuat Postman Collection Manual (Step-by-Step)

Jika ingin membuat dan mengkonfigurasi collection dari nol, ikuti langkah berikut:

### Langkah 1: Membuat Collection & Environment Variables

1. Klik tombol **New** -> pilih **Collection**.
2. Beri nama collection: `Suratcore API`.
3. Buka tab **Variables** pada collection tersebut dan tambahkan variabel berikut:
   | Variable | Initial Value | Current Value |
   | :--- | :--- | :--- |
   | `baseUrl` | `http://localhost:8080` | `http://localhost:8080` |
   | `token` | *(kosongkan)* | *(kosongkan)* |
4. Klik **Save** (`Ctrl + S`).

---

### Langkah 2: Menyiapkan Data Divisi (Prasyarat User)

Karena entity `User` memiliki foreign key `divisi_id` yang wajib terhubung ke tabel `divisions`, pastikan minimal ada 1 record divisi di database sebelum melakukan registrasi user:

**Query SQL manual (jika database masih kosong):**
```sql
USE `suratcore-db`;
INSERT INTO divisions (kode_divisi, nama_divisi) 
VALUES ('IT', 'Information Technology')
ON DUPLICATE KEY UPDATE nama_divisi='Information Technology';
```
*(Catatan: ID divisi ini nantinya digunakan sebagai `divisiId` pada saat Register).*

---

### Langkah 3: Membuat Request Register

1. Di dalam Collection `Suratcore API`, klik **Add a request** -> beri nama `Register User`.
2. Atur konfigurasi request:
   - **Method**: `POST`
   - **URL**: `{{baseUrl}}/api/auth/register`
3. Masuk ke tab **Headers**:
   - `Content-Type`: `application/json`
4. Masuk ke tab **Body**:
   - Pilih opsi **raw** dan format **JSON**.
   - Masukkan payload JSON berikut:
     ```json
     {
       "nama": "Muhamad Ibrahim Fajri",
       "divisiId": 1,
       "username": "fajri",
       "password": "password123",
       "role": "ADMIN"
     }
     ```
5. Masuk ke tab **Scripts** -> **Post-response** (atau tab **Tests** pada versi Postman lama):
   Tambahkan script otomatis untuk menyimpan token JWT ke variabel collection:
   ```javascript
   // Verifikasi status code 201 Created
   pm.test("Status code is 201 Created", function () {
       pm.response.to.have.status(201);
   });

   // Ambil token dari response dan simpan ke variabel collection
   var responseData = pm.response.json();
   if (responseData.token) {
       pm.collectionVariables.set("token", responseData.token);
       console.log("Token berhasil disimpan ke variabel: token");
   }
   ```
6. Klik **Save** dan tekan tombol **Send**.
7. **Hasil yang Diharapkan**:
   - Status: `201 Created`
   - Response Body:
     ```json
     {
       "token": "eyJhbGciOiJIUzI1NiJ9...",
       "type": "Bearer",
       "id": 1,
       "username": "fajri",
       "nama": "Muhamad Ibrahim Fajri",
       "role": "ADMIN"
     }
     ```
   - Variabel `{{token}}` pada collection akan otomatis terisi dengan token JWT.

---

### Langkah 4: Membuat Request Login

1. Klik **Add a request** pada collection -> beri nama `Login User`.
2. Atur konfigurasi request:
   - **Method**: `POST`
   - **URL**: `{{baseUrl}}/api/auth/login`
3. Masuk ke tab **Headers**:
   - `Content-Type`: `application/json`
4. Masuk ke tab **Body**:
   - Pilih **raw** -> **JSON**.
   - Masukkan payload:
     ```json
     {
       "username": "fajri",
       "password": "password123"
     }
     ```
5. Masuk ke tab **Scripts** -> **Post-response** (atau **Tests**):
   Tambahkan script:
   ```javascript
   // Verifikasi status code 200 OK
   pm.test("Status code is 200 OK", function () {
       pm.response.to.have.status(200);
   });

   // Update variabel token dengan token terbaru hasil login
   var responseData = pm.response.json();
   if (responseData.token) {
       pm.collectionVariables.set("token", responseData.token);
       console.log("Token login berhasil diperbarui di variabel collection");
   }
   ```
6. Klik **Save** dan tekan tombol **Send**.
7. **Hasil yang Diharapkan**:
   - Status: `200 OK`
   - Response Body:
     ```json
     {
       "token": "eyJhbGciOiJIUzI1NiJ9...",
       "type": "Bearer",
       "id": 1,
       "username": "fajri",
       "nama": "Muhamad Ibrahim Fajri",
       "role": "ADMIN"
     }
     ```

---

### Langkah 5: Menguji Protected Endpoint Menggunakan Token

Untuk membuktikan bahwa token JWT bekerja memproteksi endpoint:

1. Buat request baru: `Get All Divisions`.
   - **Method**: `GET`
   - **URL**: `{{baseUrl}}/api/divisions`
2. Buka tab **Authorization**:
   - **Type**: Pilih `Bearer Token`
   - **Token**: Isi dengan `{{token}}`
3. Klik **Send**.
4. **Hasil yang Diharapkan**:
   - Status: `200 OK`
   - Data list divisi berhasil ditampilkan karena request menyertakan token yang valid.

---

### Langkah 6: Pengujian Skenario Gagal (Negative Testing)

#### Skenario A: Login dengan Password Salah
1. Buat request baru: `Login - Wrong Password`.
2. Method: `POST`, URL: `{{baseUrl}}/api/auth/login`.
3. Body JSON:
   ```json
   {
     "username": "fajri",
     "password": "salahpassword"
   }
   ```
4. Klik **Send**.
5. **Hasil yang Diharapkan**: Status `401 Unauthorized` dengan pesan error autentikasi.

#### Skenario B: Akses Protected Endpoint Tanpa Token
1. Buat request baru: `Get Divisions Without Token`.
2. Method: `GET`, URL: `{{baseUrl}}/api/divisions`.
3. Tab **Authorization**: Pilih `No Auth`.
4. Klik **Send**.
5. **Hasil yang Diharapkan**: Status `401 Unauthorized` dengan respon:
   ```json
   {
     "error": "Unauthorized",
     "message": "Full authentication is required to access this resource"
   }
   ```

#### Skenario C: Registrasi dengan Username yang Sudah Terdaftar
1. Jalankan kembali request **Register User** dengan `username: "fajri"`.
2. **Hasil yang Diharapkan**: Status `400 Bad Request` dengan pesan error:
   ```json
   {
     "error": "Bad Request",
     "message": "Username sudah terdaftar"
   }
   ```

---

## 4. Tabel Referensi Endpoint

| No | Nama Request | HTTP Method | Endpoint | Auth Required | Deskripsi |
| :--- | :--- | :--- | :--- | :---: | :--- |
| 1 | Register User | `POST` | `/api/auth/register` | Tidak | Mendaftarkan akun user baru |
| 2 | Login User | `POST` | `/api/auth/login` | Tidak | Memvalidasi credential & mendapat JWT |
| 3 | Get All Divisions | `GET` | `/api/divisions` | **Ya** (Bearer) | Mengambil semua data divisi |
| 4 | Create Division | `POST` | `/api/divisions` | **Ya** (Bearer) | Menambah data divisi |
| 5 | Get Division by ID | `GET` | `/api/divisions/{id}` | **Ya** (Bearer) | Mengambil detail divisi |
| 6 | Update Division | `PUT` | `/api/divisions/{id}` | **Ya** (Bearer) | Memperbarui data divisi |
| 7 | Delete Division | `DELETE` | `/api/divisions/{id}` | **Ya** (Bearer) | Menghapus data divisi |

---

## 5. Tips & Troubleshooting

- **Token Expired**:
  Token JWT disetel memiliki masa berlaku 24 jam (`jwt.expiration-ms=86400000`). Jika expired, cukup kirim ulang request **Login User** untuk memperbarui variabel `{{token}}`.
- **Inherit Auth from Parent**:
  Pada Postman, Anda dapat menyetel tab **Authorization** pada root Collection ke `Bearer Token: {{token}}`, lalu pada tiap protected request cukup pilih `Inherit auth from parent` agar tidak perlu mengatur token satu per satu.
- **Divisi ID Tidak Ditemukan saat Register**:
  Jika muncul pesan `Divisi dengan ID x tidak ditemukan`, pastikan ID divisi yang dimasukkan pada payload register sudah ada di tabel `divisions` di database.
