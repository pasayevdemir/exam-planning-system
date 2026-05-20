# SINAV PLANLAMA SİSTEMİ — PROJE RAPORU

---

## 1. KAPAK SAYFASI

**T.C.**
**ÜNİVERSİTE ADI**
**MÜHENDİSLİK FAKÜLTESİ**
**BİLGİSAYAR MÜHENDİSLİĞİ BÖLÜMÜ**

**DERS:** BIL513 — Web Programlama
**PROJE ADI:** Sınav Planlama Sistemi (Exam Planning System)
**ÖĞRENCİ:** Demir Paşayev

---

## 2. ÖZET

Bu proje, üniversitelerde sınav dönemlerinde yaşanan **yoğun planlama yükünü otomatikleştirmek** amacıyla geliştirilmiş tam yığın (full-stack) bir web uygulamasıdır. Sistem; öğrencilerin sınıflara dağıtılması, koltuk numaralarının atanması, gözetmen (invigilator) görevlendirmesi, çakışma tespiti ve PDF raporu üretimi gibi işlemleri tek bir yönetici panelinden gerçekleştirebilmektedir.

Arka uç (backend) Spring Boot 3.4.2 ve Java 21 üzerine inşa edilmiş; kimlik doğrulama için JWT tabanlı Spring Security, kalıcılık için Spring Data JPA + MySQL kullanılmıştır. Ön uç (frontend) ise herhangi bir çatı (framework) kullanılmadan, **saf ES6 modülleri** ile hash tabanlı tek sayfa uygulama (SPA) olarak yazılmıştır. PDF üretimi için jsPDF, Excel/CSV içe aktarımı için Apache POI ve OpenCSV kütüphaneleri tercih edilmiştir.

Sistem; 11 ana varlık (entity), 12 REST denetleyici (controller) ve 14 ön uç görünümü ile gerçekleştirilmiş; planlama algoritması "en büyük sınıf önce" (largest-room-first) yaklaşımıyla optimum koltuk dağılımı sağlamaktadır.

---

## 3. GİRİŞ

Üniversitelerde her dönem sonunda yüzlerce ders için sınav planı hazırlanmaktadır. Bu süreç geleneksel olarak Excel tabloları üzerinde manuel yürütüldüğünden; **insan kaynaklı hatalara**, **çakışmalara** ve **zaman kaybına** neden olmaktadır. Aynı öğrencinin iki sınavının aynı saate denk gelmesi, bir gözetmenin iki sınıfta birden görevlendirilmesi ya da sınıf kapasitesinin aşılması gibi durumlar sık karşılaşılan problemlerdir.

Bu proje, söz konusu problemleri çözmek için **web tabanlı bir yönetim platformu** sunmaktadır. Yönetici kullanıcı tarayıcı üzerinden sisteme giriş yapar; fakülte, bölüm, ders, sınıf, öğrenci ve öğretim elemanı bilgilerini yönetir; ardından bir sınav oluşturup tek tuşla planlama motorunu çalıştırarak otomatik koltuk ve gözetmen ataması elde eder. Öğrenciler ise giriş yapmadan, yalnızca öğrenci numaraları ile sınav yerlerini sorgulayabilirler.

Raporun ilerleyen bölümlerinde sistemin problem tanımı, mimari yapısı, kullanılan teknolojiler, algoritmalar ve testleri ayrıntılı olarak ele alınacaktır.

---

## 4. PROBLEM TANIMI

Sınav planlamasının manuel olarak yapıldığı kurumlarda aşağıdaki problemler gözlemlenmektedir:

| # | Problem | Sonuç |
|---|---------|-------|
| 1 | Aynı öğrencinin birden fazla sınavının aynı tarih/saate denk gelmesi | Öğrenci mağduriyeti |
| 2 | Bir gözetmenin aynı anda iki farklı sınıfta görevlendirilmesi | Sınavın geçersiz sayılması |
| 3 | Sınıf kapasitesinin aşılması | Koltuk yetersizliği |
| 4 | Gözetmen iş yükünün dengesiz dağılması | Akademisyenler arası adaletsizlik |
| 5 | Öğrencilerin sınav yerlerine ulaşmasının zorluğu | Bilgi iletişiminde aksamalar |
| 6 | Listelerin manuel olarak Word/Excel'de hazırlanması | Hata ve zaman kaybı |

**Çözüm hedefleri:**

1. Sınav, öğrenci, sınıf ve öğretim elemanı bilgilerinin merkezi olarak yönetilmesi
2. **Otomatik koltuk dağıtım algoritması** ile sınıf kapasitelerinin optimum kullanımı
3. **Gözetmen yük dengeleme** ile görevlendirme adaleti
4. **Çakışma tespit modülü** ile potansiyel hataların önceden saptanması
5. **Genel erişimli öğrenci sorgu sayfası** ile bilgi paylaşımının kolaylaştırılması
6. **PDF rapor üretimi** ile yazılı çıktı ihtiyacının karşılanması

---

## 5. DERS KAZANIMLARI İLE PROJE İLİŞKİSİ

BIL513 Web Programlama dersinin temel kazanımları ve bu projedeki karşılıkları:

