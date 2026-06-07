# LingvoHub Telegram Bot

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
   - Windows: `copy src\main\resources\application-local.example.yml src\main\resources\application-local.yml`
2. `application-local.yml` ichiga BotFather token, username va admin Telegram ID yozing.
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

## Hetzner VPS deploy

### Variant 1: Java bilan to'g'ridan-to'g'ri

1. Serverga JDK 21 o'rnating.
2. Loyihani serverga yuklang.
3. `mvn -DskipTests package`
4. `systemd` service yarating:

```ini
[Unit]
Description=LingvoHub Telegram Bot
After=network.target

[Service]
WorkingDirectory=/opt/lingvohub
Environment=TELEGRAM_BOT_TOKEN=your_token
Environment=TELEGRAM_BOT_USERNAME=your_bot_username
Environment=ADMIN_IDS=123456789
ExecStart=/usr/bin/java -jar /opt/lingvohub/target/lingvohub-bot-1.0.0.jar
Restart=always
User=root

[Install]
WantedBy=multi-user.target
```

### Variant 2: Docker

1. `docker build -t lingvohub-bot .`
2. `docker run -d --name lingvohub-bot --restart always -e TELEGRAM_BOT_TOKEN=... -e TELEGRAM_BOT_USERNAME=... -e ADMIN_IDS=... -v $(pwd):/app/data lingvohub-bot`

## Arxitektura

- `controller` - update kirish nuqtasi
- `handler` - message va callback routing
- `service` - biznes mantiq
- `repository` - DB access
- `entity` - model
- `keyboard` - inline keyboard builderlar
- `util` - yordamchi klasslar
