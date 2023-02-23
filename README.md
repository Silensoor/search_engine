# search_engine
## Описание
Поисковый движок по сайтам, использующий фреймворк Spring.
Позволяет индексировать страницы сайтов указанных в конфигурации проекта (application.yaml) и осуществлять по ним быстрый поиск.

## Как осуществляется поиск
После запуска проекта, по адресу http://localhost:8080/ станет доступен веб-интерфейс. Он представляет собой одну веб-страницу с тремя вкладками:

### Dashboard.
Эта вкладка открывается по умолчанию. На ней отображается общая статистика по всем сайтам, а также детальная статистика и статус по каждому из сайтов.
<p align="center">
<img src="https://user-images.githubusercontent.com/8067668/217037151-67e69b1f-7cfc-4d03-a845-a8c474d59516.png" width="100%"></p>

### Management.
На этой вкладке находятся инструменты управления поисковым движком — запуск и остановка полной индексации (переиндексации), а также возможность добавить (обновить) отдельную страницу по ссылке:
<p align="center">
<img src="https://user-images.githubusercontent.com/8067668/217037157-904d6e0f-b6a1-4b2d-b850-f436c426aac2.png" width="100%"></p>

### Search.
Эта страница предназначена для тестирования поискового движка. На ней находится поле поиска, выпадающий список с выбором сайта для поиска, а при нажатии на кнопку «Найти» выводятся результаты поиска:
<p align="center">
<img src="https://user-images.githubusercontent.com/8067668/217037160-7d75c9ce-f85a-4c64-81a7-3af123033006.png" width="100%"></p>


## Используемые технологии
- Spring
- JSOUP
- Russian Morphology for Apache Lucene (в проекте используются сгенерированные JAR библиотеки леммитизации из Maven репозитория https://gitlab.skillbox.ru/)
- Maven


## Системные требования:
- Java 17 или выше
- Maven 3.8 или выше
- MySql 8 или выше

## Запуск проекта
Перед запуском проекта убедитесь, что Maven установлен правильно командой mvn -v, добавьте путь к каталогу bin в переменную окружения path.
Настройте Application.yaml, указав путь к базе данных и логин с паролем.

#### application.yaml:
```yaml
spring:
  datasource:
    username: YOUR_LOGIN
    password: YOUR_PASSWORD
    url: jdbc:mysql://localhost:3306/search_engine?useSSL=false&requireSSL=false&allowPublicKeyRetrieval=true
```

#### ШАГ 1. 

Клонировать проект: `git clone https://github.com/gvinogradov/searchengine.git`

#### ШАГ 2.

Соберите проект с помощью Maven. Выполните команду в папке с проектом: `mvn -U clean packege`

#### ШАГ 3.

Запустите проект командой: `java -jar SearchEngine-1.0-SNAPSHOT.jar` (application.yaml должен быть в этой же папке)