| Kazanım | Proje Karşılığı |
|---------|-----------------|
| HTTP/HTTPS protokollerini kavramak | REST API üzerinden istemci–sunucu haberleşmesi |
| HTML/CSS/JavaScript ile dinamik arayüz geliştirmek | 14 ES6 sınıf tabanlı görünüm, hash router |
| Sunucu tarafı programlama | Spring Boot + Java 21, 12 controller, 12 service |
| Veritabanı tasarımı ve ORM kullanımı | 11 JPA entity, MySQL, Hibernate |
| AJAX ile asenkron veri alışverişi | `fetch` API ile JSON tabanlı iletişim |
| Web güvenliği temelleri | JWT + Spring Security, BCrypt, CORS, token blacklist |
| Modüler tasarım ve MVC mimarisi | Katmanlı mimari: Controller → Service → Repository → Entity |
| Web servisleri (REST) | SpringDoc OpenAPI 3 / Swagger UI ile belgelenen 60+ endpoint |
| Versiyon kontrol ve takım çalışması | Git, conventional commits, feature branch iş akışı |

---

## 6. KULLANILAN TEKNOLOJİLER

### 6.1 Arka Uç (Backend)
- **Java 21** — uzun süreli destekli (LTS) sürüm
- **Spring Boot 3.4.2** — başlatıcı (starter) bağımlılıklar, otomatik yapılandırma
- **Spring Security + JWT (JJWT 0.11.5)** — kimlik doğrulama ve yetkilendirme
- **Spring Data JPA / Hibernate** — nesne-ilişkisel eşleme
- **MySQL 8.x** — ilişkisel veritabanı
- **HikariCP** — bağlantı havuzu
- **Apache POI 5.2.3** — Excel okuma (`.xls`, `.xlsx`)
- **OpenCSV 5.7.1** — CSV içe aktarma
- **SpringDoc OpenAPI 2.8.0** — Swagger UI ile API dokümantasyonu
- **Gradle 8** — derleme aracı

### 6.2 Ön Uç (Frontend)
- **Vanilla JavaScript (ES6 Modülleri)** — herhangi bir çatı kullanılmadan
- **Hash tabanlı SPA yönlendirici (router)**
- **jsPDF 2.5.1 + jsPDF-AutoTable 3.8.2** — istemci tarafı PDF üretimi
- **Times New Roman TTF (gömülü font)** — Türkçe karakter desteği

### 6.3 Geliştirme Araçları
- **IntelliJ IDEA / VS Code**
- **Git + GitHub**
- **Postman** — API testleri
- **MySQL Workbench**

---

## 7. İNTERNETİN TEMEL KAVRAMLARI

### 7.1 TCP/IP
İnternet üzerindeki tüm iletişimin temelini oluşturan dört katmanlı protokol yığınıdır:
- **Bağlantı (Link)** — Ethernet, Wi-Fi
- **İnternet (IP)** — paket yönlendirmesi (IPv4/IPv6)
- **Taşıma (Transport)** — güvenilir teslimat için **TCP**, hızlı/bağlantısız için UDP
- **Uygulama (Application)** — HTTP, DNS, FTP vb.

Projemizde istemci ve sunucu arasındaki tüm HTTP istekleri TCP üzerinden taşınmakta; yerel ortamda 8081 portuna bağlanılmaktadır.

### 7.2 HTTP
**HyperText Transfer Protocol** — istemci-sunucu mimarisinde istek/yanıt tabanlı, durumsuz (stateless) bir protokoldür. Projemizde:
- `GET /api/admin/students` — öğrencileri listeler
- `POST /api/admin/exams` — yeni sınav oluşturur
- `PUT /api/admin/courses/{id}` — ders günceller
- `DELETE /api/admin/classrooms/{id}` — sınıf siler

Durum kodları (200 OK, 201 Created, 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found, 500 Internal Server Error) tutarlı şekilde döndürülür.

### 7.3 DNS
**Domain Name System** — insanın okuyabildiği alan adlarını (örn. `example.com`) IP adreslerine çevirir. Yerel geliştirmede `localhost` ismi `127.0.0.1` adresine, üretim ortamına alındığında ise hosting sağlayıcısının DNS A kayıtları üzerinden gerçek IP'ye çözülecektir.

### 7.4 URL
**Uniform Resource Locator** — bir kaynağın internet üzerindeki adresidir. Yapısı:

```
http://localhost:8081/api/admin/exams/5?detail=true
└─┬─┘   └────┬──────┘ └──────┬───────┘ └─────┬──────┘
şema     host:port         yol            sorgu
```

Projemizde:
- **Statik kaynaklar:** `http://localhost:8081/index.html`
- **REST kaynakları:** `http://localhost:8081/api/...`
- **Swagger UI:** `http://localhost:8081/swagger-ui.html`

### 7.5 Web
World Wide Web; HTTP protokolü üzerinden HTML, CSS, JavaScript belgelerinin tarayıcılarda görüntülenmesini sağlayan dağıtık bilgi sistemidir. Bu proje web tabanlı bir uygulama olup, kullanıcılar herhangi bir modern tarayıcı (Chrome, Edge, Firefox) üzerinden sisteme erişebilirler.

