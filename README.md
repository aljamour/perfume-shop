# Luksus Parfumer

A production-style luxury perfume storefront and administration platform built with Java, Spring Boot, Thymeleaf, JavaScript and SQL.

## Current MVP

The current branch contains the first end-to-end implementation:

- Luxury Danish storefront
- Product catalog and detail pages
- Gender filtering and search
- Database-backed perfume sizes
- Admin-controlled selling prices
- Admin-controlled stock
- Session shopping cart
- Checkout and persisted orders
- Spring Security protected admin area
- Database-driven perfume recommendation chatbot
- H2 file database for simple local development
- MySQL profile for production-like development
- Docker + Docker Compose
- REST endpoints for perfume browsing

## Core rule: prices are not hardcoded

Selling prices live on `PerfumeSize` records in the database.

A perfume can have any combination of sizes. The admin can independently set:

- selling price
- stock quantity
- active/inactive state

No customer-facing page contains fixed product prices.

## Tech stack

- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA / Hibernate
- Spring Security
- Thymeleaf
- HTML5 / CSS3
- Vanilla JavaScript
- H2 for local development
- MySQL for production-like development
- Docker / Docker Compose
- Maven

## Run locally

### IntelliJ / Maven

1. Set environment variables for the admin user:

```
ADMIN_USERNAME=admin
ADMIN_PASSWORD=choose-a-strong-password
```

2. Run `PerfumeShopApplication`.

3. Open:

- Store: http://localhost:8080
- Catalog: http://localhost:8080/catalog
- Admin: http://localhost:8080/admin

The default local database is stored in `./data/perfumedb`.

### Docker + MySQL

```bash
docker compose up --build
```

Then open http://localhost:8080.

Before real deployment, change the admin password and database credentials in the environment.

## Admin workflow

1. Sign in at `/login`
2. Open a perfume
3. Set price for a size
4. Set stock quantity
5. Activate the size
6. The storefront immediately reads the database value

This makes pricing operational data instead of source-code configuration.

## Chatbot

The first chatbot version is intentionally small and understandable.

It does not invent products. It searches and scores the actual perfume database based on signals such as:

- gender
- brand
- category
- occasion
- scent notes

Endpoint:

```
GET /api/chatbot?q=Noget til date night med vanilje
```

The architecture can later be extended with a Python/FastAPI service or an LLM without replacing the catalog logic.

## REST API

Examples:

```
GET /api/perfumes
GET /api/perfumes?q=Tom%20Ford
GET /api/perfumes/{slug}
GET /api/chatbot?q=Find%20en%20sommerduft
```

## Project structure

```
src/main/java/dk/perfumeshop
├── config
├── controller
│   └── admin
├── dto
├── model
├── repository
└── service

src/main/resources
├── static
│   ├── css
│   └── js
└── templates
    └── admin
```

## Product data

The repository includes an initial curated subset of the supplied perfume catalog so the application has real domain data on first start.

The next data task is to import the complete catalog into a structured JSON/CSV source while preserving the admin-controlled pricing rule.

## Planned next iterations

- Full catalog import
- Category-specific pages
- Product image management
- Better admin category and scent-note editing
- Order detail/status management
- Customer accounts
- Persistent customer carts
- Flyway migrations
- Payment provider integration
- Email confirmations
- Optional FastAPI/LLM enhancement for the chatbot
- Deployment configuration

## Trademark note

Original perfume and brand names are used as scent references. The project should not imply affiliation with the referenced manufacturers.
