# Codeyzer Pass Server

Codeyzer Pass Server, Spring Boot ile geliştirilmiş güvenli bir şifre yönetimi backend servisidir. Şifrelerin saklanması, alınması ve kullanıcı kimlik doğrulaması için güçlü bir API sağlar.

## 🚀 Özellikler

- 👤 JWT token ile kullanıcı kaydı ve kimlik doğrulama
- 🔒 Şifreleme ile güvenli şifre saklama
- 🔑 Şifre yönetimi API'si (oluşturma, okuma, güncelleme, silme)
- 🔄 Token yenileme mekanizması
- 🛡️ API koruması için hız sınırlama
- ⚡ Geliştirilmiş performans için önbellekleme

## 💻 Teknoloji Stack

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Security** (kimlik doğrulama ve yetkilendirme için)
- **Spring Data JPA** (veritabanı erişimi için)
- **PostgreSQL** (veri depolama için)
- **Liquibase** (veritabanı migrasyon yönetimi için)
- **JWT** (güvenli token tabanlı kimlik doğrulama için)
- **Lombok** (tekrarlayan kod miktarını azaltmak için)
- **MapStruct** (nesne eşleştirme için)
- **Caffeine** (önbellekleme için)
- **Bucket4j** (hız sınırlama için)

## 📂 Proje Yapısı

Proje, standart bir Spring Boot mimarisini takip eder:

```
src/main/java/com/codeyzer/pass/sunucu/
├── config/            # Konfigürasyon sınıfları
├── controller/        # REST API endpoint'leri
├── dto/               # Veri Transfer Nesneleri
├── entity/            # JPA varlıkları
├── exception/         # Özel istisnalar
├── mapper/            # MapStruct eşleyicileri
├── repository/        # Veri erişim katmanı
├── service/           # İş mantığı
└── util/              # Yardımcı sınıflar
```

## 🌐 API Endpoint'leri

### Kullanıcı Yönetimi

- `POST /api/kullanici/register` - Yeni kullanıcı kaydı
- `POST /api/kullanici/login` - Kullanıcı kimlik doğrulama
- `POST /api/kullanici/refresh` - Kimlik doğrulama token'ını yenileme

### Şifre Yönetimi

- `GET /api/harici-sifre` - Mevcut kullanıcı için tüm şifreleri getir
- `POST /api/harici-sifre/kaydet` - Yeni şifre kaydet
- `PUT /api/harici-sifre/guncelle/{id}` - Mevcut şifreyi güncelle
- `DELETE /api/harici-sifre/sil/{id}` - Şifre sil

## ⚙️ Kurulum ve Yapılandırma

### Gereksinimler

- Java 17 veya üzeri
- Maven
- PostgreSQL

### Konfigürasyon

Uygulama YAML konfigürasyon dosyalarını kullanır:

- `application.yml` - Ortak konfigürasyon
- `application-dev.yml` - Geliştirme ortamı konfigürasyonu
- `application-prod.yml` - Üretim ortamı konfigürasyonu

### Veritabanı Kurulumu

Uygulama, veritabanı migrasyonları için Liquibase kullanır. Değişiklik günlüğü şu konumda bulunur:
```
src/main/resources/db/changelog/dbchangelog.xml
```

## 🚀 Uygulamayı Çalıştırma

### Maven Kullanarak

```bash
mvn spring-boot:run
```

### Docker Kullanarak

Kolay dağıtım için bir Docker Compose dosyası sağlanmıştır:

```bash
docker-compose up
```

## 🔒 Güvenlik

- Tüm API endpoint'leri (kayıt ve giriş hariç) kimlik doğrulama gerektirir
- Şifreler depolamadan önce güvenli bir şekilde şifrelenir
- Durumsuz kimlik doğrulama için JWT token'ları kullanılır
- Brute force saldırılarını önlemek için hız sınırlama uygulanır

## 👨‍💻 Geliştirme

### Projeyi Derleme

```bash
mvn clean install
```

### Testleri Çalıştırma

```bash
mvn test
```

## 🔄 Frontend Entegrasyonu

Codeyzer Pass Server, [Codeyzer Pass React](https://github.com/turbulence6th/CodeyzerPassPluginReact) frontend uygulaması ile entegre çalışmak üzere tasarlanmıştır. Frontend uygulaması, bu backend API'sini kullanarak şifre yönetimi işlemlerini gerçekleştirir.

## 📝 Lisans

MIT License © Codeyzer