---

## 8. İSTEMCİ–SUNUCU MİMARİSİ

Sistem klasik **iki katmanlı istemci–sunucu** modeline ek olarak **üç katmanlı (Presentation / Business / Data)** mimariyi de uygular:

```
┌────────────────────────┐       HTTP/JSON       ┌─────────────────────────┐       JDBC      ┌───────────────┐
│   Tarayıcı (İstemci)   │ ───── REST API ─────▶ │  Spring Boot (Sunucu)   │ ───────────────▶│  MySQL 8.x     │
│ • HTML/CSS/JS          │ ◀──── JSON yanıt ──── │ • Controller            │ ◀─────────────  │ • exam_planning│
│ • jsPDF, fetch API     │                       │ • Service               │                 │   şeması       │
│ • SPA Router           │       JWT Bearer      │ • Repository / JPA      │                 │                │
└────────────────────────┘                       └─────────────────────────┘                 └───────────────┘
       (Presentation)                                  (Business Logic)                          (Data Layer)
```

- **Sunum katmanı:** Tarayıcıda çalışan SPA — kullanıcı etkileşimi, form doğrulama, görüntüleme.
- **İş katmanı:** Spring Boot — planlama algoritması, yetki kontrolü, doğrulama, iş kuralları.
- **Veri katmanı:** MySQL — kalıcı depolama; JPA/Hibernate ORM ile soyutlanmıştır.

İstemci sunucudan tamamen ayrıdır; yalnızca **RESTful API** üzerinden konuşur, böylece ileride mobil bir istemci eklemek mümkündür.

---

## 9. SİSTEM GEREKSİNİMLERİ

### 9.1 Fonksiyonel Gereksinimler

| Kod | Gereksinim |
|-----|------------|
| FR-01 | Yönetici kullanıcı sisteme JWT ile giriş yapabilmelidir |
| FR-02 | Yönetici fakülte, bölüm, ders, sınıf CRUD işlemleri yapabilmelidir |
| FR-03 | Yönetici öğrencileri tek tek ekleyebilmeli veya CSV/Excel ile toplu içe aktarabilmelidir |
| FR-04 | Yönetici öğretim elemanlarını CRUD yapabilmelidir |
| FR-05 | Yönetici sınav oluşturup öğrencileri sınava atayabilmelidir |
| FR-06 | Sistem otomatik olarak öğrencileri sınıflara dağıtmalı ve koltuk numarası vermelidir |
| FR-07 | Sistem her sınıfa öğrenci sayısına göre 1–3 gözetmen atamalıdır |
| FR-08 | Sistem öğrenci ve gözetmen çakışmalarını tespit etmelidir |
| FR-09 | Sistem PDF formatında 3 tür rapor üretebilmelidir (sınıf bazlı, gözetmen görev, gözetmen iş yükü) |
| FR-10 | Öğrenci kayıt olmadan sınav bilgilerini sorgulayabilmelidir |
| FR-11 | Öğretim elemanı kendi gözetmenlik görevlerini görüntüleyebilmelidir |
| FR-12 | Yönetici planı önizleme (dry-run) modunda görebilmelidir |
| FR-13 | Yönetici bir sınavın planını sıfırlayıp yeniden çalıştırabilmelidir |

### 9.2 Fonksiyonel Olmayan Gereksinimler

| Kod | Gereksinim |
|-----|------------|
| NFR-01 | **Güvenlik:** Tüm yönetici uç noktaları JWT korumalı olmalıdır; parolalar BCrypt ile hash'lenmelidir |
| NFR-02 | **Performans:** 1.000 öğrenciye kadar plan üretimi 2 saniyenin altında tamamlanmalıdır |
| NFR-03 | **Kullanılabilirlik:** Arayüz Türkçe karakterleri tam desteklemeli, mobil uyumlu olmalıdır |
| NFR-04 | **Taşınabilirlik:** JAR çıktısı tek başına çalıştırılabilmelidir |
| NFR-05 | **Sürdürülebilirlik:** Katmanlı mimari ile kod bakımı kolay olmalıdır |
| NFR-06 | **Belgelenebilirlik:** API uç noktaları Swagger UI ile interaktif olarak belgelenmelidir |
| NFR-07 | **Genişletilebilirlik:** Yeni varlık eklemek mevcut katmanları bozmamalıdır |

---

## 10. SİSTEM MİMARİSİ

Sistem **çok katmanlı (layered) mimari** üzerine kurulmuştur:

