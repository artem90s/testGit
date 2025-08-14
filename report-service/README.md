# Report Service

Spring Boot microservice for generating reports using JasperReports.

## Endpoints

- `GET /api/reports/{reportType}?format=PDF&title=...&customer=...&period=...`
- `POST /api/reports/{reportType}` with JSON:

```json
{
  "format": "PDF",
  "fileName": "statement",
  "parameters": {
    "title": "Bank Statement",
    "customer": "ACME Corp",
    "period": "2025-01"
  },
  "items": [
    {"date": "2025-01-01", "description": "Opening balance", "amount": 0.0},
    {"date": "2025-01-02", "description": "Payment", "amount": 123.45}
  ]
}
```

## Templates

Templates are configured via `application.yml` under `report.templates`.

Default lookup path is `classpath:reports/{reportType}.jrxml`.