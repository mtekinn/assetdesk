# AssetDesk

AssetDesk, şirket içindeki BT ekipmanlarının takibini, kullanıcı yönetimini ve zimmet süreçlerini yönetmek için geliştirilen bir backend uygulamasıdır.

Bu proje; dağınık Excel dosyaları, sözlü takip ve manuel zimmet süreçleri yerine daha düzenli, izlenebilir ve geliştirilebilir bir yapı kurmayı amaçlar.

---

## İçindekiler

- [Proje Amacı](#proje-amacı)
- [Temel Özellikler](#temel-özellikler)
- [Modüller](#modüller)
- [İş Kuralları](#iş-kuralları)
- [Teknoloji Yığını](#teknoloji-yığını)
- [Mimari Yapı](#mimari-yapı)
- [API Dokümantasyonu](#api-dokümantasyonu)
- [Örnek Kullanım Akışı](#örnek-kullanım-akışı)
- [Kurulum ve Çalıştırma](#kurulum-ve-çalıştırma)
- [Örnek Endpointler](#örnek-endpointler)
- [Gelecek Geliştirmeler](#gelecek-geliştirmeler)

---

## Proje Amacı

Şirket içinde kullanılan cihazların:

- envanterde tutulması
- hangi kullanıcıya verildiğinin bilinmesi
- geri alındığında kaydının kapanması
- aktif ve geçmiş zimmetlerin izlenmesi

gibi süreçlerini merkezi bir yapı üzerinden yönetmek hedeflenmiştir.

Bu proje şu anda backend odaklı geliştirilmiştir ve ilk aşamada Swagger üzerinden kullanılabilecek şekilde tasarlanmıştır.

---

## Temel Özellikler

### Asset Management
- Asset oluşturma
- Asset listeleme
- Asset detay görüntüleme
- Asset güncelleme
- Asset silme
- Status bazlı filtreleme
- Asset code ve serial number için duplicate kontrolü

### User Management
- User oluşturma
- User listeleme
- User detay görüntüleme
- User güncelleme
- User silme
- Email duplicate kontrolü

### Assignment Management
- Asset'i kullanıcıya zimmetleme
- Zimmeti iade alma
- Assignment kayıtlarını filtreleme
- User bazlı zimmet geçmişi
- Asset bazlı zimmet geçmişi
- Aktif ve iade edilmiş kayıtları ayırma
- Aynı asset'in aynı anda ikinci kez atanmasını engelleme

### Validation and Error Handling
- Request validation
- 404 Not Found handling
- 409 Conflict handling
- Temiz ve okunabilir hata response'ları

---

## Modüller

### 1. Asset
Cihaz envanterini yönetir.

Örnek alanlar:
- assetCode
- name
- assetType
- brand
- model
- serialNumber
- status

### 2. User
Sistemdeki kullanıcıları temsil eder.

Örnek alanlar:
- firstName
- lastName
- email
- department
- role

### 3. Assignment
Bir asset'in hangi kullanıcıya ne zaman atandığını ve ne zaman iade edildiğini tutar.

Örnek alanlar:
- asset
- user
- assignedAt
- returnedAt
- note

---

## İş Kuralları

Sistemde uygulanan bazı temel kurallar:

- Aynı asset aynı anda sadece bir aktif kullanıcıya atanabilir.
- `RETIRED` durumundaki asset atanamaz.
- `IN_REPAIR` durumundaki asset atanamaz.
- Asset iade edildiğinde assignment kaydı kapanır.
- Asset iade edildiğinde asset status tekrar `IN_STOCK` olur.
- Asset code benzersiz olmalıdır.
- Serial number varsa benzersiz olmalıdır.
- User email benzersiz olmalıdır.

---

## Teknoloji Yığını

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Docker
- Maven
- Spring Validation
- Springdoc OpenAPI / Swagger

---

## Mimari Yapı

Proje feature-based package yapısı ile geliştirilmiştir.

```text
src/main/java/com/mtekinn/assetdesk
├── asset
├── user
├── assignment
├── common
│   └── exception
└── health
```
---

## API Dokümantasyonu

Uygulama çalışırken Swagger arayüzüne şu adresten erişilebilir:

`http://localhost:8080/swagger-ui/index.html`

OpenAPI JSON çıktısı:

`http://localhost:8080/v3/api-docs`

---

## Örnek Kullanım Akışı

### Senaryo 1: Yeni kullanıcıya cihaz atama

1. Sistemde kullanıcı oluşturulur.
2. Sistemde asset oluşturulur.
3. Assignment endpoint'i ile asset kullanıcıya atanır.
4. Asset status otomatik olarak `ASSIGNED` olur.
5. Assignment kaydı aktif olarak tutulur.

### Senaryo 2: Cihazın iade alınması

1. İlgili assignment bulunur.
2. Return endpoint'i çağrılır.
3. Assignment kaydı kapanır (`returnedAt` dolu olur).
4. Asset status tekrar `IN_STOCK` olur.

### Senaryo 3: Geçmiş zimmetlerin incelenmesi

Filtreleme ile:
- belirli kullanıcıya ait kayıtlar
- belirli asset'e ait kayıtlar
- aktif kayıtlar
- iade edilmiş kayıtlar

listelenebilir.

---

## Kurulum ve Çalıştırma

### 1. PostgreSQL container başlat

Var olan container'ı başlatmak için:

```bash
docker start assetdesk-postgres
```

---
İlk kez oluşturulacaksa:

```bash
docker run --name assetdesk-postgres -e POSTGRES_DB=assetdesk -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:17
```

---

### 2. Uygulamayı çalıştır
Linux / macOS:
```bash
./mvnw spring-boot:run
```
Windows:
```bash
.\mvnw spring-boot:run
```

### 3. Health check
Linux / macOS:
`http://localhost:8080/health`

## Örnek Endpointler

### Asset
- `GET /api/assets`
- `GET /api/assets/{id}`
- `POST /api/assets`
- `PUT /api/assets/{id}`
- `DELETE /api/assets/{id}`

Örnek filtre:
- `GET /api/assets?status=IN_STOCK`

### User
- `GET /api/users`
- `GET /api/users/{id}`
- `POST /api/users`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`

### Assignment
- `GET /api/assignments`
- `POST /api/assignments`
- `POST /api/assignments/{id}/return`

Örnek filtreler:
- `GET /api/assignments?status=ACTIVE`
- `GET /api/assignments?status=RETURNED`
- `GET /api/assignments?userId=2`
- `GET /api/assignments?assetId=1`
- `GET /api/assignments?userId=2&status=RETURNED`

---

## Örnek Hata Response'ları

### 404 Not Found

```json
{
  "timestamp": "2026-04-17T18:15:56.1715686",
  "status": 404,
  "message": "User not found with id: 112"
}
```

### 409 Conflict
```json
{
  "timestamp": "2026-04-17T18:24:57.8845523",
  "status": 409,
  "message": "Asset is already assigned: ASSET-001"
}
```