```
┌──────────────────────────────────────────────────────────┐
│            PRESENTATION LAYER (Frontend)                 │
│  index.html → app.js (Router) → 14 × View.js             │
│                                                          │
│  • LoginView, DashboardView, ExamPlanningView            │
│  • StudentView, InstructorView, ExamView                 │
│  • ReportsView, ConflictsView, StudentQueryView ...      │
└──────────────────┬───────────────────────────────────────┘
                   │ HTTP + JSON (fetch API, JWT header)
┌──────────────────▼───────────────────────────────────────┐
│              CONTROLLER LAYER (REST)                     │
│  @RestController sınıfları — istek/yanıt yönetimi        │
│  AuthController, ExamPlanningController, ...             │
└──────────────────┬───────────────────────────────────────┘
                   │
┌──────────────────▼───────────────────────────────────────┐
│              SERVICE LAYER (Business Logic)              │
│  @Service sınıfları — iş kuralları, planlama algoritması │
│  ExamPlanningService ← çekirdek planlama motoru          │
└──────────────────┬───────────────────────────────────────┘
                   │
┌──────────────────▼───────────────────────────────────────┐
│             REPOSITORY LAYER (Spring Data JPA)           │
│  JpaRepository genişletmeleri                            │
└──────────────────┬───────────────────────────────────────┘
                   │ JDBC
┌──────────────────▼───────────────────────────────────────┐
│              DATA LAYER (MySQL)                          │
│  exam_planning şeması, 11 tablo                          │
└──────────────────────────────────────────────────────────┘

Kesişen (Cross-cutting) Bileşenler:
─ Spring Security + JwtAuthenticationFilter
─ GlobalExceptionHandler
─ CORS yapılandırması
─ TokenBlacklistService (logout)
```

---

## 11. VERİTABANI TASARIMI

### 11.1 Tablolar (Varlıklar)

| Tablo | Açıklama | Anahtar Alanlar |
|-------|----------|-----------------|
| `users` | Sistem kullanıcıları | id, username, password, role |
| `faculties` | Fakülteler | id, name |
| `departments` | Bölümler | id, name, faculty_id |
| `courses` | Dersler | id, code, name, department_id |
| `classrooms` | Sınıflar | id, name, capacity |
| `students` | Öğrenciler | id, student_no, tc_no, full_name, faculty_id, department_id |
| `instructors` | Öğretim elemanları | id, full_name, email, duty_count |
| `exams` | Sınavlar | id, course_id, exam_date, start_time, end_time, duration |
| `exam_assignments` | Öğrenci-koltuk atamaları | id, exam_id, student_id, classroom_id, seat_no |
| `invigilator_assignments` | Gözetmen atamaları | id, exam_id, classroom_id, instructor_id |

### 11.2 İlişkiler (ER Özeti)

```
Faculty (1) ──< (N) Department
Department (1) ──< (N) Course
Department (1) ──< (N) Student
Course (1) ──< (N) Exam
Exam (1) ──< (N) ExamAssignment >── (1) Student
Exam (1) ──< (N) ExamAssignment >── (1) Classroom
Exam (1) ──< (N) InvigilatorAssignment >── (1) Instructor
Exam (1) ──< (N) InvigilatorAssignment >── (1) Classroom
```

### 11.3 Veritabanı Yaratım Stratejisi
- Hibernate `ddl-auto=update` ile şema otomatik üretilir.
- Yabancı anahtar kısıtları (`@ManyToOne`, `@OneToMany`) JPA anotasyonları ile tanımlanmıştır.
- Tek alan benzersizliği (örn. `studentNo`, `tcNo`) `@Column(unique = true)` ile sağlanmıştır.

---

## 12. UML DİYAGRAMLARI

### 12.1 Kullanım Senaryosu (Use Case) Diyagramı

```
              ┌─────────────────── SINAV PLANLAMA SİSTEMİ ────────────────────┐
              │                                                               │
              │   ( Giriş Yap )                                               │
              │   ( CRUD: Fakülte / Bölüm / Ders / Sınıf )                    │
              │   ( CRUD: Öğrenci )                                           │
   ┌──────┐   │   ( Toplu Öğrenci İçe Aktar )                                 │
   │ADMIN ├───┤   ( CRUD: Öğretim Elemanı )                                   │
   └──────┘   │   ( Sınav Oluştur )                                           │
              │   ( Sınava Öğrenci Ata )                                      │
              │   ( Planı Çalıştır / Önizle / Sıfırla )                       │
              │   ( Çakışmaları Görüntüle )                                   │
              │   ( PDF Rapor İndir )                                         │
              │                                                               │
   ┌─────────┐│   ( Görevlerini Görüntüle )                                   │
   │HOCA     ├┤                                                               │
   └─────────┘│                                                               │
              │                                                               │
   ┌─────────┐│   ( Sınav Yerini Sorgula )      ← giriş gerektirmez           │
   │ÖĞRENCİ  ├┤                                                               │
   └─────────┘│                                                               │
              └───────────────────────────────────────────────────────────────┘
```

### 12.2 Sınıf Diyagramı (Sadeleştirilmiş)

