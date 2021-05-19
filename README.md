# vaccineAlertOnTelegram


**Pre-requisite:**
  1. Java 11 should be installed on Machine

**Things to do before Running this Project:**
  1. Create a Telegram public channel
  2. Create a Telegram BOT via BotFather. Reference link: https://core.telegram.org/bots#3-how-do-i-create-a-bot
  3. When bot is created you will receive a token from BotFather. Please store it with you securely
  4. Set the bot as administrator in your channel
  5. Please note that your channel should be public
  6. Open TelegramApiService.java file and replace BOT_API_KEY with key which you received while creating the bot. (At line number 9)
  7. In application.property file change district id with your district id which you want alert for
  8. In application.property file put vaccine which you want alert for
  9. In application.property file Change age - 45 or 18
  10. In application.property file enter telegram channel id. You will find channel id in invite link. Add @ at the beggining of channel id. Example: @myTestChannel

**How to Run the project:**
Simply execute below command on terminal from the project directory and receive alert in your telegram channel
./mvnw spring-boot:run
