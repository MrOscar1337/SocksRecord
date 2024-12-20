# Socks API

## Описание

Это REST API для управления складом носков. Приложение позволяет регистрировать приход и отпуск носков, обновлять данные, загружать партии из файла и получать информацию о наличии носков с фильтрацией.

---

## Технологический стек

- Язык: Java 17  
- Фреймворк: Spring Boot 2.7  
- База данных: PostgreSQL  
- Система сборки: Gradle  
- Документация: Swagger/OpenAPI  
- Логирование: SLF4J  

---

## Функциональность

1. Регистрация прихода носков
   - Метод: POST /api/socks/income
   - Увеличивает количество носков на складе.

2. Регистрация отпуска носков
   - Метод: POST /api/socks/outcome
   - Уменьшает количество носков на складе, если их хватает.

3. Получение общего количества носков с фильтрацией
   - Метод: GET /api/socks
   - Возвращает количество носков с фильтрацией по цвету и содержанию хлопка (сравнение, диапазон).

4. Обновление данных носков
   - Метод: PUT /api/socks/{id}
   - Позволяет изменить параметры носков (цвет, процент хлопка, количество).

5. Загрузка партий носков из файла
   - Метод: POST /api/socks/batch
   - Загружает партии носков из файла .csv или .xlsx.

6. Логирование
   - Все операции логируются с использованием SLF4J.

7. Улучшенная обработка ошибок
   - Централизованная обработка ошибок с понятными сообщениями:
     - Некорректный формат данных.
     - Нехватка носков на складе.
     - Ошибки при обработке файлов.

---

## Запуск приложения

### 1. Склонируйте репозиторий

`bash
git clone https://github.com/MrOscar1337/SocksRecord.git

### 2. Настройте базу данных PostgreSQL

Создайте базу данных и добавьте настройки в файл application.properties:
spring.application.name=SocksRecord
spring.datasource.url=jdbc:postgresql://localhost:5432/SocksInventory
spring.datasource.username=postgres
spring.datasource.password=admin
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=create


## Примеры запросов

### 1. Регистрация прихода носков

Метод: POST /api/socks/income  
Описание: Увеличивает количество носков на складе.

Запрос:
`http
POST /api/socks/income
Content-Type: application/json

{
  "color": "red",
  "cottonPercentage": 80,
  "quantity": 100
}

### 2. Регистрация отпуска носков

Метод: POST /api/socks/outcome
Описание: Уменьшает количество носков на складе, если их хватает.

Запрос:

`http
POST /api/socks/outcome
Content-Type: application/json

{
  "color": "red",
  "cottonPercentage": 80,
  "quantity": 50
}

### 3. Получение общего количества носков с фильтрацией
Метод: GET /api/socks
Описание: Получает количество носков с фильтрацией по цвету и процентному содержанию хлопка.

Запрос:

`http
GET /api/socks?color=red&minCotton=30&maxCotton=70

### 4. Обновление данных носков
Метод: PUT /api/socks/{id}
Описание: Позволяет изменить параметры носков (цвет, процент хлопка, количество).

Запрос:

`http
PUT /api/socks/1
Content-Type: application/json

{
  "color": "blue",
  "cottonPercentage": 70,
  "quantity": 120
}

### 5. Загрузка партий носков
Метод: POST /api/socks/batch
Описание: Загружает партии носков из файла в формате .csv или .xlsx.
Файл должен содержать следующие поля: color, cottonPercentage, quantity.

Запрос:

`http
POST /api/socks/batch
Content-Type: multipart/form-data

{
  "file": "path/to/socks_batch.csv"
}