```
┌────────────────┐         ┌────────────────┐
│   Faculty      │ 1     N │   Department   │
├────────────────┤────────▶├────────────────┤
│ -id            │         │ -id            │
│ -name          │         │ -name          │
└────────────────┘         │ -faculty: F    │
                           └────────┬───────┘
                                    │ 1
                                    │
                                    ▼ N
┌────────────────┐         ┌────────────────┐
│    Student     │N      1 │    Course      │
├────────────────┤◀────────┤────────────────┤
│ -id            │         │ -id, -code     │
│ -studentNo     │         │ -name          │
│ -tcNo          │         │ -department: D │
│ -fullName      │         └────────┬───────┘
│ -dept: D       │                  │ 1
└───────┬────────┘                  ▼ N
        │                  ┌────────────────┐
        │ N                │     Exam       │
        ▼                  ├────────────────┤
┌────────────────┐         │ -id            │
│ ExamAssignment │N      1 │ -examDate      │
├────────────────┤────────▶│ -startTime     │
│ -id            │         │ -course: C     │
│ -student: S    │         └────────┬───────┘
│ -exam: E       │                  │ 1
│ -classroom: Cr │                  │
│ -seatNo        │                  │ N
└────────────────┘                  ▼
                           ┌────────────────────────┐
┌────────────────┐         │ InvigilatorAssignment  │
│   Classroom    │1      N │────────────────────────│
├────────────────┤◀────────┤ -id                    │
│ -id, -name     │         │ -exam: E               │
│ -capacity      │         │ -classroom: Cr         │
└────────────────┘         │ -instructor: I         │
                           └───────────┬────────────┘
                                       │ N
                                       ▼ 1
                              ┌────────────────┐
                              │   Instructor   │
                              ├────────────────┤
                              │ -id, -fullName │
                              │ -email         │
                              │ -dutyCount     │
                              └────────────────┘
```

### 12.3 Veri Akış Diyagramı (DFD — Seviye 1, Plan Üretimi)

```
                ┌─────────────┐
                │  YÖNETİCİ   │
                └──────┬──────┘
                       │ 1. Plan İste (examId)
                       ▼
   ┌───────────────────────────────────────┐
   │   1.0  Sınavı Doğrula                 │◀──── Exam tablosu
   └───────────────────┬───────────────────┘
                       │ exam verisi
                       ▼
   ┌───────────────────────────────────────┐
   │   2.0  Öğrencileri Getir              │◀──── ExamAssignment (boş koltuk)
   └───────────────────┬───────────────────┘
                       │ öğrenci listesi
                       ▼
   ┌───────────────────────────────────────┐
   │   3.0  Sınıfları Sırala               │◀──── Classroom (capacity DESC)
   └───────────────────┬───────────────────┘
                       │ sıralı sınıflar
                       ▼
   ┌───────────────────────────────────────┐
   │   4.0  Koltuk Ata (algoritma)         │
   └───────────────────┬───────────────────┘
                       │ atamalar
                       ▼
   ┌───────────────────────────────────────┐
   │   5.0  Gözetmen Ata (yük dengeli)     │◀──── Instructor (dutyCount ASC)
   └───────────────────┬───────────────────┘
                       │
                       ▼
   ┌───────────────────────────────────────┐
   │   6.0  Veritabanına Yaz               │────▶ ExamAssignment, InvigilatorAssignment
   └───────────────────┬───────────────────┘
                       │ JSON yanıt
                       ▼
                 ┌─────────────┐
                 │  YÖNETİCİ   │
                 └─────────────┘
```

---

## 13. MODÜL AÇIKLAMALARI

| Modül | Sorumluluk |
|-------|------------|
| **AuthModule** (`AuthController`, `UserService`, `JwtService`) | Kullanıcı kaydı, JWT üretimi, oturum kapatma, token kara liste |
| **PlanningModule** (`ExamPlanningService`) | Otomatik koltuk dağıtımı, gözetmen ataması, dry-run, reset |
| **ExamModule** (`ExamController`, `ExamService`) | Sınav CRUD, öğrenci atama/çıkarma |
| **StudentModule** (`StudentController`, `StudentService`) | Öğrenci CRUD, CSV/Excel toplu içe aktarma |
| **InstructorModule** | Öğretim elemanı CRUD ve görev sayacı |
| **ReferenceDataModule** | Faculty, Department, Course, Classroom CRUD |
| **ConflictModule** | Aynı tarih/saatte çakışan öğrenci ve gözetmenleri raporlar |
| **PublicQueryModule** (`QueryController`) | Girişsiz öğrenci sorgu ve hoca görev görüntüleme |
| **ReportingModule** (Frontend `PdfGenerator`) | jsPDF + Times New Roman ile 3 farklı PDF rapor |
| **SecurityModule** (`SecurityConfig`, `JwtAuthenticationFilter`) | URL bazlı yetki, JWT filtreleme, CORS |
| **ExceptionModule** (`GlobalExceptionHandler`) | Tek noktadan hata yakalama ve standart JSON yanıt |

---

## 14. ALGORİTMA AÇIKLAMALARI

### 14.1 Otomatik Koltuk Dağıtım Algoritması ("Largest-Room-First")

```
GİRDİ:  examId
ÇIKTI:  ExamAssignment listesi (öğrenci → sınıf, koltuk no)

1.  exam ← Sınavı veritabanından getir
2.  ogrenciler ← exam.assignments (sınıfı henüz atanmamış olanlar)
3.  siniflar ← Tüm sınıfları kapasiteye göre AZALAN sırala
4.  ogrenciler ← Soyadına göre alfabetik sırala (adil dağılım)
5.  ogrenciSayisi ← ogrenciler.size()
6.  EĞER toplam_kapasite < ogrenciSayisi İSE
        HATA fırlat ("Yetersiz sınıf kapasitesi")

7.  i ← 0
8.  her sinif İÇİN siniflar listesinde:
        EĞER i ≥ ogrenciSayisi İSE  dur
        kalan ← ogrenciSayisi - i
        kullanilan ← min(sinif.capacity, kalan)
        koltukNo ← 1
        her j İÇİN 0..kullanilan-1:
            atama ← ogrenciler[i + j]
            atama.classroom ← sinif
            atama.seatNo ← koltukNo++
        i ← i + kullanilan
9.  atamaları veritabanına kaydet
```

