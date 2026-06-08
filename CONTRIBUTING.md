# LingvoHub loyihasiga hissa qo'shish

LingvoHub loyihasini yaxshilashga yordam bermoqchi bo'lganingiz uchun rahmat. Bu loyiha Java, Spring Boot, SQLite va Maven asosida yozilgan Telegram ta'lim boti.

## Boshlash

1. Repozitoriyni fork qiling.
2. O'z forkingizni kompyuteringizga yuklab oling:

```bash
git clone https://github.com/YOUR_USERNAME/LingvoHub.git
cd LingvoHub
```

3. Sizda quyidagilar o'rnatilgan bo'lishi kerak:

- Java 21
- Maven
- BotFather orqali olingan Telegram bot tokeni

4. Local sozlamalar faylini yarating:

```bash
cp src/main/resources/application-local.example.yml src/main/resources/application-local.yml
```

Windows PowerShell uchun:

```powershell
Copy-Item src\main\resources\application-local.example.yml src\main\resources\application-local.yml
```

5. `src/main/resources/application-local.yml` fayliga o'zingizning local qiymatlaringizni yozing.

`application-local.yml` faylini gitga commit qilmang. Unda haqiqiy bot tokenlari, admin IDlar yoki boshqa maxfiy ma'lumotlar saqlanadi.

## Local ishga tushirish

PowerShell:

```powershell
.\run-local.ps1
```

Git Bash, Linux, macOS yoki WSL:

```bash
./run-local.sh
```

Qo'lda ishga tushirish:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## O'zgarish yuborishdan oldin

Buildni ishga tushiring:

```bash
mvn clean package
```

Shuningdek, quyidagi ma'lumotlar tasodifan qo'shilmaganini tekshiring:

- Telegram bot tokenlari
- Haqiqiy admin Telegram IDlari
- `src/main/resources/application-local.yml`
- `database.db`
- log fayllar
- private kurs, kanal yoki dars ma'lumotlari

## Branch va commitlar

Qisqa va tushunarli branch nomidan foydalaning:

```bash
git checkout -b fix-admin-menu
git checkout -b add-lesson-progress
git checkout -b docs-setup-guide
```

Commit xabarlarini aniq yozing:

```bash
git commit -m "Fix admin lesson creation flow"
git commit -m "Document local setup"
```

## Pull requestlar

Pull request ochganda quyidagilarni yozing:

- Nima o'zgardi
- Nima uchun bu o'zgarish kerak
- Qanday test qilindi
- Agar o'zgarish bot ishlashiga ta'sir qilsa, screenshot yoki Telegram flow misollari

Pull requestlarni imkon qadar bitta mavzuga qaratilgan holda yuboring. Kichik va aniq o'zgarishlarni ko'rib chiqish va merge qilish osonroq.

## Kod uslubi

- Loyihadagi mavjud Java va Spring Boot uslubiga amal qiling.
- Service, handler, repository va entity vazifalarini alohida saqlang.
- Feature yoki bug fix pull requestlarida aloqasiz refactoring qilmang.
- Tushunarli nomlardan foydalaning.
- Kommentariyalarni faqat koddagi noaniq joylarni tushuntirish uchun yozing.

## Xavfsizlik

Haqiqiy maxfiy ma'lumotlar bor pull request yubormang.

Agar tasodifan haqiqiy Telegram bot tokenini commit qilib yuborsangiz, darhol BotFather orqali tokenni bekor qiling, yangi token yarating va review so'rashdan oldin eski qiymatni git tarixidan ham olib tashlang.

## Issue yuborish

Bug haqida issue ochganda quyidagilarni yozing:

- Nima bo'lishi kerak edi
- Aslida nima bo'ldi
- Takrorlash qadamlari
- Kerakli loglar, lekin maxfiy ma'lumotlarsiz
- Java versiyangiz va operatsion tizimingiz

## Feature takliflari

Feature takliflari qabul qilinadi. Iltimos, quyidagilarni tushuntiring:

- Foydalanuvchi muammosi
- Taklif qilinayotgan ishlash tartibi
- Admin yoki Telegram flowdagi o'zgarishlar
- Bu feature database strukturaga ta'sir qiladimi yoki yo'qmi

LingvoHub loyihasini yaxshilashga yordam berganingiz uchun rahmat.
