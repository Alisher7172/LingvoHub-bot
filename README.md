# LingvoHub Telegram Bot

![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

LingvoHub - bu Telegram ichida ishlaydigan ta'lim boti. Foydalanuvchi til, o'qituvchi, kurs va darslarni inline tugmalar orqali tanlaydi. Video darslar private kanallardan `copyMessage` orqali yuboriladi.

## Texnologiyalar

- Java 21
- Spring Boot 3
- SQLite (`database.db`)
- TelegramBots Java
- Maven
- Lombok

## Ishga tushirish (local)

### Variant A: `application-local.yml` (tavsiya etiladi)

1. Shablonni nusxalang (birinchi marta):
   - `cp src/main/resources/application-local.example.yml src/main/resources/application-local.yml`
   - Windows uchun: `copy src\main\resources\application-local.example.yml src\main\resources\application-local.yml`
2. `application-local.yml` ichiga BotFather token => @BotFather, username va admin Telegram ID yozing => @id_bot
3. Ishga tushiring:
   - PowerShell: `.\run-local.ps1`
   - Git Bash: `./run-local.sh`
   - yoki: `mvn spring-boot:run -Dspring-boot.run.profiles=local`
   - JAR: `java -jar target/lingvohub-bot-1.0.0.jar --spring.profiles.active=local`

`application-local.yml` gitga kirmaydi (`.gitignore` da).

### Variant B: muhit o'zgaruvchilari (VPS / CI)

1. Sozlang: `TELEGRAM_BOT_TOKEN`, `TELEGRAM_BOT_USERNAME`, `ADMIN_IDS`
2. `mvn clean package`
3. `java -jar target/lingvohub-bot-1.0.0.jar`

## Admin buyruqlari

- `/admin` - admin menyu
- `/stats` - qisqa statistika

Admin faqat `ADMIN_IDS` ro'yxatidagi Telegram IDlar uchun ishlaydi.

## Asosiy user flow

- `/start` -> til tanlash
- til -> o'qituvchi tanlash
- o'qituvchi -> kurs tanlash
- kurs -> darslar ro'yxati (`✅` progress, `🔒` premium)
- dars -> video `copyMessage` bilan yuboriladi
- keyingi dars tugmasi orqali davom etish