**Karmaşıklık:** O(N log N) — sıralama baskındır, N = öğrenci sayısı.

### 14.2 Gözetmen Atama Algoritması ("Load-Balanced")

```
GİRDİ:  examId
ÇIKTI:  InvigilatorAssignment listesi

1.  her sinif İÇİN sınava ait kullanılan sınıflar:
        kisi_sayisi ← o sınıftaki öğrenci sayısı
        EĞER kisi_sayisi ≤ 50  ise gerekli ← 1
        ELSE kisi_sayisi ≤ 100 ise gerekli ← 2
        ELSE                       gerekli ← 3

        hocalar ← Tüm hocaları dutyCount ARTAN sırala
        secilenler ← hocalar.take(gerekli)
        her h İÇİN secilenler:
            InvigilatorAssignment(exam, sinif, h) oluştur
            h.dutyCount ← h.dutyCount + 1
```

### 14.3 Çakışma Tespit Algoritması

```
1.  Tüm ExamAssignment kayıtlarını öğrenciye göre grupla
2.  Her öğrencinin sınavlarını (examDate, startTime) göre kontrol et
3.  Aynı tarih + zaman aralığı çakışıyorsa: ConflictDTO listesine ekle
4.  Aynı işlemi InvigilatorAssignment için tekrarla (hoca bazlı)
5.  Sonucu yöneticiye JSON olarak döndür
```

### 14.4 BCrypt Parola Hash'leme

Spring Security `BCryptPasswordEncoder` ile saltlı (salted) hash üretilir. Aynı parola her seferinde farklı hash üretir — gökkuşağı tablosu (rainbow table) saldırılarına karşı dirençlidir.

---

## 15. ARAYÜZ EKRAN GÖRÜNTÜLERİ

> Görseller `docs/assets/` dizinine konulmuştur. Aşağıdaki referanslar PDF teslim sırasında ekran görüntüleri ile değiştirilecektir.

| # | Ekran | Açıklama |
|---|-------|----------|
| 1 | Giriş Ekranı | Kullanıcı adı / parola + "Beni hatırla" |
| 2 | Dashboard | Toplam öğrenci, sınav, sınıf sayaçları |
| 3 | Sınav Planlama | Sınav seç, öğrencileri seç, "Önizle" / "Çalıştır" |
| 4 | Öğrenciler | DataTable + CRUD + Toplu içe aktar |
| 5 | İçe Aktar Modal | CSV/Excel dosya seçimi, sonuç özeti |
| 6 | Sınavlar | Sınav listesi, öğrenci sayısı rozeti |
| 7 | Sınıflar | CRUD + kapasite alanı |
| 8 | Çakışmalar | Öğrenci & hoca çakışmaları sekmeleri |
| 9 | Raporlar | Sınav seçimli 3 PDF butonu |
| 10 | Öğrenci Sorgu | Girişsiz arama formu, sonuç kartı |
| 11 | Hoca Görevleri | Kişisel görev tablosu |

---

## 16. AJAX VE WEB SERVİS KULLANIMI

### 16.1 AJAX (fetch API)

Frontend, sayfayı yeniden yüklemeden sunucuyla haberleşmek için **`fetch`** API'sini kullanır. Tipik bir çağrı:

```javascript
// src/main/resources/static/js/views/StudentView.js (örnek)
async loadStudents() {
    const response = await fetch('/api/admin/students', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok) throw new Error('Veri alınamadı');
    const data = await response.json();
    this.render(data);
}
```

- **GET** — listeleme
- **POST** — yeni kayıt + dosya yükleme (`multipart/form-data`)
- **PUT** — güncelleme
- **DELETE** — silme

### 16.2 Web Servisleri (REST)

Sunucu tarafında her varlık için `@RestController` ile RESTful uç noktalar sunulmuştur. Toplam 60+ uç nokta Swagger UI'da etkileşimli olarak belgelenmiştir:

```
GET    /api/admin/students          → tüm öğrenciler
GET    /api/admin/students/{id}     → tek öğrenci
POST   /api/admin/students          → yeni öğrenci
PUT    /api/admin/students/{id}     → güncelle
DELETE /api/admin/students/{id}     → sil
POST   /api/admin/students/import   → CSV/Excel içe aktar
```

### 16.3 OpenAPI / Swagger UI

`http://localhost:8081/swagger-ui.html` adresinde tüm uç noktalar; istek/yanıt şemaları, örnek JSON gövdeleri ve "Try it out" özelliği ile interaktif olarak test edilebilir.

---

## 17. XML / JSON VERİ YAPISI

Sistem, istemci–sunucu haberleşmesinde **JSON** formatını kullanır (modern, hafif, JavaScript ile uyumlu).

