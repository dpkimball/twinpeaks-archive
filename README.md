# Twinpeaks — historical archive

Java/Spring REST backend with receipt models, PostgreSQL persistence, food-data services, and Elasticsearch integration.

A personal receipt and food-information project developed in 2017–2019. Heartleaf supplied the web interface and data ingestion; TwinPeaks supplied the Java backend; Monadnock explored Android access. These repositories preserve that work as historical source, with publication cleanup performed in 2026. They are not maintained or presented as production-ready software.

## Related repositories

[Heartleaf](https://github.com/dpkimball/heartleaf-archive) · [Twinpeaks](https://github.com/dpkimball/twinpeaks-archive) · [Monadnock](https://github.com/dpkimball/monadnock-archive)

## Publication changes

Recovered from the fuller February 2019 snapshot (3e3e44a), rather than the incomplete 2022 branch. Original history, database exports, credential-bearing database image, and desktop screenshots were excluded. Verified MIT starter notices were restored. The old authentication-tutorial implementation was removed and replaced with Spring-managed HTTP Basic; JWT login/signup and its example tests are omitted. This deliberately changes the authentication contract, so historical Heartleaf/Monadnock clients are not compatible without adaptation.

## Historical environment

Historical build: Java 8, Gradle 4.9, Spring Boot 2.0.1, Kotlin 1.2.21. PostgreSQL and a compatible Elasticsearch service are external dependencies. Review database profiles before execution; the test profile uses create-drop. Supply TWINPEAKS_DB_PASSWORD and USDA_API_KEY. No accounts are bundled, and no public signup endpoint is provided. The replacement uses existing BCrypt account records; mutating requests retain Spring CSRF protection. Legacy build repositories/plugins may need repair.

## Limitations

The application has not been compiled or integration-tested in the publication environment, which lacks a JDK. Historical receipt/receipt-item endpoints do not consistently enforce ownership. Do not expose this service to real user data. Controller tests remain historical and are not evidence that the replacement authentication is validated. The health indicator is illustrative.

## License and attribution

Original contributions and publication replacements are MIT licensed under LICENSE. Third-party portions retain their own notices and terms; see THIRD_PARTY.md. No ownership of upstream sample code is claimed.