### 17.1 JSON İstek Örneği — Yeni Sınav Oluşturma

```json
POST /api/admin/exams
{
  "courseId": 12,
  "examDate": "2026-06-15",
  "startTime": "10:00",
  "endTime": "12:00",
  "duration": 120
}
```

### 17.2 JSON Yanıt Örneği — Plan Sonucu

```json
{
  "examId": 5,
  "totalStudents": 87,
  "classroomsUsed": 2,
  "invigilatorsAssigned": 4,
  "assignments": [
    { "studentNo": "2023001", "fullName": "Ali Veli",
      "classroom": "A-101", "seatNo": 1 },
    { "studentNo": "2023002", "fullName": "Ayşe Kaya",
      "classroom": "A-101", "seatNo": 2 }
  ],
  "invigilators": [
    { "classroom": "A-101", "instructor": "Dr. Ahmet Yılmaz" },
    { "classroom": "A-101", "instructor": "Dr. Mehmet Demir" }
  ]
}
```

### 17.3 XML Karşılaştırması

Aynı veri XML formatında daha hantal olur ve istemci tarafında ek ayrıştırıcı (parser) gerektirir. Bu yüzden tercih edilmemiştir. Ancak Spring Boot, `Accept: application/xml` başlığı ile XML yanıtı da otomatik üretebilmektedir.

---

## 18. GÜVENLİK ÖNLEMLERİ

| Önlem | Uygulama |
|-------|----------|
| **Kimlik doğrulama** | Spring Security + **JWT** (HS256, 32 bayt gizli anahtar) |
| **Parola güvenliği** | **BCrypt** ile saltlı hash; düz metin saklanmaz |
| **Yetkilendirme** | `@PreAuthorize` ve URL bazlı `hasRole('ADMIN')` kontrolleri |
| **Token süresi** | 24 saat geçerlilik; süresi dolan token reddedilir |
| **Çıkış (logout)** | `TokenBlacklistService` ile token kara listeye alınır |
| **CORS** | Yapılandırılmış kaynak listesi; isteğe bağlı origin'ler reddedilir |
| **SQL Injection** | Tüm sorgular JPA/Hibernate parametreli sorgu — string birleştirme yok |
| **XSS** | Frontend `textContent` kullanır, `innerHTML` ile kullanıcı verisi enjekte etmez |
| **CSRF** | Durumsuz JWT API olduğundan devre dışı; her istek `Authorization` başlığı taşır |
| **Dosya yükleme** | MIME ve uzantı doğrulaması (`.csv`, `.xls`, `.xlsx`), satır limiti |
| **Hata yönetimi** | `GlobalExceptionHandler` ile stack trace istemciye sızdırılmaz |
| **HTTPS önerisi** | Üretimde TLS ile şifrelenmiş bağlantı zorunlu |

---

## 19. TEST SENARYOLARI

### 19.1 Birim Test Örnekleri

| # | Test Senaryosu | Beklenen Sonuç |
|---|----------------|----------------|
| T-01 | Geçerli kullanıcı adı/parola ile giriş | 200 OK + JWT döner |
| T-02 | Yanlış parola ile giriş | 401 Unauthorized |
| T-03 | Token'sız `/api/admin/students` çağrısı | 401 Unauthorized |
| T-04 | USER rolü ile `/api/admin/users` çağrısı | 403 Forbidden |
| T-05 | Aynı `studentNo` ile ikinci öğrenci ekleme | 400 Bad Request (unique constraint) |
| T-06 | Boş CSV dosyası yükleme | 400 + "Dosya boş" mesajı |
| T-07 | 5 hatalı satır içeren CSV | 200 + satır bazlı hata raporu |
| T-08 | Toplam kapasiteden fazla öğrenci ile plan üretimi | Hata: "Yetersiz kapasite" |
| T-09 | 30 öğrencili sınava 30 koltuk ataması | Tam 30 atama, hepsi geçerli |
| T-10 | 75 öğrencili sınıfta gözetmen sayısı | 2 gözetmen atanır |
| T-11 | 120 öğrencili sınıfta gözetmen sayısı | 3 gözetmen atanır |
| T-12 | Aynı saatte iki sınava atanmış öğrenci | Çakışma raporunda görünür |
| T-13 | Dry-run modu | Veritabanı değişmez, sonuç JSON döner |
| T-14 | Plan reset | Mevcut atamalar silinir |
| T-15 | Öğrenci numarası ile sorgu (girişsiz) | Sınav listesi döner |
| T-16 | Geçersiz öğrenci numarası ile sorgu | 404 Not Found |
| T-17 | PDF rapor indirme — Türkçe karakter | Ç, Ğ, İ, Ö, Ş, Ü doğru görüntülenir |
| T-18 | Logout sonrası eski token kullanımı | 401 (kara listede) |

### 19.2 Test Yöntemleri
- **Manuel testler:** Postman koleksiyonu + tarayıcı tabanlı kullanıcı kabul testleri
- **Otomatik testler:** JUnit 5 + Spring Boot Test (servis katmanı)
- **API testleri:** Swagger UI üzerinden interaktif

---

## 20. SONUÇ VE DEĞERLENDİRME

Bu proje kapsamında, **üniversitelerde sınav planlamasının manuel olarak yürütülmesinden kaynaklanan hataları ve zaman kaybını ortadan kaldıran** bir web uygulaması başarıyla geliştirilmiştir. Sistem:

- 11 varlık ve 60+ REST uç noktası ile **bütünsel bir yönetim platformu** sunmaktadır.
- "Largest-Room-First" koltuk dağıtım algoritması ve "Load-Balanced" gözetmen ataması ile **adil ve optimum** plan üretmektedir.
- JWT + BCrypt + GlobalExceptionHandler katmanlarıyla **güvenlik açısından kurumsal seviyede** korunmaktadır.
- Çatı kullanılmayan, saf ES6 modüllü frontend sayesinde **bağımlılıkları minimumda** tutulmuştur.
- jsPDF + gömülü Times New Roman font ile **Türkçe karakter destekli PDF raporları** üretilmektedir.

**Gelecek geliştirmeler:**
1. Çok dönemli (multi-semester) plan arşivi
2. E-posta ile öğrenciye otomatik sınav bildirimi
3. Mobil uygulama (REST API hâlihazırda hazır)
4. Yapay zekâ destekli akıllı çakışma çözücü
5. LDAP entegrasyonu ile kurum hesaplarıyla giriş
6. Docker konteynerleştirme ve CI/CD boru hattı

Sonuç olarak, proje BIL513 dersinin tüm kazanımlarını (HTTP, REST, AJAX, veritabanı, güvenlik, modüler tasarım) kapsayan, gerçek bir kurumsal ihtiyaca cevap veren tam yığın bir uygulama olarak teslim edilmektedir.

---

## 21. KAYNAKÇA

1. Spring Boot Resmi Belgeleri — https://docs.spring.io/spring-boot/docs/3.4.2/reference/htmlsingle/
2. Spring Security Reference — https://docs.spring.io/spring-security/reference/
3. JJWT (Java JWT) — https://github.com/jwtk/jjwt
4. MySQL 8.0 Reference Manual — https://dev.mysql.com/doc/refman/8.0/en/
5. Apache POI User API — https://poi.apache.org/components/spreadsheet/
6. OpenCSV Documentation — https://opencsv.sourceforge.net/
7. jsPDF Documentation — https://artskydj.github.io/jsPDF/docs/
8. MDN Web Docs — Fetch API — https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API
9. RFC 7519 — JSON Web Token (JWT) — https://datatracker.ietf.org/doc/html/rfc7519
10. RFC 2616 — Hypertext Transfer Protocol — HTTP/1.1
11. Fowler, M. *Patterns of Enterprise Application Architecture*, Addison-Wesley, 2002.
12. Gamma, E. vd. *Design Patterns: Elements of Reusable Object-Oriented Software*, 1994.
13. OWASP Top 10 — https://owasp.org/www-project-top-ten/
14. SpringDoc OpenAPI — https://springdoc.org/

---

## 22. EKLER

### Ek-A: Çalıştırma Talimatları (Kısa)
```bash
git clone https://github.com/pasayevdemir/exam-planning-system.git
cd exam-planning-system
# application.properties içinde DB bilgilerini güncelle
./gradlew bootRun
# Tarayıcı: http://localhost:8081
```

### Ek-B: Örnek CSV Dosyası
```
studentNo,tcNo,fullName,facultyId,departmentId
2023001,12345678901,Ali Veli,1,3
2023002,98765432100,Ayşe Kaya,1,3
2023003,11122233344,Mehmet Can,1,3
```

### Ek-C: Proje Dizin Yapısı
```
exam-planning-system/
├── src/main/java/com/malik/examplanningsystem/
│   ├── config/        (Spring Security, JWT)
│   ├── controller/    (12 REST controller)
│   ├── dto/           (Request/Response DTO sınıfları)
│   ├── entity/        (11 JPA entity)
│   ├── exception/     (GlobalExceptionHandler)
│   ├── repository/    (Spring Data JPA repository)
│   └── service/       (12 servis sınıfı)
├── src/main/resources/
│   ├── static/        (HTML + CSS + JS frontend)
│   └── application.properties
├── docs/              (API, swagger ve bu rapor)
├── build.gradle
└── README.md
```

### Ek-D: API Hızlı Referansı
| Grup | Temel yol |
|------|-----------|
| Auth | `/api/auth` |
| Sınav Planlama | `/api/admin/exam-planning` |
| Sınavlar | `/api/admin/exams` |
| Öğrenciler | `/api/admin/students` |
| Öğretim Elemanları | `/api/admin/instructors` |
| Sınıflar | `/api/admin/classrooms` |
| Dersler | `/api/admin/courses` |
| Bölümler | `/api/admin/departments` |
| Fakülteler | `/api/admin/faculties` |
| Sorgu (girişsiz) | `/api/student/query/**`, `/api/instructor/duties` |

### Ek-E: GitHub Reposu
**Repo:** https://github.com/pasayevdemir/exam-planning-system.git
**Maintainer:** Damir Pashayev

---

**RAPOR SONU**